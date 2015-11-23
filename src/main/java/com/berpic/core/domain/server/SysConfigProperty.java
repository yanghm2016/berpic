package com.berpic.core.domain.server;


import com.berpic.core.domain.util.Constants;
import com.berpic.core.domain.util.EnumUitls;
import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

/**
 * @author 任小斌  renxiaobin@berchin.com
 * @version V1.0
 * @Package com.bersolr.core.domain.util
 * @Description: (读取配置文件)
 * @date 2015/10/16 13:02
 */

@Component
public class SysConfigProperty {

    // 记录日志
    private final static Logger logger = LoggerFactory.getLogger(SysConfigProperty.class);

    /**
     * 获取文件源对象
     *
     * @param profiles 配置文件名称
     * @return 源对象
     * @throws Exception
     */
    public ResourceBundle sysConfigProfiles(String profiles) throws Exception {
        // 图片服务器默认配置为开发环境
        profiles = Optional.fromNullable(profiles).or(EnumUitls.DEFAULT.getName());
        logger.info("兰银图片服务器读取系统配置文件：" + profiles);
        //  开发环境配置装载
        if (profiles.equals(EnumUitls.DEV.getName())) {
            return ResourceBundle.getBundle(Constants.PIC_SYSTEM_CONF_LOCAL_);
        }
        //  测试环境配置装载
        if (profiles.equals(EnumUitls.TEST.getName())) {
            return ResourceBundle.getBundle(Constants.PIC_SYSTEM_CONF_SIT_);
        }
        // 生产环境配置装载
        if (profiles.equals(EnumUitls.PROD.getName())) {
            return ResourceBundle.getBundle(Constants.PIC_SYSTEM_CONF_PRD_);
        }
        // 系统环境装载 application.properties
        if (profiles.equals(EnumUitls.DEFAULT.getName())) {
            return ResourceBundle.getBundle(Constants.PIC_SYSTEM_CONF_APPLICATION);
        }
        // 系统日志信息
        if (profiles.equals(EnumUitls.MSG.getName())) {
            return ResourceBundle.getBundle(Constants.PIC_SYSTEM_CONF_NAME_MESSAGE);
        }
        return null;
    }

    /**
     * 获取 key 对应的 value
     *
     * @param key
     * @return
     * @throws Exception
     */
    public String getString(String key) throws Exception {
        return sysConfigProfiles(EnumUitls.DEFAULT.getName())
                .getString(Optional.fromNullable(key).or(Constants.PIC_SYSTEM_CONF_FLAG));
    }
}

