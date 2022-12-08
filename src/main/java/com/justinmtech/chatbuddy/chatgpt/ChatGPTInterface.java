package com.justinmtech.chatbuddy.chatgpt;

import com.github.plexpt.chatgpt.Chatbot;

import java.util.Map;

public class ChatGPTInterface {
    private final Chatbot chatbot;

    public ChatGPTInterface(Chatbot chatbot) {
        this.chatbot = chatbot;
    }

    public String getResponse(String input) {
        Map<String, Object> chatResponse = chatbot.getChatResponse(input);
        return chatResponse.get("message").toString();
    }
}
