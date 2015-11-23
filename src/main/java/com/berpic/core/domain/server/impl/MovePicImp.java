package com.berpic.core.domain.server.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.berpic.core.domain.bean.MoveInfo;
import com.berpic.core.domain.server.MovePic;
import com.berpic.core.domain.util.CommonTools;
import org.springframework.stereotype.Service;

import com.berpic.core.domain.bean.ResultInfo;
import com.google.gson.Gson;

@Service
public class MovePicImp implements MovePic {

    @Override
    public ResultInfo movePic(HttpServletRequest request, HttpServletResponse response) {
        ResultInfo resultInfo = new ResultInfo();
        MoveInfo moveInfo = CommonTools.getRequestParamsMove(request);
        //加密验证
        boolean resultsign = CommonTools.doSignMove(moveInfo);
        //请求IP验证
        boolean resultIp = true;//CommonTools.checkIp(request.getRemoteAddr());
        if (CommonTools.isNotEmpty(moveInfo) && resultsign && resultIp) {
            CommonTools.moveFile(moveInfo, resultInfo);
            //resultInfo.setCode("0");
        } else {
            resultInfo.setCode("1");
        }
        return resultInfo;
    }

    @Override
    public Map<String, Object> movePic3(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Gson gson = new Gson();
        ResultInfo resultInfo = new ResultInfo();
        //获取图片移动参数
        MoveInfo moveInfo = CommonTools.getRequestParamsMove(request);
        //加密验证
//		boolean resultsign=CommonTools.doSignMove(moveInfo);
        boolean resultsign = true;
        //请求IP验证
        boolean resultIp = true;//CommonTools.checkIp(request.getRemoteAddr());
        if (CommonTools.isNotEmpty(moveInfo) && resultsign && resultIp) {
            CommonTools.moveFile(moveInfo, resultInfo);
            map.put("url", moveInfo.getImgs().get(0));
            //resultInfo.setCode("0");
        } else {
            resultInfo.setCode("1");
        }
        map.put("code", resultInfo.getCode());
        map.put("desc", resultInfo.getDesc());
        CommonTools.render(response, "text/json;charset=UTF-8", gson.toJson(map), request);
        return map;
    }

//	@Override
//	public ResultInfo movePic(HttpServletRequest request,
//			HttpServletResponse response) {
//		ResultInfo resultInfo=new ResultInfo();
//		//获取图片移动参数
//		MoveInfo moveInfo=CommonTools.getRequestParamsMove(request);
//		//加密验证
//		boolean resultsign=CommonTools.doSignMove(moveInfo);
//		//请求IP验证
//		boolean resultIp=true;//CommonTools.checkIp(request.getRemoteAddr());
//		if(CommonTools.isNotEmpty(moveInfo) && resultsign && resultIp){
//			CommonTools.moveFile(moveInfo, resultInfo);
//			//resultInfo.setCode("0");
//		}else{
//			resultInfo.setCode("1");
//		}
//		return resultInfo;
//	}
//
//	@Override
//	public void movePic3(HttpServletRequest request,
//			HttpServletResponse response) {
//		Map<String, Object> map = new LinkedHashMap<String,Object>();
//		Gson gson  = new Gson();
//		ResultInfo resultInfo=new ResultInfo();
//		//获取图片移动参数
//		MoveInfo moveInfo=CommonTools.getRequestParamsMove(request);
//		//加密验证
////		boolean resultsign=CommonTools.doSignMove(moveInfo);
//		boolean resultsign= true;
//		//请求IP验证
//		boolean resultIp=true;//CommonTools.checkIp(request.getRemoteAddr());
//		if(CommonTools.isNotEmpty(moveInfo) && resultsign && resultIp){
//			CommonTools.moveFile(moveInfo, resultInfo);
//			//resultInfo.setCode("0");
//		}else{
//			resultInfo.setCode("1");
//		}
//		map.put("code", resultInfo.getCode());
//		map.put("desc", resultInfo.getDesc());
//		CommonTools.render(response, "text/json;charset=UTF-8",gson.toJson(map),request);
//	}
//

}
