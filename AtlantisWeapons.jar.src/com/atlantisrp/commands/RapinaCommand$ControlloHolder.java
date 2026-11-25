/*    */ package com.atlantisrp.commands;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import org.bukkit.inventory.Inventory;
/*    */ import org.bukkit.inventory.InventoryHolder;
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
/*    */ public class ControlloHolder
/*    */   implements InventoryHolder
/*    */ {
/*    */   final UUID bersaglio;
/*    */   Inventory inventory;
/*    */   
/*    */   ControlloHolder(UUID bersaglio) {
/* 46 */     this.bersaglio = bersaglio;
/*    */   }
/*    */ 
/*    */   
/*    */   public Inventory getInventory() {
/* 51 */     return this.inventory;
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\commands\RapinaCommand$ControlloHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */