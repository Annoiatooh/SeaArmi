/*    */ package com.atlantisrp.gui;
/*    */ import com.atlantisrp.items.ItemFactory;
/*    */ import org.bukkit.entity.HumanEntity;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.inventory.InventoryClickEvent;
/*    */ import org.bukkit.event.inventory.InventoryDragEvent;
/*    */ import org.bukkit.inventory.InventoryHolder;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.ItemMeta;
/*    */ import org.bukkit.persistence.PersistentDataType;
/*    */ 
/*    */ public class GUIListener implements Listener {
/*    */   @EventHandler
/*    */   public void onClick(InventoryClickEvent event) {
/*    */     Player player;
/* 17 */     if (!(event.getView().getTopInventory().getHolder() instanceof ArmiGUI)) {
/*    */       return;
/*    */     }
/* 20 */     event.setCancelled(true);
/*    */ 
/*    */     
/* 23 */     if (event.getClickedInventory() != event.getView().getTopInventory())
/*    */       return; 
/* 25 */     ItemStack clicked = event.getCurrentItem();
/* 26 */     if (clicked == null || clicked.getType().isAir())
/* 27 */       return;  HumanEntity humanEntity = event.getWhoClicked(); if (humanEntity instanceof Player) { player = (Player)humanEntity; }
/*    */     else
/*    */     { return; }
/* 30 */      if (clicked.getItemMeta() != null) {
/*    */       
/* 32 */       String tipoCliccato = (String)clicked.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
/* 33 */       InventoryHolder inventoryHolder = event.getView().getTopInventory().getHolder(); if (inventoryHolder instanceof ArmiGUI) { ArmiGUI gui = (ArmiGUI)inventoryHolder;
/* 34 */         if ("pagina_successiva".equals(tipoCliccato)) {
/* 35 */           gui.mostraPagina(gui.getPagina() + 1);
/*    */           return;
/*    */         } 
/* 38 */         if ("pagina_indietro".equals(tipoCliccato)) {
/* 39 */           gui.mostraPagina(gui.getPagina() - 1);
/*    */           
/*    */           return;
/*    */         }  }
/*    */     
/*    */     } 
/*    */     
/* 46 */     ItemStack copia = clicked.clone();
/* 47 */     ItemMeta meta = copia.getItemMeta();
/* 48 */     if (meta != null) {
/* 49 */       meta.lore(null);
/*    */       
/* 51 */       if (meta.getPersistentDataContainer().has(ItemFactory.KEY_ID, PersistentDataType.STRING)) {
/* 52 */         meta.getPersistentDataContainer().set(ItemFactory.KEY_ID, PersistentDataType.STRING, 
/* 53 */             UUID.randomUUID().toString());
/*    */       }
/* 55 */       copia.setItemMeta(meta);
/*    */     } 
/*    */     
/* 58 */     if (meta != null && "tronchesi".equals(meta.getPersistentDataContainer()
/* 59 */         .get(ItemFactory.KEY_TIPO, PersistentDataType.STRING))) {
/* 60 */       copia = ItemFactory.createTronchesi();
/* 61 */     } else if (meta != null && "corda".equals(meta.getPersistentDataContainer()
/* 62 */         .get(ItemFactory.KEY_TIPO, PersistentDataType.STRING))) {
/* 63 */       copia = ItemFactory.createCorda();
/*    */     } 
/*    */     
/* 66 */     player.getInventory().addItem(new ItemStack[] { copia }).values()
/* 67 */       .forEach(resto -> player.getWorld().dropItemNaturally(player.getLocation(), resto));
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onDrag(InventoryDragEvent event) {
/* 72 */     if (event.getView().getTopInventory().getHolder() instanceof ArmiGUI)
/* 73 */       event.setCancelled(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\gui\GUIListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */