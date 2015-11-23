//package com.bersolr.core.domain.util;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.ConnectException;
//
//import org.apache.log4j.Logger;
//
//import com.artofsolving.jodconverter.DocumentConverter;
//import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
//import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
//import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
//
//public class DocToSwf {
//
//	/**
//	 * 转为PDF
//	 *
//	 * @param file
//	 */
//	protected  static Logger logger=Logger.getLogger(DocToSwf.class);
//	public static void docToPdf(String docPath) throws Exception {
//
//		File docFile = new File(docPath);
//		String pdfPath=   docPath.substring(0, docPath.lastIndexOf("."))+".pdf";
//		File pdfFile = new File(pdfPath);
//		if (docFile.exists()) {
//			if (!pdfFile.exists()) {
//				logger.info("doc转换pdf开始");
//				OpenOfficeConnection connection = new SocketOpenOfficeConnection(8500);
//				try {
//					connection.connect();
//					DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
//					converter.convert(docFile, pdfFile);
//					// close the connection
//					connection.disconnect();
//					logger.debug("pdf转换成功,PDF路径：" + pdfFile.getPath());
//				} catch (java.net.ConnectException e) {
//					e.printStackTrace();
//					logger.error("swf转换器异常,openoffice服务未启动!");
//					throw e;
//				} catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
//					e.printStackTrace();
//					logger.error("swf转换器异常,读取转换文件失败:");
//					throw e;
//				} catch (Exception e) {
//					e.printStackTrace();
//					throw e;
//				}
//			} else {
//				logger.debug("已经转换为pdf，不需要再进行转化");
//			}
//		} else {
//			logger.error("swf转换器异常，需要转换的文档不存在，无法转换:"+docPath);
//		}
//	}
//
//	/**
//	 * 转换成 swf
//	 */
//	@SuppressWarnings("unused")
//	public static void pdf2swf(String pdfPath,Boolean isDel){
//		String swfPath=   pdfPath.substring(0, pdfPath.lastIndexOf("."))+".swf";
//		File swfFile = new File(swfPath);
//		File pdfFile = new File(pdfPath);
//		Runtime r = Runtime.getRuntime();
//		if (!swfFile.exists()) {
//			if (pdfFile.exists()) {
//				logger.info("pdf转换swf开始,操作系统:"+System.getProperty("os.name"));
//				if(System.getProperty("os.name") != null && System.getProperty("os.name").equals("Linux")){// linux环境处理
//						String shellStr = "pdf2swf -s languagedir=/usr/local/share/xpdf/xpdf-chinese-simplified " + pdfFile.getPath()
//								+ " -o " + swfFile.getPath() + " -T 9";
//						CommonTools.callShell(shellStr);
//						logger.info("swf转换shell:"+shellStr);
//						logger.info("swf转换成功,文件输出："+ swfFile.getPath());
//						if (pdfFile.exists()&&isDel) {
//							pdfFile.delete();
//						}
//
//				}else{// windows环境处理
//						String cmdStr = GlobalVar.SWFTOOLS_PATH+" "+ pdfFile.getPath() + " -o "+ swfFile.getPath() + " -T 9";
//						CommonTools.callShell(cmdStr);
//						logger.info("swf转换成功，文件输出："+ swfFile.getPath());
//						if (pdfFile.exists()&&isDel) {
//							pdfFile.delete();
//						}
//				}
//
//			} else {
//				logger.error("pdf不存在,无法转换:"+pdfPath);
//			}
//		} else {
//			    logger.error("swf已经存在不需要转换:"+pdfPath);
//		}
//	}
//
//
//	public static void startOpenOffice(){
//		if(System.getProperty("os.name") != null && System.getProperty("os.name").equals("Linux")){// linux环境处理
//			/*String cmd ="CD C:\\Program Files (x86)\\OpenOffice 4\\program ";
//			cmd = cmd + "soffice -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard";
//			System.out.println("cmd:"+cmd);
//			CommonTools.callShell(cmd);*/
//			/*
//			    CD C:\Program Files (x86)\OpenOffice 4\program
//                soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
//			 * */
//		}else{
//			CommonTools.callShell("soffice -headless -accept=\"socket,host=localhost,port=8500;urp;\" -nofirststartwizard & ");
//		}
//
//	}
//
//	public static void main(String[] args){
//		startOpenOffice();
//	}
//
//}
