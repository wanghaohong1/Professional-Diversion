package com.glxy.pro.service;

import com.glxy.pro.DTO.ExportDto;
import com.glxy.pro.bo.StuFromBo;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.entity.FreshmanGrades;
import com.glxy.pro.entity.Gaokao;
import com.glxy.pro.query.StudentQuery;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IDocumentService {
    String formatCellValue(Cell cell);

    List<StudentBo> getStudents(MultipartFile excelFile) throws IOException;

    List<StuFromBo> getStuFrom(MultipartFile excelFile) throws IOException;

    List<Gaokao> getGaokao(MultipartFile excelFile) throws IOException;

    List<FreshmanGrades> getFreshmanGrades(MultipartFile excelFile) throws IOException;

    List<ExportDto> getExportData(StudentQuery query);

    void exportDivisionResult(List<ExportDto> dtos, int volunteerNum, HttpServletResponse response) throws IOException;

    void updateScore(String stuId);

}
