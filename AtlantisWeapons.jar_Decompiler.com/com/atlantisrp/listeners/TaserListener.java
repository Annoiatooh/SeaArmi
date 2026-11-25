package com.atlantisrp.listeners;

import com.atlantisrp.Main;
import com.atlantisrp.items.ItemFactory;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class TaserListener implements Listener {
   private static final double PORTATA = (double)10.0F;
   private static final long COOLDOWN_MS = 667L;
   private static final int STUN_TICK = 100;
   private static final int LENTEZZA_STUN = 6;
   private static final String SUONO_SPARO = "minecraft:weapons.taser";
   private static final String SUONO_VUOTO = "weapons.empty";
   private final Map<UUID, Integer> slotMira = new HashMap();
   private final Set<UUID> inMira = new HashSet();
   private final Map<UUID, Long> ultimoSparo = new HashMap();

   private static boolean isTaser(ItemStack item) {
      return item != null && item.getItemMeta() != null && "taser".equals(item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING));
   }

   private static boolean isMirino(ItemStack item) {
      return item != null && item.getItemMeta() != null && "mirino".equals(item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING));
   }

   private static int cariche(ItemStack t) {
      return (Integer)t.getItemMeta().getPersistentDataContainer().getOrDefault(ItemFactory.KEY_CARICHE, PersistentDataType.INTEGER, 0);
   }

   @EventHandler
   public void onInteract(PlayerInteractEvent event) {
      if (event.getHand() == EquipmentSlot.HAND) {
         Player p = event.getPlayer();
         PlayerInventory inv = p.getInventory();
         ItemStack t = inv.getItemInMainHand();
         if (isTaser(t)) {
            event.setCancelled(true);
            event.setUseItemInHand(Result.DENY);
            event.setUseInteractedBlock(Result.DENY);
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
               if (!AmmanettaListener.isAmmanettato(p.getUniqueId())) {
                  int c = cariche(t);
                  int mancanti = 4 - c;
                  if (mancanti > 0) {
                     int presi = 0;

                     for(int i = 0; i < inv.getSize() && presi < mancanti; ++i) {
                        ItemStack it = inv.getItem(i);
                        if (it != null && it.getItemMeta() != null && "dardo_taser".equals(it.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING))) {
                           int quanti = Math.min(it.getAmount(), mancanti - presi);
                           presi += quanti;
                           it.setAmount(it.getAmount() - quanti);
                           inv.setItem(i, it.getAmount() <= 0 ? null : it);
                        }
                     }

                     if (presi == 0) {
                        HudArmi.avviso(p, "Non hai dardi per il taser");
                     } else {
                        inv.setItemInMainHand(ItemFactory.taserStato(c + presi, true));
                        p.getWorld().playSound(p.getLocation(), "weapons.reloadmagin", SoundCategory.PLAYERS, 1.0F, 1.0F);
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void onSneak(PlayerToggleSneakEvent event) {
      Player player = event.getPlayer();
      PlayerInventory inv = player.getInventory();
      if (event.isSneaking()) {
         if (AmmanettaListener.isAmmanettato(player.getUniqueId())) {
            return;
         }

         ItemStack main = inv.getItemInMainHand();
         if (!isTaser(main)) {
            return;
         }

         ItemStack inSecondaMano = inv.getItemInOffHand();
         if (!inSecondaMano.getType().isAir()) {
            int libero = inv.firstEmpty();
            if (libero == -1) {
               return;
            }

            inv.setItem(libero, inSecondaMano);
            inv.setItemInOffHand((ItemStack)null);
         }

         int slot = inv.getHeldItemSlot();
         this.slotMira.put(player.getUniqueId(), slot);
         this.inMira.add(player.getUniqueId());
         inv.setItemInOffHand(ItemFactory.armaInMira(main, true));
         inv.setItem(slot, ItemFactory.createMirino());
      } else {
         this.terminaMira(player);
      }

   }

   private void terminaMira(Player player) {
      UUID uuid = player.getUniqueId();
      if (this.inMira.remove(uuid)) {
         Integer slot = (Integer)this.slotMira.remove(uuid);
         PlayerInventory inv = player.getInventory();
         ItemStack offhand = inv.getItemInOffHand();
         if (slot != null && isTaser(offhand)) {
            inv.setItem(slot, ItemFactory.armaInMira(offhand, false));
            inv.setItemInOffHand((ItemStack)null);
         }
      }
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent event) {
      this.terminaMira(event.getPlayer());
      this.ultimoSparo.remove(event.getPlayer().getUniqueId());
   }

   @EventHandler
   public void onDeath(PlayerDeathEvent event) {
      if (event.getKeepInventory()) {
         this.terminaMira(event.getEntity());
      } else {
         event.getDrops().removeIf(TaserListener::isMirino);
         this.inMira.remove(event.getEntity().getUniqueId());
         this.slotMira.remove(event.getEntity().getUniqueId());
      }

   }

   @EventHandler
   public void onSwing(PlayerAnimationEvent event) {
      Player p = event.getPlayer();
      UUID uuid = p.getUniqueId();
      if (this.inMira.contains(uuid)) {
         if (!AmmanettaListener.isAmmanettato(uuid)) {
            PlayerInventory inv = p.getInventory();
            ItemStack t = inv.getItemInOffHand();
            if (isTaser(t)) {
               long ora = System.currentTimeMillis();
               if (ora - (Long)this.ultimoSparo.getOrDefault(uuid, 0L) >= 667L) {
                  this.ultimoSparo.put(uuid, ora);
                  int c = cariche(t);
                  if (c <= 0) {
                     p.getWorld().playSound(p.getLocation(), "weapons.empty", SoundCategory.PLAYERS, 1.0F, 1.0F);
                  } else {
                     --c;
                     inv.setItemInOffHand(ItemFactory.armaInMira(ItemFactory.taserStato(c, c > 0), true));
                     p.getWorld().playSound(p.getLocation(), "minecraft:weapons.taser", SoundCategory.PLAYERS, 1.0F, 1.0F);
                     Location occhi = p.getEyeLocation();
                     Vector dir = occhi.getDirection();
                     double fino = (double)10.0F;
                     RayTraceResult muro = p.getWorld().rayTraceBlocks(occhi, dir, (double)10.0F);
                     if (muro != null) {
                        fino = occhi.toVector().distance(muro.getHitPosition());
                     }

                     RayTraceResult r = p.getWorld().rayTraceEntities(occhi, dir, fino, 0.3, (e) -> {
                        boolean var10000;
                        if (e instanceof Player altro) {
                           if (!altro.equals(p) && altro.getGameMode() != GameMode.SPECTATOR) {
                              var10000 = true;
                              return var10000;
                           }
                        }

                        var10000 = false;
                        return var10000;
                     });
                     Player colpito = null;
                     if (r != null) {
                        Entity var17 = r.getHitEntity();
                        if (var17 instanceof Player) {
                           Player bersaglio = (Player)var17;
                           colpito = bersaglio;
                           fino = occhi.toVector().distance(r.getHitPosition());
                        }
                     }

                     for(double d = 0.8; d < fino; d += 0.4) {
                        Location l = occhi.clone().add(dir.clone().multiply(d)).subtract((double)0.0F, 0.15, (double)0.0F);
                        p.getWorld().spawnParticle(Particle.WHITE_SMOKE, l, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
                     }

                     if (colpito != null) {
                        this.stordisci(colpito);
                     }

                  }
               }
            }
         }
      }
   }

   private void stordisci(Player bersaglio) {
      PotionEffectType lentezza = PotionEffectType.getByKey(NamespacedKey.minecraft("slowness"));
      if (lentezza != null) {
         bersaglio.addPotionEffect(new PotionEffect(lentezza, 100, 6, false, false, false));
      }

      PotionEffectType cieco = PotionEffectType.getByKey(NamespacedKey.minecraft("blindness"));
      if (cieco != null) {
         bersaglio.addPotionEffect(new PotionEffect(cieco, 100, 0, false, false, false));
      }

      bersaglio.showTitle(Title.title(Component.text("⚡ ZzZzZ ⚡", NamedTextColor.YELLOW), Component.text("Hai ricevuto una scossa elettrica!", NamedTextColor.WHITE), Times.times(Duration.ZERO, Duration.ofMillis(5000L), Duration.ofMillis(300L))));
      Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
         if (bersaglio.isOnline()) {
            AmmanettaListener.riapplicaEffetti(bersaglio);
         }

      }, 102L);
   }

   @EventHandler
   public void onPugno(EntityDamageByEntityEvent event) {
      Entity var3 = event.getDamager();
      if (var3 instanceof Player p) {
         if (isTaser(p.getInventory().getItemInMainHand()) || this.inMira.contains(p.getUniqueId())) {
            event.setCancelled(true);
         }
      }

   }
}
