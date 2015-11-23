package com.berpic.core.domain.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 读取配置文件工具类
 *
 * @author lubang713
 */
public class PropsConfig {

    private static Logger logger = Logger.getLogger(PropsConfig.class);

    private static Properties props = new Properties();

    static {
        load();
    }

    private static synchronized void load() {


        InputStream is = null;
        try {
            if (props == null || props.isEmpty()) {
                // 获取配置文件
                String configName = System.getProperty("sys.config.name");
//				String configName = "sysconfig_.properties";
                ClassLoader clasLoader = PropsConfig.class.getClassLoader();
                if (clasLoader != null) {
                    is = clasLoader.getResourceAsStream(configName);
                    props.load(is);
                }
            }

        } catch (FileNotFoundException e) {
            logger.error("PropsConfig.load FileNotFoundException", e);
        } catch (IOException e) {
            logger.error("PropsConfig.load IOException", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error(" 关闭 InputStream 异常", e);
                }
            }
        }
    }

    public static String getPropValue(String key) {
        if (props == null || props.isEmpty()) {
            load();
        }
        return (String) props.get(key);
    }

    public static int getPropValue(String key, int defaultV) {
        return getPropValue(key) != null ? Integer.parseInt(getPropValue(key)) : defaultV;
    }

    public static String getPropValue(String key, String defaultV) {
        return getPropValue(key) != null ? getPropValue(key) : defaultV;
    }

    public static void main(String[] args) {
        System.out.println(getPropValue("upload_param_file_name_random_size"));

    }
}
