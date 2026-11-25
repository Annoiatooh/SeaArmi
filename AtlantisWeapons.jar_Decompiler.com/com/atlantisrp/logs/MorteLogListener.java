package com.atlantisrp.logs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class MorteLogListener implements Listener {
   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onMorte(PlayerDeathEvent event) {
      LogArmi.morte(event.getEntity());
   }
}
