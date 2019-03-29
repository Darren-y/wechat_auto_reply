package ai.chainbase.wechat_msg.utils;


import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.*;

@Log4j2
public class HttpUtil {

    public static String sendPostJson(String url, String jsonData) {
        String result = null;
        CloseableHttpClient httpClient;
        CloseableHttpResponse httpResponse = null;

        httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        StringEntity entity = new StringEntity(jsonData, ContentType.create("text/json", "UTF-8"));
        post.setEntity(entity);
        try {
            httpResponse = httpClient.execute(post);
            result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpResponse != null)
                    httpResponse.close();
                if (httpClient != null)
                    httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
