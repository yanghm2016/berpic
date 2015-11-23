package com.berpic.core.domain.bean;

import java.util.List;

/**
 * 服务器响应信息
 *
 * @author chenchao 991722899@qq.com
 *         2013-2-19 上午9:08:43
 */
public class ResultInfo {
    //响应码0成功其它状态失败
    private String code;
    //返回List值
    private List<?> list;
    //单个对象值
    private Object obj;
    //说明
    private String desc;
    //文件大小
    private List<String> fileSize;
    //文件名称
    private List<String> fileName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getFileSize() {
        return fileSize;
    }

    public void setFileSize(List<String> fileSize) {
        this.fileSize = fileSize;
    }

    public List<String> getFileName() {
        return fileName;
    }

    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ResultInfo [code=" + code + ", list=" + list + ", obj=" + obj
                + ", desc=" + desc + ", fileSize=" + fileSize + ", fileName="
                + fileName + "]";
    }

}
