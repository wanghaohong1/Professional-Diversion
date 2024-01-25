package com.glxy.pro.constant;

/**
 * @author Alonha
 * @create 2023-10-13-17:15
 */
public class RedisConstants {
    public static final Long SEVEN_DAY_TTL = 604800L;
    // 一小时：60*60
    public static final Long ONE_HOUR_TTL = 3600L;
    // 三十分钟：60*30
    public static final long HALF_HOUR_TTL = 1800L;

    // 十二小时
    public static final long TWELVE_HOUR_TTL = 43200L;

    // 验证码有效期5分钟
    public static final long VERIFICATION_FIVE_MIN_TTL = 300L;

    // 缓存验证码
    public static final String VERIFICATION_CACHE = "pro:login:verify:";


    // 缓存单个用户
    public static final String USER_CACHE = "pro:user:";

    // 缓存学生
    public static final String STUDENT_CACHE = "pro:student:";


    // 缓存单个学生高考成绩
    public static final String GAOKAO_CACHE = "pro:gaokao:";

    // 缓存单个学生大一成绩
    public static final String FRESHMAN_GRADES_CACHE = "pro:freshmanGrade:";

    // 缓存单个学生志愿填报情况
    public static final String VOLUNTEER_CACHE = "pro:volunteer:";

    // 缓存登录信息
    public static final String TOKEN_CACHE = "satoken:login:token:";
    public static final String USER_REMEMBER_CACHE = "satoken:login:remember:";

    // 志愿填报通道开启和结束时间
    public static final String VOLUNTEER_START_TIME = "pro:volunteer:start";
    public static final String VOLUNTEER_END_TIME = "pro:volunteer:end";

    // 是否执行录取
    public static final String IS_EXEC_ADMISSION = "pro:admission:isExec";

    // 是否发布录取
    public static final String IS_PUBLISH_ADMISSION = "pro:admission:isPublish";

    // 是否完成专业设置
    public static final String IS_PLAN_SETTING = "pro:admission:isPlanSetting";

    // 是否完成志愿填报时间段设置
    public static final String IS_VOLUNTEER_TIME_SETTING = "pro:admission:isVolunteerTimeSetting";

    // 是否完成导入
    public static final String IS_IMPORT = "pro:admission:isImport";

}
