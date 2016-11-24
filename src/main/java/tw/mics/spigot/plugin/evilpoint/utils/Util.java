package tw.mics.spigot.plugin.evilpoint.utils;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Wolf;

public class Util {
    public static Player getDamager(Entity e){
        Player damager = null;
        if(e instanceof Player){
            damager = (Player) e;
        } else if(e instanceof Arrow){
            Arrow arrow = (Arrow)e;
            if(arrow.getShooter() instanceof Player){
                damager = (Player) arrow.getShooter();
            }
        } else if(e instanceof ThrownPotion){
            ThrownPotion potion = (ThrownPotion)e;
            if(potion.getShooter() instanceof Player){
                damager = (Player) potion.getShooter();
            }
        } else if(e instanceof Wolf){
            Wolf wolf = (Wolf)e;
            if(wolf.getOwner() instanceof Player){
                damager = (Player) wolf.getOwner();
            }
        }
        return damager; //return null if not player
    }
}

