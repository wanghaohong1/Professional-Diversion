package com.glxy.pro.service.impl;

import com.glxy.pro.dto.ExportDto;
import com.glxy.pro.bo.StuFromBo;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.bo.VolunteerBo;
import com.glxy.pro.entity.FreshmanGrades;
import com.glxy.pro.entity.Gaokao;
import com.glxy.pro.mapper.DocumentMapper;
import com.glxy.pro.mapper.StudentMapper;
import com.glxy.pro.query.StudentQuery;
import com.glxy.pro.service.IDocumentService;
import com.glxy.pro.util.LoginUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentServiceImpl implements IDocumentService {
    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private StudentMapper studentMapper;

    /**
     * 转换单元格样式
     *
     * @param cell 单元格
     * @return String类型的单元格值
     */
    @Override
    public String formatCellValue(Cell cell) {
        DataFormatter dataFormatter = new DataFormatter();
        return dataFormatter.formatCellValue(cell);
    }

    @Override
    public List<StudentBo> getStudents(MultipartFile excelFile) throws IOException {
        // 初始化Excel文件
        InputStream in = excelFile.getInputStream();
        XSSFWorkbook excel = new XSSFWorkbook(in);
        XSSFSheet sheet = excel.getSheetAt(0);
        List<StudentBo> list = new ArrayList<>();
        String flag = formatCellValue(sheet.getRow(0).getCell(0));
        // 辨别Excel文件的类型
        if (flag.contains("基本")) {
            // 导入逻辑
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                StudentBo importBo = new StudentBo();
                importBo.setGrade(formatCellValue(row.getCell(0)));
                importBo.setCategory(formatCellValue(row.getCell(1)));
                importBo.setStuClass(formatCellValue(row.getCell(2)));
                importBo.setName(formatCellValue(row.getCell(3)));
                String sex = formatCellValue(row.getCell(4));
                importBo.setSexString(sex);
                importBo.setSex(sex.equals("男") ? 0 : 1);
                importBo.setStuId(formatCellValue(row.getCell(5)));
                importBo.setPassword(LoginUtil.encodePassword(formatCellValue(row.getCell(5))));
                list.add(importBo);
            }
        }
        // 关闭资源
        in.close();
        excel.close();
        return list;
    }

    @Override
    public List<StuFromBo> getStuFrom(MultipartFile excelFile) throws IOException {
        // 初始化Excel文件
        InputStream in = excelFile.getInputStream();
        XSSFWorkbook excel = new XSSFWorkbook(in);
        XSSFSheet sheet = excel.getSheetAt(0);
        List<StuFromBo> res = new ArrayList<>();
        String flag = formatCellValue(sheet.getRow(0).getCell(0));
        // 辨别Excel文件的类型
        if (flag.contains("录取")) {
            // 导入逻辑
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                StuFromBo bo = new StuFromBo();
                bo.setGrade(formatCellValue(row.getCell(0)));
                bo.setStuFrom(formatCellValue(row.getCell(1)));
                bo.setScoreLine(Double.valueOf(formatCellValue(row.getCell(2))));
                res.add(bo);
            }
        }
        // 关闭资源
        in.close();
        excel.close();
        return res;
    }

    @Override
    public List<Gaokao> getGaokao(MultipartFile excelFile) throws IOException {
        // 初始化Excel文件
        InputStream in = excelFile.getInputStream();
        XSSFWorkbook excel = new XSSFWorkbook(in);
        XSSFSheet sheet = excel.getSheetAt(0);
        List<Gaokao> res = new ArrayList<>();
        String flag = formatCellValue(sheet.getRow(0).getCell(0));
        // 辨别Excel文件的类型
        if (flag.contains("高考")) {
            // 导入逻辑
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                Gaokao importBo = new Gaokao();
                importBo.setStuId(formatCellValue(row.getCell(0)));
                importBo.setGkScore(Double.parseDouble(formatCellValue(row.getCell(2))));
                String sciLib = formatCellValue(row.getCell(3));
                importBo.setSciLib(sciLib.contains("理") ? 0 : 1);
                importBo.setStuFrom(formatCellValue(row.getCell(4)));
                res.add(importBo);
            }
        }
        // 关闭资源
        in.close();
        excel.close();
        return res;
    }

    @Override
    public List<FreshmanGrades> getFreshmanGrades(MultipartFile excelFile) throws IOException {
        // 初始化Excel文件
        InputStream in = excelFile.getInputStream();
        XSSFWorkbook excel = new XSSFWorkbook(in);
        XSSFSheet sheet = excel.getSheetAt(0);
        List<FreshmanGrades> res = new ArrayList<>();
        String flag = formatCellValue(sheet.getRow(0).getCell(0));
        // 辨别Excel文件的类型
        if (flag.contains("大一")) {
            // 导入逻辑
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                FreshmanGrades importBo = new FreshmanGrades();
                importBo.setStuId(formatCellValue(row.getCell(0)));
                importBo.setCourseName(formatCellValue(row.getCell(2)));
                importBo.setCourseWeight(Double.parseDouble(formatCellValue(row.getCell(3))));
                importBo.setCourseScore(Double.parseDouble(formatCellValue(row.getCell(4))));
                importBo.setCoursePoint(Double.parseDouble(formatCellValue(row.getCell(5))));
                res.add(importBo);
            }
        }
        // 关闭资源
        in.close();
        excel.close();
        return res;
    }

    @Override
    public List<ExportDto> getExportData(StudentQuery query) {
        return documentMapper.getExportData(query);
    }

    @Override
    public void exportDivisionResult(List<ExportDto> dtos, int volunteerNum, HttpServletResponse response) throws IOException {
        // 中文数字映射表
        String[] num2Chinese = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九"};
        // 创建Excel文件
        XSSFWorkbook excel = new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet();
        // 设置表头
        XSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("学号");
        titleRow.createCell(1).setCellValue("姓名");
        titleRow.createCell(2).setCellValue("性别");
        titleRow.createCell(3).setCellValue("班级");
        titleRow.createCell(4).setCellValue("所属大类");
        titleRow.createCell(5).setCellValue("科类");
        titleRow.createCell(6).setCellValue("绩点");
        titleRow.createCell(7).setCellValue("高考成绩");
        titleRow.createCell(8).setCellValue("高考录取批次线");
        titleRow.createCell(9).setCellValue("高考录取批次");
        titleRow.createCell(10).setCellValue("综合成绩");
        titleRow.createCell(11).setCellValue("排名");
        // 从第12列开始为志愿列表
        for (int j = 0; j < volunteerNum; j++) {
            titleRow.createCell(12 + j).setCellValue("第" + num2Chinese[j] + "志愿");
        }
        titleRow.createCell(12 + volunteerNum).setCellValue("录取专业");
        // 填充Excel内容
        for (int i = 0; i < dtos.size(); i++) {
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(dtos.get(i).getStuId());
            row.createCell(1).setCellValue(dtos.get(i).getName());
            row.createCell(2).setCellValue(dtos.get(i).getSex());
            row.createCell(3).setCellValue(dtos.get(i).getStuClass());
            row.createCell(4).setCellValue(dtos.get(i).getCategory());
            row.createCell(5).setCellValue(dtos.get(i).getSciLib());
            row.createCell(6).setCellValue(dtos.get(i).getScore());
            row.createCell(7).setCellValue(dtos.get(i).getGkScore());
            row.createCell(8).setCellValue(dtos.get(i).getScoreLine());
            row.createCell(9).setCellValue(dtos.get(i).getStuFrom());
            row.createCell(10).setCellValue(dtos.get(i).getFinalScore());
            row.createCell(11).setCellValue(dtos.get(i).getRanking());
            List<VolunteerBo> volunteerList = dtos.get(i).getVolunteerList();
            if (volunteerList == null) {
                // 说明此人没填志愿
                volunteerList = new ArrayList<>();
            }
            for (int k = 0; k < volunteerList.size(); k++) {
                row.createCell(12 + k).setCellValue(volunteerList.get(k).getMajorName());
            }
            // 志愿列表之后是录取专业
            row.createCell(12 + volunteerNum).setCellValue(dtos.get(i).getAdmMajor());
        }
        // 通过输出流将Excel文件写入磁盘
        excel.write(response.getOutputStream());
        excel.close();
    }

    @Override
    public void updateScore(String stuId) {
        studentMapper.updateScore(stuId);
    }

}
