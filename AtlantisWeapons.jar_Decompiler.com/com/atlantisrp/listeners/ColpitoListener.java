package com.atlantisrp.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class ColpitoListener implements Listener {
   private static final long DURATA_MS = 5000L;
   private static final Map<UUID, Long> COLPITI = new HashMap();

   public static void applica(Player p) {
      COLPITI.put(p.getUniqueId(), System.currentTimeMillis() + 5000L);
      p.setSprinting(false);
   }

   private static boolean attivo(Player p) {
      Long fino = (Long)COLPITI.get(p.getUniqueId());
      if (fino == null) {
         return false;
      } else if (System.currentTimeMillis() >= fino) {
         COLPITI.remove(p.getUniqueId());
         return false;
      } else {
         return true;
      }
   }

   @EventHandler
   public void onCorsa(PlayerToggleSprintEvent event) {
      if (event.isSprinting() && attivo(event.getPlayer())) {
         event.setCancelled(true);
         event.getPlayer().setSprinting(false);
      }

   }

   @EventHandler
   public void onSalto(PlayerJumpEvent event) {
      if (attivo(event.getPlayer())) {
         event.setCancelled(true);
      }

   }
}
