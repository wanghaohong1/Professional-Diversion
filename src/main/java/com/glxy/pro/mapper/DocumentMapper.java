package com.glxy.pro.mapper;

import com.glxy.pro.DTO.ExportDto;
import com.glxy.pro.query.StudentQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DocumentMapper {
    List<ExportDto> getExportData(StudentQuery query);
}
