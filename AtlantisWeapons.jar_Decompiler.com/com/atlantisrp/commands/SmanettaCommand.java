package com.atlantisrp.commands;

import com.atlantisrp.Main;
import com.atlantisrp.items.ItemFactory;
import com.atlantisrp.items.TipoArma;
import com.atlantisrp.listeners.AmmanettaListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class SmanettaCommand implements CommandExecutor {
   public static final String PERM_FDO = "atlantisweapons.fdo";
   public static final String PERM_CRIME = "atlantisweapons.crime";
   private static final int DURATA_TICK = 300;
   private static final double RAGGIO = (double)15.0F;
   private static final int CMD_TOTEM_FDO = 2;
   private static final int CMD_TOTEM_CRIME = 3;
   private static final int ABBATTIBILE_TICK = 1200;
   private static final float ALTEZZA_SCRITTA = 1.0F;
   private static final Map<UUID, BukkitTask> RICHIESTE = new HashMap();
   private static final Map<UUID, TextDisplay> SCRITTE = new HashMap();
   private static final Map<UUID, TextDisplay> ABBATTIBILI = new HashMap();

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player richiedente) {
         boolean op = richiedente.isOp();
         if (!op && richiedente.hasPermission("atlantisweapons.fdo")) {
            errore(richiedente, "Gli fdo non possono richiedere lo smanetta.");
            return true;
         } else if (!op && AmmanettaListener.haAmmanettatoQualcuno(richiedente.getUniqueId())) {
            errore(richiedente, "Chi ha ammanettato non può richiedere lo smanetta.");
            return true;
         } else if (!op && !haArmaDaFuoco(richiedente)) {
            errore(richiedente, "Devi avere un'arma da fuoco in mano.");
            return true;
         } else {
            Player ammanettato = null;
            Player fdo = null;
            if (args.length >= 1) {
               ammanettato = Bukkit.getPlayerExact(args[0]);
               if (ammanettato == null) {
                  errore(richiedente, "Giocatore non trovato.");
                  return true;
               }

               if (!"manette".equals(AmmanettaListener.tipoLegatura(ammanettato.getUniqueId()))) {
                  errore(richiedente, ammanettato.getName() + " non è ammanettato.");
                  return true;
               }

               UUID idFdo = AmmanettaListener.chiHaAmmanettato(ammanettato.getUniqueId());
               fdo = idFdo == null ? null : Bukkit.getPlayer(idFdo);
               if (fdo == null) {
                  errore(richiedente, "L'fdo che lo ha ammanettato non è online.");
                  return true;
               }
            } else {
               double migliore = Double.MAX_VALUE;

               for(Player p : Bukkit.getOnlinePlayers()) {
                  if ("manette".equals(AmmanettaListener.tipoLegatura(p.getUniqueId()))) {
                     UUID idFdo = AmmanettaListener.chiHaAmmanettato(p.getUniqueId());
                     Player candidato = idFdo == null ? null : Bukkit.getPlayer(idFdo);
                     if (candidato != null && candidato.getWorld().equals(richiedente.getWorld())) {
                        double d = candidato.getLocation().distance(richiedente.getLocation());
                        if ((op || d <= (double)15.0F) && d < migliore) {
                           migliore = d;
                           ammanettato = p;
                           fdo = candidato;
                        }
                     }
                  }
               }

               if (ammanettato == null) {
                  errore(richiedente, "Nessun fdo con un ammanettato nel raggio di 15 blocchi.");
                  return true;
               }
            }

            if (op || fdo.getWorld().equals(richiedente.getWorld()) && !(fdo.getLocation().distance(richiedente.getLocation()) > (double)15.0F)) {
               if (RICHIESTE.containsKey(ammanettato.getUniqueId())) {
                  errore(richiedente, "Una richiesta di smanetta è già in corso.");
                  return true;
               } else {
                  this.avvia(richiedente, ammanettato, fdo);
                  return true;
               }
            } else {
               errore(richiedente, "L'fdo è troppo lontano (massimo 15 blocchi).");
               return true;
            }
         }
      } else {
         sender.sendMessage("Solo i giocatori possono usare questo comando.");
         return true;
      }
   }

   private void avvia(final Player richiedente, final Player ammanettato, final Player fdo) {
      final UUID chiave = ammanettato.getUniqueId();
      Set<UUID> fatti = new HashSet();
      totemUnico(ammanettato, 3, fatti);
      totemUnico(richiedente, 3, fatti);
      totemUnico(fdo, 2, fatti);

      for(Player p : Bukkit.getOnlinePlayers()) {
         if (p.hasPermission("atlantisweapons.fdo")) {
            totemUnico(p, 2, fatti);
         } else if (p.hasPermission("atlantisweapons.crime")) {
            totemUnico(p, 3, fatti);
         }
      }

      final String nomeFdo = nome(fdo);
      final TextDisplay scritta = creaScritta(ammanettato);
      BukkitTask task = (new BukkitRunnable() {
         int tick = 0;

         private void fine(Component testo) {
            SmanettaCommand.RICHIESTE.remove(chiave);
            this.cancel();
            if (scritta.isValid()) {
               scritta.text(testo);
               Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                  scritta.remove();
                  SmanettaCommand.SCRITTE.remove(chiave, scritta);
               }, 60L);
            }

         }

         public void run() {
            boolean liberato = !ammanettato.isOnline() || !AmmanettaListener.isAmmanettato(ammanettato.getUniqueId());
            if (liberato) {
               this.fine(Component.text("SMANETTA ACCETTATA", NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true));
            } else if (richiedente.isOnline() && fdo.isOnline()) {
               if (this.tick >= 300) {
                  this.fine(Component.text("RICHIESTA FALLITA", NamedTextColor.RED).decoration(TextDecoration.BOLD, true));
                  fdo.sendMessage(Component.text("Non hai smanettato: sei abbattibile.", NamedTextColor.RED));
                  SmanettaCommand.segnaAbbattibile(fdo);
               } else {
                  double rimasti = (double)(300 - this.tick) / (double)20.0F;
                  scritta.text(((TextComponent)((TextComponent)((TextComponent)Component.text("SMANETTA", NamedTextColor.RED).decoration(TextDecoration.BOLD, true)).append(Component.text(String.format(Locale.ROOT, " (%.1fs)", rimasti), NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))).appendNewline()).append(Component.text("Richiesto a " + nomeFdo, NamedTextColor.GRAY)));
                  this.tick += 4;
               }
            } else {
               SmanettaCommand.RICHIESTE.remove(chiave);
               this.cancel();
               scritta.remove();
               SmanettaCommand.SCRITTE.remove(chiave, scritta);
            }
         }
      }).runTaskTimer(Main.getInstance(), 0L, 4L);
      RICHIESTE.put(chiave, task);
   }

   private static TextDisplay creaScritta(Player ammanettato) {
      UUID id = ammanettato.getUniqueId();
      TextDisplay vecchia = (TextDisplay)SCRITTE.remove(id);
      if (vecchia != null) {
         vecchia.remove();
      }

      TextDisplay d = (TextDisplay)ammanettato.getWorld().spawn(ammanettato.getLocation(), TextDisplay.class, (t) -> {
         t.text(Component.text("SMANETTA", NamedTextColor.RED));
         t.setBillboard(Billboard.CENTER);
         t.setPersistent(false);
         t.setTransformation(new Transformation(new Vector3f(0.0F, 1.0F, 0.0F), new AxisAngle4f(), new Vector3f(1.0F, 1.0F, 1.0F), new AxisAngle4f()));
      });
      ammanettato.addPassenger(d);
      SCRITTE.put(id, d);
      return d;
   }

   private static void totemUnico(Player p, int cmd, Set<UUID> fatti) {
      if (fatti.add(p.getUniqueId())) {
         totem(p, cmd);
      }

   }

   private static void totem(Player p, int cmd) {
      PlayerInventory inv = p.getInventory();
      ItemStack vecchio = inv.getItemInOffHand();
      ItemStack t = new ItemStack(Material.TOTEM_OF_UNDYING);
      ItemMeta meta = t.getItemMeta();
      meta.setCustomModelData(cmd);
      t.setItemMeta(meta);
      inv.setItemInOffHand(t);
      p.updateInventory();
      Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
         if (p.isOnline()) {
            p.playEffect(EntityEffect.PROTECTED_FROM_DEATH);
            p.playSound(p.getLocation(), "minecraft:item.totem.use", 1.0F, 1.0F);
         }
      }, 2L);
      Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
         if (p.isOnline()) {
            inv.setItemInOffHand(vecchio);
            p.updateInventory();
         }
      }, 8L);
   }

   private static void segnaAbbattibile(Player fdo) {
      rimuoviAbbattibile(fdo.getUniqueId());
      TextDisplay d = (TextDisplay)fdo.getWorld().spawn(fdo.getLocation(), TextDisplay.class, (t) -> {
         t.text(Component.text("ABBATTIBILE", NamedTextColor.RED).decoration(TextDecoration.BOLD, true));
         t.setBillboard(Billboard.CENTER);
         t.setPersistent(false);
         t.setTransformation(new Transformation(new Vector3f(0.0F, 1.0F, 0.0F), new AxisAngle4f(), new Vector3f(1.0F, 1.0F, 1.0F), new AxisAngle4f()));
      });
      fdo.addPassenger(d);
      ABBATTIBILI.put(fdo.getUniqueId(), d);
      Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
         if (ABBATTIBILI.get(fdo.getUniqueId()) == d) {
            rimuoviAbbattibile(fdo.getUniqueId());
         }

      }, 1200L);
   }

   private static void rimuoviAbbattibile(UUID id) {
      TextDisplay d = (TextDisplay)ABBATTIBILI.remove(id);
      if (d != null) {
         d.remove();
      }

   }

   public static void rimuoviTutti() {
      for(BukkitTask t : RICHIESTE.values()) {
         t.cancel();
      }

      RICHIESTE.clear();

      for(TextDisplay d : SCRITTE.values()) {
         d.remove();
      }

      SCRITTE.clear();

      for(TextDisplay d : ABBATTIBILI.values()) {
         d.remove();
      }

      ABBATTIBILI.clear();
   }

   private static String nome(Player p) {
      return PlainTextComponentSerializer.plainText().serialize(p.displayName());
   }

   private static void errore(Player p, String testo) {
      p.sendMessage(Component.text(testo, NamedTextColor.RED));
   }

   private static boolean haArmaDaFuoco(Player p) {
      ItemStack main = p.getInventory().getItemInMainHand();
      ItemStack off = p.getInventory().getItemInOffHand();
      return "mirino".equals(tipoDi(main)) ? isArmaDaFuoco(off) : isArmaDaFuoco(main);
   }

   private static String tipoDi(ItemStack item) {
      return item != null && item.getItemMeta() != null ? (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING) : null;
   }

   private static boolean isArmaDaFuoco(ItemStack item) {
      if (item != null && item.getItemMeta() != null) {
         String t = (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
         if (t == null) {
            return false;
         } else {
            for(TipoArma a : TipoArma.values()) {
               if (a.getId().equals(t)) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }
}
