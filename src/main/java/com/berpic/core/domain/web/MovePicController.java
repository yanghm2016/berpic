package com.berpic.core.domain.web;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.berpic.core.domain.bean.MoveInfo;
import com.berpic.core.domain.util.CommonTools;
import com.berpic.core.domain.util.GlobalVar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.berpic.core.domain.bean.ResultInfo;
import com.google.gson.Gson;

@Controller
public class MovePicController extends BaseController {

    /**
     * 文件移动
     *
     * @param request
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-21 下午2:03:12
     */
    @RequestMapping("movepic.xhtml")
    @ResponseBody
    public ResultInfo MovePic(HttpServletRequest request, HttpServletResponse response) {
        String operatFlag = GlobalVar.OPERAT_FLAG;
        if (operatFlag.equals("false")) {
            logger.error("对不起，此服务器没有上传图片权限！");
            return null;
        }
        ResultInfo resultInfo = new ResultInfo();
        //获取图片移动参数
        MoveInfo moveInfo = CommonTools.getRequestParamsMove(request);
        //加密验证
        boolean resultsign = CommonTools.doSignMove(moveInfo);
        //请求IP验证
        boolean resultIp = true;//CommonTools.checkIp(request.getRemoteAddr());
        if (CommonTools.isNotEmpty(moveInfo) && resultsign && resultIp) {
            CommonTools.moveFile(moveInfo, resultInfo);
            resultInfo.setList(moveInfo.getImgs());
            //resultInfo.setCode("0");
        } else {
            resultInfo.setCode("1");
            logger.error("文件移动失败");
        }
        return resultInfo;
    }

    /**
     * 文件移动  移动端
     *
     * @param request
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-21 下午2:03:12
     * @author yanghm 修改原因：图片移动后，返回图片真实路径。
     * @date 2015-08-20 14:59:13
     */
    @RequestMapping("movepic3.xhtml")
    public void MovePic3(HttpServletRequest request, HttpServletResponse response) {
        String operatFlag = GlobalVar.OPERAT_FLAG;
        if (operatFlag.equals("false")) {
            logger.error("对不起，此服务器没有上传图片权限！");
            return;
        }
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Gson gson = new Gson();
        ResultInfo resultInfo = new ResultInfo();
        //获取图片移动参数
        MoveInfo moveInfo = CommonTools.getRequestParamsMove(request);
        //加密验证
        boolean resultsign = CommonTools.doSignMove(moveInfo);
//		boolean resultsign= true;
        //请求IP验证
        boolean resultIp = true;//CommonTools.checkIp(request.getRemoteAddr());
        if (CommonTools.isNotEmpty(moveInfo) && resultsign && resultIp) {
            CommonTools.moveFile(moveInfo, resultInfo);
            //resultInfo.setCode("0");
        } else {
            resultInfo.setCode("1");
            logger.error("文件移动失败");
        }
        map.put("code", resultInfo.getCode());
        map.put("desc", resultInfo.getDesc());
        //add by YangHaoming 组装图片路径参数，并返回   start
        String urlArr = "";
        for (String url : moveInfo.getImgs()) {
            urlArr += url + ",";
        }
        map.put("list", urlArr.substring(0, urlArr.length() - 1));
        //add end
        CommonTools.render(response, "text/json;charset=UTF-8", gson.toJson(map), request);
    }

}
