package com.damai.service.init;

import com.damai.core.SpringUtil;
import com.damai.initialize.base.AbstractApplicationPostConstructHandler;
import com.damai.service.ProgramService;
import com.damai.service.ProgramShowTimeService;
import com.damai.util.BusinessEsHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目演出时间更新
 * @author: 阿星不是程序员
 **/
@Component
public class ProgramShowTimeRenewal extends AbstractApplicationPostConstructHandler {
    
    @Autowired
    private ProgramShowTimeService programShowTimeService;
    
    @Autowired
    private ProgramService programService;
    
    @Autowired
    private BusinessEsHandle businessEsHandle;
    
    @Override
    public Integer executeOrder() {
        return 2;
    }
    
    @Override
    public void executeInit(final ConfigurableApplicationContext context) {
        Set<Long> programIdSet = programShowTimeService.renewal();
        if (!programIdSet.isEmpty()) {
            boolean result = businessEsHandle.checkIndex(SpringUtil.getPrefixDistinctionName() + "-" +
                    ProgramDocumentParamName.INDEX_NAME, ProgramDocumentParamName.INDEX_TYPE);
            if (result) {
                businessEsHandle.deleteIndex(SpringUtil.getPrefixDistinctionName() + "-" +
                        ProgramDocumentParamName.INDEX_NAME);
            }
            for (Long programId : programIdSet) {
                programService.delRedisData(programId);
                programService.delLocalCache(programId);
            }
        }
    }
}
