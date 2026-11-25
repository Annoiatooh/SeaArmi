package com.atlantisrp.listeners;

import com.atlantisrp.items.ItemFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GiubbottoPesanteListener implements Listener {
   private static final int LIVELLO = 0;
   private static final Set<UUID> CON_EFFETTO = new HashSet();

   public static void avvia(JavaPlugin plugin) {
      Bukkit.getScheduler().runTaskTimer(plugin, () -> {
         PotionEffectType lentezza = PotionEffectType.getByKey(NamespacedKey.minecraft("slowness"));
         if (lentezza != null) {
            for(Player p : Bukkit.getOnlinePlayers()) {
               boolean indossato = pesante(p.getInventory().getChestplate());
               UUID id = p.getUniqueId();
               if (indossato) {
                  PotionEffect attuale = p.getPotionEffect(lentezza);
                  if (attuale == null || attuale.getAmplifier() < 0) {
                     p.addPotionEffect(new PotionEffect(lentezza, -1, 0, false, false, false));
                  }

                  CON_EFFETTO.add(id);
               } else if (CON_EFFETTO.remove(id) && !AmmanettaListener.isAmmanettato(id)) {
                  p.removePotionEffect(lentezza);
               }
            }

         }
      }, 20L, 5L);
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent event) {
      CON_EFFETTO.remove(event.getPlayer().getUniqueId());
   }

   private static boolean pesante(ItemStack item) {
      return item != null && item.getItemMeta() != null ? "giubbotto_pesante".equals(item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING)) : false;
   }
}
