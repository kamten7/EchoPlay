package com.echoplay.admin.controller;

import com.echoplay.entity.vo.ResponseVO;
import com.echoplay.service.SysSettingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController("adminSettingController")
@RequestMapping("/setting")
@Validated
public class SettingController extends ABaseController {

    @Resource
    private SysSettingService sysSettingService;

    @RequestMapping("/getSetting")
    public ResponseVO getSetting() {
        return getSuccessResponseVO(sysSettingService.getSetting());
    }

    @RequestMapping("/saveSetting")
    public ResponseVO saveSetting(@RequestBody Map<String, String> settings) {
        sysSettingService.saveSetting(settings);
        return getSuccessResponseVO(null);
    }
}
