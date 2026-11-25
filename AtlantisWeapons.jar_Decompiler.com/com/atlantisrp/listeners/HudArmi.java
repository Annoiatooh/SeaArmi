package com.atlantisrp.listeners;

import com.atlantisrp.items.ItemFactory;
import com.atlantisrp.items.StatoArmi;
import com.atlantisrp.items.TipoArma;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HudArmi {
   private static final Map<String, Integer> CAPACITA = new HashMap();
   private static final int CAPACITA_DEFAULT = 17;
   private static final Set<UUID> mostrando;
   private static final Key FONT_BARRA;
   private static final Key FONT_SPAZI;
   private static final char SINISTRA = '\ue000';
   private static final char[] CENTRI;
   private static final char DESTRA = '\ue00a';
   private static final char MENO_UNO = '\ue00b';
   private static final int BARRA_W = 120;
   private static final int PREFISSO_W = 68;
   private static final int NUMERI_W = 30;
   private static final int SCARICO_W = 38;
   private static final Map<UUID, Long> AVVISO_FINO;
   private static final int AVVISO_BARRA_W = 230;
   private static final int AVVISO_PX_PER_CARATTERE = 5;

   public static void avvia(JavaPlugin plugin) {
      (new BukkitRunnable() {
         public void run() {
            for(Player player : Bukkit.getOnlinePlayers()) {
               HudArmi.aggiorna(player);
            }

            HudArmi.mostrando.removeIf((uuid) -> Bukkit.getPlayer(uuid) == null);
         }
      }).runTaskTimer(plugin, 0L, 4L);
   }

   public static void avviso(Player player, String testo) {
      avviso(player, testo, NamedTextColor.RED);
   }

   public static void avviso(Player player, String testo, NamedTextColor colore) {
      AVVISO_FINO.put(player.getUniqueId(), System.currentTimeMillis() + 1500L);
      mostrando.add(player.getUniqueId());
      player.sendActionBar(costruisciAvviso(testo, colore));
   }

   private static Component costruisciAvviso(String testo, NamedTextColor colore) {
      int larghezzaTesto = testo.length() * 5;
      int margine = Math.max(0, (230 - larghezzaTesto) / 2);
      Component sfondo = ((TextComponent)Component.text(disegnaSfondo(230), NamedTextColor.WHITE).font(FONT_BARRA)).shadowColor(ShadowColor.none());
      return ((TextComponent)((TextComponent)((TextComponent)Component.empty().append(sfondo)).append(spazio(-(230 - margine)))).append(Component.text(testo, colore))).append(spazio(230 - margine - larghezzaTesto));
   }

   private static void aggiorna(Player player) {
      Long fino = (Long)AVVISO_FINO.get(player.getUniqueId());
      if (fino != null) {
         if (System.currentTimeMillis() < fino) {
            return;
         }

         AVVISO_FINO.remove(player.getUniqueId());
         player.sendActionBar(Component.empty());
      }

      ItemStack inMano = player.getInventory().getItemInMainHand();
      if (inMano.getItemMeta() != null && "mirino".equals(inMano.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING))) {
         inMano = player.getInventory().getItemInOffHand();
      }

      if (inMano.getItemMeta() != null && "taser".equals(inMano.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING))) {
         mostrando.add(player.getUniqueId());
         int cariche = (Integer)inMano.getItemMeta().getPersistentDataContainer().getOrDefault(ItemFactory.KEY_CARICHE, PersistentDataType.INTEGER, 0);
         player.sendActionBar(costruisciBarra(true, cariche, 4));
      } else {
         ItemStack item = trovaItem(player);
         if (item == null) {
            if (mostrando.remove(player.getUniqueId())) {
               player.sendActionBar(Component.empty());
            }

         } else {
            mostrando.add(player.getUniqueId());
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            String tipo = (String)pdc.get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
            int munizioni = (Integer)pdc.getOrDefault(ItemFactory.KEY_MUNIZIONI, PersistentDataType.INTEGER, 0);
            if ("caricatore".equals(tipo)) {
               String arma = (String)pdc.get(ItemFactory.KEY_ARMA_CARICATORE, PersistentDataType.STRING);
               int capacita = arma == null ? 17 : (Integer)CAPACITA.getOrDefault(arma, 17);
               player.sendActionBar(costruisciBarra(true, munizioni, capacita));
            } else {
               int capacita = (Integer)CAPACITA.getOrDefault(tipo, 17);
               StatoArmi.Stato stato = StatoArmi.di(item);
               boolean carica = stato.haCaricatore && stato.munizioni > 0;
               player.sendActionBar(costruisciBarra(carica, stato.munizioni, capacita));
            }
         }
      }
   }

   private static Component costruisciBarra(boolean carica, int munizioni, int capacita) {
      int larghezzaTesto = 68 + (carica ? 30 : 38);
      int margine = Math.max(0, (120 - larghezzaTesto) / 2);
      Component sfondo = ((TextComponent)Component.text(disegnaSfondo(120), NamedTextColor.WHITE).font(FONT_BARRA)).shadowColor(ShadowColor.none());
      Component valore;
      if (carica) {
         valore = Component.text(munizioni + "/" + capacita, NamedTextColor.WHITE);
      } else {
         valore = Component.text("Scarico", NamedTextColor.WHITE);
      }

      Component testo = ((TextComponent)((TextComponent)Component.text("Caricatore ", NamedTextColor.GRAY).append(Component.text("»", NamedTextColor.DARK_GRAY))).append(Component.text(" "))).append(valore);
      return ((TextComponent)((TextComponent)((TextComponent)Component.empty().append(sfondo)).append(spazio(-(120 - margine)))).append(testo)).append(spazio(120 - margine - larghezzaTesto));
   }

   private static String disegnaSfondo(int larghezza) {
      StringBuilder sb = new StringBuilder();
      sb.append('\ue000').append('\ue00b');
      int centro = larghezza - 10;

      for(int i = CENTRI.length - 1; i >= 0; --i) {
         if ((centro & 1 << i) != 0) {
            sb.append(CENTRI[i]).append('\ue00b');
         }
      }

      sb.append('\ue00a').append('\ue00b');
      return sb.toString();
   }

   private static Component spazio(int n) {
      if (n == 0) {
         return Component.empty();
      } else {
         char c = (char)((n > 0 ? '\ue000' : '\uf000') + Math.abs(n));
         return Component.text(String.valueOf(c)).font(FONT_SPAZI);
      }
   }

   private static ItemStack trovaItem(Player player) {
      ItemStack main = player.getInventory().getItemInMainHand();
      if (!isArma(main) && !isCaricatore(main)) {
         ItemStack off = player.getInventory().getItemInOffHand();
         return !isArma(off) && !isCaricatore(off) ? null : off;
      } else {
         return main;
      }
   }

   private static boolean isArma(ItemStack item) {
      if (item != null && item.getItemMeta() != null) {
         String tipo = (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
         return tipo != null && CAPACITA.containsKey(tipo);
      } else {
         return false;
      }
   }

   private static boolean isCaricatore(ItemStack item) {
      if (item != null && item.getItemMeta() != null) {
         String tipo = (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
         return "caricatore".equals(tipo);
      } else {
         return false;
      }
   }

   static {
      for(TipoArma tipo : TipoArma.values()) {
         CAPACITA.put(tipo.getId(), tipo.getCapacita());
      }

      mostrando = new HashSet();
      FONT_BARRA = Key.key("atlantis", "armi");
      FONT_SPAZI = Key.key("mythichud", "spaces");
      CENTRI = new char[]{'\ue001', '\ue002', '\ue003', '\ue004', '\ue005', '\ue006', '\ue007', '\ue008', '\ue009'};
      AVVISO_FINO = new HashMap();
   }
}
