package com.echoplay.mappers;

import com.echoplay.entity.po.SysSetting;
import com.echoplay.entity.query.SysSettingQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface SysSettingMapper {
    Integer insert(SysSetting sysSetting);
    Integer updateBySettingKey(SysSetting sysSetting);
    SysSetting selectBySettingKey(@Param("settingKey") String settingKey);
    List<SysSetting> selectListByCondition(@Param("query") SysSettingQuery query);
    Long selectCountByCondition(@Param("query") SysSettingQuery query);
}
