package tw.mics.spigot.plugin.evilpoint;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import tw.mics.spigot.plugin.evilpoint.config.Config;
import tw.mics.spigot.plugin.evilpoint.data.Database;
import tw.mics.spigot.plugin.evilpoint.data.EvilPointData;
import tw.mics.spigot.plugin.evilpoint.listener.EvilPointListener;
import tw.mics.spigot.plugin.evilpoint.listener.PlayerRespawnListener;
import tw.mics.spigot.plugin.evilpoint.schedule.EvilPointSchedule;

public class EvilPoint extends JavaPlugin {
    private static EvilPoint INSTANCE;
    private Database db;
    public EvilPointData evilpointdata;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Config.load();
        db = new Database(this, this.getDataFolder());
        evilpointdata = new EvilPointData(this, db.getConnection());

        new EvilPointListener(this);
        new PlayerRespawnListener(this);
        new EvilPointSchedule(this);
    }

    @Override
    public void onDisable() {
        db.close();
        this.logDebug("Unregister Listener!");
        HandlerList.unregisterAll();
        this.logDebug("Unregister Schedule tasks!");
        this.getServer().getScheduler().cancelAllTasks();
    }

    public static EvilPoint getInstance() {
        return INSTANCE;
    }

    // log system
    public void log(String str, Object... args) {
        String message = String.format(str, args);
        getLogger().info(message);
    }

    public void logDebug(String str, Object... args) {
        if (Config.DEBUG.getBoolean()) {
            String message = String.format(str, args);
            getLogger().info("(DEBUG) " + message);
        }
    }
}
