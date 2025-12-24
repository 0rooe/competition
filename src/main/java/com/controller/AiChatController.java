package com.controller;

import com.annotation.IgnoreAuth;
import com.service.AiChatService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    @IgnoreAuth
    @PostMapping("/chat")
    public R chat(@RequestBody Map<String, String> params) {
        String message = params.get("message");
        if (message == null || message.trim().isEmpty()) {
            return R.error("请输入问题");
        }
        String reply = aiChatService.chatSimple(message);
        return R.ok().put("reply", reply);
    }
}
