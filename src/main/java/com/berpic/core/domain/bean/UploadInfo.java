package com.berpic.core.domain.bean;

import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件上传信息
 *
 * @author chenchao 991722899@qq.com
 *         2013-2-19 上午9:03:17
 */
public class UploadInfo {

    //用户ID
    private String userid;
    //所属模块
    private String module;
    //返回路径
    private String returnUrl;
    //签名
    private String sign;
    //--添加----按钮id
    private String btnId;
    //上传文件集合
    private List<FileItem> fileItems;
    //文件名称集合
    private List<String> fileNames;
    //前台自定义ID
    private String type;
    //上传大小
    private String updateSize;
    //渠道标识（移动端，pc端）
    private String channelFlag;

    public String getChannelFlag() {
        return channelFlag;
    }

    public void setChannelFlag(String channelFlag) {
        this.channelFlag = channelFlag;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBtnId() {
        return btnId;
    }

    public void setBtnId(String btnId) {
        this.btnId = btnId;
    }

    public List<FileItem> getFileItems() {
        return fileItems;
    }

    public void setFileItems(List<FileItem> fileItems) {
        this.fileItems = fileItems;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateSize() {
        return updateSize;
    }

    public void setUpdateSize(String updateSize) {
        this.updateSize = updateSize;
    }
}
