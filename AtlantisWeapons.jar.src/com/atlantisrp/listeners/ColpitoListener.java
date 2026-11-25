/*    */ package com.atlantisrp.listeners;
/*    */ 
/*    */ import com.destroystokyo.paper.event.player.PlayerJumpEvent;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerToggleSprintEvent;
/*    */ 
/*    */ public class ColpitoListener
/*    */   implements Listener
/*    */ {
/*    */   private static final long DURATA_MS = 5000L;
/* 16 */   private static final Map<UUID, Long> COLPITI = new HashMap<>();
/*    */   
/*    */   public static void applica(Player p) {
/* 19 */     COLPITI.put(p.getUniqueId(), Long.valueOf(System.currentTimeMillis() + 5000L));
/* 20 */     p.setSprinting(false);
/*    */   }
/*    */   
/*    */   private static boolean attivo(Player p) {
/* 24 */     Long fino = COLPITI.get(p.getUniqueId());
/* 25 */     if (fino == null) return false; 
/* 26 */     if (System.currentTimeMillis() >= fino.longValue()) {
/* 27 */       COLPITI.remove(p.getUniqueId());
/* 28 */       return false;
/*    */     } 
/* 30 */     return true;
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onCorsa(PlayerToggleSprintEvent event) {
/* 35 */     if (event.isSprinting() && attivo(event.getPlayer())) {
/* 36 */       event.setCancelled(true);
/* 37 */       event.getPlayer().setSprinting(false);
/*    */     } 
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onSalto(PlayerJumpEvent event) {
/* 43 */     if (attivo(event.getPlayer())) event.setCancelled(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\listeners\ColpitoListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */