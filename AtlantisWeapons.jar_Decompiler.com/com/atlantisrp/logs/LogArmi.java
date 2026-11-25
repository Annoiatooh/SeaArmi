package com.atlantisrp.logs;

import com.atlantisrp.items.TipoArma;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class LogArmi {
   private static final Set<UUID> ATTIVI = new HashSet();
   private static final Map<UUID, UltimoColpo> ULTIMO = new HashMap();
   private static final long VALIDITA_MS = 3000L;

   public static void attiva(Player player) {
      ATTIVI.add(player.getUniqueId());
   }

   public static void disattiva(Player player) {
      ATTIVI.remove(player.getUniqueId());
   }

   public static boolean isAttivo(Player player) {
      return ATTIVI.contains(player.getUniqueId());
   }

   public static void colpo(Player tiratore, Player vittima, TipoArma arma) {
      ULTIMO.put(vittima.getUniqueId(), new UltimoColpo(tiratore.getName(), arma.getNome(), System.currentTimeMillis()));
      Component msg = prefisso().append(Component.text(tiratore.getName(), NamedTextColor.WHITE)).append(Component.text(" ")).append(coordinate(tiratore.getLocation())).append(Component.text(" ha sparato a ", NamedTextColor.GRAY)).append(Component.text(vittima.getName(), NamedTextColor.WHITE)).append(Component.text(" ")).append(coordinate(vittima.getLocation())).append(Component.text(" con " + arma.getNome(), NamedTextColor.GRAY));
      invia(msg);
   }

   public static void morte(Player vittima) {
      UltimoColpo ultimo = (UltimoColpo)ULTIMO.remove(vittima.getUniqueId());
      String causa;
      if (ultimo != null && System.currentTimeMillis() - ultimo.quando() <= 3000L) {
         String var10000 = ultimo.tiratore();
         causa = "ucciso da " + var10000 + " con " + ultimo.arma();
      } else if (vittima.getKiller() != null) {
         causa = "ucciso da " + vittima.getKiller().getName();
      } else {
         EntityDamageEvent ultimoDanno = vittima.getLastDamageCause();
         causa = ultimoDanno == null ? "causa sconosciuta" : traduci(ultimoDanno.getCause());
      }

      Component msg = prefisso().append(Component.text(vittima.getName(), NamedTextColor.WHITE)).append(Component.text(" ")).append(coordinate(vittima.getLocation())).append(Component.text(" è morto: ", NamedTextColor.RED)).append(Component.text(causa, NamedTextColor.GRAY));
      invia(msg);
   }

   private static String traduci(EntityDamageEvent.DamageCause causa) {
      String var10000;
      switch (causa) {
         case FALL:
            var10000 = "caduta";
            break;
         case FIRE:
         case FIRE_TICK:
            var10000 = "fuoco";
            break;
         case LAVA:
            var10000 = "lava";
            break;
         case DROWNING:
            var10000 = "annegamento";
            break;
         case SUFFOCATION:
            var10000 = "soffocamento";
            break;
         case STARVATION:
            var10000 = "fame";
            break;
         case VOID:
            var10000 = "vuoto";
            break;
         case CONTACT:
            var10000 = "contatto (cactus)";
            break;
         case ENTITY_ATTACK:
         case ENTITY_SWEEP_ATTACK:
            var10000 = "colpito da un'entità";
            break;
         case PROJECTILE:
            var10000 = "proiettile";
            break;
         case BLOCK_EXPLOSION:
         case ENTITY_EXPLOSION:
            var10000 = "esplosione";
            break;
         case POISON:
            var10000 = "veleno";
            break;
         case WITHER:
            var10000 = "wither";
            break;
         case MAGIC:
            var10000 = "magia";
            break;
         case LIGHTNING:
            var10000 = "fulmine";
            break;
         case SUICIDE:
            var10000 = "suicidio";
            break;
         case FREEZE:
            var10000 = "congelamento";
            break;
         case CRAMMING:
            var10000 = "troppe entità";
            break;
         case FLY_INTO_WALL:
            var10000 = "impatto contro un muro";
            break;
         case HOT_FLOOR:
            var10000 = "blocco di magma";
            break;
         case CUSTOM:
            var10000 = "altro";
            break;
         default:
            var10000 = causa.name().toLowerCase().replace('_', ' ');
      }

      return var10000;
   }

   private static Component prefisso() {
      return Component.text("[LOG] ", NamedTextColor.RED);
   }

   private static Component coordinate(Location l) {
      int x = l.getBlockX();
      int y = l.getBlockY();
      int z = l.getBlockZ();
      String comando = "/execute in " + String.valueOf(l.getWorld().getKey()) + " run tp @s " + x + " " + y + " " + z;
      return ((TextComponent)Component.text("[" + x + ", " + y + ", " + z + "]", NamedTextColor.AQUA).clickEvent(ClickEvent.runCommand(comando))).hoverEvent(HoverEvent.showText(Component.text("Clicca per teletrasportarti", NamedTextColor.GRAY)));
   }

   private static void invia(Component msg) {
      for(Player p : Bukkit.getOnlinePlayers()) {
         if (ATTIVI.contains(p.getUniqueId())) {
            p.sendMessage(msg);
         }
      }

   }

   private static record UltimoColpo(String tiratore, String arma, long quando) {
   }
}
