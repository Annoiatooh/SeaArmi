package com.atlantisrp.listeners;

import com.atlantisrp.items.ItemFactory;
import com.atlantisrp.items.Sparo;
import com.atlantisrp.items.TipoArma;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

public class WeaponProtectionListener implements Listener {
   private static final int SLOT_SECONDA_MANO = 40;
   private static final Set<String> ARMI = new HashSet();
   private static final Set<String> TIPI_ARMA = new HashSet();

   private String tipo(ItemStack item) {
      return item != null && item.getItemMeta() != null ? (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING) : null;
   }

   private boolean isTipoArma(ItemStack item) {
      String t = this.tipo(item);
      return t != null && TIPI_ARMA.contains(t);
   }

   private boolean isArma(ItemStack item) {
      String t = this.tipo(item);
      return t != null && ARMI.contains(t);
   }

   private boolean isMirino(ItemStack item) {
      return "mirino".equals(this.tipo(item));
   }

   @EventHandler
   public void onDrop(PlayerDropItemEvent event) {
      if (this.isTipoArma(event.getItemDrop().getItemStack())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onClick(InventoryClickEvent event) {
      if (!this.isMirino(event.getCurrentItem()) && !this.isMirino(event.getCursor())) {
         ClickType click = event.getClick();
         if ((click == ClickType.DROP || click == ClickType.CONTROL_DROP) && this.isTipoArma(event.getCurrentItem())) {
            event.setCancelled(true);
         } else if (event.getSlotType() == SlotType.OUTSIDE && this.isTipoArma(event.getCursor())) {
            event.setCancelled(true);
         } else {
            HumanEntity who = event.getWhoClicked();
            if (!who.isSneaking()) {
               boolean clickSuSecondaMano = event.getClickedInventory() instanceof PlayerInventory && event.getSlot() == 40;
               if (event.getClick() == ClickType.SWAP_OFFHAND) {
                  if (this.isArma(event.getCurrentItem())) {
                     event.setCancelled(true);
                  }

               } else if (clickSuSecondaMano) {
                  if (this.isArma(event.getCursor())) {
                     event.setCancelled(true);
                  } else {
                     if (event.getClick() == ClickType.NUMBER_KEY) {
                        int hotbar = event.getHotbarButton();
                        if (hotbar >= 0 && this.isArma(who.getInventory().getItem(hotbar))) {
                           event.setCancelled(true);
                        }
                     }

                  }
               }
            }
         }
      } else {
         event.setCancelled(true);
      }
   }

   @EventHandler
   public void onCreative(InventoryCreativeEvent event) {
      if (event.getSlotType() == SlotType.OUTSIDE && this.isTipoArma(event.getCursor())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onDrag(InventoryDragEvent event) {
      if (this.isMirino(event.getOldCursor())) {
         event.setCancelled(true);
      } else if (!event.getWhoClicked().isSneaking()) {
         if (this.isArma(event.getOldCursor())) {
            InventoryView view = event.getView();

            for(int raw : event.getRawSlots()) {
               if (view.getInventory(raw) instanceof PlayerInventory && view.convertSlot(raw) == 40) {
                  event.setCancelled(true);
                  return;
               }
            }

         }
      }
   }

   @EventHandler
   public void onHeld(PlayerItemHeldEvent event) {
      PlayerInventory inv = event.getPlayer().getInventory();
      int vecchio = event.getPreviousSlot();
      if (this.isMirino(inv.getItem(vecchio))) {
         ItemStack offhand = inv.getItemInOffHand();
         if (this.isArma(offhand)) {
            inv.setItem(vecchio, offhand);
            inv.setItemInOffHand((ItemStack)null);
         } else {
            inv.setItem(vecchio, (ItemStack)null);
         }

      }
   }

   @EventHandler
   public void onPickup(EntityPickupItemEvent event) {
      if ("bossolo".equals(this.tipo(event.getItem().getItemStack()))) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onHopperPickup(InventoryPickupItemEvent event) {
      if ("bossolo".equals(this.tipo(event.getItem().getItemStack()))) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onSwapHands(PlayerSwapHandItemsEvent event) {
      if (!this.isMirino(event.getMainHandItem()) && !this.isMirino(event.getOffHandItem())) {
         if (!event.getPlayer().isSneaking() && (this.isArma(event.getMainHandItem()) || this.isArma(event.getOffHandItem()))) {
            event.setCancelled(true);
         }

      } else {
         event.setCancelled(true);
      }
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onPugno(EntityDamageByEntityEvent event) {
      if (!Sparo.inCorso) {
         Entity var3 = event.getDamager();
         if (var3 instanceof Player) {
            Player player = (Player)var3;
            if ("mirino".equals(this.tipo(player.getInventory().getItemInMainHand()))) {
               event.setCancelled(true);
            }

         }
      }
   }

   @EventHandler
   public void onInteractSecondaMano(PlayerInteractEvent event) {
      if (event.getHand() == EquipmentSlot.OFF_HAND) {
         if (this.isTipoArma(event.getItem())) {
            event.setCancelled(true);
         }

      }
   }

   @EventHandler
   public void onFreccia(EntityShootBowEvent event) {
      if (this.isTipoArma(event.getBow())) {
         event.setCancelled(true);
      }

   }

   static {
      for(TipoArma tipo : TipoArma.values()) {
         ARMI.add(tipo.getId());
         TIPI_ARMA.add(tipo.getId());
      }

      ARMI.add("taser");
      TIPI_ARMA.add("taser");
      TIPI_ARMA.add("caricatore");
      TIPI_ARMA.add("mirino");
   }
}
