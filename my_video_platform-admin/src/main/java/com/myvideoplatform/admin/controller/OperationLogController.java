package com.myvideoplatform.admin.controller;

import com.myvideoplatform.entity.vo.ResponseVO;
import com.myvideoplatform.service.OperationLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("adminOperationLogController")
@RequestMapping("/operationLog")
@Validated
public class OperationLogController extends ABaseController {

    @Resource
    private OperationLogService operationLogService;

    @RequestMapping("/loadLog")
    public ResponseVO loadLog(Integer pageNo, Integer pageSize,
                              String operModule, String operType, String operUserName) {
        return getSuccessResponseVO(operationLogService.loadLog(pageNo, pageSize, operModule, operType, operUserName));
    }

    @RequestMapping("/cleanLog")
    public ResponseVO cleanLog() {
        operationLogService.cleanAll();
        return getSuccessResponseVO(null);
    }
}
