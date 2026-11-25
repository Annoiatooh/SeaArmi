/*     */ package com.atlantisrp.listeners;
/*     */ 
/*     */ import com.atlantisrp.items.ItemFactory;
/*     */ import com.atlantisrp.items.Sparo;
/*     */ import com.atlantisrp.items.TipoArma;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityPickupItemEvent;
/*     */ import org.bukkit.event.entity.EntityShootBowEvent;
/*     */ import org.bukkit.event.inventory.ClickType;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryCreativeEvent;
/*     */ import org.bukkit.event.inventory.InventoryDragEvent;
/*     */ import org.bukkit.event.inventory.InventoryPickupItemEvent;
/*     */ import org.bukkit.event.inventory.InventoryType;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerItemHeldEvent;
/*     */ import org.bukkit.event.player.PlayerSwapHandItemsEvent;
/*     */ import org.bukkit.inventory.EquipmentSlot;
/*     */ import org.bukkit.inventory.InventoryView;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.persistence.PersistentDataType;
/*     */ 
/*     */ public class WeaponProtectionListener
/*     */   implements Listener
/*     */ {
/*     */   private static final int SLOT_SECONDA_MANO = 40;
/*  37 */   private static final Set<String> ARMI = new HashSet<>();
/*     */ 
/*     */   
/*  40 */   private static final Set<String> TIPI_ARMA = new HashSet<>();
/*     */   
/*     */   static {
/*  43 */     for (TipoArma tipo : TipoArma.values()) {
/*  44 */       ARMI.add(tipo.getId());
/*  45 */       TIPI_ARMA.add(tipo.getId());
/*     */     } 
/*  47 */     ARMI.add("taser");
/*  48 */     TIPI_ARMA.add("taser");
/*  49 */     TIPI_ARMA.add("caricatore");
/*  50 */     TIPI_ARMA.add("mirino");
/*     */   }
/*     */   
/*     */   private String tipo(ItemStack item) {
/*  54 */     if (item == null || item.getItemMeta() == null) return null; 
/*  55 */     return (String)item.getItemMeta().getPersistentDataContainer()
/*  56 */       .get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
/*     */   }
/*     */   
/*     */   private boolean isTipoArma(ItemStack item) {
/*  60 */     String t = tipo(item);
/*  61 */     return (t != null && TIPI_ARMA.contains(t));
/*     */   }
/*     */   
/*     */   private boolean isArma(ItemStack item) {
/*  65 */     String t = tipo(item);
/*  66 */     return (t != null && ARMI.contains(t));
/*     */   }
/*     */   
/*     */   private boolean isMirino(ItemStack item) {
/*  70 */     return "mirino".equals(tipo(item));
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onDrop(PlayerDropItemEvent event) {
/*  76 */     if (isTipoArma(event.getItemDrop().getItemStack())) {
/*  77 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onClick(InventoryClickEvent event) {
/*  85 */     if (isMirino(event.getCurrentItem()) || isMirino(event.getCursor())) {
/*  86 */       event.setCancelled(true);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  91 */     ClickType click = event.getClick();
/*  92 */     if ((click == ClickType.DROP || click == ClickType.CONTROL_DROP) && 
/*  93 */       isTipoArma(event.getCurrentItem())) {
/*  94 */       event.setCancelled(true);
/*     */       
/*     */       return;
/*     */     } 
/*  98 */     if (event.getSlotType() == InventoryType.SlotType.OUTSIDE && isTipoArma(event.getCursor())) {
/*  99 */       event.setCancelled(true);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 104 */     HumanEntity who = event.getWhoClicked();
/* 105 */     if (who.isSneaking()) {
/*     */       return;
/*     */     }
/* 108 */     boolean clickSuSecondaMano = (event.getClickedInventory() instanceof PlayerInventory && event.getSlot() == 40);
/*     */ 
/*     */     
/* 111 */     if (event.getClick() == ClickType.SWAP_OFFHAND) {
/* 112 */       if (isArma(event.getCurrentItem())) {
/* 113 */         event.setCancelled(true);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 118 */     if (!clickSuSecondaMano) {
/*     */       return;
/*     */     }
/* 121 */     if (isArma(event.getCursor())) {
/* 122 */       event.setCancelled(true);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 127 */     if (event.getClick() == ClickType.NUMBER_KEY) {
/* 128 */       int hotbar = event.getHotbarButton();
/* 129 */       if (hotbar >= 0 && isArma(who.getInventory().getItem(hotbar))) {
/* 130 */         event.setCancelled(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onCreative(InventoryCreativeEvent event) {
/* 138 */     if (event.getSlotType() == InventoryType.SlotType.OUTSIDE && isTipoArma(event.getCursor())) {
/* 139 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onDrag(InventoryDragEvent event) {
/* 145 */     if (isMirino(event.getOldCursor())) {
/* 146 */       event.setCancelled(true);
/*     */       
/*     */       return;
/*     */     } 
/* 150 */     if (event.getWhoClicked().isSneaking())
/* 151 */       return;  if (!isArma(event.getOldCursor()))
/*     */       return; 
/* 153 */     InventoryView view = event.getView();
/* 154 */     for (Iterator<Integer> iterator = event.getRawSlots().iterator(); iterator.hasNext(); ) { int raw = ((Integer)iterator.next()).intValue();
/* 155 */       if (view.getInventory(raw) instanceof PlayerInventory && view
/* 156 */         .convertSlot(raw) == 40) {
/* 157 */         event.setCancelled(true);
/*     */         return;
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onHeld(PlayerItemHeldEvent event) {
/* 166 */     PlayerInventory inv = event.getPlayer().getInventory();
/* 167 */     int vecchio = event.getPreviousSlot();
/*     */ 
/*     */     
/* 170 */     if (!isMirino(inv.getItem(vecchio)))
/*     */       return; 
/* 172 */     ItemStack offhand = inv.getItemInOffHand();
/* 173 */     if (isArma(offhand)) {
/* 174 */       inv.setItem(vecchio, offhand);
/* 175 */       inv.setItemInOffHand(null);
/*     */     } else {
/* 177 */       inv.setItem(vecchio, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onPickup(EntityPickupItemEvent event) {
/* 184 */     if ("bossolo".equals(tipo(event.getItem().getItemStack()))) {
/* 185 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onHopperPickup(InventoryPickupItemEvent event) {
/* 191 */     if ("bossolo".equals(tipo(event.getItem().getItemStack()))) {
/* 192 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onSwapHands(PlayerSwapHandItemsEvent event) {
/* 200 */     if (isMirino(event.getMainHandItem()) || isMirino(event.getOffHandItem())) {
/* 201 */       event.setCancelled(true);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 206 */     if (!event.getPlayer().isSneaking() && (
/* 207 */       isArma(event.getMainHandItem()) || isArma(event.getOffHandItem()))) {
/* 208 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(ignoreCancelled = true)
/*     */   public void onPugno(EntityDamageByEntityEvent event) {
/*     */     Player player;
/* 215 */     if (Sparo.inCorso)
/* 216 */       return;  Entity entity = event.getDamager(); if (entity instanceof Player) { player = (Player)entity; } else { return; }
/* 217 */      if ("mirino".equals(tipo(player.getInventory().getItemInMainHand()))) {
/* 218 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onInteractSecondaMano(PlayerInteractEvent event) {
/* 225 */     if (event.getHand() != EquipmentSlot.OFF_HAND)
/* 226 */       return;  if (isTipoArma(event.getItem())) {
/* 227 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onFreccia(EntityShootBowEvent event) {
/* 234 */     if (isTipoArma(event.getBow()))
/* 235 */       event.setCancelled(true); 
/*     */   }
/*     */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\listeners\WeaponProtectionListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */