package com.echoplay.service;

import java.util.Map;

public interface StatisticsService {
    Map<String, Object> getActualTimeStatisticsInfo();
    Map<String, Object> getWeekStatisticsInfo();
}
