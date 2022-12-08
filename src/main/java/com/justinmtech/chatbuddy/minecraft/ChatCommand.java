package com.justinmtech.chatbuddy.minecraft;

import com.justinmtech.chatbuddy.chatgpt.ChatGPTInterface;
import com.justinmtech.chatbuddy.Chatbuddy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommand implements CommandExecutor {
    private final Chatbuddy plugin;
    private final ChatGPTInterface chatGPTInterface;

    public ChatCommand(Chatbuddy plugin) {
        this.plugin = plugin;
        this.chatGPTInterface = plugin.getChatGPTInterface();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Bukkit.broadcastMessage("§e" + player.getName() + " §7>> §aChatGPT§8: §7" + getInputFromArgs(args));
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                String response = getChatGPTInterface().getResponse(getInputFromArgs(args));
                int length = response.toCharArray().length;
                if (length <= 256) {
                    Bukkit.broadcastMessage("§aChatGPT §7>> §e" + player.getName() + "§8: §7" + response);
                } else {
                    player.sendMessage("§aChatGPT §7>> §e" + player.getName() + "§8: §7 " + "My response was too long for Minecraft chat.. Sorry :(");
                }
            });
            return true;
        }


        return false;
    }

    private String getInputFromArgs(String[] args) {
        StringBuilder input = new StringBuilder();
        for (String arg : args) {
            input.append(arg);
            input.append(" ");
        }
        return input.toString().trim();
    }

    public ChatGPTInterface getChatGPTInterface() {
        return chatGPTInterface;
    }
}
