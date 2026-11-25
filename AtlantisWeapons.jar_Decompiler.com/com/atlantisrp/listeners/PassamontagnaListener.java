package com.atlantisrp.listeners;

import com.atlantisrp.Main;
import com.atlantisrp.gui.ArmiGUI;
import com.atlantisrp.items.ItemFactory;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class PassamontagnaListener implements Listener {
   private static final String NOME_TEAM = "atl_passamontagna";
   private static final int SLOT_ELMO = 39;
   private static final Map<UUID, String> NASCOSTI = new HashMap();
   private static final Map<UUID, Component> NOME_ORIGINALE = new HashMap();
   private static final String NOME_ANONIMO = "Anonimo";
   private static final float ALTEZZA_NICK = 0.4F;
   private static final Map<UUID, TextDisplay> ETICHETTE = new HashMap();

   public static void avvia(JavaPlugin plugin) {
      Team vecchio = scoreboard().getTeam("atl_passamontagna");
      if (vecchio != null) {
         vecchio.unregister();
      }

      Bukkit.getScheduler().runTaskTimer(plugin, () -> {
         for(Player p : Bukkit.getOnlinePlayers()) {
            boolean haIlPassamontagna = isIndossabileTesta(p.getInventory().getHelmet());
            boolean nascosto = NASCOSTI.containsKey(p.getUniqueId());
            if (haIlPassamontagna && !nascosto) {
               nascondi(p);
            } else if (!haIlPassamontagna && nascosto) {
               mostra(p);
            } else if (haIlPassamontagna) {
               Team team = scoreboard().getTeam("atl_passamontagna");
               Team attuale = scoreboard().getEntryTeam(p.getName());
               if (team != null && attuale != team) {
                  team.addEntry(p.getName());
               }

               nascondiTagAltrui(p);
               controllaEtichetta(p);
               if (!"Anonimo".equals(PlainTextComponentSerializer.plainText().serialize(p.displayName()))) {
                  p.displayName(Component.text("Anonimo", NamedTextColor.WHITE));
               }
            }
         }

      }, 10L, 10L);
   }

   public static void ripristinaTutti() {
      for(Player p : Bukkit.getOnlinePlayers()) {
         if (NASCOSTI.containsKey(p.getUniqueId())) {
            mostra(p);
         }
      }

      Team t = scoreboard().getTeam("atl_passamontagna");
      if (t != null) {
         t.unregister();
      }

   }

   private static Scoreboard scoreboard() {
      return Bukkit.getScoreboardManager().getMainScoreboard();
   }

   private static void nascondi(Player p) {
      Scoreboard sb = scoreboard();
      Team team = sb.getTeam("atl_passamontagna");
      if (team == null) {
         team = sb.registerNewTeam("atl_passamontagna");
         team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
      }

      Team precedente = sb.getEntryTeam(p.getName());
      NASCOSTI.put(p.getUniqueId(), precedente == null ? null : precedente.getName());
      team.addEntry(p.getName());
      creaEtichetta(p);
      nascondiTagAltrui(p);
      NOME_ORIGINALE.put(p.getUniqueId(), p.displayName());
      p.displayName(Component.text("Anonimo", NamedTextColor.WHITE));
   }

   private static void creaEtichetta(Player p) {
      rimuoviEtichetta(p);
      TextDisplay d = (TextDisplay)p.getWorld().spawn(p.getLocation(), TextDisplay.class, (t) -> {
         t.text(Component.text("Anonimo", NamedTextColor.WHITE));
         t.setBillboard(Billboard.CENTER);
         t.setPersistent(false);
         t.setTransformation(new Transformation(new Vector3f(0.0F, 0.4F, 0.0F), new AxisAngle4f(), new Vector3f(1.0F, 1.0F, 1.0F), new AxisAngle4f()));
      });
      p.addPassenger(d);
      p.hideEntity(Main.getInstance(), d);
      ETICHETTE.put(p.getUniqueId(), d);
   }

   private static void controllaEtichetta(Player p) {
      TextDisplay d = (TextDisplay)ETICHETTE.get(p.getUniqueId());
      if (d == null || !d.isValid() || !p.getPassengers().contains(d)) {
         creaEtichetta(p);
      }

   }

   private static void rimuoviEtichetta(Player p) {
      TextDisplay d = (TextDisplay)ETICHETTE.remove(p.getUniqueId());
      if (d != null) {
         d.remove();
      }

   }

   private static void tab(Player p, boolean nascondi) {
      try {
         Class<?> api = Class.forName("me.neznamy.tab.api.TabAPI");
         Object tab = api.getMethod("getInstance").invoke((Object)null);
         Object tp = api.getMethod("getPlayer", UUID.class).invoke(tab, p.getUniqueId());
         Object nt = api.getMethod("getNameTagManager").invoke(tab);
         if (tp == null || nt == null) {
            return;
         }

         Class<?> ntc = Class.forName("me.neznamy.tab.api.nametag.NameTagManager");
         Class<?> tpc = Class.forName("me.neznamy.tab.api.TabPlayer");
         ntc.getMethod(nascondi ? "hideNameTag" : "showNameTag", tpc).invoke(nt, tp);
      } catch (Throwable var8) {
      }

   }

   private static void nascondiTagAltrui(Player p) {
      tab(p, true);
      TextDisplay nostra = (TextDisplay)ETICHETTE.get(p.getUniqueId());

      for(Entity e : p.getPassengers()) {
         if (!e.equals(nostra) && e instanceof Display) {
            for(Player v : Bukkit.getOnlinePlayers()) {
               if (!v.equals(p)) {
                  v.hideEntity(Main.getInstance(), e);
               }
            }
         }
      }

   }

   private static void mostraTagAltrui(Player p) {
      tab(p, false);

      for(Entity e : p.getPassengers()) {
         if (e instanceof Display) {
            for(Player v : Bukkit.getOnlinePlayers()) {
               if (!v.equals(p)) {
                  v.showEntity(Main.getInstance(), e);
               }
            }
         }
      }

   }

   private static void mostra(Player p) {
      rimuoviEtichetta(p);
      mostraTagAltrui(p);
      Scoreboard sb = scoreboard();
      Team team = sb.getTeam("atl_passamontagna");
      if (team != null) {
         team.removeEntry(p.getName());
      }

      Component originale = (Component)NOME_ORIGINALE.remove(p.getUniqueId());
      p.displayName((Component)(originale == null ? Component.text(p.getName()) : originale));
      String precedente = (String)NASCOSTI.remove(p.getUniqueId());
      if (precedente != null) {
         Team t = sb.getTeam(precedente);
         if (t != null) {
            t.addEntry(p.getName());
         }
      }

   }

   @EventHandler
   public void onQuit(PlayerQuitEvent event) {
      if (NASCOSTI.containsKey(event.getPlayer().getUniqueId())) {
         mostra(event.getPlayer());
      }

   }

   @EventHandler
   public void onInteract(PlayerInteractEvent event) {
      if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
         ItemStack item = event.getItem();
         if (isIndossabileTesta(item)) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            if (!AmmanettaListener.isAmmanettato(player.getUniqueId())) {
               PlayerInventory inv = player.getInventory();
               ItemStack elmoVecchio = inv.getHelmet();
               inv.setHelmet(item.clone());
               if (event.getHand() == EquipmentSlot.OFF_HAND) {
                  inv.setItemInOffHand(elmoVecchio);
               } else {
                  inv.setItemInMainHand(elmoVecchio);
               }

            }
         }
      }
   }

   @EventHandler
   public void onClick(InventoryClickEvent event) {
      HumanEntity var3 = event.getWhoClicked();
      if (var3 instanceof Player player) {
         if (!(event.getView().getTopInventory().getHolder() instanceof ArmiGUI)) {
            if (!AmmanettaListener.isAmmanettato(player.getUniqueId())) {
               PlayerInventory inv = player.getInventory();
               if (event.getClickedInventory() == inv) {
                  if ((event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.SHIFT_LEFT) && event.getSlotType() != SlotType.ARMOR && isGiubbotto(event.getCurrentItem())) {
                     event.setCancelled(true);
                     ItemStack vecchio = inv.getChestplate();
                     inv.setChestplate(event.getCurrentItem().clone());
                     event.setCurrentItem(vecchio);
                  } else if ((event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.SHIFT_LEFT) && event.getSlotType() != SlotType.ARMOR && isIndossabileTesta(event.getCurrentItem())) {
                     event.setCancelled(true);
                     ItemStack elmoVecchio = inv.getHelmet();
                     inv.setHelmet(event.getCurrentItem().clone());
                     event.setCurrentItem(elmoVecchio);
                  } else {
                     if (event.getSlotType() == SlotType.ARMOR && event.getSlot() == 39) {
                        ItemStack inCursore = event.getCursor();
                        if (isIndossabileTesta(inCursore)) {
                           event.setCancelled(true);
                           ItemStack elmoVecchio = inv.getHelmet();
                           inv.setHelmet(inCursore.clone());
                           event.setCursor(elmoVecchio);
                           return;
                        }

                        if (event.getClick() == ClickType.NUMBER_KEY) {
                           ItemStack dallaHotbar = inv.getItem(event.getHotbarButton());
                           if (isIndossabileTesta(dallaHotbar)) {
                              event.setCancelled(true);
                              ItemStack elmoVecchio = inv.getHelmet();
                              inv.setHelmet(dallaHotbar.clone());
                              inv.setItem(event.getHotbarButton(), elmoVecchio);
                           }
                        }
                     }

                  }
               }
            }
         }
      }
   }

   private static boolean isIndossabileTesta(ItemStack item) {
      if (item != null && item.getItemMeta() != null) {
         String t = (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
         return "passamontagna".equals(t) || "visore".equals(t);
      } else {
         return false;
      }
   }

   private static boolean isGiubbotto(ItemStack item) {
      if (item != null && item.getItemMeta() != null) {
         String t = (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
         return "giubbotto_leggero".equals(t) || "giubbotto_pesante".equals(t);
      } else {
         return false;
      }
   }

   private static boolean isPassamontagna(ItemStack item) {
      return item != null && item.getItemMeta() != null ? "passamontagna".equals(item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING)) : false;
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onChatPaper(AsyncChatEvent event) {
      if (NASCOSTI.containsKey(event.getPlayer().getUniqueId())) {
         event.renderer(ChatRenderer.viewerUnaware((source, nome, messaggio) -> Component.text("Anonimo: ", NamedTextColor.WHITE).append(messaggio)));
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onChat(AsyncPlayerChatEvent event) {
      if (NASCOSTI.containsKey(event.getPlayer().getUniqueId())) {
         event.setFormat("Anonimo: %2$s");
      }

   }

   @EventHandler
   public void onJoin(PlayerJoinEvent event) {
      Player p = event.getPlayer();
      Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
         if (p.isOnline() && isIndossabileTesta(p.getInventory().getHelmet()) && !NASCOSTI.containsKey(p.getUniqueId())) {
            nascondi(p);
         }

      });
   }
}
