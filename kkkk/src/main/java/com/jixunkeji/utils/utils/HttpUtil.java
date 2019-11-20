package com.jixunkeji.utils.utils;


import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
 * Created by Administrator on 2017/8/21.
 */
public class HttpUtil {

    /**
     * HTTP的Post请求方式
     * @param strUrl 访问地址
     * @param param 参数字符串
     * */
    public static String requestPost(String strUrl, String param) {
        String returnStr = null; // 返回结果定义
        URL url = null;
        HttpURLConnection httpURLConnection = null;

        try {
            url = new URL(strUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST"); // post方式
            httpURLConnection.connect();
            //POST方法时使用
            byte[] byteParam = param.getBytes("UTF-8");
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.write(byteParam);
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            returnStr = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnStr;
    }

    /**
     * HttpClient的POST请求
     * @param url
     * @param params
     * @return
     */
    public static String requestPost(String url, List<NameValuePair> params) {
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instreams = entity.getContent();
                result = CommonUtil.convertStreamToString(instreams);
                System.out.println(result);
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String requestGet(String url, String param) {
        String returnStr = null; // 返回结果定义
        HttpURLConnection httpURLConnection = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            httpURLConnection = (HttpURLConnection) realUrl.openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            //POST方法时使用
            byte[] byteParam = param.getBytes("UTF-8");
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.write(byteParam);
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            returnStr = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnStr;
    }


    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String getIp() {
        HttpServletRequest request = getRequest();
        return request == null ? "127.0.0.1" : request.getRemoteHost();
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getResponse();
    }

    public static Map<String, String> getRequestParameters() {
        HashMap<String, String> values = new HashMap();
        HttpServletRequest request = getRequest();
        if (request == null) {
            return values;
        } else {
            Enumeration enums = request.getParameterNames();

            while(enums.hasMoreElements()) {
                String paramName = (String)enums.nextElement();
                String paramValue = request.getParameter(paramName);
                values.put(paramName, paramValue);
            }

            return values;
        }
    }
}
