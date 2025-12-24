package com.service;

import com.config.SiliconFlowConfig;
import com.entity.siliconflow.SiliconFlowRequest;
import com.entity.siliconflow.SiliconFlowResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiChatService {

    @Autowired
    private SiliconFlowConfig siliconFlowConfig;

    @Autowired
    private WebClient siliconFlowWebClient;

    public String chatSimple(String userMessage) {
        try {
            SiliconFlowRequest request = new SiliconFlowRequest();
            request.setModel(siliconFlowConfig.getModel());
            request.setTemperature(0.7);
            request.setMax_tokens(siliconFlowConfig.getMaxTokens());
            request.setStream(false);

            SiliconFlowRequest.Message systemMsg = new SiliconFlowRequest.Message("system",
                    "你是一个专业的学科竞赛助手，请帮助学生解答关于学科竞赛的问题。");
            SiliconFlowRequest.Message userMsg = new SiliconFlowRequest.Message("user", userMessage);

            ArrayList<SiliconFlowRequest.Message> messages = new ArrayList<>();
            messages.add(systemMsg);
            messages.add(userMsg);
            request.setMessages(messages);

            // Use ObjectMapper to convert to Map if needed, or send object directly
            // Sending object directly is better if WebClient is configured with Jackson

            SiliconFlowResponse response = siliconFlowWebClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(SiliconFlowResponse.class)
                    .block(); // Blocking for Spring WebMVC, use subscribe/reactive for WebFlux

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "抱歉，AI服务暂时不可用：" + e.getMessage();
        }
        return "未获取到回复";
    }
}
