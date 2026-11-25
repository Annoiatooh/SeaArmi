package com.atlantisrp.gui;

import com.atlantisrp.items.ItemFactory;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GUIListener implements Listener {
   @EventHandler
   public void onClick(InventoryClickEvent event) {
      if (event.getView().getTopInventory().getHolder() instanceof ArmiGUI) {
         event.setCancelled(true);
         if (event.getClickedInventory() == event.getView().getTopInventory()) {
            ItemStack clicked = event.getCurrentItem();
            if (clicked != null && !clicked.getType().isAir()) {
               HumanEntity var4 = event.getWhoClicked();
               if (var4 instanceof Player) {
                  Player player = (Player)var4;
                  if (clicked.getItemMeta() != null) {
                     String tipoCliccato = (String)clicked.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
                     InventoryHolder var6 = event.getView().getTopInventory().getHolder();
                     if (var6 instanceof ArmiGUI) {
                        ArmiGUI gui = (ArmiGUI)var6;
                        if ("pagina_successiva".equals(tipoCliccato)) {
                           gui.mostraPagina(gui.getPagina() + 1);
                           return;
                        }

                        if ("pagina_indietro".equals(tipoCliccato)) {
                           gui.mostraPagina(gui.getPagina() - 1);
                           return;
                        }
                     }
                  }

                  ItemStack copia = clicked.clone();
                  ItemMeta meta = copia.getItemMeta();
                  if (meta != null) {
                     meta.lore((List)null);
                     if (meta.getPersistentDataContainer().has(ItemFactory.KEY_ID, PersistentDataType.STRING)) {
                        meta.getPersistentDataContainer().set(ItemFactory.KEY_ID, PersistentDataType.STRING, UUID.randomUUID().toString());
                     }

                     copia.setItemMeta(meta);
                  }

                  if (meta != null && "tronchesi".equals(meta.getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING))) {
                     copia = ItemFactory.createTronchesi();
                  } else if (meta != null && "corda".equals(meta.getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING))) {
                     copia = ItemFactory.createCorda();
                  }

                  player.getInventory().addItem(new ItemStack[]{copia}).values().forEach((resto) -> player.getWorld().dropItemNaturally(player.getLocation(), resto));
               }
            }
         }
      }
   }

   @EventHandler
   public void onDrag(InventoryDragEvent event) {
      if (event.getView().getTopInventory().getHolder() instanceof ArmiGUI) {
         event.setCancelled(true);
      }

   }
}
