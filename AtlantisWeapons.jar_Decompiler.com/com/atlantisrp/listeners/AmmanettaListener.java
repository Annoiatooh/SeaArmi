package com.atlantisrp.listeners;

import com.atlantisrp.Main;
import com.atlantisrp.items.ItemFactory;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AmmanettaListener implements Listener {
   private static final int DURATA_TICK = 60;
   private static final double DISTANZA_MAX = (double)3.5F;
   private static final int LENTEZZA = 3;
   private static final int BARRA_SEGMENTI = 20;
   private static final Set<UUID> AMMANETTATI = new HashSet();
   private static final Map<UUID, BukkitTask> AZIONI = new HashMap();
   private static final Map<UUID, String> LEGATURA = new HashMap();
   private static final Map<UUID, UUID> LEGATO_DA = new HashMap();

   public static String tipoLegatura(UUID id) {
      return AMMANETTATI.contains(id) ? (String)LEGATURA.get(id) : null;
   }

   public static boolean haAmmanettatoQualcuno(UUID id) {
      for(Map.Entry<UUID, UUID> e : LEGATO_DA.entrySet()) {
         if (((UUID)e.getValue()).equals(id) && AMMANETTATI.contains(e.getKey())) {
            return true;
         }
      }

      return false;
   }

   public static UUID chiHaAmmanettato(UUID id) {
      return (UUID)LEGATO_DA.get(id);
   }

   public static boolean liberaStaff(Player p) {
      if (!AMMANETTATI.contains(p.getUniqueId())) {
         return false;
      } else {
         smanetta(p);
         return true;
      }
   }

   private static boolean isLegatura(String t) {
      return "manette".equals(t) || "corda".equals(t);
   }

   public static boolean isAmmanettato(UUID uuid) {
      return AMMANETTATI.contains(uuid);
   }

   public static void avvia(JavaPlugin plugin) {
      for(Player p : Bukkit.getOnlinePlayers()) {
         ripristina(p);
      }

   }

   private static void ripristina(Player p) {
      String t = tipo(p.getInventory().getItemInOffHand());
      if (isLegatura(t)) {
         LEGATURA.put(p.getUniqueId(), t);
         AMMANETTATI.add(p.getUniqueId());
         applicaEffetti(p);
      }

   }

   @EventHandler
   public void onJoin(PlayerJoinEvent event) {
      Player p = event.getPlayer();
      Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
         if (p.isOnline()) {
            ripristina(p);
         }

      });
   }

   @EventHandler
   public void onClickGiocatore(PlayerInteractEntityEvent event) {
      if (event.getHand() == EquipmentSlot.HAND) {
         Entity var3 = event.getRightClicked();
         if (var3 instanceof Player) {
            Player bersaglio = (Player)var3;
            Player attore = event.getPlayer();
            String t = tipo(attore.getInventory().getItemInMainHand());
            if ("manette".equals(t) || "tronchesi".equals(t) || "corda".equals(t)) {
               event.setCancelled(true);
               if (!AMMANETTATI.contains(attore.getUniqueId())) {
                  if (!AZIONI.containsKey(attore.getUniqueId())) {
                     if (!bersaglio.equals(attore)) {
                        boolean bersaglioAmmanettato = AMMANETTATI.contains(bersaglio.getUniqueId());
                        String leg = (String)LEGATURA.getOrDefault(bersaglio.getUniqueId(), "manette");
                        Azione azione;
                        if ("tronchesi".equals(t)) {
                           if (!bersaglioAmmanettato) {
                              HudArmi.avviso(attore, "Questo giocatore non è ammanettato");
                              return;
                           }

                           if (!"manette".equals(leg)) {
                              HudArmi.avviso(attore, "È legato con una corda");
                              return;
                           }

                           azione = AmmanettaListener.Azione.TAGLIA;
                        } else if ("corda".equals(t)) {
                           if (!bersaglioAmmanettato) {
                              azione = AmmanettaListener.Azione.LEGA;
                           } else {
                              if (!"corda".equals(leg)) {
                                 HudArmi.avviso(attore, "È ammanettato");
                                 return;
                              }

                              azione = AmmanettaListener.Azione.SLEGA;
                           }
                        } else if (!bersaglioAmmanettato) {
                           azione = AmmanettaListener.Azione.AMMANETTA;
                        } else {
                           if (!"manette".equals(leg)) {
                              HudArmi.avviso(attore, "È legato con una corda");
                              return;
                           }

                           azione = AmmanettaListener.Azione.SMANETTA_CON_MANETTE;
                        }

                        if (!(attore.getLocation().distance(bersaglio.getLocation()) > (double)3.5F)) {
                           this.avviaAzione(attore, bersaglio, azione);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void avviaAzione(final Player attore, final Player bersaglio, final Azione azione) {
      final UUID id = attore.getUniqueId();
      final int slot = attore.getInventory().getHeldItemSlot();
      String var10000;
      switch (azione.ordinal()) {
         case 0 -> var10000 = "Ammanetto... ";
         case 1 -> var10000 = "Tolgo le manette... ";
         case 2 -> var10000 = "Taglio le manette... ";
         case 3 -> var10000 = "Lego... ";
         case 4 -> var10000 = "Slego... ";
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      final String testo = var10000;
      BukkitTask task = (new BukkitRunnable() {
         int tick = 0;

         public void run() {
            String motivo = AmmanettaListener.this.controlla(attore, bersaglio, azione, slot);
            if (motivo != null) {
               HudArmi.avviso(attore, motivo);
               AmmanettaListener.this.termina(id);
            } else {
               if (this.tick % 10 == 0 && azione != AmmanettaListener.Azione.LEGA && azione != AmmanettaListener.Azione.SLEGA) {
                  String var10000;
                  switch (azione.ordinal()) {
                     case 2:
                        var10000 = "minecraft:block.chain.fall";
                        break;
                     case 3:
                     case 4:
                        var10000 = "minecraft:block.wool.hit";
                        break;
                     default:
                        var10000 = "minecraft:block.chain.hit";
                  }

                  String suono = var10000;
                  bersaglio.getWorld().playSound(bersaglio.getLocation(), suono, 1.0F, 1.0F);
               }

               this.tick += 2;
               attore.sendActionBar(AmmanettaListener.this.barra(testo, (double)this.tick / (double)60.0F));
               if (this.tick >= 60) {
                  AmmanettaListener.this.termina(id);
                  AmmanettaListener.this.completa(attore, bersaglio, azione);
               }

            }
         }
      }).runTaskTimer(Main.getInstance(), 0L, 2L);
      AZIONI.put(id, task);
   }

   private String controlla(Player attore, Player bersaglio, Azione azione, int slot) {
      if (attore.isOnline() && !attore.isDead() && bersaglio.isOnline() && !bersaglio.isDead()) {
         if (attore.getInventory().getHeldItemSlot() != slot) {
            return "Azione interrotta";
         } else {
            String t = tipo(attore.getInventory().getItemInMainHand());
            String var10000;
            switch (azione.ordinal()) {
               case 2:
                  var10000 = "tronchesi";
                  break;
               case 3:
               case 4:
                  var10000 = "corda";
                  break;
               default:
                  var10000 = "manette";
            }

            String atteso = var10000;
            if (!atteso.equals(t)) {
               return "Azione interrotta";
            } else if (attore.getWorld().equals(bersaglio.getWorld()) && !(attore.getLocation().distance(bersaglio.getLocation()) > (double)3.5F)) {
               boolean ammanettato = AMMANETTATI.contains(bersaglio.getUniqueId());
               boolean daLegare = azione == AmmanettaListener.Azione.AMMANETTA || azione == AmmanettaListener.Azione.LEGA;
               if (daLegare && ammanettato) {
                  return "Il giocatore è già ammanettato";
               } else if (!daLegare && !ammanettato) {
                  return "Il giocatore non è più ammanettato";
               } else {
                  return AMMANETTATI.contains(attore.getUniqueId()) ? "Azione interrotta" : null;
               }
            } else if (azione == AmmanettaListener.Azione.AMMANETTA) {
               return "Ammanettamento interrotto: è scappato";
            } else {
               return azione == AmmanettaListener.Azione.LEGA ? "Legatura interrotta: è scappato" : "Azione interrotta: troppo lontano";
            }
         }
      } else {
         return "Azione interrotta";
      }
   }

   private void termina(UUID id) {
      BukkitTask task = (BukkitTask)AZIONI.remove(id);
      if (task != null) {
         task.cancel();
      }

   }

   private void completa(Player attore, Player bersaglio, Azione azione) {
      switch (azione.ordinal()) {
         case 0:
            this.ammanetta(bersaglio, "manette");
            LEGATO_DA.put(bersaglio.getUniqueId(), attore.getUniqueId());
            attore.sendMessage(Component.text("Hai ammanettato " + bersaglio.getName() + ".", NamedTextColor.GREEN));
            bersaglio.sendMessage(Component.text("Sei stato ammanettato.", NamedTextColor.RED));
            break;
         case 1:
            smanetta(bersaglio);
            attore.sendMessage(Component.text("Hai tolto le manette a " + bersaglio.getName() + ".", NamedTextColor.GREEN));
            bersaglio.sendMessage(Component.text("Ti hanno tolto le manette.", NamedTextColor.GREEN));
            break;
         case 2:
            smanetta(bersaglio);
            this.consumaUso(attore, "tronchesi");
            attore.sendMessage(Component.text("Hai tagliato le manette a " + bersaglio.getName() + ".", NamedTextColor.GREEN));
            bersaglio.sendMessage(Component.text("Ti hanno tagliato le manette.", NamedTextColor.GREEN));
            break;
         case 3:
            this.ammanetta(bersaglio, "corda");
            this.consumaUso(attore, "corda");
            attore.sendMessage(Component.text("Hai legato " + bersaglio.getName() + ".", NamedTextColor.GREEN));
            bersaglio.sendMessage(Component.text("Sei stato legato.", NamedTextColor.RED));
            break;
         case 4:
            smanetta(bersaglio);
            attore.sendMessage(Component.text("Hai slegato " + bersaglio.getName() + ".", NamedTextColor.GREEN));
            bersaglio.sendMessage(Component.text("Ti hanno slegato.", NamedTextColor.GREEN));
      }

   }

   private void consumaUso(Player attore, String tipoOggetto) {
      ItemStack t = attore.getInventory().getItemInMainHand();
      if (tipoOggetto.equals(tipo(t))) {
         int usi = (Integer)t.getItemMeta().getPersistentDataContainer().getOrDefault(ItemFactory.KEY_USI, PersistentDataType.INTEGER, 10) - 1;
         if (usi <= 0) {
            attore.getInventory().setItemInMainHand((ItemStack)null);
            attore.getWorld().playSound(attore.getLocation(), "minecraft:entity.item.break", 1.0F, 1.0F);
            attore.sendMessage(Component.text("corda".equals(tipoOggetto) ? "La corda si è rotta." : "Le tronchesi si sono rotte.", NamedTextColor.RED));
         } else {
            attore.getInventory().setItemInMainHand("corda".equals(tipoOggetto) ? ItemFactory.cordaConUsi(usi) : ItemFactory.tronchesiConUsi(usi));
         }

      }
   }

   @EventHandler
   public void onComando(PlayerCommandPreprocessEvent event) {
      Player cmd = event.getPlayer();
      if (!cmd.isOp() && !cmd.hasPermission("atlantisweapons.admin")) {
         if (AMMANETTATI.contains(cmd.getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Component.text("Sei ammanettato.", NamedTextColor.RED));
         }

      }
   }

   private void ammanetta(Player p, String tipoLegatura) {
      PlayerInventory inv = p.getInventory();
      p.setSneaking(false);
      Bukkit.getPluginManager().callEvent(new PlayerToggleSneakEvent(p, false));
      ItemStack vecchio = inv.getItemInOffHand();
      if (vecchio != null && !vecchio.getType().isAir()) {
         inv.addItem(new ItemStack[]{vecchio}).values().forEach((resto) -> p.getWorld().dropItemNaturally(p.getLocation(), resto));
      }

      inv.setItemInOffHand("corda".equals(tipoLegatura) ? ItemFactory.createCorda() : ItemFactory.createManette());
      LEGATURA.put(p.getUniqueId(), tipoLegatura);
      AMMANETTATI.add(p.getUniqueId());
      applicaEffetti(p);
      if (!"corda".equals(tipoLegatura)) {
         p.getWorld().playSound(p.getLocation(), "minecraft:block.chain.place", 1.0F, 1.0F);
      }

   }

   private static void smanetta(Player p) {
      boolean eraCorda = "corda".equals(LEGATURA.get(p.getUniqueId()));
      AMMANETTATI.remove(p.getUniqueId());
      LEGATURA.remove(p.getUniqueId());
      LEGATO_DA.remove(p.getUniqueId());
      if (isLegatura(tipo(p.getInventory().getItemInOffHand()))) {
         p.getInventory().setItemInOffHand((ItemStack)null);
      }

      togliEffetti(p);
      if (!eraCorda) {
         p.getWorld().playSound(p.getLocation(), "minecraft:block.chain.break", 1.0F, 1.0F);
      }

   }

   public static void riapplicaEffetti(Player p) {
      if (AMMANETTATI.contains(p.getUniqueId())) {
         applicaEffetti(p);
      }

   }

   private static void applicaEffetti(Player p) {
      PotionEffectType lentezza = PotionEffectType.getByKey(NamespacedKey.minecraft("slowness"));
      PotionEffectType salto = PotionEffectType.getByKey(NamespacedKey.minecraft("jump_boost"));
      if (lentezza != null) {
         p.addPotionEffect(new PotionEffect(lentezza, -1, 3, false, false, false));
      }

   }

   private static void togliEffetti(Player p) {
      PotionEffectType lentezza = PotionEffectType.getByKey(NamespacedKey.minecraft("slowness"));
      PotionEffectType salto = PotionEffectType.getByKey(NamespacedKey.minecraft("jump_boost"));
      if (lentezza != null) {
         p.removePotionEffect(lentezza);
      }

      if (salto != null) {
         p.removePotionEffect(salto);
      }

   }

   private Component barra(String testo, double progresso) {
      int pieni = (int)Math.round(Math.min((double)1.0F, progresso) * (double)20.0F);
      return ((TextComponent)Component.text(testo, NamedTextColor.GRAY).append(Component.text("█".repeat(pieni), NamedTextColor.GREEN))).append(Component.text("█".repeat(20 - pieni), NamedTextColor.DARK_GRAY));
   }

   @EventHandler
   public void onSalto(PlayerJumpEvent event) {
      if (AMMANETTATI.contains(event.getPlayer().getUniqueId())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onCambioSlot(PlayerItemHeldEvent event) {
      if (AMMANETTATI.contains(event.getPlayer().getUniqueId())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onSwap(PlayerSwapHandItemsEvent event) {
      if (AMMANETTATI.contains(event.getPlayer().getUniqueId())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onDrop(PlayerDropItemEvent event) {
      if (AMMANETTATI.contains(event.getPlayer().getUniqueId())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onInteract(PlayerInteractEvent event) {
      if (AMMANETTATI.contains(event.getPlayer().getUniqueId())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onAttacco(EntityDamageByEntityEvent event) {
      Entity var3 = event.getDamager();
      if (var3 instanceof Player p) {
         if (AMMANETTATI.contains(p.getUniqueId())) {
            event.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void onClickInventario(InventoryClickEvent event) {
      HumanEntity var3 = event.getWhoClicked();
      if (var3 instanceof Player p) {
         if (AMMANETTATI.contains(p.getUniqueId())) {
            boolean slotArmatura = event.getSlotType() == SlotType.ARMOR;
            boolean shift = event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT;
            if (!slotArmatura || !indossabileBloccato(event.getCursor()) && (event.getClick() != ClickType.NUMBER_KEY || !indossabileBloccato(p.getInventory().getItem(event.getHotbarButton())))) {
               if (shift && indossabileBloccato(event.getCurrentItem())) {
                  event.setCancelled(true);
               } else {
                  if (event.getSlot() == 40 || event.getClick() == ClickType.SWAP_OFFHAND || isLegatura(tipo(event.getCurrentItem())) || isLegatura(tipo(event.getCursor()))) {
                     event.setCancelled(true);
                  }

               }
            } else {
               event.setCancelled(true);
            }
         }
      }
   }

   @EventHandler
   public void onTrascina(InventoryDragEvent event) {
      HumanEntity var3 = event.getWhoClicked();
      if (var3 instanceof Player p) {
         if (AMMANETTATI.contains(p.getUniqueId())) {
            event.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void onMorte(PlayerDeathEvent event) {
      Player p = event.getEntity();
      if (AMMANETTATI.remove(p.getUniqueId())) {
         LEGATURA.remove(p.getUniqueId());
         event.getDrops().removeIf((i) -> isLegatura(tipo(i)));
         togliEffetti(p);
      }

      this.termina(p.getUniqueId());
   }

   private static boolean indossabileBloccato(ItemStack item) {
      String t = tipo(item);
      return "giubbotto_leggero".equals(t) || "giubbotto_pesante".equals(t) || "passamontagna".equals(t) || "visore".equals(t);
   }

   private static String tipo(ItemStack item) {
      return item != null && item.getItemMeta() != null ? (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING) : null;
   }

   private static enum Azione {
      AMMANETTA,
      SMANETTA_CON_MANETTE,
      TAGLIA,
      LEGA,
      SLEGA;

      // $FF: synthetic method
      private static Azione[] $values() {
         return new Azione[]{AMMANETTA, SMANETTA_CON_MANETTE, TAGLIA, LEGA, SLEGA};
      }
   }
}
