/*    */ package com.atlantisrp.listeners;
/*    */ 
/*    */ import com.atlantisrp.items.ItemFactory;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.NamespacedKey;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerQuitEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.persistence.PersistentDataType;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ import org.bukkit.potion.PotionEffect;
/*    */ import org.bukkit.potion.PotionEffectType;
/*    */ 
/*    */ public class GiubbottoPesanteListener
/*    */   implements Listener
/*    */ {
/*    */   private static final int LIVELLO = 0;
/* 24 */   private static final Set<UUID> CON_EFFETTO = new HashSet<>();
/*    */   
/*    */   public static void avvia(JavaPlugin plugin) {
/* 27 */     Bukkit.getScheduler().runTaskTimer((Plugin)plugin, () -> { PotionEffectType lentezza = PotionEffectType.getByKey(NamespacedKey.minecraft("slowness")); if (lentezza == null) return;  for (Player p : Bukkit.getOnlinePlayers()) { boolean indossato = pesante(p.getInventory().getChestplate()); UUID id = p.getUniqueId(); if (indossato) { PotionEffect attuale = p.getPotionEffect(lentezza); if (attuale == null || attuale.getAmplifier() < 0) p.addPotionEffect(new PotionEffect(lentezza, -1, 0, false, false, false));  CON_EFFETTO.add(id); continue; }  if (CON_EFFETTO.remove(id)) if (!AmmanettaListener.isAmmanettato(id)) p.removePotionEffect(lentezza);   }  }20L, 5L);
/*    */   }
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
/*    */   @EventHandler
/*    */   public void onQuit(PlayerQuitEvent event) {
/* 49 */     CON_EFFETTO.remove(event.getPlayer().getUniqueId());
/*    */   }
/*    */   
/*    */   private static boolean pesante(ItemStack item) {
/* 53 */     if (item == null || item.getItemMeta() == null) return false; 
/* 54 */     return "giubbotto_pesante".equals(item.getItemMeta().getPersistentDataContainer()
/* 55 */         .get(ItemFactory.KEY_TIPO, PersistentDataType.STRING));
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\listeners\GiubbottoPesanteListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */