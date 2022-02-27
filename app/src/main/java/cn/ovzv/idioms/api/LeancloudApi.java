package cn.ovzv.idioms.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.HttpGet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.leancloud.LCCloud;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LeancloudApi {

    private static final String CONTENT_TYPE_TAG = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String UTF8 = "UTF-8";

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";

    public static final String APP_ID = "Ao3V1A0vUSegwa8uPi8zyVWo-9Nh9j0Va";
    public static final String APP_KEY = "Eq7sgbHSUr8b3jKS7K48Hgp6";
    public static final String APP_MasterKey = "rcI6nc7mwf7QixyNvhIPdJ3o";

    public static final String REST_API = "https://idiom-api.wxiou.cn";
    public static final String FileUpload_API = "https://upload.qiniup.com";
    public static final String File_Url = "https://idiom-file.wxiou.cn";

    /**
     * 收集反馈意见
     * @return JSON格式结果
     */
    public static void Feedback_save(String str){
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("feedback", str );
        LCCloud.callFunctionInBackground("Feedback_save", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
            }

            @Override
            public void onError(Throwable throwable) {
                // failed.
            }

            @Override
            public void onComplete() {

            }
        });
    }
    /**
     * 评论文章
     * @return JSON格式结果
     */
    public static void Comment_save(String NewsId,String Username,String content){
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("NewsId", NewsId );
        dicParameters.put("Username", Username );
        dicParameters.put("content", content );
        LCCloud.callFunctionInBackground("Comment_save", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
            }

            @Override
            public void onError(Throwable throwable) {
                // failed.
            }

            @Override
            public void onComplete() {

            }
        });
    }
    /**
     * 回复文章的评论
     * @return JSON格式结果
     */
    public static void Commentreply_save(String Comment_replyID,String Username,String content){
        Map<String, Object> dicParameters = new HashMap<>();
        dicParameters.put("Comment_replyID", Comment_replyID );
        dicParameters.put("Username", Username );
        dicParameters.put("content", content );
        LCCloud.callFunctionInBackground("Commentreply_save", dicParameters).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object object) {
                // succeed.
            }

            @Override
            public void onError(Throwable throwable) {
                // failed.
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private static JSONObject streamToJson(InputStream inputStream) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,UTF8));
        String temp = "";
        StringBuilder stringBuilder = new StringBuilder();
        while ((temp = bufferedReader.readLine()) != null) {
            stringBuilder.append(temp);
        }
        JSONObject json = JSON.parseObject(stringBuilder.toString().trim());
        return json;
    }
    private static JSONObject URL_Post(String Url) throws Exception {
        URL url = new URL(Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(METHOD_POST);
        conn.setConnectTimeout(5000);
        // 处理请求的数据
        InputStream inputStream = conn.getInputStream();
        JSONObject json = streamToJson(inputStream); // 从响应流中提取 JSON
        return json;
    }
    public static String getContent(String url) throws Exception {

        StringBuilder sb = new StringBuilder();

        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();
        // 设置网络超时参数
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);

        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        HttpResponse response = client.execute(new HttpGet(url));
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    entity.getContent(), "UTF-8"), 8192);

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();

        }

        return sb.toString();
    }
    public static HttpResponse post(Map<String, Object> params, String url) {

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("charset", "UTF-8");
        httpPost.setHeader("Content-Type",
                "application/x-www-form-urlencoded; charset=utf-8");
        HttpResponse response = null;
        if (params != null && params.size() > 0) {
            String string = JSON.toJSONString(params);
            StringEntity entity = new StringEntity(string, ContentType.APPLICATION_JSON);
            try {
                httpPost.setEntity(entity);
                response = client.execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {
            try {
                response = client.execute(httpPost);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;

    }
    public static Object getValues(Map<String, Object> params, String url) {
        String token = "";
        HttpResponse response = post(params, url);
        if (response != null) {
            try {
                token = EntityUtils.toString(response.getEntity());
                response.removeHeaders("operator");
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return token;
    }
    public static Object setImgByStr(String token, String img,String url ){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("buffer", img);
        params.put("token", token);
        return getValues(params, url);
    }

}
