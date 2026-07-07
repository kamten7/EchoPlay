package com.myvideoplatform.web.controller;

import com.myvideoplatform.entity.vo.ResponseVO;
import com.myvideoplatform.service.SysSettingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController("webSysSettingController")
@RequestMapping("/sysSetting")
@Validated
public class SysSettingController extends ABaseController {

    @Resource
    private SysSettingService sysSettingService;

    @RequestMapping("/getSetting")
    public ResponseVO getSetting() {
        Map<String, String> result = sysSettingService.getSetting();
        return getSuccessResponseVO(result);
    }
}
