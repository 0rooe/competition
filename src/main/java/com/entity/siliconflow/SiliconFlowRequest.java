package com.entity.siliconflow;

import java.util.List;
import java.util.Map;

/**
 * SiliconFlow Request DTO
 */
public class SiliconFlowRequest {
    private String model; // 模型名称
    private List<Message> messages; // 对话消息列表
    private Double temperature; // 温度参数(0-1)
    private Integer max_tokens; // 最大生成token数
    private Boolean stream; // 是否流式输出
    private Map<String, Object> tools; // 工具调用(可选)

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(Integer max_tokens) {
        this.max_tokens = max_tokens;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public Map<String, Object> getTools() {
        return tools;
    }

    public void setTools(Map<String, Object> tools) {
        this.tools = tools;
    }

    public static class Message {
        private String role; // system/user/assistant
        private String content; // 消息内容

        public Message() {
        }

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
