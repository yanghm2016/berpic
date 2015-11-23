package com.berpic.core.domain.util;

import javax.validation.constraints.Null;

/**
 * @author 任小斌
 * @version V1.0
 * @Title: Constants.java
 * @Package com.bersolr.core.domain.util
 * @Description: TODO(常量类，统一管理)
 * @date Aug 22, 2015 5:07:21 PM
 */
public class Constants {

    //********************************** 系统级别属性 start  ***************************//

    // 读取系统配置环境标示
    public static final String PIC_SYSTEM_CONF_FLAG = "spring.profiles.active";

    // 读取核心配置文件名称
    public static final String PIC_SYSTEM_CONF_APPLICATION = "application";
    // 系统输出信息
    public static final String PIC_SYSTEM_CONF_NAME_MESSAGE = "application-msg";
    // 系统本地开发环境
    public static final String PIC_SYSTEM_CONF_LOCAL_ = "application-dev";
    // 系统测试环境
    public static final String PIC_SYSTEM_CONF_SIT_ = "application-test";
    // 系统生产环境
    public static final String PIC_SYSTEM_CONF_PRD_ = "application-prod";


    //********************************** 系统级别属性 end ***************************//

    // 系统绝对路径
    public static final String SYSTEM_ROOT_PATH = Constants.class.getResource("/").getFile();

    public static final String PIC_ISEMPTY = "";


}
