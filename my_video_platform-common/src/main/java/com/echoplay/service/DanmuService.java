package com.echoplay.service;

import com.echoplay.entity.dto.TokenUserInfoDto;
import com.echoplay.entity.po.Danmu;
import com.echoplay.entity.vo.PaginationResultVO;

import java.util.List;
import java.util.Map;

public interface DanmuService {
    void postDanmu(TokenUserInfoDto token, String videoId, String fileId, String text, Integer mode, String color, Integer time);
    List<Danmu> loadDanmu(String fileId, String videoId);
    PaginationResultVO<Map<String, Object>> loadDanmuByPage(Integer pageNo, Integer pageSize, String videoId, String textFuzzy);
    void delDanmu(String danmuId);
}
