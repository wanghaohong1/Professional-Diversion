drop schema if exists professional_diversion;

create schema professional_diversion;
use professional_diversion;
alter schema professional_diversion collate utf8_general_ci;

# 用户表
create table user
(
    user_id       varchar(10) primary key comment '用户ID',
    user_password varchar(64) not null comment '用户密码',
    email         varchar(50) comment '电子邮箱',
    phone         varchar(11) comment '手机号码'
);
# 学生表
create table student
(
    stu_id      varchar(10) primary key comment '学号',
    stu_name    varchar(20) not null comment '学生姓名',
    stu_sex     tinyint default 0 comment '性别:0——男，1——女',
    stu_grade   int  not null comment '年级',
    category_id varchar(64) not null comment '所属大类ID',
    stu_class   varchar(30) not null comment '班级',
    stu_score   double comment '总绩点'
);
# 高考信息表
create table gaokao
(
    stu_id     varchar(10) primary key comment '学号',
    stu_from   varchar(20) not null comment '高考录取类型',
    sci_lib    tinyint default 0 comment '文理分科:0——理，1——文',
    score_line double comment '分数线',
    gk_score   double      not null comment '高考总分'
);
# 大一成绩表
create table freshman_grades
(
    stu_id        varchar(10) not null comment '学号',
    course_name   varchar(50) not null comment '课程名称',
    course_weight int         not null comment '课程学分',
    course_point  double      not null comment '单科绩点',
    course_score  double      not null comment '单科成绩',
    primary key (stu_id, course_name)
);

# 分流结果表
create table division_result
(
    stu_id               varchar(10) primary key comment '学号',
    major_id             varchar(64) comment '专业ID',
    ranking              int comment '排名',
    gaokao_final_score   double comment '高考折算成绩',
    freshman_final_score double comment '大一折算成绩',
    final_score          double comment '综合成绩'
);
# 录取情况表
create table admission
(
    major_id                 varchar(64) not null comment '专业ID',
    adm_year                 int         not null comment '年份',
    humanities_stu_count     int         not null comment '文科招生人数',
    science_stu_count        int         not null comment '理科招生人数',
    humanities_low           int default 0 comment '文科最低排名',
    humanities_high          int default 0 comment '文科最高排名',
    science_low              int default 0 comment '理科最低排名',
    science_high             int default 0 comment '理科最高排名',
    now_humanities_stu_count int default 0 comment '当前文科录取人数',
    now_science_stu_count    int default 0 comment '当前理科录取人数',
    primary key (major_id, adm_year)
);
# 志愿填报表
create table volunteer
(
    stu_id   varchar(10) not null comment '学号',
    major_id varchar(64) not null comment '专业ID',
    which    int         not null comment '第几志愿',
    primary key (stu_id, major_id)
);
# 大类表
create table category
(
    category_id   varchar(64) primary key comment '大类ID',
    category_name varchar(20) not null comment '大类名称',
    stu_num       int         not null comment '学生总数'
);
# 专业表
create table major
(
    major_id    varchar(64) primary key comment '专业ID',
    major_name  varchar(20) not null comment '专业名称',
    category_id varchar(64) comment '所属大类ID'
);

# 初始化管理员账号
insert into user (user_id, user_password, email, phone)
values ('admin', '79b76dcf2445f517b3fee6dbb9a250c7', '1652806657@qq.com', '18023210419');

CREATE INDEX idx_grade_name
ON student (stu_grade, stu_name);

CREATE INDEX idx_grade_categoryId
ON student (stu_grade, category_id);

CREATE INDEX idx_categoryId
ON student (category_id);
