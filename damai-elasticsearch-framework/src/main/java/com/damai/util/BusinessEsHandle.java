package com.damai.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.damai.dto.EsDataQueryDto;
import com.damai.dto.EsDocumentMappingDto;
import com.damai.dto.EsGeoPointDto;
import com.damai.dto.EsGeoPointSortDto;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: elasticsearch处理
 * @author: 阿星不是程序员
 **/
@Slf4j
@AllArgsConstructor
public class BusinessEsHandle {
    
    private final RestClient restClient;
    
    private final Boolean esSwitch;
    
    private final Boolean esTypeSwitch;
    
    /**
     * 创建索引
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param list 参数集合
     */
    public void createIndex(String indexName, String indexType, List<EsDocumentMappingDto> list) throws IOException {
        if (!esSwitch) {
            return;
        }
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        IndexRequest indexRequest = new IndexRequest();
        XContentBuilder builder = JsonXContent.contentBuilder().startObject().startObject("mappings");
        if (esTypeSwitch) {
            builder = builder.startObject(indexType);
        }
        builder = builder.startObject("properties");
        for (EsDocumentMappingDto esDocumentMappingDto : list) {
            String paramName = esDocumentMappingDto.getParamName();
            String paramType = esDocumentMappingDto.getParamType();
            if ("text".equals(paramType)) {
                Map<String,Map<String,Object>> map1 = new HashMap<>(8);
                Map<String,Object> map2 = new HashMap<>(8);
                map2.put("type","keyword");
                map2.put("ignore_above",256);
                map1.put("keyword",map2);
                builder = builder.startObject(paramName).field("type", "text").field("fields",map1).endObject();
            }else {
                builder = builder.startObject(paramName).field("type", paramType).endObject();
            }
        }
        if (esTypeSwitch) {
            builder.endObject();
        }
        builder = builder.endObject().endObject().startObject("settings").field("number_of_shards", 1)
                .field("number_of_replicas", 1).endObject().endObject();
    
        indexRequest.source(builder);
        String source = indexRequest.source().utf8ToString();
        log.info("create index execute dsl : {}",source);
        HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
        Request request = new Request("PUT","/"+ indexName);
        request.setEntity(entity);
        request.addParameters(Collections.<String, String>emptyMap());
        Response performRequest = restClient.performRequest(request);
    }
    
    /**
     * 检查索引是否存在
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @return boolean
     */
    public boolean checkIndex(String indexName, String indexType)  {
        if (!esSwitch) {
            return false;
        }
        try {
            String path = "";
            if (esTypeSwitch) {
                path = "/" + indexName + "/" + indexType + "/_mapping?include_type_name";
            }else {
                path = "/" + indexName + "/_mapping";
            }
            Request request = new Request("GET", path);
            request.addParameters(Collections.<String, String>emptyMap());
            Response response = restClient.performRequest(request);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(JSON.toJSONString(result));
            return "OK".equals(response.getStatusLine().getReasonPhrase());
        }catch (Exception e) {
            if (e instanceof ResponseException && ((ResponseException)e).getResponse().getStatusLine().getStatusCode() == RestStatus.NOT_FOUND.getStatus()) {
                log.warn("index not exist ! indexName:{}, indexType:{}", indexName,indexType);
            }else {
                log.error("checkIndex error",e);
            }
            return false;
        }
    }
    /**
     * 删除索引
     *
     * @param indexName 索引名字
     * @return boolean
     */
    public boolean deleteIndex(String indexName) {
        if (!esSwitch) {
            return false;
        }
        try {
            Request request = new Request("DELETE", "/" + indexName);
            request.addParameters(Collections.<String, String>emptyMap());
            Response response = restClient.performRequest(request);
            return "OK".equals(response.getStatusLine().getReasonPhrase());
        }catch (Exception e) {
            log.error("deleteIndex error",e);
        }
        return false;
    }
    
    /**
     * 清空索引下所有数据
     *
     * @param indexName 索引名字
     */
    public void deleteData(String indexName) {
        if (!esSwitch) {
            return;
        }
        deleteIndex(indexName);
    }
    
    /**
     * 添加
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param params 参数 key:字段名 value:具体值
     * @return boolean
     */
    public boolean add(String indexName, String indexType,Map<String,Object> params) {
        return add(indexName, indexType, params, null);
    }
    
    /**
     * 添加
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param params 参数 key:字段名 value:具体值
     * @param id 文档id 如果为空，则使用es默认id
     * @return boolean
     */
    public boolean add(String indexName, String indexType,Map<String,Object> params, String id) {
        if (!esSwitch) {
            return false;
        }
        if (CollectionUtil.isEmpty(params)) {
            return false;
        }
        try {
            String jsonString = JSON.toJSONString(params);
            HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
            String endpoint = "";
            if (esTypeSwitch) {
                endpoint = "/" + indexName + "/" + indexType;
            }else {
                endpoint = "/" + indexName + "/_doc";
            }
            if (StringUtil.isNotEmpty(id)) {
                endpoint = endpoint + "/" + id;
            }
            log.info("add dsl : {}",jsonString);
            Request request = new Request("POST",endpoint);
            request.setEntity(entity);
            request.addParameters(Collections.<String, String>emptyMap());
            Response indexResponse = restClient.performRequest(request);
            String reasonPhrase = indexResponse.getStatusLine().getReasonPhrase();
            return "created".equalsIgnoreCase(reasonPhrase) || "ok".equalsIgnoreCase(reasonPhrase);
        }catch (Exception e) {
            log.error("add error",e);
        }
        return false;
    }
    
    /**
     * 查询
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esDataQueryDtoList 参数
     * @param clazz 返回的类型
     * @return List
     */
    public <T> List<T> query(String indexName, String indexType, List<EsDataQueryDto> esDataQueryDtoList, Class<T> clazz) throws IOException {
        if (!esSwitch) {
            return new ArrayList<>();
        }
        return query(indexName, indexType, null, esDataQueryDtoList, null, null, null, null, null, clazz);
    }
    
    /**
     * 查询
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esGeoPointDto 经纬度查询参数
     * @param esDataQueryDtoList 参数
     * @param clazz 返回的类型
     * @return List
     */
    public <T> List<T> query(String indexName, String indexType, EsGeoPointDto esGeoPointDto, List<EsDataQueryDto> esDataQueryDtoList, Class<T> clazz) throws IOException {
        if (!esSwitch) {
            return new ArrayList<>();
        }
        return query(indexName, indexType, esGeoPointDto, esDataQueryDtoList, null, null, null, null,null,clazz);
    }
    
    /**
     * 查询
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esDataQueryDtoList 参数
     * @param sortParam 普通参数排序 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param sortOrder 升序还是降序，为空则降序
     * @param clazz 返回的类型
     * @return List
     */
    public <T> List<T> query(String indexName, String indexType, List<EsDataQueryDto> esDataQueryDtoList, String sortParam, SortOrder sortOrder, Class<T> clazz) throws IOException {
        if (!esSwitch) {
            return new ArrayList<>();
        }
        return query(indexName, indexType, null, esDataQueryDtoList, sortParam, null, sortOrder, null, null, clazz);
    }
    
    /**
     * 查询
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esDataQueryDtoList 参数
     * @param geoPointDtoSortParam 经纬度參數排序 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param sortOrder 升序还是降序，为空则降序
     * @param clazz 返回的类型
     * @return List
     */
    public <T> List<T> query(String indexName, String indexType, List<EsDataQueryDto> esDataQueryDtoList, EsGeoPointSortDto geoPointDtoSortParam, SortOrder sortOrder, Class<T> clazz) throws IOException {
        if (!esSwitch) {
            return new ArrayList<>();
        }
        return query(indexName, indexType, null, esDataQueryDtoList, null, geoPointDtoSortParam, sortOrder,null,null, clazz);
    }
    
    
    
    /**
     * 查询
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esGeoPointDto 经纬度查询参数
     * @param esDataQueryDtoList 参数
     * @param sortParam 普通參數排序 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param geoPointDtoSortParam 经纬度參數排序 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param sortOrder 升序还是降序，为空则降序
     * @param pageSize searchAfterSort搜索的页大小
     * @param searchAfterSort sort值
     * @param clazz 返回的类型
     * @return List
     */
    public <T> List<T> query(String indexName, String indexType, EsGeoPointDto esGeoPointDto, List<EsDataQueryDto> esDataQueryDtoList, String sortParam, EsGeoPointSortDto geoPointDtoSortParam, SortOrder sortOrder, Integer pageSize, Object[] searchAfterSort, Class<T> clazz) throws IOException {
        List<T> list = new ArrayList<>();
        if (!esSwitch) {
            return list;
        }
        SearchSourceBuilder sourceBuilder = getSearchSourceBuilder(esGeoPointDto,esDataQueryDtoList,sortParam,geoPointDtoSortParam,sortOrder);
        executeQuery(indexName,indexType,list,null,clazz,sourceBuilder,null);
        return list;
    }
    
    
    /**
     * 查询(分页)
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esDataQueryDtoList 参数
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param clazz 返回的类型
     * @return PageInfo
     */
    public <T> PageInfo<T> queryPage(String indexName, String indexType, List<EsDataQueryDto> esDataQueryDtoList, Integer pageNo, Integer pageSize, Class<T> clazz) throws IOException {
        return queryPage(indexName, indexType, esDataQueryDtoList, null, null, pageNo, pageSize, clazz);
    }
    
    /**
     * 查询(分页)
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esDataQueryDtoList 参数
     * @param sortParam 排序参数 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param sortOrder 升序还是降序，为空则降序
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param clazz 返回的类型
     * @return PageInfo
     */
    public <T> PageInfo<T> queryPage(String indexName, String indexType, List<EsDataQueryDto> esDataQueryDtoList, String sortParam, SortOrder sortOrder, Integer pageNo, Integer pageSize, Class<T> clazz) throws IOException {
        return queryPage(indexName, indexType, null, esDataQueryDtoList, sortParam, null,sortOrder, pageNo, pageSize, clazz);
    }
    
    /**
     * 查询(分页)
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esGeoPointDto 经纬度查询参数
     * @param esDataQueryDtoList 参数
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param clazz 返回的类型
     * @return PageInfo
     */
    public <T> PageInfo<T> queryPage(String indexName, String indexType, EsGeoPointDto esGeoPointDto, List<EsDataQueryDto> esDataQueryDtoList, Integer pageNo, Integer pageSize, Class<T> clazz) throws IOException {
        return queryPage(indexName, indexType, esGeoPointDto, esDataQueryDtoList, null, null,null, pageNo, pageSize, clazz);
    }
    
    /**
     * 查询(分页)
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esGeoPointDto 经纬度查询参数
     * @param esDataQueryDtoList 参数
     * @param sortParam 排序参数 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param sortOrder 升序还是降序，为空则降序
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param clazz 返回的类型
     * @return
     * @throws IOException
     */
    public <T> PageInfo<T> queryPage(String indexName, String indexType, EsGeoPointDto esGeoPointDto, 
                                     List<EsDataQueryDto> esDataQueryDtoList, String sortParam, 
                                     EsGeoPointSortDto geoPointDtoSortParam, SortOrder sortOrder, Integer pageNo, 
                                     Integer pageSize, Class<T> clazz) throws IOException {
        List<T> list = new ArrayList<>();
        PageInfo<T> pageInfo = new PageInfo<>(list);
        pageInfo.setPageNum(pageNo);
        pageInfo.setPageSize(pageSize);
        if (!esSwitch) {
            return pageInfo;
        }
        SearchSourceBuilder sourceBuilder = getSearchSourceBuilder(esGeoPointDto,esDataQueryDtoList,sortParam,geoPointDtoSortParam,sortOrder);
        sourceBuilder.from((pageNo - 1) * pageSize);
        sourceBuilder.size(pageSize);
        executeQuery(indexName,indexType,list,pageInfo,clazz,sourceBuilder,null);
        return pageInfo;
    }
    
    private SearchSourceBuilder getSearchSourceBuilder(EsGeoPointDto esGeoPointDto, List<EsDataQueryDto> esDataQueryDtoList, String sortParam, EsGeoPointSortDto geoPointDtoSortParam, SortOrder sortOrder){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if (Objects.isNull(sortOrder)) {
            sortOrder = SortOrder.DESC;
        }
        if (StringUtil.isNotEmpty(sortParam)) {
            FieldSortBuilder sort = SortBuilders.fieldSort(sortParam);
            sort.order(sortOrder);
            sourceBuilder.sort(sort);
        }
        if (Objects.nonNull(geoPointDtoSortParam)) {
            GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort("geoPoint", geoPointDtoSortParam.getLatitude().doubleValue(), geoPointDtoSortParam.getLongitude().doubleValue());
            sort.unit(DistanceUnit.METERS);
            sort.order(sortOrder);
            sourceBuilder.sort(sort);
        }
        if (Objects.nonNull(esGeoPointDto)) {
            QueryBuilder geoQuery = new GeoDistanceQueryBuilder(esGeoPointDto.getParamName()).distance(Long.MAX_VALUE, DistanceUnit.KILOMETERS)
                    .point(esGeoPointDto.getLatitude().doubleValue(), esGeoPointDto.getLongitude().doubleValue()).geoDistance(GeoDistance.PLANE);
            sourceBuilder.query(geoQuery);
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (EsDataQueryDto esDataQueryDto : esDataQueryDtoList) {
            String paramName = esDataQueryDto.getParamName();
            Object paramValue = esDataQueryDto.getParamValue();
            Date startTime = esDataQueryDto.getStartTime();
            Date endTime = esDataQueryDto.getEndTime();
            boolean analyse = esDataQueryDto.isAnalyse();
            
            if (Objects.nonNull(paramValue)) {
                if (paramValue instanceof Collection) {
                    if (analyse) {
                        BoolQueryBuilder builds = QueryBuilders.boolQuery();
                        Collection<?> collection = (Collection<?>)paramValue;
                        for (Object value : collection) {
                            builds.should(QueryBuilders.matchQuery(paramName, value));
                        }
                        boolQuery.must(builds);
                    }else {
                        QueryBuilder builds = QueryBuilders.termsQuery(paramName, (Collection<?>)paramValue);
                        boolQuery.must(builds);
                    }
                }else {
                    QueryBuilder builds;
                    if (analyse) {
                        builds = QueryBuilders.matchQuery(paramName, paramValue);
                    } else {
                        builds = QueryBuilders.termQuery(paramName, paramValue);
                    }
                    boolQuery.must(builds);
                }
            }
            if (Objects.nonNull(startTime) || Objects.nonNull(endTime)) {
                QueryBuilder builds = QueryBuilders.rangeQuery(paramName)
                        .from(startTime).to(endTime).includeLower(true);
                boolQuery.must(builds);
            }
        }
        sourceBuilder.trackTotalHits(true);
        sourceBuilder.query(boolQuery);
        return sourceBuilder;
    }
    
    public <T> void executeQuery(String indexName, String indexType,List<T> list,PageInfo<T> pageInfo,Class<T> clazz, 
                                 SearchSourceBuilder sourceBuilder,List<String> highLightFieldNameList) throws IOException {
        String string = sourceBuilder.toString();
        HttpEntity entity = new NStringEntity(string, ContentType.APPLICATION_JSON);
        StringBuilder endpointStringBuilder = new StringBuilder("/" + indexName);
        if (esTypeSwitch) {
            endpointStringBuilder.append("/").append(indexType).append("/_search");
        }else {
            endpointStringBuilder.append("/_search");
        }
        String endpoint = endpointStringBuilder.toString();
        log.info("query execute query dsl : {}",string);
        Request request = new Request("POST",endpoint);
        request.setEntity(entity);
        request.addParameters(Collections.emptyMap());
        Response response = restClient.performRequest(request);
        String result = EntityUtils.toString(response.getEntity());
        if (StringUtil.isEmpty(result)) {
            return;
        }
        JSONObject resultJsonObject =  JSONObject.parseObject(result);
        if (Objects.isNull(resultJsonObject)) {
            return;
        }
        JSONObject hits = resultJsonObject.getJSONObject("hits");
        if (Objects.isNull(hits)) {
            return;
        }
        Long value = null;
        if (esTypeSwitch) {
            value = hits.getLong("total");
        }else {
            JSONObject totalJsonObject = hits.getJSONObject("total");
            if (Objects.nonNull(totalJsonObject)) {
                value = totalJsonObject.getLong("value");
            }
        }
        if (Objects.nonNull(pageInfo) && Objects.nonNull(value)) {
            pageInfo.setTotal(value);
        }
        JSONArray arrayData = hits.getJSONArray("hits");
        if (Objects.isNull(arrayData) || arrayData.isEmpty()) {
            return;
        }
        for (int i = 0, size = arrayData.size(); i < size; i++) {
            JSONObject data = arrayData.getJSONObject(i);
            if (Objects.isNull(data)) {
                continue;
            }
            String esId = data.getString("_id");
            JSONObject jsonObject = data.getJSONObject("_source");
            JSONArray jsonArray = data.getJSONArray("sort");
            if (Objects.nonNull(jsonArray) && !jsonArray.isEmpty()) {
                Long sort = jsonArray.getLong(0);
                jsonObject.put("sort",sort);
            }
            JSONObject highlight = data.getJSONObject("highlight");
            if (Objects.nonNull(highlight) && Objects.nonNull(highLightFieldNameList)) {
                for (String highLightFieldName : highLightFieldNameList) {
                    JSONArray highLightFieldValue = highlight.getJSONArray(highLightFieldName);
                    if (Objects.isNull(highLightFieldValue) || highLightFieldValue.isEmpty()) {
                        continue;
                    }
                    jsonObject.put(highLightFieldName,highLightFieldValue.get(0));
                }
            }
            if (StringUtil.isNotEmpty(esId)) {
                jsonObject.put("esId",esId);
            }
            list.add(JSONObject.parseObject(jsonObject.toJSONString(),clazz));
        }
    }
    
    public void deleteByDocumentId(String index,String documentId) {
        if (!esSwitch) {
            return;
        }
        try {
            Request request = new Request("DELETE", "/" + index + "/_doc/" + documentId);
            request.addParameters(Collections.<String, String>emptyMap());
            Response response = restClient.performRequest(request);
            log.info("deleteByDocumentId result : {}",response.getStatusLine().getReasonPhrase());
        }catch (Exception e) {
            log.error("deleteData error",e);
        }
    }
}
