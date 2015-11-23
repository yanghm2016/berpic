package com.berpic.core.domain.server;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.berpic.core.domain.bean.ResultInfo;


/**
 * 图片移动接口
 *
 * @author yanghaoming
 */


public interface MovePic {
    /**
     * 返回ResultInfo 文件移动
     *
     * @param request
     * @param response
     * @return
     */
    public ResultInfo movePic(HttpServletRequest request, HttpServletResponse response);


    /**
     * 文件移动
     *
     * @param request
     * @param response
     * @return
     */
    public Map<String, Object> movePic3(HttpServletRequest request, HttpServletResponse response);
}
