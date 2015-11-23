package com.berpic.core.domain.bean;

import java.io.Serializable;

/**
 * 模块信息
 *
 * @author chenchao 991722899@qq.com
 *         2013-2-26 上午8:58:37
 */
public class ModuleInfo implements Serializable {
    private static final long serialVersionUID = 1742086475146776512L;

    //模块名称
    private String moduleName;
    //当前模块文件上传最大长度 单位字节
    private Long moduleMaxSize;


    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getModuleMaxSize() {
        return moduleMaxSize;
    }

    public void setModuleMaxSize(Long moduleMaxSize) {
        this.moduleMaxSize = moduleMaxSize;
    }

}
