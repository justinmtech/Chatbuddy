package com.justinmtech.chatbuddy;

import com.github.plexpt.chatgpt.Chatbot;
import com.justinmtech.chatbuddy.minecraft.ChatCommand;
import com.justinmtech.chatbuddy.chatgpt.ChatGPTInterface;
import org.bukkit.plugin.java.JavaPlugin;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

public final class Chatbuddy extends JavaPlugin {
    private ChatGPTInterface chatGPTInterface;
    private DiscordApi discordApi;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configureChatGptInterface();
        if (getConfig().getBoolean("discord_bot.enabled", false)) {
            new DiscordApiBuilder()
                    .setToken(getConfig().getString("discord_bot.bot_token"))
                    .addIntents(Intent.MESSAGE_CONTENT)
                    .login()
                    .thenAccept(this::onConnectToDiscord)
                    .exceptionally(error -> {
                        getLogger().warning("The bot failed to connect to Discord. Plugin disabling!");
                        getPluginLoader().disablePlugin(this);
                        return null;
                    });
        }
        getCommand("chatgpt").setExecutor(new ChatCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void onConnectToDiscord(DiscordApi api) {
        setDiscordApi(api);

        getLogger().info("Bot connected to Discord server as: " + api.getYourself().getDiscriminatedName());
        getLogger().info("Go to this link to invite bot to your server: " + api.createBotInvite());

        api.addListener(new com.justinmtech.chatbuddy.discord.ChatCommand(this));

    }

    private void configureChatGptInterface() {
        //String email = getConfig().getString("email");
        //String password = getConfig().getString("password");
        String token = getConfig().getString("session_token");
        setChatGPTInterface(new ChatGPTInterface(new Chatbot(token)));
    }

    public ChatGPTInterface getChatGPTInterface() {
        return chatGPTInterface;
    }

    public void setChatGPTInterface(ChatGPTInterface chatGPTInterface) {
        this.chatGPTInterface = chatGPTInterface;
    }

    public void setDiscordApi(DiscordApi discordApi) {
        this.discordApi = discordApi;
    }

    public DiscordApi getDiscordApi() {
        return discordApi;
    }
}
