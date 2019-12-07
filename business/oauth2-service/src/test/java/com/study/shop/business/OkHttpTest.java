package com.study.shop.business;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author Tiger
 * @date 2019-09-11
 * @see com.study.shop.business
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class OkHttpTest {
    @Test
    public void test(){
        String url="https://www.baidu.com";
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url).build();
        Call call=client.newCall(request);
        try{
            Response response=call.execute();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUtils(){
//        OkHttpClientUtil.getInstance().postData()
    }
}
