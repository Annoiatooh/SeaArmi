package com.atlantisrp.listeners;

import com.atlantisrp.Main;
import com.atlantisrp.items.ItemFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class FumogenoListener implements Listener {
   private static final int DURATA_TICK = 400;
   private static final int CMD_FUMO = 3;
   private static final int QUANTI = 3;
   private static final float SCALA = 1.0F;
   private static final float SCALA_FINE = 2.5F;
   private static final int MOVIMENTO_TICK = 30;
   private static final double DERIVA = 0.9;
   private static final double SALITA = 0.6;
   private static final double DISPERSIONE = 1.2;
   private static final double VELOCITA = 1.2;
   private static final long COOLDOWN_MS = 20000L;
   private static final double RAGGIO = (double)3.5F;
   private static final int CIECITA_DOPO_USCITA_TICK = 60;
   private static final Map<UUID, Long> COOLDOWN = new HashMap();
   private static final Map<UUID, Long> ACCECATO_FINO = new HashMap();
   private static final Set<UUID> CON_VISIONE = new HashSet();
   private static final List<Nube> NUBI = new ArrayList();
   private static final List<ItemDisplay> ATTIVI = new ArrayList();
   private final Random random = new Random();

   private static PotionEffectType eff(String nome) {
      return PotionEffectType.getByKey(NamespacedKey.minecraft(nome));
   }

   private static boolean visore(Player p) {
      ItemStack h = p.getInventory().getHelmet();
      return h != null && h.getItemMeta() != null && "visore".equals(h.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING));
   }

   private static boolean dentroNube(Player p) {
      long ora = System.currentTimeMillis();

      for(Nube n : NUBI) {
         if (n.fineMs() > ora && n.centro().getWorld().equals(p.getWorld()) && n.centro().distanceSquared(p.getLocation()) <= (double)12.25F) {
            return true;
         }
      }

      return false;
   }

   public static boolean nonPuoiSparare(Player p) {
      Long fino = (Long)ACCECATO_FINO.get(p.getUniqueId());
      if (fino != null && System.currentTimeMillis() < fino && !visore(p)) {
         HudArmi.avviso(p, "Non puoi sparare dentro a un fumogeno");
         return true;
      } else {
         return false;
      }
   }

   public static void avvia(JavaPlugin plugin) {
      Bukkit.getScheduler().runTaskTimer(plugin, () -> {
         long ora = System.currentTimeMillis();
         NUBI.removeIf((n) -> n.fineMs() <= ora);
         PotionEffectType cieco = eff("blindness");
         PotionEffectType notte = eff("night_vision");

         for(Player p : Bukkit.getOnlinePlayers()) {
            UUID id = p.getUniqueId();
            boolean conVisore = visore(p);
            if (notte != null) {
               if (conVisore) {
                  PotionEffect a = p.getPotionEffect(notte);
                  if (a == null || a.getDuration() != -1) {
                     p.addPotionEffect(new PotionEffect(notte, -1, 0, false, false, false));
                  }

                  CON_VISIONE.add(id);
               } else if (CON_VISIONE.remove(id)) {
                  p.removePotionEffect(notte);
               }
            }

            if (cieco != null) {
               if (!conVisore && dentroNube(p)) {
                  ACCECATO_FINO.put(id, ora + 3000L);
                  p.addPotionEffect(new PotionEffect(cieco, 70, 0, false, false, false));
               } else if (conVisore && ACCECATO_FINO.remove(id) != null) {
                  p.removePotionEffect(cieco);
               } else {
                  Long fino = (Long)ACCECATO_FINO.get(id);
                  if (fino != null && fino <= ora) {
                     ACCECATO_FINO.remove(id);
                  }
               }
            }
         }

      }, 20L, 5L);
   }

   private NamespacedKey chiave() {
      return new NamespacedKey(Main.getInstance(), "fumogeno_proiettile");
   }

   @EventHandler
   public void onLancio(PlayerInteractEvent event) {
      if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
         if (event.getHand() == EquipmentSlot.HAND) {
            Player p = event.getPlayer();
            ItemStack item = p.getInventory().getItemInMainHand();
            if (item.getItemMeta() != null && "fumogeno".equals(item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING))) {
               event.setCancelled(true);
               if (!AmmanettaListener.isAmmanettato(p.getUniqueId())) {
                  long adesso = System.currentTimeMillis();
                  long fine = (Long)COOLDOWN.getOrDefault(p.getUniqueId(), 0L);
                  if (adesso < fine) {
                     long sec = (fine - adesso + 999L) / 1000L;
                     HudArmi.avviso(p, "Fumogeno in ricarica: " + sec + "s");
                  } else {
                     COOLDOWN.put(p.getUniqueId(), adesso + 20000L);
                     ItemStack visuale = ItemFactory.createFumogeno();
                     p.launchProjectile(Snowball.class, p.getEyeLocation().getDirection().multiply(1.2), (s) -> {
                        s.setItem(visuale);
                        s.getPersistentDataContainer().set(this.chiave(), PersistentDataType.BYTE, (byte)1);
                     });
                     p.getWorld().playSound(p.getLocation(), "minecraft:entity.snowball.throw", 1.0F, 1.0F);
                     if (p.getGameMode() != GameMode.CREATIVE) {
                        item.setAmount(item.getAmount() - 1);
                        p.getInventory().setItemInMainHand(item.getAmount() <= 0 ? null : item);
                     }

                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void onImpatto(ProjectileHitEvent event) {
      Projectile var3 = event.getEntity();
      if (var3 instanceof Snowball s) {
         if (s.getPersistentDataContainer().has(this.chiave(), PersistentDataType.BYTE)) {
            Location pos = s.getLocation();
            event.setCancelled(true);
            s.remove();
            this.fumo(pos);
         }
      }
   }

   private Vector3f scalaPulsante() {
      float f = 2.5F * (0.92F + this.random.nextFloat() * 0.16F);
      return new Vector3f(f, f, f);
   }

   private void fumo(Location centro) {
      NUBI.add(new Nube(centro.clone(), System.currentTimeMillis() + 20000L));
      List<ItemDisplay> questi = new ArrayList();

      for(int i = 0; i < 3; ++i) {
         Location l = centro.clone().add((this.random.nextDouble() - (double)0.5F) * (double)2.0F * 1.2, 0.6 + this.random.nextDouble() * 0.4, (this.random.nextDouble() - (double)0.5F) * (double)2.0F * 1.2);
         l.setYaw(this.random.nextFloat() * 360.0F);
         ItemDisplay d = (ItemDisplay)centro.getWorld().spawn(l, ItemDisplay.class, (disp) -> {
            ItemStack corpetto = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta meta = (LeatherArmorMeta)corpetto.getItemMeta();
            meta.setCustomModelData(3);
            corpetto.setItemMeta(meta);
            disp.setItemStack(corpetto);
            disp.setBillboard(Billboard.FIXED);
            disp.setPersistent(false);
            disp.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(2.5F, 2.5F, 2.5F), new AxisAngle4f()));
         });
         questi.add(d);
         ATTIVI.add(d);
      }

      BukkitTask movimento = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
         for(ItemDisplay d : questi) {
            if (d.isValid()) {
               d.setInterpolationDelay(0);
               d.setInterpolationDuration(30);
               d.setTransformation(new Transformation(new Vector3f((float)((this.random.nextDouble() - (double)0.5F) * (double)2.0F * 0.9), (float)(0.6 + (this.random.nextDouble() - (double)0.5F) * 0.6), (float)((this.random.nextDouble() - (double)0.5F) * (double)2.0F * 0.9)), new AxisAngle4f((float)Math.toRadians((double)(this.random.nextInt(180) - 90)), 0.0F, 1.0F, 0.0F), this.scalaPulsante(), new AxisAngle4f()));
            }
         }

      }, 2L, 30L);
      Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
         movimento.cancel();

         for(ItemDisplay d : questi) {
            d.remove();
            ATTIVI.remove(d);
         }

      }, 400L);
   }

   public static void rimuoviTutti() {
      NUBI.clear();

      for(ItemDisplay d : ATTIVI) {
         d.remove();
      }

      ATTIVI.clear();
   }

   private static record Nube(Location centro, long fineMs) {
   }
}
