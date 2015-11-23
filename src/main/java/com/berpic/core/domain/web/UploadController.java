package com.berpic.core.domain.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.berpic.core.domain.util.CommonTools;
import com.berpic.core.domain.util.GlobalVar;
import com.berpic.core.domain.util.LimitUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.berpic.core.domain.bean.MoveInfo;
import com.berpic.core.domain.bean.ResultInfo;
import com.berpic.core.domain.bean.UploadInfo;
import com.google.gson.Gson;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController extends BaseController {


    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public
    @ResponseBody
    String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    String handleFileUpload(@RequestParam("name") String name,
                            @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }


    /**
     * 文件上传
     *
     * @param request
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 上午10:23:17
     */
    @RequestMapping("/uploadfile.xhtml")
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) {

        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(GlobalVar.RESULTINFO_ERROR_CODE);
        request.getParameter("channelFlag");
        //获取上传参数
        UploadInfo uploadInfo = CommonTools.getRequestParamsUpload(request);
        String type = CommonTools.isNotEmpty(uploadInfo.getType()) ? uploadInfo.getType() : "0";
        String desc = "";
        String returnUrl = uploadInfo.getReturnUrl();
        try {
            String operatFlag = GlobalVar.OPERAT_FLAG;
            if (operatFlag.equals("false")) {
                logger.error("对不起，此服务器没有上传图片权限!");
//				logger.info(uploadInfo.getReturnUrl());
//				response.sendRedirect(uploadInfo.getReturnUrl()+"&code=88");
                return;
            }
            if (CommonTools.isNotEmpty(uploadInfo)) {
                //add by viwen 2014-12-12
                //渠道标识
//				boolean resultChannel =true;
                boolean resultChannel = LimitUtils.isExistsChannel(uploadInfo.getChannelFlag(), resultInfo);
                //模块验证
                boolean resultModule = LimitUtils.isExistsModule(uploadInfo.getModule(), resultInfo);
//				boolean resultModule= true;
                //加密验证
                boolean resultsign = CommonTools.doSignUpload(uploadInfo, resultInfo);
//				boolean resultsign= true;
                //单个文件大小验证
                boolean resultFileSize = CommonTools.checkFileSize(uploadInfo, resultInfo);
//				boolean resultFileSize = true;
                //文件后缀名验证
                boolean resultFileSuffix = CommonTools.checkFileSuffix(uploadInfo, resultInfo);
                //文件头信息
                boolean resultFileType = CommonTools.checkFileType(uploadInfo, resultInfo);

                if (StringUtils.isNotBlank(returnUrl) && returnUrl.indexOf("?") == -1) {
                    uploadInfo.setReturnUrl(returnUrl + "?");
                }
                //所以验证全部通过后写入文件
                if (resultChannel && resultModule && resultsign && resultFileSize && resultFileSuffix && resultFileType) {
                    //写入文件
                    CommonTools.writeFile(uploadInfo, resultInfo);
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
                        name = URLEncoder.encode(name, "utf-8");
                        //desc = CommonTools.isNotEmpty(resultInfo.getDesc())?URLEncoder.encode(resultInfo.getDesc(),"utf-8"):"";
                        logger.info(uploadInfo.getReturnUrl());
                        response.sendRedirect(uploadInfo.getReturnUrl() + "&code=" + resultInfo.getCode() + "&picurl=" + path.substring(path.indexOf("/files/") + 6) + "&filesize=" + size + "&type=" + type + "&desc=" + resultInfo.getDesc() + "&filename=" + name + "&hurl=" + request.getContextPath() + "&serverAddr=" + request.getRemoteAddr() + "&btnId=" + uploadInfo.getBtnId());
                    }
                } else {
                    //desc = CommonTools.isNotEmpty(resultInfo.getDesc())?URLEncoder.encode(resultInfo.getDesc(),"utf-8"):"";
                    response.sendRedirect(uploadInfo.getReturnUrl() + "&code=" + resultInfo.getCode() + "&type=" + type + "&desc=" + resultInfo.getDesc());
                }
            } else {
                if (StringUtils.isNotBlank(returnUrl) && returnUrl.indexOf("?") == -1) {
                    uploadInfo.setReturnUrl(returnUrl + "?");
                }
                //desc = CommonTools.isNotEmpty(resultInfo.getDesc())?URLEncoder.encode(resultInfo.getDesc(),"utf-8"):"";
                response.sendRedirect(uploadInfo.getReturnUrl() + "&code=" + resultInfo.getCode() + "&type=" + type + "&desc=" + resultInfo.getDesc());
                logger.error("上传参数获取失败:" + resultInfo.toString());
            }
        } catch (IOException e) {
            if (StringUtils.isNotBlank(returnUrl) && returnUrl.indexOf("?") == -1) {
                uploadInfo.setReturnUrl(returnUrl + "?");
            }
            resultInfo.setCode(GlobalVar.RESULTINFO_ERROR_CODE);
            try {
                response.sendRedirect(uploadInfo.getReturnUrl() + "&code=" + resultInfo.getCode() + "&type=" + type + "&desc=" + resultInfo.getDesc());
                logger.error("文件上传失败:" + e.getLocalizedMessage());
            } catch (IOException e1) {
                logger.error("文件上传失败:" + e.getLocalizedMessage());
            }

        }
    }


    /**
     * FCK编辑器文件上传
     */
    @RequestMapping("/uploadfile2.xhtml")
    public void uploadFile2(HttpServletRequest request, HttpServletResponse response) {

        String operatFlag = GlobalVar.OPERAT_FLAG;
        if (operatFlag.equals("false")) {
            logger.error("对不起，此服务器没有上传图片权限！");
            return;
        }
        //fck需要用到的参数
        String callback = request.getParameter("CKEditorFuncNum");


        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(GlobalVar.RESULTINFO_ERROR_CODE);
        //获取上传参数
        UploadInfo uploadInfo = CommonTools.getRequestParamsUploadFCK(request);
        String type = CommonTools.isNotEmpty(uploadInfo.getType()) ? uploadInfo.getType() : "0";
        String desc = "";
        try {

            if (CommonTools.isNotEmpty(uploadInfo)) {

                //add by viwen 2014-12-12
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
                //所以验证全部通过后写入文件
                if (resultChannel && resultModule && resultsign && resultFileSize && resultFileSuffix && resultFileType) {
                    //写入文件
                    CommonTools.writeFile(uploadInfo, resultInfo);
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


                        //-------移动图片start---------
                        ResultInfo bean = new ResultInfo();
                        MoveInfo moveInfo = new MoveInfo();
                        List<String> pathList = new ArrayList<String>();
                        String fileType = LimitUtils.suffixMap.get(CommonTools.getFileSuffixName(path));
                        pathList.add(path);
                        moveInfo.setImgs(pathList);
                        CommonTools.moveFile(moveInfo, bean);
                        if ("1".equals(fileType)) {
                            path = path.replace("imageTemp", "image");
                        } else if ("2".equals(fileType)) {
                            path = path.replace("docTemp", "doc");
                        } else if ("3".equals(fileType)) {
                            path = path.replace("mediaTemp", "media");
                        }
                        //-------移动图片end---------
                        //name = URLEncoder.encode(name,"utf-8");
                        //desc = CommonTools.isNotEmpty(resultInfo.getDesc())?URLEncoder.encode(resultInfo.getDesc(),"utf-8"):"";

                        response.sendRedirect(uploadInfo.getReturnUrl() + "?&code=" + resultInfo.getCode() + "&picurl=" + path + "&filesize=" + size + "&type=" + type + "&callbackNum=" + callback + "&desc=" + resultInfo.getDesc() + "&filename=" + name);
                    }
                } else {
                    //desc = CommonTools.isNotEmpty(resultInfo.getDesc())?URLEncoder.encode(resultInfo.getDesc(),"utf-8"):"";
                    response.sendRedirect(uploadInfo.getReturnUrl() + "?&code=" + resultInfo.getCode() + "&type=" + type + "&desc=" + resultInfo.getDesc());
                    logger.error(resultInfo.getDesc());
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


    /**
     * 移动端文件上传
     *
     * @param request
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 上午10:23:17
     */
    @RequestMapping("/uploadfile3.xhtml")
    public void uploadFile3(HttpServletRequest request, HttpServletResponse response) {
        String operatFlag = GlobalVar.OPERAT_FLAG;
        if (operatFlag.equals("false")) {
            logger.error("对不起，此服务器没有上传图片权限！");
            return;
        }
        Gson gson = new Gson();
        Map<String, Object> map = new LinkedHashMap<String, Object>();


        String json = "";

        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(GlobalVar.RESULTINFO_ERROR_CODE);
        //获取上传参数
        UploadInfo uploadInfo = CommonTools.getRequestParamsUpload(request);
        String type = CommonTools.isNotEmpty(uploadInfo.getType()) ? uploadInfo.getType() : "0";
        String desc = "";
        try {
            if (CommonTools.isNotEmpty(uploadInfo)) {

                //add by viwen 2014-12-12
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
                //所以验证全部通过后写入文件
                if (resultChannel && resultModule && resultsign && resultFileSize && resultFileSuffix && resultFileType) {
                    //写入文件
                    CommonTools.writeFile(uploadInfo, resultInfo);
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
                        desc = CommonTools.isNotEmpty(resultInfo.getDesc()) ? URLEncoder.encode(resultInfo.getDesc(), "utf-8") : "";

                        map.put("code", resultInfo.getCode());
                        map.put("picurl", path);
                        map.put("filename", name);
                        map.put("filesize", size);
                        map.put("type", type);
                        map.put("desc", resultInfo.getDesc());
                        //json=gson.toJson(map);
                        //response.sendRedirect(uploadInfo.getReturnUrl()+"?&code="+resultInfo.getCode()+"&picurl="+path+"&filesize="+size+"&type="+type+"&desc="+desc+"&filename="+name);

                        //CommonTools.render(response, "application/json;charset=UTF-8", json);

                    }
                } else {

                    desc = CommonTools.isNotEmpty(resultInfo.getDesc()) ? URLEncoder.encode(resultInfo.getDesc(), "utf-8") : "";
                    map.put("code", resultInfo.getCode());
                    map.put("desc", resultInfo.getDesc());
                    //json=gson.toJson(map);
                    //response.sendRedirect(uploadInfo.getReturnUrl()+"?&code="+resultInfo.getCode()+"&type="+type+"&desc="+desc);


                }
            } else {
                //desc = URLEncoder.encode("上传参数获取失败","utf-8");
                map.put("code", resultInfo.getCode());
                map.put("desc", resultInfo.getDesc());
                //json=gson.toJson(map);
                //response.sendRedirect(uploadInfo.getReturnUrl()+"?&code="+resultInfo.getCode()+"&type="+type+"&desc="+desc);
                json = gson.toJson(map);

            }

            json = gson.toJson(map);
            System.out.println("json:" + json);
            CommonTools.render(response, "text/json;charset=UTF-8", json, request);// application/json
        } catch (Exception e) {
            resultInfo.setCode(GlobalVar.RESULTINFO_ERROR_CODE);
            map.put("code", resultInfo.getCode());
            map.put("desc", "文件上传失败:" + e.getLocalizedMessage());
            json = gson.toJson(map);
            logger.error("文件上传失败:" + e.getLocalizedMessage());
            CommonTools.render(response, "text/json;charset=UTF-8", json, request);
        }

        //return json;
    }


    private void writeLog(Logger logger, String string, Object object) {
        // TODO Auto-generated method stub

    }


    /**
     * 文件上传
     *
     * @param request
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 上午10:23:17
     */
    @RequestMapping("/clearpic.xhtml")
    public void clearPic(HttpServletRequest request, HttpServletResponse response) {
        /*谨慎操作 暂时屏蔽*/
        /*CleanPic clean=new CleanPic();
        clean.execute();*/
    }


    public static void main(String args[]) {
        Gson gson = new Gson();
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("desc", "xxxx");
        map.put("url", "/data/http/ss");
        map.put("code", "0");
        String json = gson.toJson(map);
        System.out.println("json=" + json);
    }

    /**
     * zip文件上传
     *
     * @param request
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 上午10:23:17
     */
    @RequestMapping("/uploadzipfile.xhtml")
    public void uploadZipFile(HttpServletRequest request, HttpServletResponse response) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(GlobalVar.RESULTINFO_ERROR_CODE);
        request.getParameter("channelFlag");
        //获取上传参数
        UploadInfo uploadInfo = CommonTools.getRequestParamsUpload(request);

        String type = CommonTools.isNotEmpty(uploadInfo.getType()) ? uploadInfo.getType() : "0";
        String desc = "";
        String returnUrl = uploadInfo.getReturnUrl();
        try {
            if (CommonTools.isNotEmpty(uploadInfo)) {
                //add by viwen 2014-12-12
                //渠道标识
//				boolean resultChannel =true;
                boolean resultChannel = LimitUtils.isExistsChannel(uploadInfo.getChannelFlag(), resultInfo);
                //模块验证
                boolean resultModule = LimitUtils.isExistsModule(uploadInfo.getModule(), resultInfo);
//				boolean resultModule= true;
                //加密验证
                boolean resultsign = CommonTools.doSignUpload(uploadInfo, resultInfo);
//				boolean resultsign= true;
                //单个文件大小验证
                boolean resultFileSize = CommonTools.checkFileSize(uploadInfo, resultInfo);
//				boolean resultFileSize = true;
                //文件后缀名验证
                boolean resultFileSuffix = CommonTools.checkFileSuffix(uploadInfo, resultInfo);
                //文件头信息
                boolean resultFileType = CommonTools.checkFileType(uploadInfo, resultInfo);

                if (StringUtils.isNotBlank(returnUrl) && returnUrl.indexOf("?") == -1) {
                    uploadInfo.setReturnUrl(returnUrl + "?");
                }
                //所以验证全部通过后写入文件
                if (resultChannel && resultModule && resultsign && resultFileSize && resultFileSuffix && resultFileType) {
                    //写入文件
                    CommonTools.writeFile(uploadInfo, resultInfo);
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
                        logger.info(uploadInfo.getReturnUrl());
                        response.sendRedirect(uploadInfo.getReturnUrl() + "&code=" + resultInfo.getCode() + "&picurl=" + path + "&filesize=" + size + "&type=" + type + "&desc=" + resultInfo.getDesc() + "&filename=" + name + "&hurl=" + request.getContextPath() + "&serverAddr=" + request.getRemoteAddr() + "&btnId=" + uploadInfo.getBtnId());
                    }
                } else {
                    //desc = CommonTools.isNotEmpty(resultInfo.getDesc())?URLEncoder.encode(resultInfo.getDesc(),"utf-8"):"";
                    response.sendRedirect(uploadInfo.getReturnUrl() + "&code=" + resultInfo.getCode() + "&type=" + type + "&desc=" + resultInfo.getDesc());
                }
            } else {
                if (StringUtils.isNotBlank(returnUrl) && returnUrl.indexOf("?") == -1) {
                    uploadInfo.setReturnUrl(returnUrl + "?");
                }
                //desc = CommonTools.isNotEmpty(resultInfo.getDesc())?URLEncoder.encode(resultInfo.getDesc(),"utf-8"):"";
                response.sendRedirect(uploadInfo.getReturnUrl() + "&code=" + resultInfo.getCode() + "&type=" + type + "&desc=" + resultInfo.getDesc());
                logger.error("上传参数获取失败:" + resultInfo.toString());
            }
        } catch (IOException e) {
            if (StringUtils.isNotBlank(returnUrl) && returnUrl.indexOf("?") == -1) {
                uploadInfo.setReturnUrl(returnUrl + "?");
            }
            resultInfo.setCode(GlobalVar.RESULTINFO_ERROR_CODE);
            try {
                response.sendRedirect(uploadInfo.getReturnUrl() + "&code=" + resultInfo.getCode() + "&type=" + type + "&desc=" + resultInfo.getDesc());
                logger.error("文件上传失败:" + e.getLocalizedMessage());
            } catch (IOException e1) {
                logger.error("文件上传失败:" + e.getLocalizedMessage());
            }

        }
    }
}
