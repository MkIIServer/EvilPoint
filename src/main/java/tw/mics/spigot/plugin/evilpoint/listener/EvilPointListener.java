package tw.mics.spigot.plugin.evilpoint.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

import tw.mics.spigot.plugin.cupboard.Cupboard;
import tw.mics.spigot.plugin.evilpoint.EvilPoint;
import tw.mics.spigot.plugin.evilpoint.data.EvilPointData;
import tw.mics.spigot.plugin.evilpoint.utils.Util;


public class EvilPointListener extends MyListener {
    EvilPointData evilpointdata;
    public EvilPointListener(EvilPoint instance) {
        super(instance);
        evilpointdata = EvilPoint.getInstance().evilpointdata;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        evilpointdata.scoreboardUpdate(event.getPlayer());
    }
    
    //傷害玩家
    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        Player damager = null;
        if(event instanceof EntityDamageByEntityEvent)
            damager = Util.getDamager(((EntityDamageByEntityEvent)event).getDamager());
        //傷害計算
        double modifer = 1;
        int ep = EvilPoint.getInstance().evilpointdata.getEvil((Player) event.getEntity());
        if(damager != null && damager != event.getEntity()){
            //傷害增幅
            if(ep > 1500){
                modifer = 4;
            } else if(ep > 999) {
                modifer = 3;
            } else if(ep > 401) {
                modifer = 1.6;
            } else if(ep > 301) {
                modifer = 1.3;
            } else if(ep > 101) {
                modifer = 1.2;
            } else if(ep > 51) {
                modifer = 1.1;
            } else if(ep > 0) {
                modifer = 1;
            } else if(ep == 0) {
                modifer = 0.5;
            }
        } else {
            if(ep > 9999){
                modifer = 6;
            } else if(ep > 5000) {
                modifer = 5;
            }
        }
        if(modifer != 1){
            double absorption = event.getDamage(DamageModifier.ABSORPTION) * modifer;
            double armor = event.getDamage(DamageModifier.ARMOR) * modifer;
            double blocking = event.getDamage(DamageModifier.BLOCKING) * modifer;
            double magic = event.getDamage(DamageModifier.MAGIC) * modifer;
            double resistance = event.getDamage(DamageModifier.RESISTANCE) * modifer;
            double base = event.getDamage(DamageModifier.BASE) * modifer;
            event.setDamage(DamageModifier.ABSORPTION, absorption);
            event.setDamage(DamageModifier.ARMOR, armor);
            event.setDamage(DamageModifier.BLOCKING, blocking);
            event.setDamage(DamageModifier.MAGIC, magic);
            event.setDamage(DamageModifier.RESISTANCE, resistance);
            event.setDamage(DamageModifier.BASE, base);
        }
        
        //點數計算
        if(
                damager != null && 
                damager != event.getEntity() && 
                event.getFinalDamage() >= 2 && 
                event.getCause() != DamageCause.THORNS
        ){
            double evil_point = event.getFinalDamage();
            try {
                if(
                    Cupboard.getInstance().cupboards.checkIsLimit(damager.getLocation().getBlock()) &&
                    !Cupboard.getInstance().cupboards.checkIsLimit(damager.getLocation().getBlock(), damager)
                ) evil_point /= 2.0;
            } catch (Exception e){}
            evilpointdata.plusEvil(damager, (int)Math.ceil(evil_point));
            evilpointdata.scoreboardUpdate(damager);
        }
    }
    
    //殺死玩家
    @EventHandler
    public void onPlayerKilledByPlayer(EntityDeathEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
        if(!(damageEvent instanceof EntityDamageByEntityEvent))
            return;
        Player killer = Util.getDamager(((EntityDamageByEntityEvent)damageEvent).getDamager());
        if(killer != null && killer != event.getEntity()){
            int evil_point;
            if(evilpointdata.getEvil(killer) < evilpointdata.getEvil((Player) event.getEntity())){
                evil_point = 20;
            } else {
                evil_point = 60;
            }
            try {
                if(
                    Cupboard.getInstance().cupboards.checkIsLimit(killer.getLocation()) &&
                    !Cupboard.getInstance().cupboards.checkIsLimit(killer.getLocation(), killer)
                ) evil_point /= 2;
            } catch (Exception e){}
            evilpointdata.plusEvil(killer, evil_point);
        }
    }
    
    //放置TNT
    @EventHandler
    public void onTNTPlaced(BlockPlaceEvent event){
        if(event.isCancelled())return;
        if(event.getBlockPlaced().getType() == Material.TNT){
            Player player = event.getPlayer();
            evilpointdata.plusEvil(player, 30);
        }
    }
    
    //製作TNT
    @EventHandler
    public void onTNTMaked(InventoryClickEvent event){
        if(event.getClickedInventory() == null)return;
        if(event.getClickedInventory().getType() != InventoryType.WORKBENCH)return;
        if(event.getCurrentItem() == null)return;
        if(event.getCurrentItem().getType() != Material.TNT)return;
        //Cupboard.getInstance().log("%s %d",event.getAction().toString(), event.getRawSlot());
        
        if(event.getRawSlot() != 0) return;
        Inventory inv = event.getClickedInventory();
        switch(event.getAction()){
        case PICKUP_ONE:
        case PICKUP_HALF:
        case PICKUP_ALL:
            evilpointdata.plusEvil((Player) event.getWhoClicked(), 20);
            break;
        case MOVE_TO_OTHER_INVENTORY:
            int min_craft = 64;
            for(int i=1 ;i<=9; i++){
                if(inv.getItem(i).getAmount() < min_craft)min_craft = inv.getItem(i).getAmount();
            }
            evilpointdata.plusEvil((Player) event.getWhoClicked(), 20*min_craft);
            break;
        default:
            break;
        }
    }
}
