package com.berpic.core.config;

import com.berpic.core.domain.server.SysConfigProperty;
import com.berpic.core.domain.util.*;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


@Component
public class RundListener implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RundListener.class);

    @Autowired
    private SysConfigProperty systemConfigProperty;

    @Autowired
    private RealPathResolver realPathResolver;

    @Override
    public void afterPropertiesSet() throws Exception {

        // 系统级配置信息
        ResourceBundle sysResource = systemConfigProperty
                .sysConfigProfiles(systemConfigProperty.getString(Constants.PIC_SYSTEM_CONF_FLAG));

        // 日志级配置信息
        ResourceBundle logResource
                = systemConfigProperty.sysConfigProfiles(EnumUitls.MSG.getName());

        GlobalVar.ROOT_PATH = realPathResolver.get("");
        logger.info(logResource.getString("bean_init_start"));

        logger.info(logResource.getString("root_path") + ":" + GlobalVar.ROOT_PATH);

        //获取操作标示 暂时无用
//        GlobalVar.OPERAT_FLAG = sysResource.getString("operatFlag");
        //获取密钥
//        GlobalVar.signKey = sysResource.getString("signkey");
//        logger.info("key_value: " + GlobalVar.signKey);

        //图片服务器域名
        GlobalVar.PIC_SERVER_DOMAIN = sysResource.getString("pic_server_domain");
        logger.info(logResource.getString("pic_server_domain") + GlobalVar.PIC_SERVER_DOMAIN);

        //图片服务器临时域名
        GlobalVar.PIC_SERVER_DOMAIN_TEMP = sysResource.getString("pic_server_domain_temp");
        logger.info(logResource.getString("pic_server_domain_temp") + GlobalVar.PIC_SERVER_DOMAIN_TEMP);

        //缩放图片域名
        GlobalVar.PIC_SERVER_SCALE_DOMAIN = sysResource.getString("pic_server_scale_domain");
        logger.info(logResource.getString("pic_server_scale_domain") + GlobalVar.PIC_SERVER_SCALE_DOMAIN);

        //缩放图片域名 临时
        GlobalVar.PIC_SERVER_SCALE_DOMAIN_TEMP = sysResource.getString("pic_server_scale_domain_temp");
        logger.info(logResource.getString("pic_server_scale_domain_temp") + GlobalVar.PIC_SERVER_SCALE_DOMAIN_TEMP);
        //add by yanghm start
        //压缩图片缩略图域名
        GlobalVar.PIC_SERVER_ZIP_SCALE_DOMAIN = sysResource.getString("pic_server_zip_scale_domain");
        //首页图片缩略图域名
        GlobalVar.PIC_SERVER_HOME_SCALE_DOMAIN = sysResource.getString("pic_server_home_scale_domain");
        //add end

        //文件存放根目录
        logger.info("root_path:" + GlobalVar.ROOT_PATH);

        //图片根目录
        GlobalVar.PIC_ROOT_PATH = GlobalVar.ROOT_PATH.concat(sysResource.getString("pic_root_path"));
        logger.info("pic_root_path:" + GlobalVar.PIC_ROOT_PATH);

        //add by yanghaoming start
        //解压图片根目录
        GlobalVar.PIC_HOME_ROOT_PATH = GlobalVar.ROOT_PATH.concat(sysResource.getString("pic_home_root_path"));
        logger.info("pic_home_root_path:" + GlobalVar.PIC_HOME_ROOT_PATH);
        //解压图片根目录
        GlobalVar.PIC_ZIP_ROOT_PATH = GlobalVar.ROOT_PATH.concat(sysResource.getString("pic_zip_root_path"));
        logger.info("pic_zip_root_path:" + GlobalVar.PIC_ZIP_ROOT_PATH);
        //add end

        //图片根临时目录
        GlobalVar.PIC_ROOT_PATH_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("pic_root_path_temp"));
        logger.info("pic_root_path_temp:" + GlobalVar.PIC_ROOT_PATH_TEMP);

        //缩放文件目录
        GlobalVar.PIC_ROOT_SCALE = GlobalVar.ROOT_PATH.concat(sysResource.getString("pic_root_scale"));
        logger.info("pic_root_scale:" + GlobalVar.PIC_ROOT_SCALE);

        //缩放文件临时目录
        GlobalVar.PIC_ROOT_SCALE_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("pic_root_scale_temp"));
        logger.info("pic_root_scale_temp:" + GlobalVar.PIC_ROOT_SCALE_TEMP);

        //缩放文件根目录名称
        GlobalVar.PIC_ROOT_SCALE_DIR_NAME = GlobalVar.ROOT_PATH.concat(sysResource.getString("pic_root_scale_dir_name"));
        logger.info("pic_root_scale_dir_name:" + GlobalVar.PIC_ROOT_SCALE_DIR_NAME);

        //文档文件目录
        GlobalVar.DOC_ROOT_PATH = GlobalVar.ROOT_PATH.concat(sysResource.getString("doc_root_path"));
        logger.info("doc_root_path:" + GlobalVar.DOC_ROOT_PATH);

        //文档文件临时目录
        GlobalVar.DOC_ROOT_PATH_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("doc_root_path_temp"));
        logger.info("doc_root_path_temp:" + GlobalVar.DOC_ROOT_PATH_TEMP);

        //媒体文件目录
        GlobalVar.MEDIA_ROOT_PATH = GlobalVar.ROOT_PATH.concat(sysResource.getString("media_root_path"));
        logger.info("media_root_path:" + GlobalVar.MEDIA_ROOT_PATH);

        //媒体文件临时目录
        GlobalVar.MEDIA_ROOT_PATH_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("media_root_path_temp"));
        logger.info("media_root_path_temp:" + GlobalVar.MEDIA_ROOT_PATH_TEMP);

        //=======================多渠道（start）====================
        //手机渠道临时存放目录
        GlobalVar.PIC_ROOT_MOBILE_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("PIC_ROOT_MOBILE_TEMP"));
        logger.info("PIC_ROOT_MOBILE_TEMP:" + GlobalVar.PIC_ROOT_MOBILE_TEMP);

        //电脑渠道临时存放目录
        GlobalVar.PIC_ROOT_PC_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("PIC_ROOT_PC_TEMP"));
        logger.info("PIC_ROOT_PC_TEMP:" + GlobalVar.PIC_ROOT_PC_TEMP);

        //公共渠道临时存放目录
        GlobalVar.PIC_ROOT_COMMON_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("PIC_ROOT_COMMON_TEMP"));
        logger.info("PIC_ROOT_COMMON_TEMP:" + GlobalVar.PIC_ROOT_COMMON_TEMP);


        //图片服务器缩放文件手机临时目录
        GlobalVar.PIC_ROOT_SCALE_MOBILE_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("PIC_ROOT_SCALE_MOBILE_TEMP"));
        logger.info("PIC_ROOT_SCALE_MOBILE_TEMP:" + GlobalVar.PIC_ROOT_SCALE_MOBILE_TEMP);

        //图片服务器缩放文件电脑临时目录
        GlobalVar.PIC_ROOT_SCALE_PC_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("PIC_ROOT_SCALE_PC_TEMP"));
        logger.info("PIC_ROOT_SCALE_PC_TEMP:" + GlobalVar.PIC_ROOT_SCALE_PC_TEMP);

        //图片服务器缩放文件公共临时目录
        GlobalVar.PIC_ROOT_SCALE_COMMON_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("PIC_ROOT_SCALE_COMMON_TEMP"));
        logger.info("PIC_ROOT_SCALE_COMMON_TEMP:" + GlobalVar.PIC_ROOT_SCALE_COMMON_TEMP);


        //文档手机渠道临时存放目录
        GlobalVar.DOC_ROOT_MOBILE_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("DOC_ROOT_MOBILE_TEMP"));
        logger.info("DOC_ROOT_MOBILE_TEMP:" + GlobalVar.DOC_ROOT_MOBILE_TEMP);

        //文档电脑渠道临时存放目录
        GlobalVar.DOC_ROOT_PC_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("DOC_ROOT_PC_TEMP"));
        logger.info("DOC_ROOT_PC_TEMP:" + GlobalVar.DOC_ROOT_PC_TEMP);

        //文档公共渠道临时存放目录
        GlobalVar.DOC_ROOT_COMMON_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("DOC_ROOT_COMMON_TEMP"));
        logger.info("DOC_ROOT_COMMON_TEMP:" + GlobalVar.DOC_ROOT_COMMON_TEMP);

        //媒体手机渠道临时存放目录
        GlobalVar.MEDIA_ROOT_MOBILE_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("MEDIA_ROOT_MOBILE_TEMP"));
        logger.info("MEDIA_ROOT_MOBILE_TEMP:" + GlobalVar.MEDIA_ROOT_MOBILE_TEMP);

        //媒体电脑渠道临时存放目录
        GlobalVar.MEDIA_ROOT_PC_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("MEDIA_ROOT_PC_TEMP"));
        logger.info("MEDIA_ROOT_PC_TEMP:" + GlobalVar.MEDIA_ROOT_PC_TEMP);

        //媒体公共渠道临时存放目录
        GlobalVar.MEDIA_ROOT_COMMON_TEMP = GlobalVar.ROOT_PATH.concat(sysResource.getString("MEDIA_ROOT_COMMON_TEMP"));
        logger.info("MEDIA_ROOT_COMMON_TEMP:" + GlobalVar.MEDIA_ROOT_COMMON_TEMP);

        //=======================多渠道（end）====================

        //用于软连接--源地址
        GlobalVar.SRC_PATH = sysResource.getString("SRC_PATH");
        logger.info("SRC_PATH:" + GlobalVar.SRC_PATH);

        //用于软连接--软连接地址
        GlobalVar.LINK_PATH = GlobalVar.ROOT_PATH.concat(sysResource.getString("LINK_PATH"));
        logger.info("LINK_PATH:" + GlobalVar.LINK_PATH);


        //SWFTools安装目录
        GlobalVar.SWFTOOLS_PATH = sysResource.getString("SWFTOOLS_PATH");
        logger.info("SWFTOOLS_PATH:" + GlobalVar.SWFTOOLS_PATH);

        //需要转换为swf的文件类型
        GlobalVar.CONVERT_TOSWF_FILES = sysResource.getString("CONVERT_TOSWF_FILES");
        logger.info("CONVERT_TOSWF_FILES:" + GlobalVar.CONVERT_TOSWF_FILES);

        //加载限定模块
        LimitUtils.addModuleList(sysResource.getString("module"));
        logger.info("module:" + sysResource.getString("module"));

        //上传文件总大小
        GlobalVar.UPLOAD_PARAM_FILE_TOTALS_SIZE = Long.parseLong(sysResource.getString("upload_param_file_totals_size"));
        logger.info("upload_param_file_totals_size:" + GlobalVar.UPLOAD_PARAM_FILE_TOTALS_SIZE);

        //清理标识
        GlobalVar.PIC_CLEAN_FLAG = Integer.parseInt(sysResource.getString("pic_clean_flag"));
        logger.info("pic_clean_flag:" + GlobalVar.PIC_CLEAN_FLAG);

        //清理n天前的数据
        GlobalVar.PIC_CLEAN_DAY = Integer.parseInt(sysResource.getString("pic_clean_day"));
        logger.info("pic_clean_day:" + GlobalVar.PIC_CLEAN_DAY);

        //水印存放路径
        GlobalVar.WATER_FILE_ROOT_PATH = GlobalVar.ROOT_PATH.concat(sysResource.getString("water_file_root_path"));
        logger.info("WATER_FILE_ROOT_PATH:" + GlobalVar.WATER_FILE_ROOT_PATH);

        //水印XML配置文件

        GlobalVar.WATER_XML_FILE_CONFIG = GlobalVar.ROOT_PATH.concat(sysResource.getString("water_xml_file_config"));
        logger.info("WATER_XML_FILE_CONFIG:" + GlobalVar.WATER_XML_FILE_CONFIG);

        //系统默认返回图片
        GlobalVar.DEFAULT_SYSTEM_FILE_PATH = GlobalVar.ROOT_PATH.concat(sysResource.getString("default_system_file_path"));
        logger.info("default_system_file_path:" + GlobalVar.DEFAULT_SYSTEM_FILE_PATH);


        //系统默认返回缩略图片
        GlobalVar.DEFAULT_SYSTEM_SCALE_FILE_PATH = GlobalVar.ROOT_PATH.concat(sysResource.getString("default_system_scale_file_path"));
        logger.info("default_system_scale_file_path:" + GlobalVar.DEFAULT_SYSTEM_SCALE_FILE_PATH);


        //获取站点根目录
        if (this.getClass().getResource("").getPath() != null) ;
        {
            String thisClassPath = this.getClass().getResource("").getPath();
            if (thisClassPath.indexOf("WEB-INF") > 0) {
                GlobalVar.WEB_WORK_PATH = thisClassPath.substring(0, thisClassPath.indexOf("WEB-INF"));
            }
        }

        //限定图片缩放大小
        List<String> sw = Arrays.asList(sysResource.getString("width").split(","));
        List<String> sh = Arrays.asList(sysResource.getString("height").split(","));
        for (String w : sw) {
            LimitUtils.addWlistLimit(Integer.parseInt(w));
        }
        for (String h : sh) {
            LimitUtils.addHlistLimit(Integer.parseInt(h));
        }
        logger.info("pic_height:" + LimitUtils.getHlistLimit());
        logger.info("pic_width:" + LimitUtils.getWlistLimit());

        //web服务器内网IP集合
        LimitUtils.addIps(sysResource.getString("ips"));
        logger.info("ips:" + sysResource.getString("ips"));

        //加载限定渠道
        LimitUtils.addChannels(sysResource.getString("channel_flag"));
        logger.info("channel_flag:" + sysResource.getString("channel_flag"));


        //支持的文件后缀集合
        LimitUtils.addSuffixMap(sysResource.getString("upload_param_file_suffixlist"));
        logger.info("upload_param_file_suffixlist:" + sysResource.getString("upload_param_file_suffixlist"));

        //文件头信息
        LimitUtils.addFileType(sysResource.getString("upload_params_file_typelist"));
        logger.info("upload_params_file_typelist:" + sysResource.getString("upload_params_file_typelist"));

        //如果是linux，获取当前站点路径，创建软链接
        if (System.getProperty(GlobalVar.OS_NAME) != null && System.getProperty(GlobalVar.OS_NAME).equals("Linux")) {
            //创建软连接
            CommonTools.createDir(GlobalVar.SRC_PATH);
            CommonTools.createLinkDir(GlobalVar.SRC_PATH, GlobalVar.LINK_PATH);
        }

        CommonTools.createDir(GlobalVar.PIC_ROOT_PATH + "/mobile");
        CommonTools.createDir(GlobalVar.PIC_ROOT_PATH + "/pc");
        CommonTools.createDir(GlobalVar.PIC_ROOT_PATH + "/common");
        CommonTools.createDir(GlobalVar.PIC_ZIP_ROOT_PATH);
        //add by viwen
        CommonTools.createDir(GlobalVar.PIC_ROOT_MOBILE_TEMP);
        CommonTools.createDir(GlobalVar.PIC_ROOT_PC_TEMP);
        CommonTools.createDir(GlobalVar.PIC_ROOT_COMMON_TEMP);


        CommonTools.createDir(GlobalVar.PIC_ROOT_SCALE + "/mobile");
        CommonTools.createDir(GlobalVar.PIC_ROOT_SCALE + "/pc");
        CommonTools.createDir(GlobalVar.PIC_ROOT_SCALE + "/common");

        CommonTools.createDir(GlobalVar.PIC_ROOT_SCALE_MOBILE_TEMP);
        CommonTools.createDir(GlobalVar.PIC_ROOT_SCALE_PC_TEMP);
        CommonTools.createDir(GlobalVar.PIC_ROOT_SCALE_COMMON_TEMP);

        CommonTools.createDir(GlobalVar.DOC_ROOT_PATH + "/mobile");
        CommonTools.createDir(GlobalVar.DOC_ROOT_PATH + "/pc");
        CommonTools.createDir(GlobalVar.DOC_ROOT_PATH + "/common");

        CommonTools.createDir(GlobalVar.DOC_ROOT_MOBILE_TEMP);
        CommonTools.createDir(GlobalVar.DOC_ROOT_PC_TEMP);
        CommonTools.createDir(GlobalVar.DOC_ROOT_COMMON_TEMP);

        CommonTools.createDir(GlobalVar.MEDIA_ROOT_PATH + "/mobile");
        CommonTools.createDir(GlobalVar.MEDIA_ROOT_PATH + "/pc");
        CommonTools.createDir(GlobalVar.MEDIA_ROOT_PATH + "/common");

        CommonTools.createDir(GlobalVar.MEDIA_ROOT_MOBILE_TEMP);
        CommonTools.createDir(GlobalVar.MEDIA_ROOT_PC_TEMP);
        CommonTools.createDir(GlobalVar.MEDIA_ROOT_COMMON_TEMP);

        CommonTools.createDir(GlobalVar.WATER_FILE_ROOT_PATH);

        //将WaterConfig.xml、default文件夹复制到files目录下面
        String waterConfigPath = Constants.SYSTEM_ROOT_PATH + "WaterConfig.xml";

        File waterConfigFile = new File(waterConfigPath);
        File waterConfigNewFile = new File(GlobalVar.WATER_XML_FILE_CONFIG);
        if (!waterConfigNewFile.exists()) {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(waterConfigFile));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(waterConfigNewFile));
            Streams.copy(in, out, true);
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
        // 读取系统路径
        String defaultPath = GlobalVar.ROOT_PATH;
        String defaultNewPath = GlobalVar.ROOT_PATH + GlobalVar.FILES + GlobalVar.WEB_SPLIT_CHAR + "default";
        File defaultNewFile = new File(defaultNewPath);
        if (!defaultNewFile.exists()) {
            FileUtils.copyDirectory(new File(defaultPath), defaultNewFile);
        }

        XmlUtil.getXmlFileValue(GlobalVar.WATER_XML_FILE_CONFIG);
        GlobalVar.DEFAULT_FILE_URL = GlobalVar.WEB_SPLIT_CHAR + GlobalVar.FILES + GlobalVar.WEB_SPLIT_CHAR + "default" + GlobalVar.WEB_SPLIT_CHAR + "ebcp.png";

        GlobalVar.REET_SCALE_URL = GlobalVar.WEB_SPLIT_CHAR + GlobalVar.FILES + GlobalVar.WEB_SPLIT_CHAR + "default" + GlobalVar.WEB_SPLIT_CHAR + "scale" + GlobalVar.WEB_SPLIT_CHAR;
        GlobalVar.DEFAULT_SCALE_URL = GlobalVar.ROOT_PATH + GlobalVar.FILES + File.separator + "default" + File.separator;
    }

    public static void main(String args[]) {

        String name = "/home/wasadmin/fmallimg/imagescale/image".substring(0, "/home/wasadmin/fmallimg/imagescale/image".lastIndexOf("/"));

        System.out.println("name=" + name);
    }
}
