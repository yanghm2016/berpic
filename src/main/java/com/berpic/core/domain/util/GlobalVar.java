package com.berpic.core.domain.util;


/**
 * 全局常量/变量
 *
 * @author liusq
 */
public class GlobalVar {
    //日志级别
    public static final String debuglev = "1";
    public static final String errorlev = "2";

    //HTTP请求分割符
    public static final String WEB_SPLIT_CHAR = "/";
    //指定在内存中缓存数据大小,单位为byte
    public static final int UPLOAD_PARAM_CACHE_SIZE = 10240000;
    // 指定一次上传多个文件的总尺寸
    public static Long UPLOAD_PARAM_FILE_TOTALS_SIZE = 10000000L;
    //上传图片名称 随机X位数
    public static final int UPLOAD_PARAM_FILE_NAME_RANDOM_SIZE = 8;
    //图片服务域名
    public static String PIC_SERVER_DOMAIN = "";
    //图片服务器域名（临时）
    public static String PIC_SERVER_DOMAIN_TEMP = "";

    //文件存放根目录
    public static String ROOT_PATH = "";
    //缩放图片访问地址
    public static String PIC_SERVER_SCALE_DOMAIN = "";
    //缩放图片访问地址临时
    public static String PIC_SERVER_SCALE_DOMAIN_TEMP = "";
    //图片服务根路径
    public static String PIC_ROOT_PATH = "";
    //解压图片根目录
    public static String PIC_ZIP_ROOT_PATH = "";
    //解压图片缩略图访问地址
    public static String PIC_SERVER_ZIP_SCALE_DOMAIN = "";

    //解压图片根目录
    public static String PIC_HOME_ROOT_PATH = "";
    //解压图片缩略图访问地址
    public static String PIC_SERVER_HOME_SCALE_DOMAIN = "";
    //图片服务跟路径 临时
    public static String PIC_ROOT_PATH_TEMP = "";
    //图片服务器缩放文件目录
    public static String PIC_ROOT_SCALE = "";
    //图片服务器缩放文件临时目录
    public static String PIC_ROOT_SCALE_TEMP = "";
    //图片服务器缩放根目录名称
    public static String PIC_ROOT_SCALE_DIR_NAME = "";
    //图片清理标识 0:清理 1:不清理 默认不清理
    public static int PIC_CLEAN_FLAG = 1;
    //密钥
    public static String signKey = "D3E45D107969303044A8A0F53FEB2194";
    //清理多少天前的文件
    public static int PIC_CLEAN_DAY = 0;
    //缩略图文件名称分割符（150_150.jpg）
    public static String PIC_NAME_SPLIT = "_";

    //操作失败
    public static String RESULTINFO_ERROR_CODE = "1";
    //操作成功
    public static String RESULTINFO_SUCCESS_CODE = "0";
    //超出指定的上传大小
    public static String RESULTINFO_OUTSIZE_CODE = "2";
    //上传模块不存在
    public static String RESULTINFO_NOMODULE_CODE = "3";
    //签名失败
    public static String RESULTINFO_SIGNERROR_CODE = "4";
    //文件后缀不支持
    public static String RESULTINFO_SUFFIX_CODE = "5";
    //上传文件类型错
    public static String RESULTINFO_FILETYPE_CODE = "6";

    //add by viwen 2012-12-12
    //上传的渠道不存在
    public static String RESULTINFO_NOCHANNEL_CODE = "7";

    //文档文件目录
    public static String DOC_ROOT_PATH = "";

    //文档文件临时目录
    public static String DOC_ROOT_PATH_TEMP = "";


    //媒体文件目录
    public static String MEDIA_ROOT_PATH = "";

    //媒体文件临时目录
    public static String MEDIA_ROOT_PATH_TEMP = "";

    //站点工作路径
    public static String WEB_WORK_PATH = "";

    //手机渠道临时存放目录
    public static String PIC_ROOT_MOBILE_TEMP = "";
    //电脑渠道临时存放目录
    public static String PIC_ROOT_PC_TEMP = "";
    //公共渠道临时存放目录
    public static String PIC_ROOT_COMMON_TEMP = "";


    //图片服务器缩放文件手机临时目录
    public static String PIC_ROOT_SCALE_MOBILE_TEMP = "";
    //图片服务器缩放文件电脑临时目录
    public static String PIC_ROOT_SCALE_PC_TEMP = "";
    //图片服务器缩放文件公共临时存放目录
    public static String PIC_ROOT_SCALE_COMMON_TEMP = "";

    //文档手机渠道临时存放目录
    public static String DOC_ROOT_MOBILE_TEMP = "";
    //文档电脑渠道临时存放目录
    public static String DOC_ROOT_PC_TEMP = "";
    //文档公共渠道临时存放目录
    public static String DOC_ROOT_COMMON_TEMP = "";


    //媒体手机渠道临时存放目录
    public static String MEDIA_ROOT_MOBILE_TEMP = "";
    //媒体电脑渠道临时存放目录
    public static String MEDIA_ROOT_PC_TEMP = "";
    //媒体公共渠道临时存放目录
    public static String MEDIA_ROOT_COMMON_TEMP = "";

    //用于软连接--源地址
    public static String SRC_PATH = "";

    //用于软连接--软连接地址
    public static String LINK_PATH = "";

    //SWFTools安装目录
    public static String SWFTOOLS_PATH = "";

    //需要转换为swf的文件类型
    public static String CONVERT_TOSWF_FILES = "";

    //水印存放路径
    public static String WATER_FILE_ROOT_PATH = "";


    //水印xml配置文件路径
    public static String WATER_XML_FILE_CONFIG = "";

    //默认系统图片路径
    public static String DEFAULT_SYSTEM_FILE_PATH = "";

    //默认系统缩略图片路径
    public static String DEFAULT_SYSTEM_SCALE_FILE_PATH = "";


    //获取操作系统
    public static String OS_NAME = "os.name";

    //Linux
    public static String LINUX = "Linux";

    //返回默认图片url
    public static String DEFAULT_FILE_URL = "";

    //files
    public static String FILES = "files";

    public static String REET_SCALE_URL = "";

    public static String DEFAULT_SCALE_URL = "";

    //图片服务操作权限控制
    public static String OPERAT_FLAG = "";


}

