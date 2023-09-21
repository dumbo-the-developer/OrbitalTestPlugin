package me.adarsh.ftp;

import me.adarsh.ftp.cmds.Balance;
import me.adarsh.ftp.cmds.Earn;
import me.adarsh.ftp.cmds.Give;
import me.adarsh.ftp.cmds.SetBalance;
import me.adarsh.ftp.db.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Economy extends JavaPlugin {
    private DatabaseManager databaseManager;
    private static Economy instance;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.saveConfig();

        databaseManager = new DatabaseManager(this);
        databaseManager.connectDatabase();

        registerCommands();
    }

    public void registerCommands() {
        new Balance(databaseManager);
        new SetBalance(databaseManager);
        new Earn(databaseManager);
        new Give(databaseManager);
    }

    public static Economy getInstance() {
        return instance;
    }
}
