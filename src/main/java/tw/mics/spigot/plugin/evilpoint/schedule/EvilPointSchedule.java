package tw.mics.spigot.plugin.evilpoint.schedule;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Material;

import tw.mics.spigot.plugin.evilpoint.EvilPoint;
import tw.mics.spigot.plugin.evilpoint.data.EvilPointData;

public class EvilPointSchedule {
    EvilPoint plugin;
    Runnable runnable;
    EvilPointData evilpoint;
    LinkedHashMap<UUID, Integer> limit;
    
    int schedule_id;
    public EvilPointSchedule(EvilPoint i){
        this.plugin = i;
        evilpoint = i.evilpointdata;
        setupRunnable();
        limit = new LinkedHashMap<UUID, Integer>();
    }
    
    public void removeRunnable(){
        this.plugin.getServer().getScheduler().cancelTask(schedule_id);
        this.plugin.logDebug("Evilpoint timer task removed");
    }
    
    private void setupRunnable(){
        runnable = new Runnable(){
            public void run() {
                plugin.getServer().getOnlinePlayers().forEach(p->{
                    if(!limit.containsKey(p.getUniqueId())) limit.put(p.getUniqueId(), 0);
                    if(
                            !p.getInventory().contains(Material.TNT) &&
                            limit.get(p.getUniqueId()) < 50 &&
                            evilpoint.getEvil(p) > 0
                    ){
                        int count = 1 + evilpoint.getEvil(p) / 500; //每500點多扣1
                        evilpoint.minusEvil(p, count);
                        limit.put(p.getUniqueId(), limit.get(p.getUniqueId())+1);
                    }
                });
                evilpoint.scoreboardUpdate();
            }
        };
        schedule_id = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, runnable, 0, 1200);
        this.plugin.logDebug("Evilpoint timer task added");
    }
}
