package com.berpic.core.domain.util;

/**
 * 图片服务器响应数据
 *
 * @author chenchao 991722899@qq.com
 * @date 2014-8-14 下午4:50:28
 */
public class ResponseData {

    /**
     * 响应值 0表示操作成功
     */
    private String code;

    /**
     * 上传成功后的URL地址
     */
    private String picurl;

    /**
     * 用户自定义值
     */
    private String type;

    /**
     * 上传文件大小
     */
    private String filesize;

    private String callbackNum;

    /**
     * 上传文件名称
     */
    private String filename;

    private String desc;


    public String getCallbackNum() {
        return callbackNum;
    }

    public void setCallbackNum(String callbackNum) {
        this.callbackNum = callbackNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ResponseData [code=" + code + ", picurl=" + picurl + ", type="
                + type + ", filesize=" + filesize + ", callbackNum="
                + callbackNum + ", filename=" + filename + ", desc=" + desc
                + "]";
    }

}
