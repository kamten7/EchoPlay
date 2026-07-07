package com.myvideoplatform.mappers;

import com.myvideoplatform.entity.po.OperationLog;
import com.myvideoplatform.entity.query.OperationLogQuery;
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
