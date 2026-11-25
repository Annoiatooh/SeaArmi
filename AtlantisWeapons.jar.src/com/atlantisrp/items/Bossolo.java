/*    */ package com.atlantisrp.items;
/*    */ 
/*    */ import com.atlantisrp.Main;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.entity.Display;
/*    */ import org.bukkit.entity.ItemDisplay;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ import org.bukkit.util.Transformation;
/*    */ import org.bukkit.util.Vector;
/*    */ import org.joml.AxisAngle4f;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ 
/*    */ public class Bossolo
/*    */ {
/*    */   private static final float SCALA = 0.25F;
/*    */   private static final int DURATA_TICK = 40;
/*    */   private static final double GRAVITA = 0.06D;
/*    */   private static final float ROTAZIONE = 35.0F;
/*    */   
/*    */   public static void espelli(Player player) {
/* 25 */     Location occhi = player.getEyeLocation();
/* 26 */     Vector dir = occhi.getDirection().normalize();
/*    */ 
/*    */     
/* 29 */     Vector destra = dir.clone().crossProduct(new Vector(0, 1, 0));
/* 30 */     if (destra.lengthSquared() < 1.0E-4D) destra = new Vector(1, 0, 0); 
/* 31 */     destra.normalize();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 36 */     final Location posizione = occhi.clone().add(dir.clone().multiply(0.5D)).add(destra.clone().multiply(0.3D)).add(0.0D, -0.3D, 0.0D);
/*    */     
/* 38 */     final ItemDisplay display = (ItemDisplay)posizione.getWorld().spawn(posizione, ItemDisplay.class, d -> {
/*    */           d.setItemStack(ItemFactory.createBossolo());
/*    */           
/*    */           d.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
/*    */           
/*    */           d.setBillboard(Display.Billboard.FIXED);
/*    */           
/*    */           d.setTransformation(new Transformation(new Vector3f(0.0F, 0.0F, 0.0F), new AxisAngle4f(), new Vector3f(0.25F, 0.25F, 0.25F), new AxisAngle4f()));
/*    */           
/*    */           d.setViewRange(0.3F);
/*    */           
/*    */           d.setPersistent(false);
/*    */         });
/* 51 */     final Vector velocita = destra.clone().multiply(0.12D).add(new Vector(0.0D, 0.15D, 0.0D));
/*    */     
/* 53 */     (new BukkitRunnable() {
/* 54 */         int tick = 0;
/*    */         boolean atterrato = false;
/* 56 */         final Location pos = posizione.clone();
/*    */ 
/*    */         
/*    */         public void run() {
/* 60 */           if (!display.isValid() || this.tick >= 40) {
/* 61 */             display.remove();
/* 62 */             cancel();
/*    */             return;
/*    */           } 
/* 65 */           this.tick++;
/*    */           
/* 67 */           if (!this.atterrato) {
/* 68 */             velocita.setY(velocita.getY() - 0.06D);
/* 69 */             Location prossima = this.pos.clone().add(velocita);
/* 70 */             Block blocco = prossima.getBlock();
/*    */             
/* 72 */             if (blocco.getType().isSolid()) {
/* 73 */               this.atterrato = true;
/*    */             } else {
/* 75 */               this.pos.setX(prossima.getX());
/* 76 */               this.pos.setY(prossima.getY());
/* 77 */               this.pos.setZ(prossima.getZ());
/* 78 */               this.pos.setYaw(this.pos.getYaw() + 35.0F);
/* 79 */               display.teleport(this.pos);
/*    */             } 
/*    */           } 
/*    */         }
/* 83 */       }).runTaskTimer((Plugin)Main.getInstance(), 1L, 1L);
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\items\Bossolo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */