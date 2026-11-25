/*    */ package com.atlantisrp.items;
/*    */ 
/*    */ import com.atlantisrp.listeners.ColpitoListener;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.persistence.PersistentDataType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Giubbotto
/*    */ {
/*    */   private static final double VITA = 20.0D;
/*    */   private static final int EXTRA_LEGGERO = 2;
/*    */   private static final int EXTRA_PESANTE = 3;
/*    */   private static final double MARGINE = 0.02D;
/*    */   
/*    */   public static double danno(LivingEntity target, TipoArma tipo, boolean testa, double base) {
/*    */     Player player;
/*    */     double voluto;
/* 25 */     if (target instanceof Player) { player = (Player)target; } else { return base; }
/*    */ 
/*    */     
/* 28 */     ColpitoListener.applica(player);
/*    */     
/* 30 */     int livello = 0;
/* 31 */     double punti = 0.0D;
/* 32 */     double resistenza = 0.0D;
/*    */     
/* 34 */     ItemStack petto = player.getInventory().getChestplate();
/* 35 */     if (petto != null && petto.getItemMeta() != null) {
/*    */       
/* 37 */       String id = (String)petto.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
/* 38 */       if ("giubbotto_leggero".equals(id)) {
/* 39 */         livello = 1;
/* 40 */         punti = 5.0D;
/* 41 */       } else if ("giubbotto_pesante".equals(id)) {
/* 42 */         livello = 2;
/* 43 */         punti = 8.0D;
/* 44 */         resistenza = 2.0D;
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 49 */     int colpiTabella = tipo.colpiPerUccidere(testa, livello);
/* 50 */     if (colpiTabella > 0) {
/* 51 */       voluto = 20.0D / colpiTabella + 0.02D;
/*    */     } else {
/* 53 */       if (livello == 0) return base; 
/* 54 */       if (livello == 1 && tipo == TipoArma.HK416) return base; 
/* 55 */       int colpi = (int)Math.ceil(20.0D / base - 1.0E-9D);
/* 56 */       int extra = (livello == 1) ? 2 : 3;
/* 57 */       voluto = 20.0D / (colpi + extra) + 0.02D;
/*    */     } 
/*    */     
/* 60 */     if (livello == 0) return voluto;
/*    */ 
/*    */     
/* 63 */     double x = voluto;
/* 64 */     for (int i = 0; i < 5; i++) {
/* 65 */       double riduzione = Math.min(20.0D, Math.max(punti / 5.0D, punti - x / (2.0D + resistenza / 4.0D))) / 25.0D;
/* 66 */       x = voluto / (1.0D - riduzione);
/*    */     } 
/* 68 */     return x;
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\items\Giubbotto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */