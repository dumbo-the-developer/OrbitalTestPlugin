package me.adarsh.ftp.cmds;

import me.adarsh.ftp.db.DatabaseManager;
import me.adarsh.ftp.utils.ColorTranslator;
import me.adarsh.ftp.utils.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Give extends Command {
    private final DatabaseManager databaseManager;

    public Give(DatabaseManager databaseManager) {
        super("give", new String[]{"givemoney"}, "Give money to whoever you want.", "eco.give");
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorTranslator.consoleMessage());
            return;
        }

        Player player = (Player) sender;

        if (args.length != 2) {
            sender.sendMessage(ColorTranslator.translate("&cUsage: &b/give &e<target> &a<amount>"));
            return;
        }

        String targetUsername = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetUsername);
        if (targetPlayer == null) {
            sender.sendMessage(ColorTranslator.translate("&cPlayer not found."));
            return;
        }

        if (player.getName().equalsIgnoreCase(targetUsername)) {
            sender.sendMessage(ColorTranslator.translate("&cYou cant give money to yourself."));
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ColorTranslator.translate("&cInvalid Amount."));
            return;
        }

        if (amount <= 0) {
            sender.sendMessage(ColorTranslator.translate("&cAmount must be positive or greater than zero (0)."));
            return;
        }

        double senderBalance = databaseManager.getBalance(player.getName());
        if (amount > senderBalance) {
            sender.sendMessage(ColorTranslator.translate("&cYou don't have enough money to send."));
            return;
        }

        double targetBalance = databaseManager.getBalance(targetUsername);
        targetBalance += amount;
        senderBalance -= amount;

        databaseManager.setBalance(player.getName(), senderBalance);
        databaseManager.setBalance(targetUsername, targetBalance);

        sender.sendMessage(ColorTranslator.translate("&aSuccessfully sent &e"+amount+" to &b"+targetPlayer));
    }
}
