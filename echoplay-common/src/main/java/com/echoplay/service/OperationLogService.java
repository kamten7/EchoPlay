package com.echoplay.service;

import com.echoplay.entity.po.OperationLog;
import com.echoplay.entity.vo.PaginationResultVO;

public interface OperationLogService {

    PaginationResultVO<OperationLog> loadLog(Integer pageNo, Integer pageSize,
                                             String operModule, String operType, String operUserName);

    void saveLog(OperationLog log);

    void cleanAll();
}
