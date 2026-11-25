/*    */ package com.atlantisrp.logs;
/*    */ 
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*    */ 
/*    */ public class MorteLogListener
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler(priority = EventPriority.MONITOR)
/*    */   public void onMorte(PlayerDeathEvent event) {
/* 13 */     LogArmi.morte(event.getEntity());
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\logs\MorteLogListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */