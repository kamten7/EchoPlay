package com.myvideoplatform.service;

import com.myvideoplatform.entity.po.OperationLog;
import com.myvideoplatform.entity.vo.PaginationResultVO;

public interface OperationLogService {

    PaginationResultVO<OperationLog> loadLog(Integer pageNo, Integer pageSize,
                                             String operModule, String operType, String operUserName);

    void saveLog(OperationLog log);

    void cleanAll();
}
