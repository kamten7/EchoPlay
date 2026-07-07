package com.myvideoplatform.mappers;

import com.myvideoplatform.entity.po.SeriesVideo;
import com.myvideoplatform.entity.query.SeriesVideoQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeriesVideoMapper {
    Integer insert(SeriesVideo seriesVideo);
    Integer insertBatch(@Param("list") List<SeriesVideo> list);
    Integer deleteBySeriesId(@Param("seriesId") String seriesId);
    Integer deleteBySeriesVideo(@Param("seriesId") String seriesId, @Param("videoId") String videoId);
    List<SeriesVideo> selectListByCondition(@Param("query") SeriesVideoQuery query);
    Long selectCountByCondition(@Param("query") SeriesVideoQuery query);
}
