package com.hxh.basic.project.utils.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


/***
 *
 * @Description: HTTP辅助类
 * @Team: 公有云技术支持小组
 * @Author: 天云小生
 * @Date: 2018年01月14日
 */
public class HttpHelper {

    private static final String KEY = null;

    /***
     * POST请求的头部信息
     *
     * @param projectId
     * @param signature
     * @param ContentType
     * @param encoding
     * @return
     */
    public static LinkedHashMap<String, String> getPOSTHeaders(String projectId, String signature, String ContentType,
                                                               String encoding) {
        LinkedHashMap<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("X-Tsign-Open-App-Id", projectId);
        headers.put("X-Tsign-Open-Token", signature);
        headers.put("Content-Type", ContentType);
        headers.put("Charset", encoding);
        return headers;
    }

    /***
     * 向指定URL发送POST方法的请求
     *
     * @param apiUrl
     * @param data
     * @param encoding
     * @return
     */
    public static JSONObject sendPOST(String apiUrl, String data, LinkedHashMap<String, String> headers,
                                      String encoding) {
        StringBuffer strBuffer = null;
        JSONObject obj = null;
        try {
            // 建立连接
            URL url = new URL(apiUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // 需要输出
            httpURLConnection.setDoOutput(true);
            // 需要输入
            httpURLConnection.setDoInput(true);
            // 不允许缓存
            httpURLConnection.setUseCaches(false);

            httpURLConnection.setRequestMethod("POST");
            // 设置Headers
            if (null != headers) {
                for (String key : headers.keySet()) {
                    httpURLConnection.setRequestProperty(key, headers.get(key));
                }
            }

            // 连接会话
            httpURLConnection.connect();
            // 建立输入流，向指向的URL传入参数
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            // 设置请求参数
            dos.write(data.getBytes(encoding));
            dos.flush();
            dos.close();
            // 获得响应状态
            int http_StatusCode = httpURLConnection.getResponseCode();
            String http_ResponseMessage = httpURLConnection.getResponseMessage();
            obj = new JSONObject();
            if (HttpURLConnection.HTTP_OK == http_StatusCode) {
                strBuffer = new StringBuffer();
                String readLine = new String();
                BufferedReader responseReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    strBuffer.append(readLine);
                }
                responseReader.close();
                // System.out.println("http_StatusCode = " + http_StatusCode + "
                // request_Parameter = " + data);
                obj = JSONObject.parseObject(strBuffer.toString());
            } else {
                // 如果是因为权限调用失败,重新获取token
                throw new RuntimeException(
                        MessageFormat.format("请求失败,失败原因: Http状态码 = {0} , {1}", http_StatusCode, http_ResponseMessage));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /***
     * 向指定URL发送PUT方法的请求
     *
     * @return
     */

    /***
     * 向指定URL发送PUT方法的请求
     *
     * @return
     */
    public static JSONObject sendPUT(String fileUploadUrl, String param, LinkedHashMap<String, String> headers) {
        StringBuffer strBuffer = null;
        JSONObject object = new JSONObject();
        // 是否上传成功
        try {
            // 建立连接
            URL url = new URL(fileUploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // 需要输出
            httpURLConnection.setDoOutput(true);
            // 需要输入
            httpURLConnection.setDoInput(true);
            // 不允许缓存
            httpURLConnection.setUseCaches(false);

            httpURLConnection.setRequestMethod("PUT");
            // 设置Headers
            if (null != headers) {
                for (String key : headers.keySet()) {
                    httpURLConnection.setRequestProperty(key, headers.get(key));
                }
            }
            // 连接会话
            httpURLConnection.connect();
            // 建立输入流，向指向的URL传入参数
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            // 设置请求参数
            if (param != null) {
                dos.write(param.getBytes());
                dos.flush();
                dos.close();
            }
            // 获得响应状态
            int http_StatusCode = httpURLConnection.getResponseCode();
            String http_ResponseMessage = httpURLConnection.getResponseMessage();

            if (HttpURLConnection.HTTP_OK == http_StatusCode) {
                strBuffer = new StringBuffer();
                String readLine = new String();
                BufferedReader responseReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    strBuffer.append(readLine);
                }
                responseReader.close();
                object = JSONObject.parseObject(strBuffer.toString());
                // System.out.println("存证环节编号（证据点编号） = " + evId + "
                // 的待存证文档上传成功！Http状态码 = " + http_StatusCode);
            } else {
                throw new RuntimeException(MessageFormat.format("put调用失败= {0} ,失败信息:Http状态码 = {0} , {1}",
                        http_StatusCode, http_ResponseMessage));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    /***
     * 向指定URL发送GET方法的请求
     *
     * @param apiUrl
     * @param data

     * @param encoding
     * @return
     */
    public static JSONObject sendGET(String apiUrl, String data, Map<String, String> headers, String encoding) {
        StringBuffer strBuffer = null;
        JSONObject obj = null;
        String code = "";
        String str = "";
        try {
            // 建立连接
            CloseableHttpClient httpsClient = SSLClient.getHttpsClient();
            HttpGet get = new HttpGet(apiUrl);
            // 设置Headers
            if (null != headers) {
                for (String key : headers.keySet()) {
                    get.setHeader(key, headers.get(key));
                }
            }
            CloseableHttpResponse execute = httpsClient.execute(get);
            code = execute.getStatusLine().getStatusCode() + "";
            HttpEntity entity = execute.getEntity();
            str = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return JSONObject.parseObject(str);
        } catch (Exception e) {
            System.out.println(str);
            e.printStackTrace();
            throw new RuntimeException("get调用失败" + code);

        }
    }

    /***
     * 向指定URL发送GET方法的请求
     *
     * @param apiUrl
     * @param data
     * @param encoding
     * @return
     */
    public static String sendGET1(String apiUrl, String data, Map<String, String> headers, String encoding) {
        StringBuffer strBuffer = null;
        JSONObject obj = null;
        String code = "";
        String str = "";
        CloseableHttpClient httpsClient = SSLClient.getHttpsClient();
        HttpGet get = new HttpGet(apiUrl);
        try {
            // 建立连接


            // 设置Headers
            if (null != headers) {
                for (String key : headers.keySet()) {
                    get.setHeader(key, headers.get(key));
                }
            }
            CloseableHttpResponse execute = httpsClient.execute(get);
            code = execute.getStatusLine().getStatusCode() + "";
            HttpEntity entity = execute.getEntity();
            str = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return (str);
        } catch (Exception e) {
            System.out.println(str);
            e.printStackTrace();
            throw new RuntimeException("get调用失败" + code);

        } finally {
            try {
                httpsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /***
     * 向指定URL发送GET方法的请求
     *
     *
     * @return
     */

    public static String PutFile(String url2, LinkedHashMap<String, String> headers, File file) {
        StringBuffer strBuffer = null;
        String str = null;
        String code = "";
        try {
            // 建立连接
            CloseableHttpClient httpsClient = SSLClient.getHttpsClient();
            HttpPut get = new HttpPut(url2);
            // 设置Headers
            if (null != headers) {
                for (String key : headers.keySet()) {
                    get.setHeader(key, headers.get(key));
                }
            }

            FileBody bin = new FileBody(file);

            HttpEntity mulentity = new FileEntity(file, headers.get("Content-Type"));
            get.setEntity(mulentity);
            CloseableHttpResponse execute = httpsClient.execute(get);
            HttpEntity entity = execute.getEntity();
            StatusLine statusLine = execute.getStatusLine();
            str = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            return str;
        } catch (Exception e) {
            throw new RuntimeException("get调用失败");
        }
    }

    /**
     * 获得跳转后的地址
     */

    public static String getRedirectUrl(String url) {
        try {
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");
            // 必须设置false，否则会自动redirect到Location的地址
            conn.setInstanceFollowRedirects(false);

            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
            conn.connect();
            String location = conn.getHeaderField("Location");

            serverUrl = new URL(location);
            conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");

            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
            conn.addRequestProperty("Referer", "http://zuidaima.com/");
            conn.connect();
            return location;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 获得回调者的ip
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 根据文件下载地址下载文件
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        // 防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        // 得到输入流
        InputStream inputStream = conn.getInputStream();
        // 获取自己数组

        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();

        byte[] getData = bos.toByteArray();

        // 文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }


    }

    /**
     * delete方法
     */
    public static JSONObject sendDelete(String url, String data, LinkedHashMap<String, String> headers) {
        try {
            // 建立连接
            CloseableHttpClient httpsClient = SSLClient.getHttpsClient();
            HttpDelete delete = new HttpDelete(url);
            // 设置Headers
            if (null != headers) {
                for (String key : headers.keySet()) {
                    delete.setHeader(key, headers.get(key));
                }
            }
            CloseableHttpResponse execute = httpsClient.execute(delete);
            HttpEntity entity = execute.getEntity();
            String str = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return JSONObject.parseObject(str);
        } catch (Exception e) {
            throw new RuntimeException("delete调用失败");

        }
    }

    /**
     * post参数乱码转换器
     */
    public static HttpServletRequest doFilterPOST(HttpServletRequest request) {
        if ("POST".equals(request.getMethod())) {
            // 参数转换，因为我们已经它肯定是Http协议的请求。
            try {
                request.setCharacterEncoding("UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Map parameterMap = request.getParameterMap();

            if (!parameterMap.isEmpty()) {
                Iterator it = parameterMap.keySet().iterator();
                String value[] = null;
                while (it.hasNext()) {
                    String key = it.next().toString();
                    value = (String[]) parameterMap.get(key);
                    for (int i = 0; i < value.length; i++) {
                        try {
                            String string = new String(value[i].getBytes("UTF-8"), "UTF-8");
                            request.setAttribute(key, string);
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }

            }
        }
        return request;
    }

    public static String post1(String url, List<BasicNameValuePair> params) throws IOException {
        HttpPost httppost = new HttpPost(url); //建立HttpPost对象


        httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//设置编码

        HttpResponse response = new DefaultHttpClient().execute(httppost);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(response.getEntity(), "UTF-8");
        return content;
    }

}
