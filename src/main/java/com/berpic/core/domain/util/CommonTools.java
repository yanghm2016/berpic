package com.berpic.core.domain.util;

import com.berpic.core.domain.bean.ModuleInfo;
import com.berpic.core.domain.util.gif.AnimatedGifEncoder;
import com.berpic.core.domain.util.gif.GifDecoder;
import com.berpic.core.domain.bean.MoveInfo;
import com.berpic.core.domain.bean.ResultInfo;
import com.berpic.core.domain.bean.UploadInfo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * 工具类
 *
 * @author chenchao 991722899@qq.com
 *         2013-2-21 下午5:05:45
 */
@Service
public class CommonTools {
    private
    @Autowired
    MessageSource message;

    protected static Logger logger = LoggerFactory.getLogger(CommonTools.class);
    protected static final String dateFormat = "yyyy-MM-dd";
    protected static final String dateFormat2 = "yyyyMMddHHmmssSSS";
    protected static final String dateFormat3 = "yyyyMMddhhmmss";

    private static MessageSource messageSource;


    @PostConstruct
    public void init() {
        messageSource = message;
    }

    /**
     * 判断对象不为空
     *
     * @param obj
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 上午11:48:18
     */
    public static boolean isNotEmpty(Object obj) {
        if (obj != null && obj != "") {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获得当前年月日路径
     *
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-21 下午5:12:12
     */
    public static String getDatePath() {
        String dateStr = getDateFormatString(dateFormat);
        return dateStr.replace("-", File.separator);

    }


    /**
     * 根据时间和指定随机数生成文件名称
     *
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-21 下午5:12:52
     */
    public static String getPicName() {
        return getDateFormatString(dateFormat2) + getRandomStr();
    }


    /**
     * 当前日期往前推X天
     *
     * @param date
     * @return
     */
    public static String getWeekdayBeforeDate(Date date, int day) {
        java.text.Format formatter = new java.text.SimpleDateFormat(dateFormat);
        long beforeTime = (date.getTime() / 1000) - 24 * 60 * 60 * day;
        date.setTime(beforeTime * 1000);
        return formatter.format(date);

    }

    /**
     * 获取随机串 可重复
     *
     * @return
     */
    public static String getRandomStr() {
        String[] str = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                "a", "b", "c", "d", "e", "f", "g",
                "h", "i", "j", "k", "l", "m", "n",
                "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z",
                "A", "B", "C", "D", "E", "F", "G",
                "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z",
        };

        String ret = "";
        int index = 0;
        for (int i = 0; i < GlobalVar.UPLOAD_PARAM_FILE_NAME_RANDOM_SIZE; i++) {
            index = (int) (Math.random() * str.length);
            ret += str[index];
        }
        return ret;
    }

    /**
     * 时间转换器
     * 第一个参数 要转化的数据类型   ---   java.util.Date
     * 第二个参数 要转化的数据       ---   "20101212121212"
     */
    public static Date convertStrTODate(Class<Date> type, String value) {
        if (value == null) {
            return null;
        } else {
            if (type == java.util.Date.class) {
                if (value instanceof String) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat3);
                        return sdf.parse(value);
                    } catch (ParseException e) {
                        throw new RuntimeException("您输入的数据格式不对");
                    }
                } else {
                    throw new RuntimeException("您要转化的数据输入不是String类型");
                }
            } else {
                throw new RuntimeException("您要转化的数据类型不对");
            }
        }
    }


    /**
     * 获取文件后缀
     *
     * @param filename
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 上午8:43:52
     */
    public static String getFileSuffix(String filename) {
        if (isNotEmpty(filename) && filename.indexOf(".") != -1) {
            return filename.substring(filename.lastIndexOf(".")).toLowerCase();
        } else {
            logger.info("获取文件后缀失败，无法解析文件名称");
        }
        return "";
    }

    /**
     * 获取文件后缀，不包含"."
     *
     * @param filename
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-24 下午2:57:13
     */
    public static String getFileSuffixName(String filename) {
        if (isNotEmpty(filename) && filename.indexOf(".") != -1) {
            return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        } else {
            logger.info("获取文件后缀失败，无法解析文件名称");
        }
        return "";
    }


    /**
     * 获取图片移动请求参数
     *
     * @param request
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-21 上午10:26:52
     */
    public static MoveInfo getRequestParamsMove(HttpServletRequest request) {
        MoveInfo moveInfo = new MoveInfo();
        moveInfo.setUserid(request.getParameter("userid"));
        moveInfo.setSign(request.getParameter("sign"));
        String temp = request.getParameter("imgs");
//		String temp=request.getParameter("newPath");
        List<String> url = new LinkedList<String>();
        if (isNotEmpty(temp) && isNotEmpty(temp.trim())) {
            //modify by yanghaoming  start
            //传参数最后一位是逗号情况，进行补位，对应返回图片路径个数
            if (temp.lastIndexOf(",") == temp.length() - 1) {
                temp += "nullFlag";
            }
            if (temp.indexOf(",") != -1) {
                String[] arr = temp.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (!arr[i].equals("nullFlag"))
                        url.add(arr[i]);
                    else
                        url.add("");
                }
                //modify end
                moveInfo.setImgs(url);
            } else {
                url.add(temp);
                moveInfo.setImgs(url);
            }
        } else {
            logger.error("需要移动的文件地址为空");
        }
        return moveInfo;
    }


    /**
     * 获取上传参数信息
     *
     * @param request
     * @author viwen 773632470@qq.com
     * @date 20141118
     */
    public static UploadInfo getRequestParamsUpload(HttpServletRequest request) {
        UploadInfo uploadInfo = new UploadInfo();
        if (ServletFileUpload.isMultipartContent(request)) {
            request.getParameter("type");
            request.getParameter("module");
            DiskFileItemFactory dff = new DiskFileItemFactory();
            //指定在内存中缓存数据大小,单位为byte
            dff.setSizeThreshold(GlobalVar.UPLOAD_PARAM_CACHE_SIZE);
            ServletFileUpload sfu = new ServletFileUpload(dff);
            sfu.setHeaderEncoding("UTF-8");
            sfu.setFileSizeMax(Long.MAX_VALUE);
            //指定一次上传多个文件的总尺寸
            sfu.setSizeMax(Long.MAX_VALUE);

            List<FileItem> listIS = new LinkedList<FileItem>();
            List<String> listName = new ArrayList<String>();
            List<?> list = null;
            try {
                list = sfu.parseRequest(request);
            } catch (Exception e) {
                logger.info("解析文件错误:" + e.getLocalizedMessage());
                return null;
            }
            FileItem item = null;
            Class<?> c = uploadInfo.getClass();
            Field[] fields = uploadInfo.getClass().getDeclaredFields();
            Method method = null;
            for (Object obj : list) {
                item = (FileItem) obj;
                if (!item.isFormField()) {
                    listIS.add(item);
                    listName.add(item.getName());
                    //new String(q_title.getBytes("ISO8859-1"),"UTF-8");
                } else {
                    //通过反射将请求的值封闭到对象中
                    try {
                        for (Field fi : fields) {
                            if (fi.getName().equalsIgnoreCase(item.getFieldName())) {
                                method = c.getDeclaredMethod("set" + fi.getName().substring(0, 1).toUpperCase() + fi.getName().substring(1), String.class);
                                method.invoke(uploadInfo, item.getString("utf-8"));
                                break;
                            }
                        }
                    } catch (Exception e) {
                        logger.error("请求参数映射到对象失败:" + e.getLocalizedMessage());
                        return null;
                    }
                }
            }
            //如果用户ID为0或为空，则随机一个指定倍数的编号
            //uploadInfo.setUserid(getRandomStr());
            uploadInfo.setFileItems(listIS);
            uploadInfo.setFileNames(listName);
            return uploadInfo;
        } else {
            return null;
        }
    }


    /**
     * 获取上传参数信息
     *
     * @param request
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 上午9:26:04
     */
    public static UploadInfo getRequestParamsUploadFCK(HttpServletRequest request) {

        String sign = request.getParameter("sign");
        String userid = request.getParameter("userid");
        String module = request.getParameter("module");
        String upurl = request.getParameter("upurl");
        String returnUrl = request.getParameter("returnUrl");
        String updateSize = request.getParameter("updateSize");
        String channelFlag = request.getParameter("channelFlag");
        //add by yanghaoming 页面图片对应按钮id start
        String btnId = request.getParameter("btnId");
        //add end


        UploadInfo uploadInfo = new UploadInfo();
        if (ServletFileUpload.isMultipartContent(request)) {
            DiskFileItemFactory dff = new DiskFileItemFactory();
            //指定在内存中缓存数据大小,单位为byte
            dff.setSizeThreshold(GlobalVar.UPLOAD_PARAM_CACHE_SIZE);
            ServletFileUpload sfu = new ServletFileUpload(dff);
            sfu.setFileSizeMax(Long.MAX_VALUE);
            //指定一次上传多个文件的总尺寸
            sfu.setSizeMax(Long.MAX_VALUE);

            List<FileItem> listIS = new LinkedList<FileItem>();
            List<String> listName = new ArrayList<String>();
            List<?> list = null;
            try {
                list = sfu.parseRequest(request);
            } catch (Exception e) {
                logger.info("解析文件错误:" + e.getLocalizedMessage());
                return null;
            }
            FileItem item = null;
            Class<?> c = uploadInfo.getClass();
            for (Object obj : list) {
                item = (FileItem) obj;
                if (!item.isFormField()) {
                    listIS.add(item);
                    listName.add(item.getName());
                }
            }
            //如果用户ID为0或为空，则随机一个指定倍数的编号
            //uploadInfo.setUserid(getRandomStr());
            uploadInfo.setFileItems(listIS);
            uploadInfo.setFileNames(listName);

            //封装
            uploadInfo.setSign(sign);
            uploadInfo.setModule(module);
            uploadInfo.setReturnUrl(returnUrl);
            uploadInfo.setUserid(userid);
            uploadInfo.setUpdateSize(updateSize);
            uploadInfo.setChannelFlag(channelFlag);

            //add by yanghaoming 添加上传按钮标识 start
            uploadInfo.setBtnId(btnId);
            //add end

            return uploadInfo;
        } else {
            return null;
        }
    }


    /**
     * 签名认证(上传)
     *
     * @param uploadInfo
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 上午11:18:41
     */
    public static boolean doSignUpload(UploadInfo uploadInfo, ResultInfo resultInfo) {
        if (isNotEmpty(uploadInfo.getModule()) && isNotEmpty(uploadInfo.getReturnUrl()) && isNotEmpty(uploadInfo.getUserid())) {
            logger.info("图片上传MD5信息:" + GlobalVar.signKey + "|" + uploadInfo.getChannelFlag() + "|" + uploadInfo.getModule() + "|" + uploadInfo.getReturnUrl() + "|" + uploadInfo.getUserid());
            String sSign = MD5.getMD5(GlobalVar.signKey + "|" + uploadInfo.getChannelFlag() + "|" + uploadInfo.getModule() + "|" + uploadInfo.getReturnUrl() + "|" + uploadInfo.getUserid());
            if (isNotEmpty(uploadInfo.getSign()) && uploadInfo.getSign().equals(sSign)) {
                return true;
            } else {
                resultInfo.setCode(GlobalVar.RESULTINFO_SIGNERROR_CODE);
                resultInfo.setDesc("签名验证失败");
                logger.error("签名验证失败");
                return false;
            }
        }
        return false;
    }


    /**
     * 检查上传文件大小是否配置范围内
     *
     * @param uploadInfo
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-26 下午2:53:43
     */
    public static boolean checkFileSize(UploadInfo uploadInfo, ResultInfo resultInfo) {
        ModuleInfo moduleInfo = LimitUtils.moduleMap.get(uploadInfo.getModule());
        long totalSize = 0;
        if (isNotEmpty(moduleInfo)) {
            if (isNotEmpty(uploadInfo.getUpdateSize())) {
                moduleInfo.setModuleMaxSize(Long.parseLong(uploadInfo.getUpdateSize()));
            }
            int length = isNotEmpty(uploadInfo.getFileItems()) ? uploadInfo.getFileItems().size() : 0;
            for (int i = 0; i < length; i++) {
                totalSize += uploadInfo.getFileItems().get(i).getSize();
                if (uploadInfo.getFileItems().get(i).getSize() > moduleInfo.getModuleMaxSize().longValue()) {
                    logger.error("单个文件上传，超出指定大小");
                    resultInfo.setCode(GlobalVar.RESULTINFO_OUTSIZE_CODE);
                    resultInfo.setDesc("单个文件上传，超出指定大小");
                    return false;
                }
            }
            if (totalSize > GlobalVar.UPLOAD_PARAM_FILE_TOTALS_SIZE) {
                logger.error("文件上传总大小超出指定大小");
                resultInfo.setDesc("文件上传总大小超出指定大小");
                return false;
            }
            return true;
        } else {
            logger.error("找不到模块对应的上传大小限制");
            resultInfo.setDesc("找不到模块对应的上传大小限制");
        }
        return false;
    }

    /**
     * 检查文件后缀是否在可支持的范围内
     *
     * @param uploadInfo
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-26 下午3:04:54
     */
    public static boolean checkFileSuffix(UploadInfo uploadInfo, ResultInfo resultInfo) {
        int length = isNotEmpty(uploadInfo.getFileNames()) ? uploadInfo.getFileNames().size() : 0;
        String suffix = "";
        for (int i = 0; i < length; i++) {
            suffix = getFileSuffixName(uploadInfo.getFileNames().get(i));
            if (!LimitUtils.suffixMap.containsKey(suffix)) {
                logger.error("后缀名:" + suffix + "不支持");
                resultInfo.setDesc("后缀名:" + suffix + "不支持");
                resultInfo.setCode(GlobalVar.RESULTINFO_SUFFIX_CODE);
                return false;
            }
        }
        return length > 0 ? true : false;
    }

    /**
     * 检查请求的IP是否在配置的IP集合中
     *
     * @param requestIp
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2014-3-1 上午10:11:17
     */
    public static boolean checkIp(String requestIp) {
        logger.info("图片移动请求IP:" + requestIp);
        if (isNotEmpty(requestIp)) {
            if (LimitUtils.ipMap.containsKey(requestIp)) {
                return true;
            } else {
                logger.error("IP验证失败");
            }
        }
        return false;
    }


    /**
     * 文件头信息检测
     *
     * @param uploadInfo
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-26 下午4:56:04
     */
    public static boolean checkFileType(UploadInfo uploadInfo, ResultInfo resultInfo) {
        int length = isNotEmpty(uploadInfo.getFileItems()) ? uploadInfo.getFileItems().size() : 0;
        String suffix = "";
        for (int i = 0; i < length; i++) {
            //当前格式的文件是否有文件头信息，如果有进行文件头信息验证
            suffix = getFileSuffixName(uploadInfo.getFileNames().get(i));
            if (LimitUtils.fileTypeMap.containsKey(suffix)) {
                byte[] b = new byte[4];
                try {
                    uploadInfo.getFileItems().get(i).getInputStream().read(b, 0, b.length);
                    String type = bytesToHexString(b);
                    //如果读取的文件头信息和对应的文件类型不正确，则视为非法文件
                    if (LimitUtils.fileTypeMap.containsKey(suffix)) {
                        if (!type.toUpperCase().contains(LimitUtils.fileTypeMap.get(suffix))) {
                            resultInfo.setCode(GlobalVar.RESULTINFO_FILETYPE_CODE);
                            resultInfo.setDesc("上传文件类型错误");
                            logger.error("上传文件类型错误");
                            return false;
                        }
                    }
                } catch (IOException e) {
                    logger.error("文件头解析出错:" + e.getLocalizedMessage());
                }
            }
        }
        return length > 0 ? true : false;
    }

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-26 下午5:00:38
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * 签名认证（图片移动）
     *
     * @param moveInfo
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-21 上午10:34:07
     */
    public static boolean doSignMove(MoveInfo moveInfo) {
        List<String> tempImgs = moveInfo.getImgs();
        if (tempImgs == null || tempImgs.size() <= 0)
            return false;
        String tempStr = "";
        for (String s : tempImgs) {
            tempStr += s.concat(",");
        }
        tempStr = tempStr.substring(0, tempStr.length() - 1);
        String ssign = MD5.getMD5(tempStr + "|" + GlobalVar.signKey + "|" + moveInfo.getUserid());
        if (ssign.equals(moveInfo.getSign())) {
            return true;
        } else {
            logger.error("图片移动签名验证失败");
            return false;
        }

    }

    /**
     * 写入文件到指定地址
     *
     * @param uploadInfo
     * @param resultInfo
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 下午12:18:57
     */
    public static ResultInfo writeFile(UploadInfo uploadInfo, ResultInfo resultInfo) {

        //保存上传成功的文件地址
        List<String> paths = new LinkedList<String>();
        //保存上传文件大小
        List<String> filesize = new LinkedList<String>();
        //保存文件名称
        List<String> files = new LinkedList<String>();
        //原文件名，新文件名,文件后缀,新文件写入地址
        String fileName = "", newFileName = "", suffix = "", newFilePath = "", docFilePath = "", pdfFilePath = "";

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        FileOutputStream fos = null;
        try {
            //文件总个数
            int length = uploadInfo.getFileItems().size();
            logger.info("上传文件总个数:" + length);
            String type = "";
            String rootPath = "";
            StringBuffer tmpDir;
            for (int i = 0; i < length; i++) {
                fileName = uploadInfo.getFileNames().get(i);
                type = LimitUtils.suffixMap.get(getFileSuffixName(fileName));
                //根据模块创建文件夹
                tmpDir = new StringBuffer();
                if ("1".equals(type)) {
                    rootPath = GlobalVar.PIC_ROOT_PATH_TEMP;
                } else if ("2".equals(type)) {
                    rootPath = GlobalVar.DOC_ROOT_PATH_TEMP;
                } else if ("3".equals(type)) {
                    rootPath = GlobalVar.MEDIA_ROOT_PATH_TEMP;
                }
                tmpDir.append(rootPath).append(File.separator)
                        .append(uploadInfo.getChannelFlag()).append(File.separator)//渠道标识 mobile pc
                        .append(CommonTools.getDatePath()).append(File.separator)
                        .append(uploadInfo.getUserid()).append(File.separator)
                        .append(uploadInfo.getModule());
                newFileName = getPicName();
                if (!isNotEmpty(fileName)) {
                    logger.error("文件名称为空:" + i);
                    break;
                }
                //获得文件输入流
                in = new BufferedInputStream(uploadInfo.getFileItems().get(i).getInputStream());
                logger.info("获得文件输入流");
                //文件后缀
                suffix = CommonTools.getFileSuffix(fileName);
                //根据图片名称创建文件夹
                createDir(tmpDir.toString());
                logger.info("根据图片名称创建文件夹");
                //最终写入地址
                newFilePath = tmpDir.toString().concat(File.separator).concat(newFileName).concat(suffix);
                logger.info("写入地址:" + newFilePath);
                //docFilePath = newFilePath.substring(0, newFilePath.lastIndexOf("."))+suffix;
                //pdfFilePath = newFilePath.substring(0, newFilePath.lastIndexOf("."))+".pdf";
                //返回给客户端的图片地址列表(文件地址)

                String uri = newFilePath.replace(GlobalVar.ROOT_PATH, "").replace(File.separator, GlobalVar.WEB_SPLIT_CHAR);
                logger.info("返回给客户端的图片地址列表(文件地址):" + uri);
                newFilePath.replace(rootPath, "").replace(File.separator, GlobalVar.WEB_SPLIT_CHAR);
                //返回给客户端的图片地址列表(转换后的HTTP地址)
                paths.add(uri);
                //返回给客户端的图片大小
                filesize.add(String.valueOf(uploadInfo.getFileItems().get(i).getSize()));
                //返回客户端文件名称
                files.add(fileName);
                File file = new File(newFilePath);
                fos = new FileOutputStream(file);
                out = new BufferedOutputStream(fos);
                logger.info("文件写入地址:" + newFilePath);

                //添加图片水印
                //File waterFile = new File(GlobalVar.ROOT_PATH+GlobalVar.WEB_SPLIT_CHAR+"default"+GlobalVar.WEB_SPLIT_CHAR+"watermark.png");
                String waterPath = GlobalVar.ROOT_PATH + WaterVar.MARK_PATH;
                //通过水印xml最后修改时间，判断水印xml是否更新
                if (XmlUtil.getFileLastModified(GlobalVar.WATER_XML_FILE_CONFIG).after(WaterVar.READ_WATER_CONFIG_DATE)) {
                    XmlUtil.getXmlFileValue(GlobalVar.WATER_XML_FILE_CONFIG);
                }
                File waterFile = new File(waterPath);
                if ((".jpg".equals(suffix) || ".jpeg".equals(suffix) || ".png".equals(suffix) || ".bmp".equals(suffix)) && WaterVar.MARK_ON == 1 && waterFile.exists()) {
                    imageMark(in, file, WaterVar.MARK_WIDTH, WaterVar.MARK_HEIGHT, WaterVar.MARK_POSITION, WaterVar.MARK_OFFSETX, WaterVar.MARK_OFFSETY, WaterVar.MARK_ALPHA, waterFile);
                } else if (".gif".equals(suffix) && WaterVar.MARK_ON == 1 && waterFile.exists()) {
                    imageGifMark(in, file, WaterVar.MARK_WIDTH, WaterVar.MARK_HEIGHT, WaterVar.MARK_POSITION, WaterVar.MARK_OFFSETX, WaterVar.MARK_OFFSETY, WaterVar.MARK_ALPHA, waterFile, out);
                } else {
                    //开始把文件写到你指定的上传文件夹
                    Streams.copy(in, out, true);
                }


                //判断是否需要将文件转换为swf  暂时不用
                /*if(GlobalVar.CONVERT_TOSWF_FILES.indexOf(CommonTools.getFileSuffixName(fileName))>-1){
                    if(".pdf".equals(suffix)){//pdf直接转换为swf,无需删除pdf文件
						DocToSwf.pdf2swf(pdfFilePath,false);
						
					}else{//其他类型文件需先转换为pdf,然后将pdf转换为swf,最后将生成pdf文件删除
						DocToSwf.docToPdf(docFilePath);
						DocToSwf.pdf2swf(pdfFilePath,true);
						 
					}
				}*/

            }
            resultInfo.setList(paths);
            resultInfo.setFileSize(filesize);
            resultInfo.setFileName(files);
            resultInfo.setCode("0");
            logger.info("文件上传成功:" + newFilePath);
        } catch (Exception e) {
            resultInfo.setCode("1");
            logger.error("文件上传失败:" + newFilePath);
            logger.error(e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            } catch (Exception e2) {
                resultInfo.setCode("1");
                logger.error("流关闭失败");
            }

        }
        return resultInfo;
    }


    /**
     * 写入水印文件到指定地址
     *
     * @param uploadInfo
     * @param resultInfo
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 下午12:18:57
     */
    public static ResultInfo writeWaterFile(UploadInfo uploadInfo, ResultInfo resultInfo) {

        //保存上传成功的文件地址
        List<String> paths = new LinkedList<String>();
        //保存上传文件大小
        List<String> filesize = new LinkedList<String>();
        //保存文件名称
        List<String> files = new LinkedList<String>();
        //原文件名，新文件名,文件后缀,新文件写入地址
        String fileName = "", newFileName = "", suffix = "", newFilePath = "";

        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        FileOutputStream fos = null;
        try {
            //文件总个数
            int length = uploadInfo.getFileItems().size();
            logger.info("上传文件总个数:" + length);
            String type = "";
            String rootPath = "";
            StringBuffer tmpDir;
            for (int i = 0; i < length; i++) {
                fileName = uploadInfo.getFileNames().get(i);
                type = LimitUtils.suffixMap.get(getFileSuffixName(fileName));
                //根据模块创建文件夹
                tmpDir = new StringBuffer();
                /*if("1".equals(type)){
                    rootPath = GlobalVar.PIC_ROOT_PATH_TEMP;
				}else if("2".equals(type)){
					rootPath = GlobalVar.DOC_ROOT_PATH_TEMP;
				}else if("3".equals(type)){
					rootPath = GlobalVar.MEDIA_ROOT_PATH_TEMP;
				}*/
                rootPath = GlobalVar.WATER_FILE_ROOT_PATH;
                tmpDir.append(rootPath);
                /*.append(File.separator)
                    .append(uploadInfo.getChannelFlag()).append(File.separator)//渠道标识 mobile pc
					.append(CommonTools.getDatePath()).append(File.separator)
					.append(uploadInfo.getUserid()).append(File.separator)
					.append(uploadInfo.getModule());*/
                newFileName = getPicName();
                if (!isNotEmpty(fileName)) {
                    logger.error("文件名称为空:" + i);
                    break;
                }
                //获得文件输入流
                in = new BufferedInputStream(uploadInfo.getFileItems().get(i).getInputStream());
                logger.info("获得文件输入流");
                //文件后缀
                suffix = CommonTools.getFileSuffix(fileName);
                //根据图片名称创建文件夹
                createDir(tmpDir.toString());
                logger.info("根据图片名称创建文件夹");
                //最终写入地址
                newFilePath = tmpDir.toString().concat(File.separator).concat(newFileName).concat(suffix);
                logger.info("写入地址:" + newFilePath);
                //docFilePath = newFilePath.substring(0, newFilePath.lastIndexOf("."))+suffix;
                //pdfFilePath = newFilePath.substring(0, newFilePath.lastIndexOf("."))+".pdf";
                //返回给客户端的图片地址列表(文件地址)

                String uri = newFilePath.replace(GlobalVar.ROOT_PATH, "").replace(File.separator, GlobalVar.WEB_SPLIT_CHAR);
                logger.info("返回给客户端的图片地址列表(文件地址):" + uri);
                newFilePath.replace(rootPath, "").replace(File.separator, GlobalVar.WEB_SPLIT_CHAR);
                //返回给客户端的图片地址列表(转换后的HTTP地址)
                paths.add(uri);
                //返回给客户端的图片大小
                filesize.add(String.valueOf(uploadInfo.getFileItems().get(i).getSize()));
                //返回客户端文件名称
                files.add(fileName);
                File file = new File(newFilePath);
                fos = new FileOutputStream(file);
                out = new BufferedOutputStream(fos);
                logger.info("文件写入地址:" + newFilePath);

                //开始把文件写到你指定的上传文件夹
                Streams.copy(in, out, true);


            }
            resultInfo.setList(paths);
            resultInfo.setFileSize(filesize);
            resultInfo.setFileName(files);
            resultInfo.setCode("0");
            logger.info("文件上传成功:" + newFilePath);
        } catch (Exception e) {
            resultInfo.setCode("1");
            logger.error("文件上传失败:" + newFilePath);
            logger.error(e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            } catch (Exception e2) {
                resultInfo.setCode("1");
                logger.error("流关闭失败");
            }

        }
        return resultInfo;
    }

    /**
     * 移动图片到指定地址
     *
     * @param moveInfo   要被移动的信息
     * @param resultInfo 移动之后的信息
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-21 上午10:45:44
     */
    public static ResultInfo moveFile(MoveInfo moveInfo, ResultInfo resultInfo) {
        List<String> tempPath = moveInfo.getImgs();//获取所有的临时目录
        List<String> movedFile = new ArrayList<String>();
        //打印入参日志
        for (int i = 0; i < tempPath.size(); i++) {
            logger.info("move img params=" + tempPath.get(i));
        }
        //添加新路径保存
        List<String> newPaths = new ArrayList<String>();

        String rpath = "", wpath = "";
        boolean flg = true;
        //如果存在要被移动的图片
        if (isNotEmpty(tempPath) && tempPath.size() > 0) {
            //循环移动图片
            for (String path : tempPath) {
                //请求是否包含图片域名
                //modify by Tangxc
                //if(path.contains(GlobalVar.PIC_SERVER_DOMAIN_TEMP)){
                if (path.contains("/imageTemp/")) {
                    //将图片域名替换为临时地址根目录
                    //rpath=path.replace(GlobalVar.PIC_SERVER_DOMAIN_TEMP,GlobalVar.PIC_ROOT_PATH_TEMP).replace("/",File.separator);
                    rpath = GlobalVar.PIC_ROOT_PATH_TEMP.concat(path.substring(path.indexOf("/imageTemp/") + 10).replace(GlobalVar.WEB_SPLIT_CHAR, File.separator));
                    //将临时地址根目录替换为正式根目录
                    wpath = rpath.replace(GlobalVar.PIC_ROOT_PATH_TEMP, GlobalVar.PIC_ROOT_PATH);
                    flg = true;
                } else if (path.contains("/docTemp/")) {
                    //将文档域名替换为临时地址目录
                    rpath = GlobalVar.DOC_ROOT_PATH_TEMP.concat(path.substring(path.indexOf("/docTemp/") + 8).replace(GlobalVar.WEB_SPLIT_CHAR, File.separator));
                    //将临时地址根目录替换为正式根目录
                    wpath = rpath.replace(GlobalVar.DOC_ROOT_PATH_TEMP, GlobalVar.DOC_ROOT_PATH);
                    flg = true;
                } else if (path.contains("/mediaTemp/")) {
                    //将音视频域名替换为正式根目录
                    rpath = GlobalVar.MEDIA_ROOT_PATH_TEMP.concat(path.substring(path.indexOf("/mediaTemp/") + 10).replace(GlobalVar.WEB_SPLIT_CHAR, File.separator));
                    //将临时地址根目录替换为正式根目录
                    wpath = rpath.replace(GlobalVar.MEDIA_ROOT_PATH_TEMP, GlobalVar.MEDIA_ROOT_PATH);
                    flg = true;
                } else if (path.contains("/imageScale/")) {
                    //将缩放图片域名替换为正式根目录
                    rpath = GlobalVar.PIC_ROOT_SCALE_TEMP.concat(path.substring(path.indexOf("/scaleTemp/") + 10).replace(GlobalVar.WEB_SPLIT_CHAR, File.separator));
                    //将临时地址根目录替换为正式根目录
                    wpath = rpath.replace(GlobalVar.PIC_ROOT_SCALE_TEMP, GlobalVar.PIC_ROOT_SCALE);
                    flg = true;
                } else {
                    logger.info("文件地址:" + path + "不移动，原地址返回");
                    if (path.indexOf("image/") != -1) {
                        wpath = path;
                    } else {
                        wpath = path;
                    }
                    flg = false;
                }
                //如果修改成功
                if (flg) {
                    //创建正式图片地址文件夹
                    createDir(wpath.substring(0, wpath.lastIndexOf(File.separator)));
                    flg = moveFile(rpath, wpath);
                    //add by yanghm start
                    //------添加新路径保存-------------
                    newPaths.add(wpath.substring(wpath.indexOf("cportalFileServer/files/") + 24));
                    //add end
                    //如果移动失败
                    if (!flg) {
                        resultInfo.setCode("1");
                        //移动失败需删除所有移动成功文件
                        for (String cleanPath : movedFile) {
                            File file = new File(cleanPath);
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                        return resultInfo;
                    } else {
                        movedFile.add(wpath);
                        resultInfo.setCode("0");
                    }
                } else {
                    //add by yanghm start
                    //如果路径不需要移动，原样返回
                    if (StringUtils.isNotBlank(wpath)) {
                        if (wpath.indexOf("files/") != -1) {
                            wpath = wpath.substring(wpath.indexOf("files/") + 6);
                        }

                    }
                    newPaths.add(wpath);
                    resultInfo.setCode("0");
                    //add end
                }
				/*//创建正式图片地址文件夹
				createDir(wpath.substring(0,wpath.lastIndexOf(File.separator)));
				boolean flg=moveFile(rpath,wpath);
				if(!flg){
					//移动失败需删除所有移动成功文件
					
				}else{
					resultInfo.setCode("0");
					movedFile.add(wpath);
				}*/
            }
            //moveInfo 设置新路径
            moveInfo.setImgs(newPaths);

        } else {
            logger.error("文件移动失败，没可移动的文件地址数据");
            resultInfo.setCode("1");
        }
        return resultInfo;
    }

    private static final String SERVER_NAME = "cportalFileServer";
    private static final String SERVER_PATH = "/";

    /**
     * 公共图片移动方法
     *
     * @param filePath
     * @param newFilePath
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-21 上午11:04:54
     */
    public static boolean moveFile(String filePath, String newFilePath) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                in = new BufferedInputStream(new FileInputStream(file));
                out = new BufferedOutputStream(new FileOutputStream(new File(newFilePath)));
                Streams.copy(in, out, true);
                logger.info(messageSource.getMessage("file_move_success", new Object[]{filePath, newFilePath}, null));
				/*
				//如果是需要转换swf文件,则swf文件一起移动   现在不用转换 故不移动swf文件
				String swfFilePath= filePath.substring(0, filePath.lastIndexOf("."))+".swf";
				String newSwfFilePath= newFilePath.substring(0, newFilePath.lastIndexOf("."))+".swf";
				String suffix = CommonTools.getFileSuffixName(filePath);
				if(GlobalVar.CONVERT_TOSWF_FILES.indexOf(suffix)>-1){
					File swfFile = new File(swfFilePath);
					if(swfFile.exists()){
						in =  new BufferedInputStream(new FileInputStream(swfFile));
						out =  new BufferedOutputStream(new FileOutputStream(new File(newSwfFilePath)));
						Streams.copy(in, out, true);
						writeLog(logger,messageSource.getMessage("file_move_success",new Object[]{swfFile,newSwfFilePath},null),null);
						return true;
					}else{
						//删除移动的文件
						File newFile=new File(newFilePath);
						if(newFile.exists()){
							newFile.delete();
						}
						writeLog(logger,messageSource.getMessage("file_move_empty",new Object[]{swfFile},null),null);
						return  false;
					}
				}*/
                return true;
            } else {
                logger.error(messageSource.getMessage("file_move_empty", new Object[]{filePath}, null));
                return false;
            }

        } catch (FileNotFoundException e) {
            logger.error(messageSource.getMessage("file_move_empty", new Object[]{e.getLocalizedMessage()}, null));
            return false;
        } catch (IOException e) {
            logger.error(messageSource.getMessage("file_move_empty", new Object[]{e.getLocalizedMessage()}, null));
            return false;
        }

    }

    /**
     * 根据路径创建文件夹
     *
     * @param path
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 上午11:32:40
     */
    public static String createDir(String path) {
        if (isNotEmpty(path)) {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
                logger.info("create_dir_success:" + path);
                return path;
            } else {
                logger.info("dir_exists:" + path);
                return "";
            }
        } else {
            logger.error("dir_path_empty:" + path);
        }
        return "";
    }

    /**
     *	公共日志写入
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 上午11:41:51
     * @param mess
     * @param lev
     */
	/*public static void writeLog(Logger log,String mess,String lev){
		if(!isNotEmpty(lev)){
			log.info(mess); 
		}else{
			if(lev.equals("1")){
				log.debug(mess);
			}else if(lev.equals("2")){
				log.error(mess);
			}
		}
	}*/

    /**
     * 获取项目发布路径
     *
     * @param request
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-19 下午2:14:53
     */
    public static String getTomcatUrl(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("");
    }

    /**
     * 根据指定格式创建一个时间字符串
     *
     * @param formatStr
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-21 下午5:09:25
     */
    public static String getDateFormatString(String formatStr) {
        return new SimpleDateFormat(formatStr).format(new Date());
    }

    /**
     * 根据图片尺寸大小、后缀名，格式化图片名称并返回
     *
     * @param width
     * @param height
     * @param srcpicExt
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-24 上午10:57:09
     */
    public static String getPicName(String width, String height, String srcpicExt) {
        if (isNotEmpty(width) && isNotEmpty(height) && isNotEmpty(srcpicExt)) {
            return width.concat(GlobalVar.PIC_NAME_SPLIT).concat(height).concat(srcpicExt);
        } else {
            logger.info(messageSource.getMessage("form_pic_name_error", null, null));
            return null;
        }
    }

    /**
     * 根据创建文件夹链接
     *
     * @author tangxc
     * @srcpath 源路径
     * @lnkpath 链接路径
     */
    public static String createLinkDir(String srcpath, String lnkpath) {
        if (isNotEmpty(lnkpath)) {
            File file = new File(lnkpath);
            if (!file.exists()) {
                String lnkStr = "ln -s " + srcpath + " " + lnkpath;

                callShell(lnkStr);
                logger.info(messageSource.getMessage("create_dir_success", new Object[]{lnkpath}, null));
                return lnkpath;
            } else {
                logger.info(messageSource.getMessage("dir_exists", new Object[]{lnkpath}, null));
                return "";
            }
        } else {
            logger.error(messageSource.getMessage("dir_path_empty", new Object[]{lnkpath}, null));
        }
        return "";
    }


    /**
     * 执行Shell命令，并获取执行结果
     *
     * @author tangxc
     */
    public static List<String> callShell(String shellStr) {
        Process process = null;
        List<String> pList = new ArrayList<String>();
        try {
            process = Runtime.getRuntime().exec(shellStr);
            int exitVal = process.waitFor();
            if (0 != exitVal) {
                //这里返回错误状态码
                pList.add(String.valueOf(exitVal));
            } else {
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";
                while ((line = input.readLine()) != null) {
                    pList.add(line);
                }
                if (input != null) {
                    input.close();
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            process.destroy();
        }

        return pList;
    }


    /**
     * 发送内容。使用UTF-8编码。
     *
     * @param response
     * @param contentType
     * @param text
     * @throws IOException
     */
    public static void render(HttpServletResponse response, String contentType,
                              String text, HttpServletRequest request) {
        response.setContentType(contentType);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Access-Control-Allow-Origin", "*");//'*'表示允许所有域名访问，可以设置为指定域名访问，多个域名中间用','隔开
        response.addHeader("Access-Control-Allow-Methods", "POST,GET");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        //如果IE浏览器则设置头信息如下

        if ("IE".equals(request.getParameter("type"))) {

            response.addHeader("XDomainRequestAllowed", "1");

        }

        OutputStream out = null;
        try {
            out = response.getOutputStream();
            out.write(text.getBytes());

        } catch (IOException e) {
            logger.error(e.getMessage().toString());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                logger.error("关闭OutputStream异常", e);
            }
        }

    }


    /**
     * 添加图片水印
     *
     * @param bis       源图片文件。需要加水印的图片文件。
     * @param destFile  目标图片。加水印后保存的文件。如果和源图片文件一致，则覆盖源图片文件。
     * @param minWidth  需要加水印的最小宽度，如果源图片宽度小于该宽度，则不加水印。
     * @param minHeight 需要加水印的最小高度，如果源图片高度小于该高度，则不加水印。
     * @param pos       加水印的位置。
     * @param offsetX   加水印的位置的偏移量x。
     * @param offsetY   加水印的位置的偏移量y。
     * @param alpha     加水印透明度（0-100）。
     * @param markFile  水印图片
     * @throws IOException
     */
    public static void imageMark(BufferedInputStream bis, File destFile, int minWidth,
                                 int minHeight, int pos, int offsetX, int offsetY, int alpha, File markFile)
            throws IOException {
        BufferedImage imgBuff = ImageIO.read(bis);
        int width = imgBuff.getWidth();
        int height = imgBuff.getHeight();
		/*BufferedImage waterBuff = ImageIO.read(markFile);
		int waterWidth = waterBuff.getWidth()+5;
		int waterHeight = waterBuff.getHeight()+5;*/
        if (width >= minWidth && height >= minHeight) {
            ImageUtil.Position p = ImageUtil.markPosition(width, height, pos, offsetX,
                    offsetY);
            Graphics2D g = imgBuff.createGraphics();
            AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) alpha / 100);
            g.setComposite(a);
            g.drawImage(ImageIO.read(markFile), ((ImageUtil.Position) p).getX(), ((ImageUtil.Position) p).getY(), null);
            g.dispose();
        }
        ImageIO.write(imgBuff, "png", destFile);
        imgBuff = null;
        //waterBuff = null;


    }

    /**
     * 添加图片水印
     *
     * @param bis       源图片文件。需要加水印的图片文件。
     * @param destFile  目标图片。加水印后保存的文件。如果和源图片文件一致，则覆盖源图片文件。
     * @param minWidth  需要加水印的最小宽度，如果源图片宽度小于该宽度，则不加水印。
     * @param minHeight 需要加水印的最小高度，如果源图片高度小于该高度，则不加水印。
     * @param pos       加水印的位置。
     * @param offsetX   加水印的位置的偏移量x。
     * @param offsetY   加水印的位置的偏移量y。
     * @param alpha     加水印透明度（0-100）。
     * @param markFile  水印图片
     * @param out       目标文件。
     * @throws IOException
     */
    public static void imageGifMark(BufferedInputStream bis, File destFile, int minWidth,
                                    int minHeight, int pos, int offsetX, int offsetY, int alpha, File markFile, BufferedOutputStream out)
            throws IOException {
		
		/*BufferedImage waterBuff = ImageIO.read(markFile);
		int waterWidth = waterBuff.getWidth()+5;
		int waterHeight = waterBuff.getHeight()+5;*/
        GifDecoder gd = new GifDecoder();
        int status = gd.read(bis);
        if (status != GifDecoder.STATUS_OK) {
            return;
        }

        AnimatedGifEncoder ge = new AnimatedGifEncoder();
        ge.start(out);
        ge.setRepeat(0);
        //encoder.start(saveFilePath);
        //encoder.setRepeat(decoder.getLoopCount());
        for (int i = 0; i < gd.getFrameCount(); i++) {
            BufferedImage frame = gd.getFrame(i);
            int width = frame.getWidth();
            int height = frame.getHeight();
            if (width >= minWidth && height >= minHeight) {
                ImageUtil.Position p = ImageUtil.markPosition(width, height, pos, offsetX,
                        offsetY);
                Graphics2D g = frame.createGraphics();
                AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) alpha / 100);
                g.setComposite(a);
                g.drawImage(ImageIO.read(markFile), ((ImageUtil.Position) p).getX(), ((ImageUtil.Position) p).getY(), null);
                g.dispose();
                //ImageIO.write(frame, "jpeg", destFile);
            }
            int delay = gd.getDelay(i);
            ge.setDelay(delay);
            ge.addFrame(frame);

        }
        ge.finish();
        //waterBuff = null;

    }

    /**
     * 标题.
     * <br>水印信息设置签名认证
     *
     * @param userId
     * @param markPath
     * @param sign
     * @param resultInfo
     * @return boolean
     * @throws
     * @author fengjian
     * @date 2015年3月31日 上午11:57:08
     */
    public static boolean doSignWaterConfigUpload(String userId, String markPath, String sign, ResultInfo resultInfo) {
        if (isNotEmpty(sign) && isNotEmpty(markPath) && isNotEmpty(userId)) {
            //String sSign = MD5.getMD5(GlobalVar.signKey+"|"+uploadInfo.getChannelFlag()+"|"+uploadInfo.getModule()+"|"+uploadInfo.getReturnUrl()+"|"+uploadInfo.getUserid());
            String ssign = MD5.getMD5("waterconfig" + markPath + GlobalVar.signKey + "|" + userId);
            if (sign.equals(ssign)) {
                return true;
            } else {
                resultInfo.setCode(GlobalVar.RESULTINFO_SIGNERROR_CODE);
                resultInfo.setDesc("签名验证失败");
                logger.error("签名验证失败");
                return false;
            }
        }
        return false;
    }

    /**
     * 返回默认的图片
     * <br>返回默认的图片
     *
     * @param httpServletRequest
     * @param response
     * @param w
     * @param h
     * @return void
     * @throws IOException
     * @throws ServletException
     * @throws
     * @author fengjian
     * @date 2015年5月29日 下午7:34:29
     */
    public static void retDefaltScale(HttpServletRequest httpServletRequest, ServletResponse response, Integer w, Integer h) throws ServletException, IOException {
        ImageUtil imageUtil = new ImageUtil();
        if (w != null && h != null) {
            String fileName = CommonTools.getPicName(w.toString(), h.toString(), ".png");
            String reetScaleUrl = GlobalVar.REET_SCALE_URL + fileName;
            String defaultUrl = GlobalVar.DEFAULT_SCALE_URL + "ebcp.png";
            String scaleUrl = GlobalVar.DEFAULT_SYSTEM_SCALE_FILE_PATH + File.separator + fileName;
            File scaleFile = new File(scaleUrl);
            if (scaleFile.exists() && !scaleFile.isDirectory()) {
                httpServletRequest.getRequestDispatcher(reetScaleUrl).forward(httpServletRequest, response);
            } else {
                try {
                    imageUtil.saveImg(defaultUrl, scaleUrl, w, h);
                    logger.error("返回生成默认剪切图片:" + reetScaleUrl);
                    httpServletRequest.getRequestDispatcher(reetScaleUrl).forward(httpServletRequest, response);
                } catch (Exception e1) {
                    logger.error("剪切默认图片错误,返回默认图片:" + GlobalVar.DEFAULT_FILE_URL);
                    httpServletRequest.getRequestDispatcher(GlobalVar.DEFAULT_FILE_URL).forward(httpServletRequest, response);
                }
            }

        } else {
            httpServletRequest.getRequestDispatcher(GlobalVar.DEFAULT_FILE_URL).forward(httpServletRequest, response);
        }

    }


}
