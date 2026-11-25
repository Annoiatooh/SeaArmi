/*    */ package com.atlantisrp.items;
/*    */ 
/*    */ import com.atlantisrp.Main;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.entity.Display;
/*    */ import org.bukkit.entity.ItemDisplay;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.ItemMeta;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ import org.bukkit.util.Transformation;
/*    */ import org.bukkit.util.Vector;
/*    */ import org.joml.AxisAngle4f;
/*    */ import org.joml.Vector3f;
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
/*    */ public class Proiettile
/*    */ {
/*    */   private static final int CMD = 91;
/*    */   private static final float SCALA = 0.5F;
/*    */   private static final double VELOCITA = 8.0D;
/*    */   private static final boolean INVERTI_PUNTA = false;
/*    */   
/*    */   public static void lancia(Location partenza, Vector dir, double distanza) {
/* 34 */     final Vector direzione = dir.clone().normalize();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 39 */     final Location inizio = partenza.clone().add(direzione.clone().multiply(1.0D)).add(0.0D, -0.15D, 0.0D);
/* 40 */     inizio.setDirection(direzione);
/*    */     
/* 42 */     ItemStack stick = new ItemStack(Material.STICK);
/* 43 */     ItemMeta meta = stick.getItemMeta();
/* 44 */     meta.setCustomModelData(Integer.valueOf(91));
/* 45 */     stick.setItemMeta(meta);
/*    */     
/* 47 */     final ItemDisplay display = (ItemDisplay)inizio.getWorld().spawn(inizio, ItemDisplay.class, d -> {
/*    */           d.setItemStack(stick);
/*    */           
/*    */           d.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
/*    */           
/*    */           d.setBillboard(Display.Billboard.FIXED);
/*    */           
/*    */           float angolo = (float)Math.toRadians(90.0D);
/*    */           
/*    */           d.setTransformation(new Transformation(new Vector3f(0.0F, 0.0F, 0.0F), new AxisAngle4f(angolo, 1.0F, 0.0F, 0.0F), new Vector3f(0.5F, 0.5F, 0.5F), new AxisAngle4f()));
/*    */           
/*    */           d.setTeleportDuration(1);
/*    */           d.setViewRange(1.0F);
/*    */           d.setPersistent(false);
/*    */         });
/* 62 */     final double daPercorrere = Math.max(1.0D, distanza - 1.0D);
/*    */     
/* 64 */     (new BukkitRunnable() {
/* 65 */         double percorso = 0.0D;
/*    */ 
/*    */         
/*    */         public void run() {
/* 69 */           if (!display.isValid() || this.percorso >= daPercorrere) {
/* 70 */             display.remove();
/* 71 */             cancel();
/*    */             return;
/*    */           } 
/* 74 */           this.percorso = Math.min(daPercorrere, this.percorso + 8.0D);
/* 75 */           Location punto = inizio.clone().add(direzione.clone().multiply(this.percorso));
/* 76 */           punto.setDirection(direzione);
/* 77 */           display.teleport(punto);
/*    */         }
/* 79 */       }).runTaskTimer((Plugin)Main.getInstance(), 1L, 1L);
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\items\Proiettile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */