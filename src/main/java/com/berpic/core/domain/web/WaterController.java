package com.berpic.core.domain.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.berpic.core.domain.bean.UploadInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import com.berpic.core.domain.bean.ResultInfo;
import com.berpic.core.domain.util.CommonTools;
import com.berpic.core.domain.util.GlobalVar;
import com.berpic.core.domain.util.LimitUtils;
import com.berpic.core.domain.util.MarkConfig;
import com.berpic.core.domain.util.WaterVar;
import com.berpic.core.domain.util.XmlUtil;


/**
 * 标题.
 * <br>水印参数设置
 *
 * @author fengjian
 * @date 2015年3月30日 上午10:54:15
 */
@RestController
public class WaterController extends BaseController {

    @RequestMapping("/setWaterFile.xhtml")
    public void setWaterFile(HttpServletRequest request, HttpServletResponse response) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(GlobalVar.RESULTINFO_ERROR_CODE);
        //获取上传参数
        UploadInfo uploadInfo = CommonTools.getRequestParamsUpload(request);
        String type = CommonTools.isNotEmpty(uploadInfo.getType()) ? uploadInfo.getType() : "0";
        //String desc = "";
        try {
            if (CommonTools.isNotEmpty(uploadInfo)) {

                //渠道标识
                boolean resultChannel = LimitUtils.isExistsChannel(uploadInfo.getChannelFlag(), resultInfo);

                //模块验证
                boolean resultModule = LimitUtils.isExistsModule(uploadInfo.getModule(), resultInfo);
                //加密验证
                boolean resultsign = CommonTools.doSignUpload(uploadInfo, resultInfo);

                //单个文件大小验证
                boolean resultFileSize = CommonTools.checkFileSize(uploadInfo, resultInfo);
                //文件后缀名验证
                boolean resultFileSuffix = CommonTools.checkFileSuffix(uploadInfo, resultInfo);
                //文件头信息
                boolean resultFileType = CommonTools.checkFileType(uploadInfo, resultInfo);
                //所有验证全部通过后写入文件
                if (resultChannel && resultModule && resultsign && resultFileSize && resultFileSuffix && resultFileType) {
                    //写入文件
                    CommonTools.writeWaterFile(uploadInfo, resultInfo);
                    //获取上传成功后的文件列表
                    String path = "", size = "", name = "";
                    int length = CommonTools.isNotEmpty(resultInfo.getList()) ? resultInfo.getList().size() : 0;
                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            path += resultInfo.getList().get(i).toString().concat(",");
                            size += resultInfo.getFileSize().get(i).concat(",");
                            name += resultInfo.getFileName().get(i).concat(",");
                        }
                        if (path.indexOf(",") != -1) {
                            path = path.substring(0, path.length() - 1);
                            size = size.substring(0, size.length() - 1);
                            name = name.substring(0, name.length() - 1);
                        }
                        resultInfo.setCode(GlobalVar.RESULTINFO_SUCCESS_CODE);
                        //name = URLEncoder.encode(name,"utf-8");
                        //desc = CommonTools.isNotEmpty(resultInfo.getDesc())?URLEncoder.encode(resultInfo.getDesc(),"utf-8"):"";
                        response.sendRedirect(uploadInfo.getReturnUrl() + "?&code=" + resultInfo.getCode() + "&picurl=" + path + "&filesize=" + size + "&type=" + type + "&desc=" + resultInfo.getDesc() + "&filename=" + name);
                    }
                } else {
                    //desc = CommonTools.isNotEmpty(resultInfo.getDesc())?URLEncoder.encode(resultInfo.getDesc(),"utf-8"):"";
                    response.sendRedirect(uploadInfo.getReturnUrl() + "?&code=" + resultInfo.getCode() + "&type=" + type + "&desc=" + resultInfo.getDesc());
                    logger.info(resultInfo.getDesc());
                }
            } else {
                //desc = CommonTools.isNotEmpty(resultInfo.getDesc())?URLEncoder.encode(resultInfo.getDesc(),"utf-8"):"";
                response.sendRedirect(uploadInfo.getReturnUrl() + "?&code=" + resultInfo.getCode() + "&type=" + type + "&desc=" + resultInfo.getDesc());
                logger.error("上传参数获取失败");
            }
        } catch (IOException e) {
            resultInfo.setCode(GlobalVar.RESULTINFO_ERROR_CODE);
            logger.error("文件上传失败:" + e.getLocalizedMessage());
        }
    }

    @RequestMapping("/setWaterConfig.xhtml")
    @ResponseBody
    public ResultInfo setWaterConfig(HttpServletRequest request, HttpServletResponse response) {

        String flag = "";
        ResultInfo resultInfo = new ResultInfo();
        String userid = request.getParameter("userid");
        String sign = request.getParameter("sign");

        int markOn = Integer.parseInt(request.getParameter("markOn"));
        String markPath = request.getParameter("markPath");
        String markWidth = request.getParameter("markWidth");
        String markHeight = request.getParameter("markHeight");
        String markPosition = request.getParameter("markPosition");
        String markOffsetX = request.getParameter("markOffsetX");
        String markOffsetY = request.getParameter("markOffsetY");
        String markAlpha = request.getParameter("markAlpha");
        //加密验证
        boolean resultsign = CommonTools.doSignWaterConfigUpload(userid, markPath, sign, resultInfo);
        if (resultsign) {

            WaterVar.MARK_ON = markOn;
            WaterVar.MARK_PATH = markPath;
            if (StringUtils.isNotBlank(markWidth)) {
                WaterVar.MARK_WIDTH = Integer.parseInt(markWidth);
            }
            if (StringUtils.isNotBlank(markHeight)) {
                WaterVar.MARK_HEIGHT = Integer.parseInt(markHeight);
            }
            if (StringUtils.isNotBlank(markPosition)) {
                WaterVar.MARK_POSITION = Integer.parseInt(markPosition);
            }
            if (StringUtils.isNotBlank(markOffsetX)) {
                WaterVar.MARK_OFFSETX = Integer.parseInt(markOffsetX);
            }
            if (StringUtils.isNotBlank(markOffsetY)) {
                WaterVar.MARK_OFFSETY = Integer.parseInt(markOffsetY);
            }
            if (StringUtils.isNotBlank(markAlpha)) {
                WaterVar.MARK_ALPHA = Integer.parseInt(markAlpha);
            }
            if (XmlUtil.setXmlFileValue(GlobalVar.WATER_XML_FILE_CONFIG)) {
                flag = "0";
            } else {
                flag = "1";
            }
            resultInfo.setCode(flag);
        }

        return resultInfo;


    }

    @RequestMapping("/getWaterConfig.xhtml")
    @ResponseBody
    public MarkConfig getWaterConfig(HttpServletRequest request, HttpServletResponse response) {
        MarkConfig markConfig = new MarkConfig();
        try {

            File f = new File(GlobalVar.WATER_XML_FILE_CONFIG);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(f);

            String markOn = doc.getElementsByTagName("markOn").item(0).getFirstChild().getNodeValue();
            String markPath = doc.getElementsByTagName("markPath").item(0).getFirstChild().getNodeValue();
            String markWidth = doc.getElementsByTagName("markWidth").item(0).getFirstChild().getNodeValue();
            String markHeight = doc.getElementsByTagName("markHeight").item(0).getFirstChild().getNodeValue();
            String markPosition = doc.getElementsByTagName("markPosition").item(0).getFirstChild().getNodeValue();
            String markOffsetX = doc.getElementsByTagName("markOffsetX").item(0).getFirstChild().getNodeValue();
            String markOffsetY = doc.getElementsByTagName("markOffsetY").item(0).getFirstChild().getNodeValue();
            String markAlpha = doc.getElementsByTagName("markAlpha").item(0).getFirstChild().getNodeValue();

            boolean on = false;
            if ("1".equals(markOn)) {
                on = true;
            }
            markConfig.setOn(on);
            markConfig.setImagePath(markPath);
            markConfig.setMinWidth(Integer.valueOf(markWidth));
            markConfig.setMinHeight(Integer.valueOf(markHeight));
            markConfig.setPos(Integer.valueOf(markPosition));
            markConfig.setOffsetX(Integer.valueOf(markOffsetX));
            markConfig.setOffsetY(Integer.valueOf(markOffsetY));
            markConfig.setAlpha(Integer.valueOf(markAlpha));
        } catch (Exception e) {
            logger.error("获取水印xml文件值错误：", e);
        }
        return markConfig;

    }


}
