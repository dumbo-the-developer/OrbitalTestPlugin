package me.adarsh.ftp.cmds;

import me.adarsh.ftp.db.DatabaseManager;
import me.adarsh.ftp.utils.ColorTranslator;
import me.adarsh.ftp.utils.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Earn extends Command {
    private final DatabaseManager databaseManager;
    private final Map<String, Long> lastEarnTimes;

    public Earn(DatabaseManager databaseManager) {
        super("earn", new String[]{"earning"}, "Just a way to do time pass. XD", "eco.earn");
        this.databaseManager = databaseManager;
        this.lastEarnTimes = new HashMap<>();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorTranslator.consoleMessage());
            return;
        }

        Player player = (Player) sender;
        long currentTime = System.currentTimeMillis();
        long lastEarnTime = lastEarnTimes.getOrDefault(player.getName(), 0L);

        if (currentTime - lastEarnTime < 60000) {
            sender.sendMessage(ColorTranslator.translate("&cYou can only use this command once per minute."));
            return;
        }

        int earnedAmount = (int) (Math.random() * 5) + 1;
        double currentBalance = databaseManager.getBalance(player.getName());
        double newBalance = currentBalance + earnedAmount;

        databaseManager.addBalance(player.getName(), newBalance);
        sender.sendMessage(ColorTranslator.translate("&aYou earned &e"+earnedAmount+". &aYour new balance is &b"+newBalance));
        lastEarnTimes.put(player.getName(), currentTime);
    }
}