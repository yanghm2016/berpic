package com.berpic.core.domain.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.berpic.core.domain.bean.ModuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berpic.core.domain.bean.ResultInfo;
import com.berpic.core.domain.web.BaseController;


/**
 * 限定模块的处理\IP集合处理\多渠道的处理
 *
 * @author chenchao 991722899@qq.com
 *         2013-2-26 上午9:11:33
 */
public class LimitUtils extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(LimitUtils.class);

    //模块限定 模块与文件大小的对应
    public static Map<String, ModuleInfo> moduleMap = new LinkedHashMap<String, ModuleInfo>();
    //web服务器内网IP集合
    public static Map<String, String> ipMap = new LinkedHashMap<String, String>();
    //支持的文件后缀集合
    public static Map<String, String> suffixMap = new LinkedHashMap<String, String>();
    //文件头信息
    public static Map<String, String> fileTypeMap = new LinkedHashMap<String, String>();
    //图片缩放长宽限定
    private static List<Integer> wlist = new ArrayList<Integer>();
    private static List<Integer> hlist = new ArrayList<Integer>();

    //渠道限定
    public static Map<String, String> channelMap = new LinkedHashMap<String, String>();


    /**
     * 文件后缀格式化
     *
     * @param suffixs
     * @author chenchao 991722899@qq.com
     * @date 2013-2-26 下午3:16:43
     */
    public static void addSuffixMap(String suffixs) {
        if (CommonTools.isNotEmpty(suffixs)) {
            suffixMap.clear();
            if (suffixs.indexOf(",") != -1) {
                String[] suffixArray = suffixs.split(",");
                for (String s : suffixArray) {
                    if (s.indexOf("_") != -1) {
                        String[] arr = s.split("_");
                        suffixMap.put(arr[0], arr[1]);
                    } else {
                        logger.error("文件后缀解析错误,格式不合法(" + s + ")");
                    }

                }
            } else {
                //可能只有一个，直接分割保存(判断格式是否正确)
                if (suffixs.indexOf("_") != -1) {
                    String[] arr = suffixs.split("_");
                    suffixMap.put(arr[0], arr[1]);
                } else {
                    logger.error("文件后缀解析错误,格式不合法(" + suffixs + ")");
                }
            }
        } else {
            logger.error("文件后缀格式化内容为空");
        }
    }

    /**
     * 添加文件头信息
     *
     * @param fileType
     * @author chenchao 991722899@qq.com
     * @date 2013-2-26 下午4:53:19
     */
    public static void addFileType(String fileType) {
        if (CommonTools.isNotEmpty(fileType)) {
            fileTypeMap.clear();
            String[] fileTypeArry = null;
            String[] fileTypeInfoArray = null;
            if (fileType.indexOf(",") != -1) {
                fileTypeArry = fileType.split(",");
                for (int i = 0; i < fileTypeArry.length; i++) {
                    if (fileTypeArry[i].indexOf("_") != -1) {
                        fileTypeInfoArray = fileTypeArry[i].split("_");
                        fileTypeMap.put(fileTypeInfoArray[0], fileTypeInfoArray[1]);
                    } else {
                        logger.error("文件头信息解析错误,格式不合法");
                    }
                }
            } else {
                //可能只有一个，直接分割保存(判断格式是否正确)
                if (fileType.indexOf("_") != -1) {
                    fileTypeInfoArray = fileType.split("_");
                    fileTypeMap.put(fileTypeInfoArray[0], fileTypeInfoArray[1]);
                } else {
                    logger.error("文件头信息解析错误,格式不合法");
                }

            }
        }

    }

    /**
     * 上传模块是否存在
     *
     * @param module
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-26 上午9:03:39
     */
    public static boolean isExistsModule(String module, ResultInfo resultInfo) {
        if (moduleMap.containsKey(module)) {
            return true;
        } else {
            resultInfo.setCode(GlobalVar.RESULTINFO_NOMODULE_CODE);
            return false;
        }
    }


    /**
     * 上传渠道是否存在
     */
    public static boolean isExistsChannel(String channel, ResultInfo resultInfo) {
        if (channelMap.containsKey(channel)) {
            return true;
        } else {
            resultInfo.setCode(GlobalVar.RESULTINFO_NOCHANNEL_CODE);
            return false;
        }
    }


    /**
     * 模块信息格式化
     *
     * @param moduleInfo
     * @author chenchao 991722899@qq.com
     * @date 2013-2-26 上午9:03:34
     */
    public static void addModuleList(String moduleInfo) {
        if (CommonTools.isNotEmpty(moduleInfo)) {
            moduleMap.clear();
            String[] moduleArry = null;
            String[] moduleInfoArray = null;
            ModuleInfo module = null;
            if (moduleInfo.indexOf(",") != -1) {
                moduleArry = moduleInfo.split(",");
                for (int i = 0; i < moduleArry.length; i++) {
                    if (moduleArry[i].indexOf("_") != -1) {
                        moduleInfoArray = moduleArry[i].split("_");
                        module = new ModuleInfo();
                        module.setModuleName(moduleInfoArray[0]);
                        module.setModuleMaxSize(Long.parseLong(moduleInfoArray[1]));
                        moduleMap.put(module.getModuleName(), module);
                    } else {
                        logger.error("限定模块解析错误,格式不合法");
                    }
                }
            } else {
                //可能只有一个，直接分割保存(判断格式是否正确)
                if (moduleInfo.indexOf("_") != -1) {
                    moduleInfoArray = moduleInfo.split("_");
                    module = new ModuleInfo();
                    module.setModuleName(moduleInfoArray[0]);
                    module.setModuleMaxSize(Long.parseLong(moduleInfoArray[1]));
                    moduleMap.put(module.getModuleName(), module);
                } else {
                    logger.error("限定模块解析错误,格式不合法");
                }

            }
        }
    }

    /**
     * 格式化IP
     *
     * @param ips
     * @author chenchao 991722899@qq.com
     * @date 2013-2-26 上午9:52:23
     */
    public static void addIps(String ips) {
        if (CommonTools.isNotEmpty(ips)) {
            ipMap.clear();
            if (ips.indexOf(",") != -1) {
                String[] ipArray = ips.split(",");
                for (int i = 0; i < ipArray.length; i++) {
                    ipMap.put(ipArray[i], ipArray[i]);
                }
            } else {
                ipMap.put(ips, ips);
            }
        }
    }


    /**
     * 格式化渠道
     *
     * @param channels add by viwen 2014-12-12
     */
    public static void addChannels(String channels) {
        if (CommonTools.isNotEmpty(channels)) {
            channelMap.clear();
            if (channels.indexOf(",") != -1) {
                String[] channelArray = channels.split(",");
                for (int i = 0; i < channelArray.length; i++) {
                    channelMap.put(channelArray[i], channelArray[i]);
                }
            } else {
                channelMap.put(channels, channels);
            }
        }
    }


    /**
     * 图片缩放参数是否合法
     *
     * @param w
     * @param h
     * @return
     */
    public static boolean checkScale(int w, int h) {
        if (hlist.contains(h) && wlist.contains(w)) {
            return true;
        }
        return false;
    }

    /**
     * 新增图片缩放宽度集合
     *
     * @return
     */
    public static void addWlistLimit(int w) {
        LimitUtils.wlist.add(w);
    }

    /**
     * 新增图片缩放高度集合
     *
     * @return
     */
    public static void addHlistLimit(int h) {
        LimitUtils.hlist.add(h);
    }

    /**
     * 获取图片缩放宽度限定集合
     *
     * @param list
     */
    public static List<Integer> getWlistLimit() {
        return LimitUtils.wlist;
    }

    /**
     * 获取图片缩放高度限定集合
     *
     * @param list
     */
    public static List<Integer> getHlistLimit() {
        return LimitUtils.hlist;
    }
}
