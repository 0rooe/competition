package com.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@ConfigurationProperties(prefix = "siliconflow.api")
public class SiliconFlowConfig {
    private String baseUrl; // API地址
    private String key; // API密钥
    private String model; // 模型名称
    private Integer timeout; // 超时时间
    private Integer maxTokens; // 最大token数

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    @Bean
    public WebClient siliconFlowWebClient() {
        // Updated for compatibility with older Spring Boot 2.2.x / Reactor Netty 0.9.x
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client -> client
                        .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS,
                                timeout == null ? 60000 : timeout)
                        .doOnConnected(conn -> conn
                                .addHandlerLast(new io.netty.handler.timeout.ReadTimeoutHandler(
                                        (timeout == null ? 60000 : timeout) / 1000))
                                .addHandlerLast(new io.netty.handler.timeout.WriteTimeoutHandler(
                                        (timeout == null ? 60000 : timeout) / 1000))));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Authorization", "Bearer " + key)
                .defaultHeader("Content-Type", "application/json")
                .exchangeStrategies(org.springframework.web.reactive.function.client.ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                        .build())
                .build();
    }
}
