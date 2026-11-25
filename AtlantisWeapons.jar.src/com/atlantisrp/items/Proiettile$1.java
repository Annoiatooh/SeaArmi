/*    */ package com.atlantisrp.items;
/*    */ 
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.ItemDisplay;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ import org.bukkit.util.Vector;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends BukkitRunnable
/*    */ {
/* 65 */   double percorso = 0.0D;
/*    */ 
/*    */   
/*    */   public void run() {
/* 69 */     if (!display.isValid() || this.percorso >= daPercorrere) {
/* 70 */       display.remove();
/* 71 */       cancel();
/*    */       return;
/*    */     } 
/* 74 */     this.percorso = Math.min(daPercorrere, this.percorso + 8.0D);
/* 75 */     Location punto = inizio.clone().add(direzione.clone().multiply(this.percorso));
/* 76 */     punto.setDirection(direzione);
/* 77 */     display.teleport(punto);
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\items\Proiettile$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */