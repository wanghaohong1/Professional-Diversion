package com.glxy.pro.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.glxy.pro.DTO.ExportDto;
import com.glxy.pro.bo.StuFromBo;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.bo.VolunteerBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.*;
import com.glxy.pro.query.StudentQuery;
import com.glxy.pro.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.glxy.pro.common.CommonEnum.*;
import static com.glxy.pro.constant.CommonConstant.CURRENT_GRADE;

@SaCheckLogin
@RestController
@RequestMapping("/teacher")
public class DocumentController {
    @Autowired
    private IMajorService majorService;
    @Autowired
    private IVolunteerService volunteerService;
    @Autowired
    private IDocumentService documentService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IGaokaoService gaokaoService;
    @Autowired
    private IFreshmanGradesService freshmanGradesService;

    @ApiOperation("导入志愿填报信息")
    @PostMapping("/import/volunteers")
    public ResultBody ImportVolunteer(@RequestPart MultipartFile excelFile,
                                      @RequestParam("categoryName") String categoryName) throws Exception {
        // 获取大类下的专业映射
        List<Major> majors = majorService.getMajorByCategoryName(categoryName);
        Map<String, String> majorMap = majors.stream()
                .collect(Collectors.toMap(Major::getMajorId, Major::getMajorName));
        int res = 0;
        InputStream in = excelFile.getInputStream();
        // 读取磁盘上已经有的Excel文件
        Workbook excel = new XSSFWorkbook(in);
        // 读取Excel文件中的第一个sheet页
        Sheet sheet = excel.getSheetAt(0);
        // 获取sheet页中有文字的最后一行行号
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= lastRowNum; i++) {
            // 获得某一行
            Row row = sheet.getRow(i);
            String stuId = documentService.formatCellValue(row.getCell(0));
            Map<Integer, String> volunteerMap = new HashMap<>();
            for (String v : documentService.formatCellValue(row.getCell(3)).split("\n")) {
                String[] s = v.split(":");
                volunteerMap.put(Integer.valueOf(s[0]), s[1]);
            }
            for (Map.Entry<Integer, String> entry : volunteerMap.entrySet()) {
                Volunteer volunteer = new Volunteer();
                volunteer.setStuId(stuId);
                volunteer.setWhich(entry.getKey());
                volunteer.setMajorId(majorMap.get(entry.getValue()));
                res += volunteerService.saveOrUpdate(volunteer) ? 0 : -1;
            }
        }
        // 关闭资源
        in.close();
        excel.close();
        return res == 0 ? ResultBody.success("导入成功") : ResultBody.error("导入失败");
    }

    @ApiOperation("导入当年学生信息")
    @PostMapping("/import/students")
    public ResultBody importStudents(@RequestPart MultipartFile excelFile) throws Exception {
        if (excelFile == null) return ResultBody.error(EXCEL_EMPTY);
        // 解析Excel文件 -> 获取学生信息列表
        List<StudentBo> importStudents = null;
        try {
            importStudents = documentService.getStudents(excelFile);
        } catch (NumberFormatException e) {
            // 解析到空数据项 -> 返回错误信息
            return ResultBody.error(EXCEL_HAS_EMPTY_CELL);
        }
        // 解析结果为空 -> 返回错误信息
        if (importStudents.isEmpty()) {
            return ResultBody.error(EXCEL_INVALID);
        } else {
            // 解析结果正常 -> 执行导入逻辑
            // 统计学生大类
            List<String> cateNameList = new ArrayList<>();
            for (StudentBo student : importStudents) {
                cateNameList.add(student.getCategory());
            }
            Map<String, Integer> importCategorys = CollUtil.countMap(cateNameList);
            // 将大类更新到数据库
            boolean categoryRes = false;
            for (Map.Entry<String, Integer> entry : importCategorys.entrySet()) {
                categoryRes = categoryService.saveOrUpdateCategory(entry.getKey(), entry.getValue());
            }
            // 构造用户导入列表
            List<User> importUsers = new ArrayList<>();
            for (StudentBo bo : importStudents) {
                User user = new User();
                BeanUtils.copyProperties(bo, user);
                user.setUserId(bo.getStuId());
                importUsers.add(user);
                // 添加学生大类ID
                bo.setCategoryId(categoryService.getCategoryByName(bo.getCategory()).getCategoryId());
            }
            // 覆盖当年学生用户信息
            userService.removeUserByGrade(CURRENT_GRADE);
            studentService.removeStudentsByGrade(CURRENT_GRADE);
            // 将studentBo改造回student
            List<Student> students = new ArrayList<>();
            for (StudentBo studentBo : importStudents) {
                Student student = new Student();
                BeanUtil.copyProperties(studentBo, student);
                students.add(student);
            }
            // 批量添加学生和用户信息
            if (studentService.saveBatch(students)
                    && userService.saveBatch(importUsers) && categoryRes) {
                return ResultBody.success("学生用户数据导入成功");
            }
        }
        return ResultBody.error("学生用户数据导入失败");
    }

    @ApiOperation("导入当年录取批次信息")
    @PostMapping("/import/stuFrom")
    public ResultBody importStuFrom(@RequestPart("excelFile") MultipartFile excelFile) throws Exception {
        if (excelFile == null) return ResultBody.error(EXCEL_EMPTY);
        // 解析Excel文件 -> 获取学生信息列表
        List<StuFromBo> importStuFrom = null;
        try {
            importStuFrom = documentService.getStuFrom(excelFile);
        } catch (NumberFormatException e) {
            // 解析到空数据项 -> 返回错误信息
            return ResultBody.error(EXCEL_HAS_EMPTY_CELL);
        }
        // 解析结果为空 -> 返回错误信息
        if (importStuFrom.isEmpty()) {
            return ResultBody.error(EXCEL_INVALID);
        } else {
            // 解析结果正常 -> 执行导入逻辑
            // 获取学生高考信息
            List<Gaokao> gaokaoList = gaokaoService.getGaokaoByGrade(CURRENT_GRADE);

            // 将录取分数线添加到Map中
            Map<String, Double> stuFromBoMap = new HashMap<>(30);
            for (StuFromBo stuFromBo : importStuFrom) {
                stuFromBoMap.put(stuFromBo.getStuFrom() + stuFromBo.getGrade(), stuFromBo.getScoreLine());
            }
            int res = 1;
            // 根据录取批次录入录取分数线
            String comeYear;
            String key;
            Double scoreLine;
            for (Gaokao gaokao : gaokaoList) {
//                for (StuFromBo stuFromBo : importStuFrom) {
//                    String comeYear = "20" + gaokao.getStuId().substring(2, 4);
//                    // 根据入学年份和高考录取类型录入录取分数线
//                    if (gaokao.getStuFrom().equals(stuFromBo.getStuFrom())
//                            && comeYear.equals(stuFromBo.getGrade())) {
//                        gaokao.setScoreLine(stuFromBo.getScoreLine());
//                    }
//                }
                comeYear = "20" + gaokao.getStuId().substring(2, 4);
                key = gaokao.getStuFrom() + comeYear;
                scoreLine = stuFromBoMap.get(key);

                // 如果有学生获取不到生源地对应的分数线，就跳过该学生，设定最终会返回error，并且继续添加其他学生的录取分数线
                if(scoreLine == null) {
                    res *= 0;
                    continue;
                }
                gaokao.setScoreLine(scoreLine);
                int r = gaokaoService.updateById(gaokao) ? 1 : 0;
                res *= r;
            }
            return res == 1 ? ResultBody.success() : ResultBody.error("部分录取批次数据导入失败，请检查导入数据是否完整");
        }
    }

    @ApiOperation("导入当年学生高考成绩")
    @PostMapping("/import/gaokao")
    public ResultBody importGaokao(@RequestPart MultipartFile excelFile) throws IOException {
        if (excelFile == null) return ResultBody.error(EXCEL_EMPTY);
        // 解析Excel文件 -> 获取学生信息列表
        List<Gaokao> importGaokao = null;
        try {
            importGaokao = documentService.getGaokao(excelFile);
        } catch (NumberFormatException e) {
            // 解析到空数据项 -> 返回错误信息
            return ResultBody.error(EXCEL_HAS_EMPTY_CELL);
        }
        // 解析结果为空 -> 返回错误信息
        if (importGaokao.isEmpty()) {
            return ResultBody.error(EXCEL_INVALID);
        } else {
            // 解析结果正常 -> 执行导入逻辑
            // 校验数据库中是否存在学生，添加对应批次的分数线
            List<Gaokao> stuNotExist = new ArrayList<>();
            for (Gaokao gaokao : importGaokao) {
                if (studentService.getById(gaokao.getStuId()) == null) {
                    stuNotExist.add(gaokao);
                }
            }
            importGaokao.removeAll(stuNotExist);
            // 添加到数据库 -> 返回未查询到学生信息的高考成绩列表
            gaokaoService.removeGaokaoByGrade(CURRENT_GRADE);
            return gaokaoService.saveBatch(importGaokao) ? ResultBody.success(stuNotExist) : ResultBody.error("高考成绩数据导入失败");
        }
    }

    @ApiOperation("导入当年学生大一成绩")
    @PostMapping("/import/freshmanGrade")
    public ResultBody importFreshmanGrade(@RequestPart MultipartFile excelFile) throws IOException {
        if (excelFile == null) return ResultBody.error(EXCEL_EMPTY);
        // 解析Excel文件 -> 获取学生信息列表
        List<FreshmanGrades> importFreshmanGrades = null;
        try {
            importFreshmanGrades = documentService.getFreshmanGrades(excelFile);
        } catch (NumberFormatException e) {
            // 解析到空数据项 -> 返回错误信息
            return ResultBody.error(EXCEL_HAS_EMPTY_CELL);
        }
        // 解析结果为空 -> 返回错误信息
        if (importFreshmanGrades.isEmpty()) {
            return ResultBody.error(EXCEL_INVALID);
        } else {
            // 解析结果正常 -> 执行导入逻辑
            // 校验学生是否存在
            List<FreshmanGrades> stuNotExist = new ArrayList<>();
            Map<String, List<FreshmanGrades>> resultMap =
                    importFreshmanGrades.stream()
                            .collect(Collectors.groupingBy(FreshmanGrades::getStuId));
            for (Map.Entry<String, List<FreshmanGrades>> entry : resultMap.entrySet()) {
                if (studentService.getById(entry.getKey()) == null) {
                    stuNotExist.addAll(entry.getValue());
                }
            }
            // 删除学生信息不存在的大一成绩数据
            importFreshmanGrades.removeAll(stuNotExist);
            // 添加到数据库 -> 返回未查询到学生信息的大一成绩列表
            freshmanGradesService.removeFreshmanGradesByGrade(CURRENT_GRADE);
            boolean res = freshmanGradesService.saveFreshmanGradesBatch(importFreshmanGrades);
            // 根据添加的大一成绩算出每个学生的总绩点
            for (String stuId : resultMap.keySet()) {
                studentService.updateScore(stuId);
            }
            return res ? ResultBody.success(stuNotExist) : ResultBody.error("大一成绩数据导入失败");
        }
    }

    @ApiOperation("导出当年录取情况")
    @GetMapping("/export/divisionResult")
    public ResultBody exportDivisionResult(StudentQuery query, HttpServletResponse response) throws Exception {
        // 需要按照大类导出 -> 大类ID不能为空
        if (query.getCategoryId() == null) return ResultBody.error(PARAM_REQUIRE);
        // 获取该大类下的志愿数量
        int volunteerNum = majorService.getMajorByCategoryId(query.getCategoryId()).size();
        // 获取导出数据 (缺少志愿列表)
        List<ExportDto> dtos = documentService.getExportData(query);
        // 填充志愿列表 -> 通过学号获取志愿列表
        for (ExportDto exportDto : dtos) {
            List<VolunteerBo> volunteerBos = volunteerService.getVolunteerById(exportDto.getStuId());
            exportDto.setVolunteerList(volunteerBos);
        }
        // 执行导出操作
        documentService.exportDivisionResult(dtos, volunteerNum, response);
        return ResultBody.success("导出录取情况成功");
    }
}
