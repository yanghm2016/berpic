package com.berpic.core.domain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 任小斌  renxiaobin@berchin.com
 * @version V1.0
 * @Package com.bersolr.core.domain.util
 * @Description: (热词存入日志文件，为词库提取提供方便)
 * @date 2015/10/21 17:24
 */
public class LogUtils {


    private static final Logger logUitls = LoggerFactory.getLogger("logUtils");
    private static final Logger siteLog = LoggerFactory.getLogger("SiteLog");

    /**
     * 三维商城热词统计
     *
     * @param keywork
     */
    public static void getLogUitls(String keywork) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(keywork)) {
//            logUitls.info(StringUtil.replaceBlank(keywork.trim()));
        }
    }

    /**
     * 门户热词统计
     *
     * @param keywork
     */
    public static void getSiteLog(String keywork) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(keywork)) {
//            siteLog.info(StringUtil.replaceBlank(keywork.trim()));
        }
    }
}
