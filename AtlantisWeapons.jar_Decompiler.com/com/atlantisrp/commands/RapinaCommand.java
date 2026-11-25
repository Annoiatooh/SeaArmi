package com.atlantisrp.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class RapinaCommand implements CommandExecutor, Listener {
   private static final double RAGGIO = (double)5.0F;
   private static final Map<UUID, UUID> RAPINE = new HashMap();

   public static void avvia(JavaPlugin plugin) {
      Bukkit.getScheduler().runTaskTimer(plugin, () -> {
         Iterator<Map.Entry<UUID, UUID>> it = RAPINE.entrySet().iterator();

         while(it.hasNext()) {
            Map.Entry<UUID, UUID> e = (Map.Entry)it.next();
            Player rapinatore = Bukkit.getPlayer((UUID)e.getKey());
            Player vittima = Bukkit.getPlayer((UUID)e.getValue());
            if (rapinatore == null || vittima == null) {
               it.remove();
            }
         }

         for(Player p : Bukkit.getOnlinePlayers()) {
            InventoryHolder patt0$temp = p.getOpenInventory().getTopInventory().getHolder();
            if (patt0$temp instanceof ControlloHolder h) {
               Player v = Bukkit.getPlayer(h.bersaglio);
               if (v == null) {
                  p.closeInventory();
               } else {
                  riempi(h.inventory, v);
               }
            }
         }

      }, 10L, 10L);
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player player) {
         if (args.length != 1) {
            errore(player, "Uso: /" + label + " (nick)");
            return true;
         } else {
            Player bersaglio = Bukkit.getPlayerExact(args[0]);
            if (bersaglio == null) {
               errore(player, "Giocatore non trovato.");
               return true;
            } else if (bersaglio.equals(player)) {
               errore(player, "Non puoi farlo a te stesso.");
               return true;
            } else if (label.equalsIgnoreCase("rapina")) {
               if (!vicini(player, bersaglio)) {
                  errore(player, bersaglio.getName() + " è troppo lontano (massimo 5 blocchi).");
                  return true;
               } else {
                  RAPINE.put(player.getUniqueId(), bersaglio.getUniqueId());
                  player.sendMessage(Component.text("Hai iniziato a rapinare " + bersaglio.getName() + ".", NamedTextColor.GREEN));
                  azione(player, "sta rapinando", bersaglio);
                  return true;
               }
            } else {
               boolean conRapina = bersaglio.getUniqueId().equals(RAPINE.get(player.getUniqueId()));
               boolean fdo = player.hasPermission("atlantisweapons.fdo");
               if (!conRapina && !fdo) {
                  errore(player, "Prima devi iniziare una /rapina su " + bersaglio.getName() + ".");
                  return true;
               } else if (!conRapina && !vicini(player, bersaglio)) {
                  errore(player, bersaglio.getName() + " è troppo lontano (massimo 5 blocchi).");
                  return true;
               } else {
                  azione(player, "sta controllando", bersaglio);
                  ControlloHolder holder = new ControlloHolder(bersaglio.getUniqueId());
                  holder.inventory = Bukkit.createInventory(holder, 54, Component.text("Inventario di " + bersaglio.getName(), NamedTextColor.DARK_GRAY));
                  riempi(holder.inventory, bersaglio);
                  player.openInventory(holder.inventory);
                  return true;
               }
            }
         }
      } else {
         sender.sendMessage("Solo i giocatori possono usare questo comando.");
         return true;
      }
   }

   private static void azione(Player attore, String verbo, Player bersaglio) {
      String comando = "azione " + nomeVisibile(attore) + " " + verbo + " " + nomeVisibile(bersaglio);
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), comando);
   }

   private static String nomeVisibile(Player p) {
      return PlainTextComponentSerializer.plainText().serialize(p.displayName());
   }

   private static void riempi(Inventory inv, Player v) {
      PlayerInventory pi = v.getInventory();
      inv.clear();

      for(int i = 9; i < 36; ++i) {
         inv.setItem(i - 9, copia(pi.getItem(i)));
      }

      for(int i = 0; i < 9; ++i) {
         inv.setItem(27 + i, copia(pi.getItem(i)));
      }

      inv.setItem(45, copia(pi.getHelmet()));
      inv.setItem(46, copia(pi.getChestplate()));
      inv.setItem(47, copia(pi.getLeggings()));
      inv.setItem(48, copia(pi.getBoots()));
      inv.setItem(49, copia(pi.getItemInOffHand()));
   }

   private static ItemStack copia(ItemStack i) {
      return i != null && !i.getType().isAir() ? i.clone() : null;
   }

   private static boolean vicini(Player a, Player b) {
      return a.getWorld().equals(b.getWorld()) && a.getLocation().distance(b.getLocation()) <= (double)5.0F;
   }

   private static void errore(Player p, String testo) {
      p.sendMessage(Component.text(testo, NamedTextColor.RED));
   }

   @EventHandler
   public void onClick(InventoryClickEvent event) {
      if (event.getView().getTopInventory().getHolder() instanceof ControlloHolder) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onDrag(InventoryDragEvent event) {
      if (event.getView().getTopInventory().getHolder() instanceof ControlloHolder) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onQuit(PlayerQuitEvent event) {
      RAPINE.remove(event.getPlayer().getUniqueId());
   }

   public static class ControlloHolder implements InventoryHolder {
      final UUID bersaglio;
      Inventory inventory;

      ControlloHolder(UUID bersaglio) {
         this.bersaglio = bersaglio;
      }

      public Inventory getInventory() {
         return this.inventory;
      }
   }
}
