package com.jixunkeji.utils.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Scope("singleton")
@Component(value = "ossUtil")
public class OSSUtil {


	protected static final Logger log = LoggerFactory.getLogger(OSSUtil.class);
//	/** 测试 **/
//	 private static String ENDPOINT = Constance.OSS_ENDPOINT_TEST;
//	 private static String ACCESS_KEY_ID = Constance.OSS_ACCESS_KEY_ID_TEST;
//	 private static String accessKeySecret = Constance.OSS_ACCESSKEYSECRET_TEST;
//	 private static String bucketName = Constance.OSS_BUCKETNAME_TEST;
	/** 生产 **/
	private final static String ENDPOINT = "http://oss-cn-shenzhen.aliyuncs.com";
	private final static String ACCESS_KEY_ID = "LTAInJW0Yi4un1mL";
	private final static String ACCESS_KEY_SECRET = "DkpdVtY6MB4iSq6FHuEVTh7WhVVs8q";
	private final static String BUCKET_NAME = "honghutupian";
	private final static String VISIT_PATH = "https://jiangsuhonghu.oss-cn-beijing.aliyuncs.com";

	private static OSSClient ossClient;

	/**
	 * web
	 */
	private String filedir = "userImg/";

	private OSSUtil() {
		// 创建OSSClient实例。
		ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
	}

	public static void main(String[] args) throws Exception {
		OSSUtil ossUtil = new OSSUtil();
		ossUtil.downFileUrl("DeviceInfo.xls", "device/template");

		//Banner/BN1557912808394_1557912808394
		String s = ossUtil.downFileUrl("Banner/BN1557912808394_1557912808394");
		System.out.println(s);
	}

	public String uploadByInputStream(File file, String sName) {
		ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		ossClient.putObject(BUCKET_NAME, sName, file);
		// 关闭client
		ossClient.shutdown();
		return VISIT_PATH + "/" + sName;
	}
	
	/**
	 * 文件上传
	 * 
	 * @param filePath
	 *            本地文件路径
	 * @param fileName
	 *            上传至服务器的文件名称
	 * @param objectType
	 *            上传至服务器文件夹路径
	 * @return true-成功， false-失败
	 */

	public boolean uploadFile(String filePath, String fileName, String objectType) {
		if (StringUtils.isBlank(fileName) || StringUtils.isBlank(objectType) || StringUtils.isBlank(filePath)) {
			return false;
		}
		boolean flag = false;
		try {
			// 设置服务器文件位置及命名
			String objectName = objectType + "/" + fileName;
			// 上传文件流。
			File f = new File(filePath);
			FileInputStream fin = new FileInputStream(f);

			ossClient.putObject(BUCKET_NAME, objectName, fin);
			flag = true;
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e.getMessage());
		} finally {
			// ossClient.shutdown();
		}
		return flag;
	}

	/**
	 * 文件上传（InputStream 流上传）
	 * 
	 * @param fin
	 *            文件流
	 * @param fileName
	 *            上传至服务器的文件名称
	 * @param objectType
	 *            上传至服务器文件夹路径
	 * @return true-成功， false-失败
	 */
	public boolean uploadFile(InputStream fin, String fileName, String objectType) {
		if (StringUtils.isBlank(fileName) || StringUtils.isBlank(objectType) || fin == null) {
			return false;
		}
		boolean flag = false;
		try {
			// 设置服务器文件位置及命名
			String objectName = objectType + "/" + fileName;
			// 上传文件流。
			ossClient.putObject(BUCKET_NAME, objectName, fin);
			flag = true;
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e.getMessage());
		} finally {
			// ossClient.shutdown();
		}
		return flag;
	}

	/**
	 * 文件下载 （下载返回byte数组）
	 * 
	 * @param fileName
	 *            文件名称
	 * @param objectType
	 *            服务器文件路径
	 * @return 文件byte数组
	 * @throws Exception
	 */
	public byte[] downFile(String fileName, String objectType) throws Exception {
		if (StringUtils.isBlank(fileName) || StringUtils.isBlank(objectType)) {
			return null;
		}
		// 设置服务器文件位置及命名
		String objectName = objectType + "/" + fileName;
		// ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
		OSSObject ossObject = ossClient.getObject(BUCKET_NAME, objectName);
		InputStream in = ossObject.getObjectContent();
		// 读取文件内容。
		ByteArrayOutputStream baOut = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = in.read(buff, 0, 100)) > 0) {
			baOut.write(buff, 0, rc);
		}
		byte[] in2b = baOut.toByteArray();

		// ossClient.shutdown();
		return in2b;
		// 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
	}

	/**
	 * 文件下载 （下载返回InputStream 流）
	 * 
	 * @param fileName
	 *            文件名称
	 * @param objectType
	 *            服务器文件路径
	 * @return 文件流InputStream
	 * @throws Exception
	 */
	public InputStream downFileIn(String fileName, String objectType) throws Exception {
		if (StringUtils.isBlank(fileName) || StringUtils.isBlank(objectType)) {
			return null;
		}
		// 设置服务器文件位置及命名
		String objectName = objectType + "/" + fileName;
		// ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
		OSSObject ossObject = ossClient.getObject(BUCKET_NAME, objectName);
		InputStream in = ossObject.getObjectContent();

		// ossClient.shutdown();
		return in;
		// 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
	}

	/**
	 * 文件下载 （下载返回URL链接）
	 * 
	 * @param fileName
	 *            文件名称
	 * @param objectType
	 *            服务器文件路径
	 * @return 文件网络连接
	 * @throws Exception
	 */
	public String downFileUrl(String fileName, String objectType) {
		if (StringUtils.isBlank(fileName) || StringUtils.isBlank(objectType)) {
			return null;
		}
		// 设置服务器文件位置及命名
		String objectName = objectType + "/" + fileName;
		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
		// 生成URL
		URL url = ossClient.generatePresignedUrl(BUCKET_NAME, objectName, expiration);
		if (url != null) {
			return url.toString();
		}
		return null;
	}

	/**
	 * 文件下载 （下载返回URL链接）
	 * 
	 * @param fileName
	 *            文件名称
	 * @return 文件网络连接
	 * @throws Exception
	 */
	public String downFileUrl(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}
		// 设置服务器文件位置及命名
		String objectName = fileName;
		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
		// 生成URL
		URL url = ossClient.generatePresignedUrl(BUCKET_NAME, objectName, expiration);
		if (url != null) {
			return url.toString();
		}
		return null;
	}


	/**
	 *
	 * 上传图片
	 * @param file
	 * @return
	 */
	public String uploadImg2Oss(MultipartFile file) {
		if (file.getSize() > 1024 * 1024 *20) {
			return "图片太大";
		}
		String originalFilename = file.getOriginalFilename();
		String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
		Random random = new Random();
		String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
		try {
			InputStream inputStream = file.getInputStream();
			this.uploadFile2OSS(inputStream, name);
			return name;
		} catch (Exception e) {
			return "上传失败";
		}
	}

	/**
	 * 上传图片获取fileUrl
	 * @param instream
	 * @param fileName
	 * @return
	 */
	private String uploadFile2OSS(InputStream instream, String fileName) {
		String ret = "";
		try {
			//创建上传Object的Metadata
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(instream.available());
			objectMetadata.setCacheControl("no-cache");
			objectMetadata.setHeader("Pragma", "no-cache");
			objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
			objectMetadata.setContentDisposition("inline;filename=" + fileName);
			//上传文件

			OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
			PutObjectResult putResult = ossClient.putObject(BUCKET_NAME, filedir + fileName, instream, objectMetadata);
			ret = putResult.getETag();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static String getcontentType(String FilenameExtension) {
		if (FilenameExtension.equalsIgnoreCase(".bmp")) {
			return "image/bmp";
		}
		if (FilenameExtension.equalsIgnoreCase(".gif")) {
			return "image/gif";
		}
		if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
				FilenameExtension.equalsIgnoreCase(".jpg") ||
				FilenameExtension.equalsIgnoreCase(".png")) {
			return "image/jpeg";
		}
		if (FilenameExtension.equalsIgnoreCase(".html")) {
			return "text/html";
		}
		if (FilenameExtension.equalsIgnoreCase(".txt")) {
			return "text/plain";
		}
		if (FilenameExtension.equalsIgnoreCase(".vsd")) {
			return "application/vnd.visio";
		}
		if (FilenameExtension.equalsIgnoreCase(".pptx") ||
				FilenameExtension.equalsIgnoreCase(".ppt")) {
			return "application/vnd.ms-powerpoint";
		}
		if (FilenameExtension.equalsIgnoreCase(".docx") ||
				FilenameExtension.equalsIgnoreCase(".doc")) {
			return "application/msword";
		}
		if (FilenameExtension.equalsIgnoreCase(".xml")) {
			return "text/xml";
		}
		return "image/jpeg";
	}

	/**
	 * 获取图片路径
	 * @param fileUrl
	 * @return
	 */
	public String getImgUrl(String fileUrl) {
		if (!StringUtils.isEmpty(fileUrl)) {
			String[] split = fileUrl.split("/");
			String url =  this.getUrl(this.filedir + split[split.length - 1]);
			return url;
		}
		return null;
	}

	/**
	 * 获得url链接
	 *
	 * @param key
	 * @return
	 */
	public String getUrl(String key) {
		// 设置URL过期时间为10年  3600l* 1000*24*365*10
		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
		// 生成URL
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		URL url = ossClient.generatePresignedUrl(BUCKET_NAME, key, expiration);
		if (url != null) {
			return url.toString();
		}
		return null;
	}


	/**
	 * 多图片上传
	 * @param fileList
	 * @return
	 */
	public String checkList(List<MultipartFile> fileList) {
		String  fileUrl = "";
		String  str = "";
		String  photoUrl = "";
		for(int i = 0;i< fileList.size();i++){
			fileUrl = uploadImg2Oss(fileList.get(i));
			str = getImgUrl(fileUrl);
			if(i == 0){
				photoUrl = str;
			}else {
				photoUrl += "," + str;
			}
		}
		return photoUrl.trim();
	}

	/**
	 * 单个图片上传
	 * @param file
	 * @return
	 */
	public String checkImage(MultipartFile file){
		String fileUrl = uploadImg2Oss(file);
		String str = getImgUrl(fileUrl);
		return str.trim();
	}

}