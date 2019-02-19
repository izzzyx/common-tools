package izzzyx.common.tools;


import izzzyx.common.tools.entity.HttpHeader;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author zhangshuning. Date:2018/7/26
 */
public class HttpUtil {
    
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);

    public static <T> T get(String url, ResponseHandler<T> responseHandler){

        CloseableHttpClient httpclient = HttpClients.createDefault();

        T responseBody = null;

        try {

            HttpGet httpget = new HttpGet(url);

            LOG.info("Executing request " + httpget.getRequestLine());

            responseBody = httpclient.execute(httpget, responseHandler);
            LOG.info("----------------------------------------");
            LOG.info("resposeBody:{}", responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responseBody;

    }


    /**
     * 发送post请求
     * @param url url
     * @param data stringEntiry，param=val&param2=val2
     * @param responseHandler 处理response的类
     * @param <T> 返回数据javabean
     * @return 返回数据javabean
     */
    public static <T> T post(String url, HttpEntity data, ResponseHandler<T> responseHandler, HttpHeader header){
        return post(url, data, responseHandler, header.toString());
    }

    /**
     * 发送post请求
     * @param url url
     * @param entity httpentity(curl的d)
     * @param responseHandler 处理response的类
     * @param <T> 返回数据javabean
     * @return 返回数据javabean
     */
    public static <T> T post(String url, HttpEntity entity, ResponseHandler<T> responseHandler, String header){

        HttpPost httpPost = new HttpPost(url);

        if (header != null && !header.equals("")) {
            httpPost.setHeader("content-type", header);
        }
        httpPost.setEntity(entity);

        T responseBody = post(httpPost, responseHandler);

        return responseBody;

    }


    /**
     * 带验证的post请求
     * @param url url
     * @param entity httpentity(curl的d)
     * @param responseHandler 处理response的类
     * @param <T> 返回数据javabean
     * @return 返回数据javabean
     */
    public static <T> T authPost(String url, HttpEntity entity, ResponseHandler<T> responseHandler, String header, Pair<String, String> authPair){

        HttpPost httpPost = new HttpPost(url);

        if (header != null && !header.equals("")) {
            httpPost.setHeader("content-type", header);
        }
        if (authPair != null){
            try {
                UsernamePasswordCredentials creds = new UsernamePasswordCredentials(authPair.getLeft(), authPair.getRight());
                httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
            } catch (AuthenticationException e) {
                e.printStackTrace();
            }
        }
        httpPost.setEntity(entity);

        T responseBody = post(httpPost, responseHandler);

        return responseBody;

    }


    /**
     * 发送post请求
     * @param httpPost httpPost
     * @param responseHandler  处理response的类
     * @param <T> 返回数据javabean
     * @return 返回数据javabean
     */
    public static <T> T post(HttpPost httpPost, ResponseHandler<T> responseHandler){

        CloseableHttpClient httpclient = HttpClients.createDefault();

        T responseBody = null;

        try {

            LOG.info("Executing request:{}{}\nheader:{}", httpPost, EntityUtils.toString(httpPost.getEntity()), httpPost.getAllHeaders());

            responseBody = httpclient.execute(httpPost, responseHandler);
            LOG.info("----------------------------------------");
            LOG.info("resposeBody:{}", responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responseBody;

    }


    /**
     * 生成post data为键值对类型的httpEntity
     * utf-8编码
     * @param data
     * @return
     */
    public static HttpEntity nameValPairEntity(List<BasicNameValuePair> data){
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LOG.error("generate nameValPairEntity error:{}, cause by:{}", e.getMessage(), e.getCause());
        }
        return entity;
    }


    /**
     * 生成post data为字符串(比如json)类型的httpEntity
     * utf-8编码
     * @param data
     * @return
     */
    public static HttpEntity stringEntity(String data){
        return new StringEntity(data, "UTF-8");
    }



}
