package com.entity.siliconflow;

import java.util.List;

/**
 * SiliconFlow Response DTO
 */
public class SiliconFlowResponse {
    private String id; // 响应ID
    private String object; // 对象类型
    private Long created; // 创建时间戳
    private String model; // 使用的模型
    private List<Choice> choices; // 回复选项列表
    private Usage usage; // token使用统计

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public static class Choice {
        private Integer index; // 选项索引
        private Message message; // 消息内容
        private String finish_reason; // 结束原因

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public String getFinish_reason() {
            return finish_reason;
        }

        public void setFinish_reason(String finish_reason) {
            this.finish_reason = finish_reason;
        }
    }

    public static class Message {
        private String role;
        private String content;

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

    public static class Usage {
        private Integer prompt_tokens; // 输入token数
        private Integer completion_tokens; // 输出token数
        private Integer total_tokens; // 总token数

        public Integer getPrompt_tokens() {
            return prompt_tokens;
        }

        public void setPrompt_tokens(Integer prompt_tokens) {
            this.prompt_tokens = prompt_tokens;
        }

        public Integer getCompletion_tokens() {
            return completion_tokens;
        }

        public void setCompletion_tokens(Integer completion_tokens) {
            this.completion_tokens = completion_tokens;
        }

        public Integer getTotal_tokens() {
            return total_tokens;
        }

        public void setTotal_tokens(Integer total_tokens) {
            this.total_tokens = total_tokens;
        }
    }
}
