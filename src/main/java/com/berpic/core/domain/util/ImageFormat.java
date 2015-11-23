package com.berpic.core.domain.util;

/**
 * 图片后缀定义
 *
 * @author chenchao 991722899@qq.com
 *         2013-2-24 下午12:22:40
 */
public enum ImageFormat {
    BMP("bmp"),
    JPG("jpg"),
    WBMP("wbmp"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif");

    private String value;

    private ImageFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
