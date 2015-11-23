package com.berpic.core.domain.util;

import java.util.Date;

/**
 * 标题.
 * <br>水印设置常量
 *
 * @author fengjian
 * @date 2015年3月30日 上午9:34:09
 */

public class WaterVar {
    /**
     * 水印是否开启  1 开启  0 关闭
     */
    public static int MARK_ON = 1;

    /**
     * 水印路径
     */
    public static String MARK_PATH = "";

    /**
     * 小于该宽度图片不添加水印
     */
    public static int MARK_WIDTH = 120;

    /**
     * 小于该高度图片不添加水印
     */
    public static int MARK_HEIGHT = 120;

    /**
     * 水印位置(0-5)
     */
    public static int MARK_POSITION = 5;

    /**
     * x坐标偏移量
     */
    public static int MARK_OFFSETX = 50;

    /**
     * y坐标偏移量
     */
    public static int MARK_OFFSETY = 50;

    /**
     * 水印透明度（0-100）
     */
    public static int MARK_ALPHA = 50;

    /**
     * 记录读取水印配置文件时间
     */
    public static Date READ_WATER_CONFIG_DATE = null;


}
