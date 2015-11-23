package com.berpic.core.domain.util;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.berpic.core.domain.util.gif.AnimatedGifEncoder;
import com.berpic.core.domain.util.gif.GifDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 图片工具类，处理图片
 *
 * @author chenchao 991722899@qq.com
 *         2013-2-24 下午12:10:59
 */
public class ImageUtil {
    private Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    private int width;
    private int height;
    private int scaleWidth;
    double support = (double) 3.0;
    double PI = (double) 3.14159265358978;
    double[] contrib;
    double[] normContrib;
    double[] tmpContrib;
    int startContrib, stopContrib;
    int nDots;
    int nHalfDots;

    /** */
    /**
     * Start: Use Lanczos filter to replace the original algorithm for image
     * scaling. Lanczos improves quality of the scaled image modify by :blade
     */

    public static void main(String[] args) {
        ImageUtil is = new ImageUtil();
        try {
            is.saveImg("D:\\1.jpg", "D:\\2.jpg", 80, 80);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 根据需要绽放的尺寸来剪切图片
     *
     * @param readeFilePath 需要剪切的图片地址
     * @param saveFilePath  剪切后的图片保存地址
     * @param width         需要剪切的最终宽度
     * @param heigth        需要剪切的最终高度
     * @throws Exception
     * @author chenchao 991722899@qq.com
     * @date 2013-2-24 下午12:02:09
     */
    public void saveImg(String readeFilePath, String saveFilePath, int width, int heigth) throws Exception {
        String suffix = CommonTools.getFileSuffixName(readeFilePath);
        if (CommonTools.isNotEmpty(suffix)) {
            if (suffix.toLowerCase().equals(ImageFormat.GIF.getValue())) {
                imgGIF(readeFilePath, saveFilePath, width, heigth, suffix);
            } else {
                imgJPG(readeFilePath, saveFilePath, width, heigth, suffix);
            }
        } else {
            logger.error("找不到文件后缀，无法解析");
        }

    }

    /**
     * JPG图片缩放处理
     *
     * @param readeFilePath
     * @param saveFilePath
     * @param width
     * @param heigth
     * @throws IOException
     * @author chenchao 991722899@qq.com
     * @date 2013-2-24 下午2:08:14
     */
    public void imgJPG(String readeFilePath, String saveFilePath, int width, int heigth, String fuffix) throws IOException {

    	 /*logger.info("******readeFilePath*****"+readeFilePath);
    	 logger.info("******saveFilePath*****"+saveFilePath);
    	 File saveFile = new File(saveFilePath);
         File fromFile = new File(readeFilePath);
         BufferedImage srcImage = javax.imageio.ImageIO.read(fromFile);
         srcImage = imageZoomOut(srcImage,width,heigth);
         try {
			ImageIO.write(srcImage,fuffix, saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}*/

        File saveFile = new File(saveFilePath);
        File fromFile = new File(readeFilePath);
        BufferedImage srcImage = javax.imageio.ImageIO.read(fromFile);
        if (fuffix.equals("png")) {
            int imageWideth = srcImage.getWidth(null);
            int imageHeight = srcImage.getHeight(null);
            int changeToWideth = 0;
            int changeToHeight = 0;
            //得到合适的压缩大小，按比例。
            if (imageWideth > 0 && imageHeight > 0) {
                if (imageWideth / imageHeight >= width / heigth) {
                    if (imageWideth > width) {
                        changeToWideth = width;
                        changeToHeight = (imageHeight * width) / imageWideth;
                    } else {
                        changeToWideth = imageWideth;
                        changeToHeight = imageHeight;
                    }
                } else {
                    if (imageHeight > heigth) {
                        changeToHeight = heigth;
                        changeToWideth = (imageWideth * heigth) / imageHeight;
                    } else {
                        changeToWideth = imageWideth;
                        changeToHeight = imageHeight;
                    }
                }
            }
            //构建图片对象
            BufferedImage _image = new BufferedImage(changeToWideth, changeToHeight, BufferedImage.TYPE_INT_ARGB_PRE);
            //绘制缩小后的图
            _image.getGraphics().drawImage(srcImage, 0, 0, changeToWideth, changeToHeight, null);
            ImageIO.write(_image, fuffix, saveFile);
        } else {
            srcImage = imageZoomOut(srcImage, width, heigth);
            ImageIO.write(srcImage, fuffix, saveFile);
        }
    }

    /**
     * GIF图片缩放处理
     *
     * @param width
     * @param heigth
     * @param fuffix
     * @throws IOException
     * @author chenchao 991722899@qq.com
     * @date 2013-2-24 下午2:39:58
     */
    public void imgGIF(String readeFilePath, String saveFilePath, int width, int heigth, String fuffix) throws IOException {
        GifDecoder decoder = new GifDecoder();
        File file = new File(readeFilePath);
        if (!file.exists()) {
            logger.error("===========gif 文件不存在:" + readeFilePath);
        }
        int status = decoder.read(new FileInputStream(file));// .read(readeFilePath);
        if (status != GifDecoder.STATUS_OK) {
            throw new IOException("read image " + readeFilePath + "error!");
        }
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(saveFilePath);
        encoder.setRepeat(decoder.getLoopCount());
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            encoder.setDelay(decoder.getDelay(i));
            BufferedImage childImage = decoder.getFrame(i);
            BufferedImage image = imageZoomOut(childImage, width, heigth);
            encoder.addFrame(image);
        }
        encoder.finish();
    }

    /**
     * 图片缩放公共方法
     *
     * @param srcBufferImage
     * @param w
     * @param h
     * @return
     * @author chenchao 991722899@qq.com
     * @date 2013-2-24 下午3:18:46
     */
    public BufferedImage imageZoomOut(BufferedImage srcBufferImage, int w, int h) {
        int imageWideth = srcBufferImage.getWidth(null);
        int imageHeight = srcBufferImage.getHeight(null);
        int changeToWideth = 0;
        int changeToHeight = 0;
        if (imageWideth > 0 && imageHeight > 0) {
            if (imageWideth / imageHeight >= w / h) {
                if (imageWideth > w) {
                    changeToWideth = w;
                    changeToHeight = (imageHeight * w) / imageWideth;
                } else {
                    changeToWideth = imageWideth;
                    changeToHeight = imageHeight;
                }
            } else {
                if (imageHeight > h) {
                    changeToHeight = h;
                    changeToWideth = (imageWideth * h) / imageHeight;
                } else {
                    changeToWideth = imageWideth;
                    changeToHeight = imageHeight;
                }
            }
        }
        width = srcBufferImage.getWidth();
        height = srcBufferImage.getHeight();
        scaleWidth = w;

        if (DetermineResultSize(changeToWideth, changeToHeight) == 1) {
            return srcBufferImage;
        }
        CalContrib();
        BufferedImage pbOut = HorizontalFiltering(srcBufferImage, changeToWideth);
        BufferedImage pbFinalOut = VerticalFiltering(pbOut, changeToHeight);
        return pbFinalOut;
    }

    /** */
    /**
     * 决定图像尺寸
     */
    private int DetermineResultSize(int w, int h) {
        double scaleH, scaleV;
        scaleH = (double) w / (double) width;
        scaleV = (double) h / (double) height;
        // 需要判断一下scaleH，scaleV，不做放大操作
        if (scaleH >= 1.0 && scaleV >= 1.0) {
            return 1;
        }
        return 0;

    } // end of DetermineResultSize()

    private double Lanczos(int i, int inWidth, int outWidth, double Support) {
        double x;

        x = (double) i * (double) outWidth / (double) inWidth;

        return Math.sin(x * PI) / (x * PI) * Math.sin(x * PI / Support)
                / (x * PI / Support);

    }

    private void CalContrib() {
        nHalfDots = (int) ((double) width * support / (double) scaleWidth);
        nDots = nHalfDots * 2 + 1;
        try {
            contrib = new double[nDots];
            normContrib = new double[nDots];
            tmpContrib = new double[nDots];
        } catch (Exception e) {
            System.out.println("init   contrib,normContrib,tmpContrib" + e);
        }

        int center = nHalfDots;
        contrib[center] = 1.0;

        double weight = 0.0;
        int i = 0;
        for (i = 1; i <= center; i++) {
            contrib[center + i] = Lanczos(i, width, scaleWidth, support);
            weight += contrib[center + i];
        }

        for (i = center - 1; i >= 0; i--) {
            contrib[i] = contrib[center * 2 - i];
        }

        weight = weight * 2 + 1.0;

        for (i = 0; i <= center; i++) {
            normContrib[i] = contrib[i] / weight;
        }

        for (i = center + 1; i < nDots; i++) {
            normContrib[i] = normContrib[center * 2 - i];
        }
    } // end of CalContrib()

    // 处理边缘
    private void CalTempContrib(int start, int stop) {
        double weight = 0;

        int i = 0;
        for (i = start; i <= stop; i++) {
            weight += contrib[i];
        }

        for (i = start; i <= stop; i++) {
            tmpContrib[i] = contrib[i] / weight;
        }

    } // end of CalTempContrib()

    private int GetRedValue(int rgbValue) {
        int temp = rgbValue & 0x00ff0000;
        return temp >> 16;
    }

    private int GetGreenValue(int rgbValue) {
        int temp = rgbValue & 0x0000ff00;
        return temp >> 8;
    }

    private int GetBlueValue(int rgbValue) {
        return rgbValue & 0x000000ff;
    }

    private int ComRGB(int redValue, int greenValue, int blueValue) {

        return (redValue << 16) + (greenValue << 8) + blueValue;
    }

    // 行水平滤波
    private int HorizontalFilter(BufferedImage bufImg, int startX, int stopX,
                                 int start, int stop, int y, double[] pContrib) {
        double valueRed = 0.0;
        double valueGreen = 0.0;
        double valueBlue = 0.0;
        int valueRGB = 0;
        int i, j;

        for (i = startX, j = start; i <= stopX; i++, j++) {
            valueRGB = bufImg.getRGB(i, y);

            valueRed += GetRedValue(valueRGB) * pContrib[j];
            valueGreen += GetGreenValue(valueRGB) * pContrib[j];
            valueBlue += GetBlueValue(valueRGB) * pContrib[j];
        }

        valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen),
                Clip((int) valueBlue));
        return valueRGB;

    } // end of HorizontalFilter()

    // 图片水平滤波
    private BufferedImage HorizontalFiltering(BufferedImage bufImage, int iOutW) {
        int dwInW = bufImage.getWidth();
        int dwInH = bufImage.getHeight();
        int value = 0;
        BufferedImage pbOut = new BufferedImage(iOutW, dwInH,
                BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < iOutW; x++) {

            int startX;
            int start;
            int X = (int) (((double) x) * ((double) dwInW) / ((double) iOutW) + 0.5);
            int y = 0;

            startX = X - nHalfDots;
            if (startX < 0) {
                startX = 0;
                start = nHalfDots - X;
            } else {
                start = 0;
            }

            int stop;
            int stopX = X + nHalfDots;
            if (stopX > (dwInW - 1)) {
                stopX = dwInW - 1;
                stop = nHalfDots + (dwInW - 1 - X);
            } else {
                stop = nHalfDots * 2;
            }

            if (start > 0 || stop < nDots - 1) {
                CalTempContrib(start, stop);
                for (y = 0; y < dwInH; y++) {
                    value = HorizontalFilter(bufImage, startX, stopX, start,
                            stop, y, tmpContrib);
                    pbOut.setRGB(x, y, value);
                }
            } else {
                for (y = 0; y < dwInH; y++) {
                    value = HorizontalFilter(bufImage, startX, stopX, start,
                            stop, y, normContrib);
                    pbOut.setRGB(x, y, value);
                }
            }
        }

        return pbOut;

    } // end of HorizontalFiltering()

    private int VerticalFilter(BufferedImage pbInImage, int startY, int stopY,
                               int start, int stop, int x, double[] pContrib) {
        double valueRed = 0.0;
        double valueGreen = 0.0;
        double valueBlue = 0.0;
        int valueRGB = 0;
        int i, j;

        for (i = startY, j = start; i <= stopY; i++, j++) {
            valueRGB = pbInImage.getRGB(x, i);

            valueRed += GetRedValue(valueRGB) * pContrib[j];
            valueGreen += GetGreenValue(valueRGB) * pContrib[j];
            valueBlue += GetBlueValue(valueRGB) * pContrib[j];
            // System.out.println(valueRed+"->"+Clip((int)valueRed)+"<-");
            //   
            // System.out.println(valueGreen+"->"+Clip((int)valueGreen)+"<-");
            // System.out.println(valueBlue+"->"+Clip((int)valueBlue)+"<-"+"-->");
        }

        valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen),
                Clip((int) valueBlue));
        // System.out.println(valueRGB);
        return valueRGB;

    } // end of VerticalFilter()

    private BufferedImage VerticalFiltering(BufferedImage pbImage, int iOutH) {
        int iW = pbImage.getWidth();
        int iH = pbImage.getHeight();
        int value = 0;
        BufferedImage pbOut = new BufferedImage(iW, iOutH,
                BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < iOutH; y++) {

            int startY;
            int start;
            int Y = (int) (((double) y) * ((double) iH) / ((double) iOutH) + 0.5);

            startY = Y - nHalfDots;
            if (startY < 0) {
                startY = 0;
                start = nHalfDots - Y;
            } else {
                start = 0;
            }

            int stop;
            int stopY = Y + nHalfDots;
            if (stopY > (int) (iH - 1)) {
                stopY = iH - 1;
                stop = nHalfDots + (iH - 1 - Y);
            } else {
                stop = nHalfDots * 2;
            }

            if (start > 0 || stop < nDots - 1) {
                CalTempContrib(start, stop);
                for (int x = 0; x < iW; x++) {
                    value = VerticalFilter(pbImage, startY, stopY, start, stop,
                            x, tmpContrib);
                    pbOut.setRGB(x, y, value);
                }
            } else {
                for (int x = 0; x < iW; x++) {
                    value = VerticalFilter(pbImage, startY, stopY, start, stop,
                            x, normContrib);
                    pbOut.setRGB(x, y, value);
                }
            }

        }

        return pbOut;

    } // end of VerticalFiltering()

    int Clip(int x) {
        if (x < 0)
            return 0;
        if (x > 255)
            return 255;
        return x;
    }


    /**
     * 获得水印位置
     *
     * @param width   原图宽度
     * @param height  原图高度
     * @param p       水印位置 1-5，其他值为随机。1：左上；2：右上；3：左下；4：右下；5：中央。
     * @param offsetx 水平偏移。
     * @param offsety 垂直偏移。
     * @return 水印位置
     */
    public static Position markPosition(int width, int height, int p,
                                        int offsetx, int offsety) {
        if (p < 1 || p > 5) {
            p = (int) (Math.random() * 5) + 1;
        }
        int x, y;
        switch (p) {
            // 左上
            case 1:
                x = offsetx;
                y = offsety;
                break;
            // 右上
            case 2:
                x = width + offsetx;
                y = offsety;
                break;
            // 左下
            case 3:
                x = offsetx;
                y = height + offsety;
                break;
            // 右下
            case 4:
                x = width + offsetx;
                y = height + offsety;
                break;
            // 中央
            case 5:
                x = (width / 2) + offsetx;
                y = (height / 2) + offsety;
                break;
            default:
                throw new RuntimeException("never reach ...");
        }
        return new Position(x, y);
    }


    /**
     * 水印位置
     * <p/>
     * 包含左边偏移量，右边偏移量。
     */
    public static class Position {
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }


}