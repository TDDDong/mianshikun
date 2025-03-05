package com.dd.mianshikun.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.volcengine.ark.runtime.service.ArkService;
import com.zhipu.oapi.ClientV4;
import lombok.Data;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiConfig {
    private String zhipuKey;

    private String deepSeekKey;

    @Bean
    public ClientV4 getClient() {
        return new ClientV4.Builder(zhipuKey).build();
    }

    @Bean
    public ArkService getService() {
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        ArkService service = ArkService.builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .baseUrl("https://ark.cn-beijing.volces.com/api/v3")
                .apiKey(deepSeekKey)
                .build();
        return service;
    }
}
