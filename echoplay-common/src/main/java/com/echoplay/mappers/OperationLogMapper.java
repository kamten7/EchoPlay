package com.echoplay.mappers;

import com.echoplay.entity.po.OperationLog;
import com.echoplay.entity.query.OperationLogQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface OperationLogMapper {
    Integer insert(OperationLog operationLog);

    List<OperationLog> selectListByCondition(@Param("query") OperationLogQuery query);

    Long selectCountByCondition(@Param("query") OperationLogQuery query);

    void cleanAll();
}
