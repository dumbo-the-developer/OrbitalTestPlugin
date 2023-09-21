package me.adarsh.ftp.utils;

import org.bukkit.ChatColor;

public class ColorTranslator {

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String consoleMessage(){
        return ChatColor.translateAlternateColorCodes('&', "&cConsole Cannot send this command");
    }
}
