package com.justinmtech.chatbuddy.discord;

import com.justinmtech.chatbuddy.chatgpt.ChatGPTInterface;
import com.justinmtech.chatbuddy.Chatbuddy;
import org.bukkit.Bukkit;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class ChatCommand implements MessageCreateListener {
    private final Chatbuddy plugin;
    private final ChatGPTInterface chatGPTInterface;

    public ChatCommand(Chatbuddy plugin) {
        this.chatGPTInterface = plugin.getChatGPTInterface();
        this.plugin = plugin;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().contains("!chatbuddy")) {
            String input = event.getMessageContent().replace("!chatbuddy ", "");
            buildDiscordMessage(event, "Thinking...");
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                String response = getChatGPTInterface().getResponse(input);
                buildDiscordMessage(event, response);
            });
        }
    }

    public ChatGPTInterface getChatGPTInterface() {
        return chatGPTInterface;
    }

    private void buildDiscordMessage(MessageCreateEvent e, String response) {
        new MessageBuilder()
                .append(response)
                .send(e.getChannel());
    }
}
