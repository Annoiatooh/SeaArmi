/*    */ package com.atlantisrp.items;
/*    */ 
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.block.Block;
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
/*    */ class null
/*    */   extends BukkitRunnable
/*    */ {
/* 54 */   int tick = 0;
/*    */   boolean atterrato = false;
/* 56 */   final Location pos = posizione.clone();
/*    */ 
/*    */   
/*    */   public void run() {
/* 60 */     if (!display.isValid() || this.tick >= 40) {
/* 61 */       display.remove();
/* 62 */       cancel();
/*    */       return;
/*    */     } 
/* 65 */     this.tick++;
/*    */     
/* 67 */     if (!this.atterrato) {
/* 68 */       velocita.setY(velocita.getY() - 0.06D);
/* 69 */       Location prossima = this.pos.clone().add(velocita);
/* 70 */       Block blocco = prossima.getBlock();
/*    */       
/* 72 */       if (blocco.getType().isSolid()) {
/* 73 */         this.atterrato = true;
/*    */       } else {
/* 75 */         this.pos.setX(prossima.getX());
/* 76 */         this.pos.setY(prossima.getY());
/* 77 */         this.pos.setZ(prossima.getZ());
/* 78 */         this.pos.setYaw(this.pos.getYaw() + 35.0F);
/* 79 */         display.teleport(this.pos);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\items\Bossolo$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */