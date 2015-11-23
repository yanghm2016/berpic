package com.berpic.core.domain.util;

import java.io.File;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * 标题. <br>
 * Xml文件操作类
 *
 * @author fengjian
 * @date 2015年3月30日 下午9:23:02
 */

public class XmlUtil {

    protected static Logger logger = LoggerFactory.getLogger(XmlUtil.class);

    /**
     * 标题.
     * <br>编辑xml内容
     *
     * @param document
     * @param filename
     * @return boolean
     * @throws
     * @author fengjian
     * @date 2015年3月31日 下午3:39:19
     */
    public static boolean setXmlFileValue(Document document, String filename) {
        boolean flag = true;
        try {
            /** 将document中的内容写入文件中   */
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);
        } catch (Exception e) {
            flag = false;
            logger.error("将文件内容写入xml中错误:", e);
        }
        return flag;

    }

    /**
     * 标题.
     * <br>获取水印xml文件内容
     *
     * @param filePath
     * @return boolean
     * @throws
     * @author fengjian
     * @date 2015年3月31日 下午3:39:53
     */
    public static boolean getXmlFileValue(String filePath) {
        boolean flag = false;
        try {

            File f = new File(filePath);
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
            if (StringUtils.isNotBlank(markOn)) {
                WaterVar.MARK_ON = Integer.parseInt(markOn);
            }
            if (StringUtils.isNotBlank(markWidth)) {
                WaterVar.MARK_PATH = markPath;
            }
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
            WaterVar.READ_WATER_CONFIG_DATE = new Date();

            flag = true;

        } catch (Exception e) {
            logger.error("获取水印xml文件值错误：", e);
            flag = false;
        }
        return flag;

    }

    /**
     * 标题.
     * <br>设置水印xml值
     *
     * @param filePath
     * @return boolean
     * @throws
     * @author fengjian
     * @date 2015年3月31日 下午3:40:23
     */
    public static boolean setXmlFileValue(String filePath) {
        boolean flag = false;
        try {
            File f = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(f);
            doc.getElementsByTagName("markOn").item(0).getFirstChild().setNodeValue(String.valueOf(WaterVar.MARK_ON));
            doc.getElementsByTagName("markPath").item(0).getFirstChild().setNodeValue(WaterVar.MARK_PATH);
            doc.getElementsByTagName("markWidth").item(0).getFirstChild().setNodeValue(String.valueOf(WaterVar.MARK_WIDTH));
            doc.getElementsByTagName("markHeight").item(0).getFirstChild().setNodeValue(String.valueOf(WaterVar.MARK_HEIGHT));
            doc.getElementsByTagName("markPosition").item(0).getFirstChild().setNodeValue(String.valueOf(WaterVar.MARK_POSITION));
            doc.getElementsByTagName("markOffsetX").item(0).getFirstChild().setNodeValue(String.valueOf(WaterVar.MARK_OFFSETX));
            doc.getElementsByTagName("markOffsetY").item(0).getFirstChild().setNodeValue(String.valueOf(WaterVar.MARK_OFFSETY));
            doc.getElementsByTagName("markAlpha").item(0).getFirstChild().setNodeValue(String.valueOf(WaterVar.MARK_ALPHA));
            flag = setXmlFileValue(doc, filePath);

        } catch (Exception e) {
            flag = false;
            logger.error("设置水印xml文件值错误：", e);
        }

        return flag;

    }

    /**
     * 标题.
     * <br>获取文件最后修改日期
     *
     * @param filePath
     * @return Date
     * @throws
     * @author fengjian
     * @date 2015年4月16日 下午3:44:57
     */
    public static Date getFileLastModified(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return new Date(file.lastModified());
        } else {
            return null;
        }
    }

}
