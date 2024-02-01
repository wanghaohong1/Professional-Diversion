package com.glxy.pro.mapper;

import com.glxy.pro.DTO.GradeManagePageDTO;
import com.glxy.pro.DTO.RankingDTO;
import com.glxy.pro.query.StudentQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Alonha
 * @create 2024-01-31-0:46
 */
@Mapper
public interface GradeMapper {

    List<GradeManagePageDTO> selectGradeManagePage(@Param("query") StudentQuery query, @Param("begin")int begin);

    Integer selectGradeTotal(@Param("query")StudentQuery query);

    List<RankingDTO> selectRanking(@Param("query")StudentQuery query, @Param("begin")int begin);

    Integer selectRankingTotal(@Param("query")StudentQuery query);


}
