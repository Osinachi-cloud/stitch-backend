package com.stitch.gateway.config.http;

//import org.apache.http.client.HttpClient;
//import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {

//    @Autowired
//    private HttpProperties httpProperties;
//
//    @Bean
//    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(){
//
//        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(2, TimeUnit.MINUTES);
//        connManager.setMaxTotal(httpProperties.getMaxTotal());
//        connManager.setDefaultMaxPerRoute(httpProperties.getMaxPerRoute());
//        return connManager;
//    }
//
//    @Bean
//    public RequestConfig requestConfig(){
//        return RequestConfig.custom()
//                .setConnectionRequestTimeout(httpProperties.getConnectionRequestTimeout()) //in milliseconds
//                .setConnectTimeout(httpProperties.getConnectionTimeout())
//                .setSocketTimeout(httpProperties.getSocketTimeout())
//                .build();
//    }
//
//    @Bean
//    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig){
//
//        return HttpClientBuilder.create()
//                .setConnectionManager(poolingHttpClientConnectionManager)
//                .setDefaultRequestConfig(requestConfig)
//                .build();
//    }

//    @Bean
//    public RestTemplate restTemplate(HttpClient httpClient){
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setHttpClient(httpClient);
//        return new RestTemplate(requestFactory);
//
//    }

    @Bean
    public RestTemplate restTemplate(){
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        return new RestTemplate();

    }
}
