package tw.mics.spigot.plugin.evilpoint.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import tw.mics.spigot.plugin.evilpoint.EvilPoint;

public class PlayerRespawnListener extends MyListener {
	private Map<String, List<ItemStack>> saveinv;
	
    public PlayerRespawnListener(EvilPoint instance)
    {
        super(instance);
        saveinv = new HashMap<String, List<ItemStack>>();
    }
	
	//死亡儲存物品
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player p = event.getEntity();
        List<ItemStack> keepInv = new ArrayList<ItemStack>();
        double recover_percent = 0;
        int evilpoint = EvilPoint.getInstance().evilpointdata.getEvil(p);
        if(evilpoint == 0){
            recover_percent = 0.5;
        } else if(evilpoint <=100) {
            recover_percent = 0.3;
        } else if(evilpoint <=300) {
            recover_percent = 0.1;
        }
        if(recover_percent !=0 && p.isOnline()){
            for(ItemStack i: Arrays.asList(event.getEntity().getInventory().getContents())){
                if(i == null) continue;
                if(i.getAmount() < 1/recover_percent){
                    if(new Random().nextDouble() < recover_percent ){
                            keepInv.add(i);
                            event.getDrops().remove(i);
                    }
                } else {
                    int keep_amount = (int)Math.rint(i.getAmount() * recover_percent);
                    int drop_amount = i.getAmount()-keep_amount;
                    ItemStack keep_item = i.clone();
                    ItemStack drop_item = i.clone();
                    keep_item.setAmount(keep_amount);
                    drop_item.setAmount(drop_amount);
                    
                    keepInv.add(keep_item);
                    event.getDrops().remove(i);
                    event.getDrops().add(drop_item);
                }
            }
            saveinv.put(p.getUniqueId().toString(), keepInv);
        }
    }
    
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player p = event.getPlayer();
        
        //物品恢復
        if(saveinv.containsKey(p.getUniqueId().toString())){
            for(ItemStack i: saveinv.get(p.getUniqueId().toString())){
                event.getPlayer().getInventory().addItem(i);
            }
            saveinv.remove(p.getUniqueId().toString());
        }
        
        //效果套用
        int bufftime = 0;
        int evilpoint = EvilPoint.getInstance().evilpointdata.getEvil(p);
        if(evilpoint == 0){
            bufftime = 1200;
        } else if(evilpoint <=100) {
            bufftime = 600;
        } else if(evilpoint <=300) {
            bufftime = 400;
        } else if(evilpoint <=500) {
            bufftime = 200;
        }
        if(bufftime != 0){
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EvilPoint.getInstance(), new Runnable(){
                int gbt;
                public Runnable init(int bt) {
                    this.gbt=bt;
                    return(this);
                }
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, gbt, 1));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, gbt, 1));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, gbt, 1));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, gbt, 0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
                }
            }.init(bufftime));
        }
	}
}
