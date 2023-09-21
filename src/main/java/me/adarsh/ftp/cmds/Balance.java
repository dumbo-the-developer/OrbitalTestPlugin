package me.adarsh.ftp.cmds;

import me.adarsh.ftp.db.DatabaseManager;
import me.adarsh.ftp.utils.ColorTranslator;
import me.adarsh.ftp.utils.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Balance extends Command {
    private final DatabaseManager databaseManager;

    public Balance(DatabaseManager databaseManager) {
        super("bal", new String[]{"balance"}, "Check yours or someone else money.", "eco.balance");
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorTranslator.consoleMessage());
            return;
        }

        if (args.length == 0) {
            Player player = (Player) sender;
            displayBalance(player, player.getName());
        } else if (args.length == 1) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(ColorTranslator.translate("&cPlayer not found."));
                return;
            }
            displayBalance(sender, targetPlayer.getName());
        } else {
            sender.sendMessage(ColorTranslator.translate("&cUsage: &b/bal &e<player_name>"));
        }
    }

    private void displayBalance(CommandSender sender, String targetUsername) {
        double balance = databaseManager.getBalance(targetUsername);
        sender.sendMessage(ColorTranslator.translate("&a"+targetUsername+"'s"+" Balance is "+"&b"+balance));
    }
}
