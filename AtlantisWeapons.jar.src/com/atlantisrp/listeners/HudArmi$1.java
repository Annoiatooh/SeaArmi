/*    */ package com.atlantisrp.listeners;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends BukkitRunnable
/*    */ {
/*    */   public void run() {
/* 60 */     for (Player player : Bukkit.getOnlinePlayers()) {
/* 61 */       HudArmi.aggiorna(player);
/*    */     }
/*    */     
/* 64 */     HudArmi.mostrando.removeIf(uuid -> (Bukkit.getPlayer(uuid) == null));
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\listeners\HudArmi$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */