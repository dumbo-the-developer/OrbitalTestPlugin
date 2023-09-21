package me.adarsh.ftp.cmds;

import me.adarsh.ftp.db.DatabaseManager;
import me.adarsh.ftp.utils.ColorTranslator;
import me.adarsh.ftp.utils.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetBalance extends Command {
    private final DatabaseManager databaseManager;

    public SetBalance(DatabaseManager databaseManager) {
        super("setbal", new String[]{"setbalance"}, "Used to set money", "eco.setbal");
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ColorTranslator.translate("You need to be an server operator to execute this command"));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(ColorTranslator.translate("&cUsage: &b/setbal &e<player> &a<amount>"));
            return;
        }

        String targetUsername = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetUsername);
        if (targetPlayer == null) {
            sender.sendMessage(ColorTranslator.translate("&cPlayer not found."));
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ColorTranslator.translate("&cInvalid Amount"));
            return;
        }

        if (amount < 0) {
            sender.sendMessage(ColorTranslator.translate("&cAmount must be positive."));
            return;
        }

        databaseManager.setBalance(targetPlayer.getName(), amount);
        sender.sendMessage(ColorTranslator.translate("&a Successfully set balance of &b"+targetUsername+" &ato &e"+amount));
    }
}
