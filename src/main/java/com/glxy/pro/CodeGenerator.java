package com.glxy.pro;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CodeGenerator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/professional_diversion?setUnicode=true&characterEncoding=utf8";
        String username = "root";
        String password = "123456";
        String srcPath = "F:/【桌面文件】/study/编程/专业分流系统/代码/后端/Professional-Diversion-Single/Professional-Diversion-Single";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("lgynb") // 设置作者
                            // .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir() //禁止打开输出目录
                            .outputDir(srcPath + "/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.glxy") // 设置父包名
                            .moduleName("pro") // 设置父包模块名
                            .entity("entity") //设置entity包名
                            //  .other("model.dto") // 设置dto包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, srcPath
                                    + "/src/main/resources/com/glxy/pro/mapper")); // 设置mapperXml生成路径
                })
                .injectionConfig(consumer -> {
                    Map<String, String> customFile = new HashMap<>();
                    // customFile.put("DTO.java", "/templates/entityDTO.java.ftl");
                    consumer.customFile(customFile);
                })
                .strategyConfig(builder -> {
                    builder.addInclude("admission"); // 设置需要生成的表名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("category"); // 设置需要生成的表名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("division_result"); // 设置需要生成的表名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("freshman_grades"); // 设置需要生成的表名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("gaokao"); // 设置需要生成的表名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("major"); // 设置需要生成的表名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("student"); // 设置需要生成的表名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user"); // 设置需要生成的表名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("volunteer"); // 设置需要生成的表名
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
