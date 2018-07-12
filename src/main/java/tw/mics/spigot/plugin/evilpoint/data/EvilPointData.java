package tw.mics.spigot.plugin.evilpoint.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import tw.mics.spigot.plugin.evilpoint.EvilPoint;

public class EvilPointData {
    EvilPoint plugin;
    Connection db_conn;
    Scoreboard board;
    Objective objective;
    private LinkedHashMap<String, Integer> cache;
    public EvilPointData(EvilPoint p, Connection conn){
        this.plugin = p;
        db_conn = conn;
        cache = new LinkedHashMap<String, Integer>();
        scoreboardInit();
    }
    private void scoreboardInit(){
        board = Bukkit.getScoreboardManager().getMainScoreboard();
        objective = board.getObjective("evilpoint");
        if(objective == null){
            objective = board.registerNewObjective("evilpoint", "dummy");
        } else if(!objective.getCriteria().equals("dummy")){
            objective.unregister();
            objective = board.registerNewObjective("evilpoint", "dummy");
        }
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        for(Player online : Bukkit.getOnlinePlayers()){
            online.setScoreboard(board);
            String UUID = online.getUniqueId().toString();
            objective.getScore(UUID).setScore(getEvil(online));
        }
    }
    
    @SuppressWarnings("deprecation")
    public void scoreboardUpdate(Player player){
        player.setScoreboard(board);
        int evil = getEvil(player);
        objective.getScore(player).setScore(evil);
        //紅名 + 發光判斷
        if(evil > 300){
            player.setGlowing(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 72000, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 72000, 1));
        } else {
            player.setGlowing(false);
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
            getdark_redTeam().removePlayer(player);
            getdark_greenTeam().removePlayer(player);
            getdark_aquaTeam().removePlayer(player);
            getdark_purpleTeam().removePlayer(player);
            getgoldTeam().removePlayer(player);
            getblueTeam().removePlayer(player);
            getgreenTeam().removePlayer(player);
            getaquaTeam().removePlayer(player);
            getredTeam().removePlayer(player);
            getlight_purpleTeam().removePlayer(player);
            getyellowTeam().removePlayer(player);
            getblackTeam().removePlayer(player);
        }
    }
    //black
    private Team getblackTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("black");
        if (team == null) {
            team = board.registerNewTeam("black");
            team.setCanSeeFriendlyInvisibles(false);
            team.setAllowFriendlyFire(true);
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option black color black");
        }
        return team;
    }
    //dark_red  
    private Team getdark_redTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("dark_red");
        if (team == null) {
          team = board.registerNewTeam("dark_red");
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option dark_red color dark_red");
        }
        return team;
    }
    //dark_green
    private Team getdark_greenTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("dark_green");
        if (team == null) {
          team = board.registerNewTeam("dark_green");
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option dark_green color dark_green");
        }
        return team;
    }
    //dark_aqua
    private Team getdark_aquaTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("dark_aqua");
        if (team == null) {
          team = board.registerNewTeam("dark_aqua");
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option dark_aqua color dark_aqua");
        }
        return team;
    }
    //dark_purple
    private Team getdark_purpleTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("dark_purple");
        if (team == null) {
          team = board.registerNewTeam("dark_purple");
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option dark_purple color dark_purple");
        }
        return team;
    }
    //gold
    private Team getgoldTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("gold");
        if (team == null) {
          team = board.registerNewTeam("gold");
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option gold color gold");
        }
        return team;
    }
    //blue
    private Team getblueTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("blue");
        if (team == null) {
          team = board.registerNewTeam("blue");
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option blue color blue");
        }
        return team;
    }
    //green
    private Team getgreenTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("green");
        if (team == null) {
          team = board.registerNewTeam("green");
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option green color green");
        }
        return team;
    }
    //aqua
    private Team getaquaTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("aqua");
        if (team == null) {
          team = board.registerNewTeam("aqua");
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option aqua color aqua");
        }
        return team;
    }
    //red
    private Team getredTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("red");
        if (team == null) {
          team = board.registerNewTeam("red");
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option red color red");
        }
        return team;
    }
    //light_purple
    private Team getlight_purpleTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("light_purple");
        if (team == null) {
          team = board.registerNewTeam("light_purple");
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option light_purple color light_purple");
        }
        return team;
    }
    //yellow
    private Team getyellowTeam(){
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam("yellow");
        if (team == null) {
          team = board.registerNewTeam("yellow");
          team.setCanSeeFriendlyInvisibles(false);
          team.setAllowFriendlyFire(true);
          Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard teams option yellow color yellow");
        }
        return team;
    }
    
//no color_____


    public void scoreboardUpdate(){
        for(Player online : Bukkit.getOnlinePlayers()){
            this.scoreboardUpdate(online);
        }
    }
    private void initEvil(Player player){
        try {
            String sql = "INSERT INTO PLAYER_EVIL_POINT (UUID, EVIL_POINT) " +
                    "VALUES (?,?);"; 
            PreparedStatement pstmt = db_conn.prepareStatement(sql);
            pstmt.setString(1, player.getUniqueId().toString());
            pstmt.setInt(2,0);
            pstmt.execute();
            pstmt.close();
            db_conn.commit();
        } catch ( SQLException e ) {
            plugin.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
            plugin.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    public void plusEvil(Player player, int modify_point){
        int old_point = getEvil(player);
        int point = old_point + modify_point;
        String UUID = player.getUniqueId().toString();
        if(point > 100000) point = 100000;
        try {
            String sql = "UPDATE PLAYER_EVIL_POINT SET EVIL_POINT=? WHERE UUID=?;"; 
            PreparedStatement pstmt = db_conn.prepareStatement(sql);
            pstmt.setInt(1, point);
            pstmt.setString(2, UUID);
            pstmt.execute();
            pstmt.close();
            db_conn.commit();
        } catch ( SQLException e ) {
            plugin.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
            plugin.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        } 
        cache.put(UUID, point);
        scoreboardUpdate(player);
    }

    public void minusEvil(Player player, int modify_point){
        int old_point = getEvil(player);
        int point = old_point - modify_point;
        String UUID = player.getUniqueId().toString();
        if(point < 0) point = 0;
        try {
            String sql = "UPDATE PLAYER_EVIL_POINT SET EVIL_POINT=? WHERE UUID=?;"; 
            PreparedStatement pstmt = db_conn.prepareStatement(sql);
            pstmt.setInt(1, point);
            pstmt.setString(2, UUID);
            pstmt.execute();
            pstmt.close();
            db_conn.commit();
        } catch ( SQLException e ) {
            plugin.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
            plugin.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        } 
        cache.put(UUID, point);
        scoreboardUpdate(player);
    }
    
    public int getEvil(Player player){
        int point = 0;
        String UUID = player.getUniqueId().toString();
        if(cache.containsKey(UUID)){
            return cache.get(UUID);
        }
        try {
            Statement stmt = db_conn.createStatement();
            String sql = String.format("SELECT EVIL_POINT FROM PLAYER_EVIL_POINT WHERE UUID = \"%s\"", UUID);
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                point = rs.getInt(1);
            } else {
                initEvil(player);
            }
            stmt.close();
            rs.close();
        } catch ( SQLException e ) {
            plugin.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
            plugin.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        } 
        cache.put(UUID, point);
        return point;
    }
}
