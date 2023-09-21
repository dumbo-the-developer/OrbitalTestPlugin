package me.adarsh.ftp.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class Command extends BukkitCommand {


    public Command(String command, String[] aliases, String description, String permission) {
        super(command);

        this.setAliases(Arrays.asList(aliases));
        this.setDescription(description);
        this.setPermission(permission);
        this.setPermissionMessage(ColorTranslator.translate("&u&l[Economy] &cYou don't have required permission to use this command."));

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(command, this);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        execute(commandSender, strings);
        return false;
    }

    public abstract void execute(CommandSender sender, String[] args);
}