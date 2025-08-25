package com.damai.service.tool;

import cn.hutool.core.collection.CollectionUtil;
import com.damai.vo.SeatVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 座位自动匹配
 * @author: 阿星不是程序员
 **/
public class SeatMatch {
    
    /**
     * 自动选座
     * @param allSeats 可选座位（已按票档过滤）
     * @param seatCount 需要的座位数
     * @return 匹配到的座位列表
     * @throws RuntimeException 如果找不到满足条件的座位
     */
    public static List<SeatVo> findAdjacentSeatVos(List<SeatVo> allSeats, int seatCount) {
        if (CollectionUtil.isEmpty(allSeats)) {
            throw new RuntimeException("没有可用的座位");
        }
        if (seatCount <= 0) {
            throw new IllegalArgumentException("seatCount 必须大于 0");
        }
        
        Map<Integer, List<SeatVo>> rowMap = allSeats.stream()
                .collect(Collectors.groupingBy(SeatVo::getRowCode));
        rowMap.values().forEach(row -> row.sort(Comparator.comparingInt(SeatVo::getColCode)));

        for (int row : rowMap.keySet().stream().sorted().toList()) {
            List<SeatVo> rowSeats = rowMap.get(row);
            List<SeatVo> result = findConsecutiveSeats(rowSeats, seatCount);
            if (!result.isEmpty()) {
                return result;
            }
        }
        
        List<SeatVo> sameColResult = findSameColumnSeats(allSeats, seatCount);
        if (!sameColResult.isEmpty()) {
            return sameColResult;
        }

        if (allSeats.size() >= seatCount) {
            List<SeatVo> shuffled = new ArrayList<>(allSeats);
            Collections.shuffle(shuffled);
            return shuffled.subList(0, seatCount);
        }
        
        throw new RuntimeException("没有足够的座位可供分配");
    }
    
    private static List<SeatVo> findConsecutiveSeats(List<SeatVo> rowSeats, int seatCount) {
        for (int i = 0; i <= rowSeats.size() - seatCount; i++) {
            boolean ok = true;
            for (int j = 1; j < seatCount; j++) {
                if (rowSeats.get(i + j).getColCode() - rowSeats.get(i + j - 1).getColCode() != 1) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                return rowSeats.subList(i, i + seatCount);
            }
        }
        return Collections.emptyList();
    }
    
    private static List<SeatVo> findSameColumnSeats(List<SeatVo> allSeats, int seatCount) {
        Map<Integer, List<SeatVo>> colMap = allSeats.stream()
                .collect(Collectors.groupingBy(SeatVo::getColCode));
        colMap.values().forEach(col -> col.sort(Comparator.comparingInt(SeatVo::getRowCode)));
        
        for (int col : colMap.keySet()) {
            List<SeatVo> colSeats = colMap.get(col);
            for (int i = 0; i <= colSeats.size() - seatCount; i++) {
                if (colSeats.get(i + seatCount - 1).getRowCode() - colSeats.get(i).getRowCode() == seatCount - 1) {
                    return colSeats.subList(i, i + seatCount);
                }
            }
        }
        return Collections.emptyList();
    }
}
