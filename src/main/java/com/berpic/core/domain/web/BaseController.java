package com.berpic.core.domain.web;

import com.berpic.core.domain.util.CommonTools;
import com.berpic.core.domain.util.PropsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;


/**
 * 基础控制器
 *
 * @author chenchao 991722899@qq.com
 *         2013-2-20 下午3:45:09
 */
public class BaseController {
    //日志对象
    //protected Logger logger=Logger.getLogger(getClass());
    protected Logger logger = LoggerFactory.getLogger(getClass());
    //资源文本对象
    protected
    @Autowired
    MessageSource message;

    /**
     * 根据KEY获取资源文本信息
     *
     * @param key
     * @param obj
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-25 下午10:47:31
     */
    public String getMessageValue(String key, Object[] obj) {
        if (CommonTools.isNotEmpty(key)) {
            return message.getMessage(key, obj, null);
        } else {
            return null;
        }
    }

    /**
     * 根据KEY获取系统配置信息
     *
     * @param key
     * @param obj
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-25 下午10:48:10
     */
    public String getResourceValue(String key, Object[] obj) {
        if (CommonTools.isNotEmpty(key)) {
            //return resources.getMessage(key,obj,null);
            return PropsConfig.getPropValue(key, null);
        } else {
            return null;
        }
    }
}
