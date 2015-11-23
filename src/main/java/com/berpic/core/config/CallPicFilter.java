package com.berpic.core.config;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.berpic.core.domain.util.CommonTools;
import com.berpic.core.domain.util.GlobalVar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berpic.core.domain.util.ImageUtil;


/**
 *	拦截所有图片请求，生成缩略图
 * @author chenchao 991722899@qq.com
 * 2013-2-24 下午3:46:02
 */
public class CallPicFilter implements Filter {
	private Logger logger=LoggerFactory.getLogger(CallPicFilter.class);
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hsrequest = (HttpServletRequest)request;
		
		hsrequest.getContextPath();
		logger.info(hsrequest.getRequestURL().toString());
		//获取缩略图参数
		String w = hsrequest.getParameter("w");
		String h = hsrequest.getParameter("h");
		
		File file = null;
		//请求的url
		String reqURL = hsrequest.getRequestURL().toString();
		//add by yanghaoming
		//添加 *_100_100.*图片访问的处理方法
		int counter = 0; //"_"个数
		String imgName = reqURL.substring(reqURL.lastIndexOf("/")+1);
		String imgNameTemp=imgName;
		//计算"_"数目
		while(imgNameTemp.indexOf("_")!=-1){
			counter ++;
			imgNameTemp = imgNameTemp.substring(imgNameTemp.indexOf("_")+1);
		}
		
		if(counter>=2){
			String reqURL_temp = reqURL;
			String tail = reqURL_temp.substring(reqURL_temp.lastIndexOf("."));
			reqURL_temp = reqURL_temp.substring(0,reqURL_temp.lastIndexOf("."));
			//获取高度值
			String str_h = reqURL_temp.substring(reqURL_temp.lastIndexOf("_")+1);
			reqURL_temp =  reqURL_temp.substring(0,reqURL_temp.lastIndexOf("_"));
			//获取宽度值
			String str_w = reqURL_temp.substring(reqURL_temp.lastIndexOf("_")+1);
			reqURL_temp =  reqURL_temp.substring(0,reqURL_temp.lastIndexOf("_"));
			int tp_w=0;
			int tp_h=0;
			try {
				tp_w = Integer.parseInt(str_w);
				tp_h = Integer.parseInt(str_h);
			} catch (Exception e) {
				tp_w = 0;
				tp_h = 0;
			}
			if(tp_w != 0 && tp_h != 0){
				reqURL = reqURL_temp+tail;
				h = str_h;
				w = str_w;
			}
			
		}
		//end
		
		//add by tangxc
		//String svlPath = hsrequest.getRequestURI();
				
		//根据请求地址和请求地址中所包含的文件夹名称(临时路径还是正式路径)，获取到文件的真实地址
		//boolean flag=reqURL.indexOf(GlobalVar.PIC_SERVER_DOMAIN_TEMP)!=-1;
		boolean flag=reqURL.indexOf("/imageTemp/")!=-1;
		String tempPath=flag? GlobalVar.PIC_ROOT_PATH_TEMP:GlobalVar.PIC_ROOT_PATH;
		String tempDomain=flag?GlobalVar.PIC_SERVER_DOMAIN_TEMP:GlobalVar.PIC_SERVER_DOMAIN;
		String tempScaleDomain=flag?GlobalVar.PIC_SERVER_SCALE_DOMAIN_TEMP:GlobalVar.PIC_SERVER_SCALE_DOMAIN;
		String scalePath=flag?GlobalVar.PIC_ROOT_SCALE_TEMP:GlobalVar.PIC_ROOT_SCALE;
		
		//根据请求获取请求文件的真实地址
		//String srcpicpath =tempPath.concat(reqURL.replace(tempDomain,"").replace(GlobalVar.WEB_SPLIT_CHAR,File.separator));
		//add by tangxc
		String filePath = null;
		String srcpicpath="";
		if(reqURL.indexOf("/image/") > 0){
			filePath = reqURL.substring(reqURL.indexOf("/image/")+6).replace(GlobalVar.WEB_SPLIT_CHAR,File.separator);
			srcpicpath =tempPath.concat(filePath);
		}else if(reqURL.indexOf("/imageTemp/") > 0){
			filePath = reqURL.substring(reqURL.indexOf("/imageTemp/")+10).replace(GlobalVar.WEB_SPLIT_CHAR,File.separator);
			logger.info("新路径为："+filePath);
			srcpicpath =tempPath.concat(filePath);
		}else if(reqURL.indexOf("/zipImage/")>0){
			filePath = reqURL.substring(reqURL.indexOf("/zipImage/")+9).replace(GlobalVar.WEB_SPLIT_CHAR,File.separator);
			srcpicpath =GlobalVar.PIC_ZIP_ROOT_PATH.concat(filePath);
			tempScaleDomain=GlobalVar.PIC_SERVER_ZIP_SCALE_DOMAIN;
			scalePath=GlobalVar.PIC_ZIP_ROOT_PATH;
			tempDomain=tempScaleDomain;
		}else if(reqURL.indexOf("/uxunimg/")>0){
			filePath = reqURL.substring(reqURL.indexOf("/uxunimg/")+8).replace(GlobalVar.WEB_SPLIT_CHAR,File.separator);
			srcpicpath =GlobalVar.PIC_HOME_ROOT_PATH.concat(filePath);
			tempScaleDomain=GlobalVar.PIC_SERVER_HOME_SCALE_DOMAIN;
			scalePath=GlobalVar.PIC_HOME_ROOT_PATH;
			tempDomain=tempScaleDomain;
		}
		
		//缩略图最终地址（包含图片地址）
		String scaleImgPath="";
		String suffix = CommonTools.getFileSuffix(reqURL);
		//判断是否需要访问缩略图
		if(CommonTools.isNotEmpty(w) && CommonTools.isNotEmpty(h)){
			//返回缩略图路径 将图片地址替换为缩放图片地址以图片名称做为缩放地址最后一级
			String retScaleImgPath =srcpicpath.substring(0,srcpicpath.lastIndexOf(".")).replace(tempPath,scalePath);
			//原图片名称扩展名
			
			int iw = Integer.parseInt(w);
			int ih = Integer.parseInt(h);
			//判断缩略的数值是否在指定集合中存在  暂时屏蔽
			//if(LimitUtils.checkScale(iw,ih)){
				//判断缩略图是否已经存在
				scaleImgPath=retScaleImgPath.concat(File.separator).concat(CommonTools.getPicName(w,h,suffix));
				file = new File(scaleImgPath);
				if(file.exists()&&!file.isDirectory()){
					//存在则直接返回原缩略图
					//String resultUrl=tempScaleDomain.concat(GlobalVar.WEB_SPLIT_CHAR).concat(retScaleImgPath.replace(scalePath,""));
					//Add by tangxc 
					String resultUrl=tempScaleDomain.concat(scaleImgPath.replace(scalePath, "")).replace(File.separator, GlobalVar.WEB_SPLIT_CHAR);
					resultUrl = GlobalVar.WEB_SPLIT_CHAR+GlobalVar.FILES+resultUrl.replace(tempDomain.substring(0,tempDomain.lastIndexOf(GlobalVar.WEB_SPLIT_CHAR)),"");
					hsrequest.getRequestDispatcher(resultUrl).forward(hsrequest, response);
				}else{
					//原文件路径
					file = new File(srcpicpath);
					ImageUtil imageUtil = new ImageUtil();
					if(file.exists()&&!file.isDirectory()){
						//定义生成缩略图对象
						try {
							//创建缩略文件
							CommonTools.createDir(retScaleImgPath);
							//剪切图片
							imageUtil.saveImg(srcpicpath,scaleImgPath, iw,ih);
							logger.info("文件写入地址:"+scaleImgPath);
							//剪切后的图片地址
							String requestUrl=scaleImgPath.replace(scalePath,tempScaleDomain).replace(File.separator,GlobalVar.WEB_SPLIT_CHAR);
							//转发的图片地址
							requestUrl=GlobalVar.WEB_SPLIT_CHAR+GlobalVar.FILES+requestUrl.replace(tempDomain.substring(0,tempDomain.lastIndexOf(GlobalVar.WEB_SPLIT_CHAR)),"");
							logger.info("返回地址:"+requestUrl);
							hsrequest.getRequestDispatcher(requestUrl).forward(hsrequest, response);
						} catch (Exception e) {
							CommonTools.retDefaltScale(hsrequest, response, iw, ih );
						} 
						
					}else{
						CommonTools.retDefaltScale(hsrequest, response, iw, ih );
					}
					
				}
			/*}else{
				//不存在则视为非法访问
				CommonTools.writeLog(logger,"请求图片尺寸不存在",null);
			}*/
		}else{
			//不需要访问缩略图
			file =  new File(srcpicpath);
			if(file.exists()&&!file.isDirectory()){
//				logger.info("访问图片存在:"+srcpicpath+"  "+suffix);
				logger.info("访问图片存在:"+srcpicpath);
				chain.doFilter(hsrequest,response);
			}else{
				logger.error("访问的图片不存在,返回默认图片:"+GlobalVar.DEFAULT_FILE_URL);
				hsrequest.getRequestDispatcher(GlobalVar.DEFAULT_FILE_URL).forward(hsrequest, response);
			}
		}
		
		
		
	}
	

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
}
