package com.atlantisrp.listeners;

import com.atlantisrp.Main;
import com.atlantisrp.items.Bossolo;
import com.atlantisrp.items.ItemFactory;
import com.atlantisrp.items.Munizione;
import com.atlantisrp.items.Proiettile;
import com.atlantisrp.items.Sparo;
import com.atlantisrp.items.StatoArmi;
import com.atlantisrp.items.TipoArma;
import com.atlantisrp.items.Vetri;
import com.atlantisrp.logs.LogArmi;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class AUGListener implements Listener {
   private static final TipoArma TIPO;
   private static final long COOLDOWN_MS = 500L;
   private static final String SOUND_VUOTO = "weapons.empty";
   private static final String SOUND_RICARICA_COLPO = "weapons.reloadbullet";
   private static final String SOUND_INNESTO = "weapons.reloadmagin";
   private static final String SOUND_ESTRAZIONE = "weapons.reloadmagout";
   private static final String SOUND_SCARICA_COLPO = "weapons.reloadbullet";
   private static final long RICARICA_MS = 200L;
   private final Map<UUID, Integer> slotMira = new HashMap();
   private final Set<UUID> inMira = new HashSet();
   private final Set<UUID> stannoRicaricando = new HashSet();
   private final Map<UUID, Long> ultimoSparo = new HashMap();
   private final Map<UUID, BukkitTask> raffica = new HashMap();
   private static final int SPARATO = 0;
   private static final int VUOTO = 1;
   private static final int NIENTE = 2;

   private String tipoId(ItemStack item) {
      return item != null && item.getItemMeta() != null ? (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING) : null;
   }

   private TipoArma arma(ItemStack item) {
      return TipoArma.da(this.tipoId(item));
   }

   private TipoArma armaDelCaricatore(ItemStack item) {
      if (!"caricatore".equals(this.tipoId(item))) {
         return null;
      } else {
         String a = (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_ARMA_CARICATORE, PersistentDataType.STRING);
         return TipoArma.da(a);
      }
   }

   private boolean isMirino(ItemStack item) {
      return "mirino".equals(this.tipoId(item));
   }

   private int munizioniDi(ItemMeta meta) {
      return (Integer)meta.getPersistentDataContainer().getOrDefault(ItemFactory.KEY_MUNIZIONI, PersistentDataType.INTEGER, 0);
   }

   @EventHandler
   public void onInteract(PlayerInteractEvent event) {
      if (event.getHand() == EquipmentSlot.HAND) {
         Player player = event.getPlayer();
         ItemStack main = player.getInventory().getItemInMainHand();
         TipoArma arma = this.arma(main) == TIPO ? TIPO : null;
         TipoArma armaCaricatore = this.armaDelCaricatore(main) == TIPO ? TIPO : null;
         boolean mirino = this.isMirino(main);
         if (arma != null || armaCaricatore != null || mirino) {
            event.setCancelled(true);
            event.setUseItemInHand(Result.DENY);
            event.setUseInteractedBlock(Result.DENY);
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
               if (arma != null) {
                  ItemStack offhand = player.getInventory().getItemInOffHand();
                  if (this.armaDelCaricatore(offhand) == arma) {
                     this.innestaCaricatore(player, arma);
                  } else {
                     this.estraiCaricatore(player, arma);
                  }
               } else if (armaCaricatore != null) {
                  this.ricaricaCaricatore(player, main, armaCaricatore);
               }

            }
         }
      }
   }

   private void estraiCaricatore(Player player, TipoArma tipo) {
      PlayerInventory inv = player.getInventory();
      ItemStack arma = inv.getItemInMainHand();
      StatoArmi.Stato stato = StatoArmi.di(arma);
      if (stato.haCaricatore) {
         this.daiOLascia(player, ItemFactory.createCaricatore(tipo, stato.munizioni));
         stato.munizioni = 0;
         stato.haCaricatore = false;
         player.getWorld().playSound(player.getLocation(), "weapons.reloadmagout", SoundCategory.PLAYERS, 1.0F, 1.0F);
      }
   }

   private void innestaCaricatore(Player player, TipoArma tipo) {
      PlayerInventory inv = player.getInventory();
      ItemStack arma = inv.getItemInMainHand();
      ItemStack caricatore = inv.getItemInOffHand();
      if (this.armaDelCaricatore(caricatore) == tipo) {
         StatoArmi.Stato stato = StatoArmi.di(arma);
         stato.munizioni = this.munizioniDi(caricatore.getItemMeta());
         stato.haCaricatore = true;
         inv.setItemInOffHand((ItemStack)null);
         player.getWorld().playSound(player.getLocation(), "weapons.reloadmagin", SoundCategory.PLAYERS, 1.0F, 1.0F);
      }
   }

   private void daiOLascia(Player player, ItemStack item) {
      player.getInventory().addItem(new ItemStack[]{item}).values().forEach((resto) -> player.getWorld().dropItemNaturally(player.getLocation(), resto));
   }

   private void ricaricaCaricatore(final Player player, ItemStack caricatore, final TipoArma tipo) {
      final UUID uuid = player.getUniqueId();
      if (!this.stannoRicaricando.contains(uuid)) {
         int capacita = tipo.getCapacita();
         final Munizione tipoMunizione = tipo.getMunizione();
         int munizioniAttuali = this.munizioniDi(caricatore.getItemMeta());
         int munizioniDisponibili = this.contaMunizioni(player, tipoMunizione);
         if (munizioniAttuali < capacita && munizioniDisponibili != 0) {
            final int slotCaricatore = player.getInventory().getHeldItemSlot();
            final int daCaricare = Math.min(capacita - munizioniAttuali, munizioniDisponibili);
            this.stannoRicaricando.add(uuid);
            (new BukkitRunnable() {
               int caricati = 0;

               public void run() {
                  ItemStack itemAttuale = player.getInventory().getItem(slotCaricatore);
                  if (player.isOnline() && player.getInventory().getHeldItemSlot() == slotCaricatore && AUGListener.this.armaDelCaricatore(itemAttuale) == tipo && this.caricati < daCaricare) {
                     if (!AUGListener.this.rimuoviUnaMunizione(player, tipoMunizione)) {
                        AUGListener.this.stannoRicaricando.remove(uuid);
                        this.cancel();
                     } else {
                        ItemMeta m = itemAttuale.getItemMeta();
                        int nuove = AUGListener.this.munizioniDi(m) + 1;
                        m.getPersistentDataContainer().set(ItemFactory.KEY_MUNIZIONI, PersistentDataType.INTEGER, nuove);
                        itemAttuale.setItemMeta(m);
                        player.getInventory().setItem(slotCaricatore, itemAttuale);
                        player.getWorld().playSound(player.getLocation(), "weapons.reloadbullet", SoundCategory.PLAYERS, 1.0F, 1.0F);
                        ++this.caricati;
                        if (this.caricati >= daCaricare) {
                           AUGListener.this.stannoRicaricando.remove(uuid);
                           this.cancel();
                        }

                     }
                  } else {
                     AUGListener.this.stannoRicaricando.remove(uuid);
                     this.cancel();
                  }
               }
            }).runTaskTimer(Main.getInstance(), 0L, this.msToTick(200L));
         } else {
            this.scaricaCaricatore(player, caricatore, tipo);
         }
      }
   }

   private void scaricaCaricatore(final Player player, ItemStack caricatore, final TipoArma tipo) {
      final UUID uuid = player.getUniqueId();
      if (!this.stannoRicaricando.contains(uuid)) {
         final int munizioni = this.munizioniDi(caricatore.getItemMeta());
         if (munizioni > 0) {
            final Munizione tipoMunizione = tipo.getMunizione();
            final int slotCaricatore = player.getInventory().getHeldItemSlot();
            this.stannoRicaricando.add(uuid);
            (new BukkitRunnable() {
               int scaricati = 0;
               final int totale = munizioni;

               public void run() {
                  ItemStack itemAttuale = player.getInventory().getItem(slotCaricatore);
                  if (player.isOnline() && player.getInventory().getHeldItemSlot() == slotCaricatore && AUGListener.this.armaDelCaricatore(itemAttuale) == tipo && this.scaricati < this.totale) {
                     ItemMeta m = itemAttuale.getItemMeta();
                     int munizioniOra = AUGListener.this.munizioniDi(m);
                     if (munizioniOra <= 0) {
                        AUGListener.this.stannoRicaricando.remove(uuid);
                        this.cancel();
                     } else {
                        m.getPersistentDataContainer().set(ItemFactory.KEY_MUNIZIONI, PersistentDataType.INTEGER, munizioniOra - 1);
                        itemAttuale.setItemMeta(m);
                        player.getInventory().setItem(slotCaricatore, itemAttuale);
                        AUGListener.this.daiOLascia(player, ItemFactory.createMunizioni(tipoMunizione, 1));
                        player.getWorld().playSound(player.getLocation(), "weapons.reloadbullet", SoundCategory.PLAYERS, 1.0F, 1.0F);
                        ++this.scaricati;
                        if (this.scaricati >= this.totale) {
                           AUGListener.this.stannoRicaricando.remove(uuid);
                           this.cancel();
                        }

                     }
                  } else {
                     AUGListener.this.stannoRicaricando.remove(uuid);
                     this.cancel();
                  }
               }
            }).runTaskTimer(Main.getInstance(), 0L, this.msToTick(200L));
         }
      }
   }

   private long msToTick(long ms) {
      return Math.max(1L, ms / 50L);
   }

   private int contaMunizioni(Player player, Munizione tipo) {
      int totale = 0;

      for(ItemStack item : player.getInventory().getContents()) {
         if (item != null && tipo.getId().equals(this.tipoId(item))) {
            totale += item.getAmount();
         }
      }

      return totale;
   }

   private boolean rimuoviUnaMunizione(Player player, Munizione tipo) {
      for(ItemStack item : player.getInventory().getContents()) {
         if (item != null && item.getAmount() > 0 && tipo.getId().equals(this.tipoId(item))) {
            item.setAmount(item.getAmount() - 1);
            return true;
         }
      }

      return false;
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
         if (this.arma(main) != TIPO) {
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
      this.fermaRaffica(uuid);
      if (this.inMira.remove(uuid)) {
         Integer slot = (Integer)this.slotMira.remove(uuid);
         PlayerInventory inv = player.getInventory();
         ItemStack offhand = inv.getItemInOffHand();
         if (slot != null && this.arma(offhand) != null) {
            inv.setItem(slot, ItemFactory.armaInMira(offhand, false));
            inv.setItemInOffHand((ItemStack)null);
         }
      }
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent event) {
      this.terminaMira(event.getPlayer());
      UUID uuid = event.getPlayer().getUniqueId();
      this.stannoRicaricando.remove(uuid);
      this.ultimoSparo.remove(uuid);
   }

   @EventHandler
   public void onDeath(PlayerDeathEvent event) {
      if (event.getKeepInventory()) {
         this.terminaMira(event.getEntity());
      } else {
         event.getDrops().removeIf(this::isMirino);
         this.fermaRaffica(event.getEntity().getUniqueId());
         this.inMira.remove(event.getEntity().getUniqueId());
         this.slotMira.remove(event.getEntity().getUniqueId());
      }

   }

   @EventHandler
   public void onSwing(PlayerAnimationEvent event) {
      Player player = event.getPlayer();
      if (this.inMira.contains(player.getUniqueId())) {
         if (TIPO.isAutomatica()) {
            this.avviaRaffica(player);
         } else {
            this.sparaUnColpo(player, false);
         }

      }
   }

   private void avviaRaffica(final Player player) {
      final UUID uuid = player.getUniqueId();
      if (!this.raffica.containsKey(uuid)) {
         long periodo = Math.max(1L, Math.round((double)10.0F));
         long trascorso = System.currentTimeMillis() - (Long)this.ultimoSparo.getOrDefault(uuid, 0L);
         long attesa = Math.max(0L, (long)Math.ceil((double)(500L - trascorso) / (double)50.0F));
         BukkitTask task = (new BukkitRunnable() {
            public void run() {
               if (player.isOnline() && player.isSneaking() && AUGListener.this.inMira.contains(uuid) && AUGListener.this.arma(player.getInventory().getItemInOffHand()) == AUGListener.TIPO) {
                  if (AUGListener.this.sparaUnColpo(player, true) == 1) {
                     AUGListener.this.fermaRaffica(uuid);
                  }

               } else {
                  AUGListener.this.fermaRaffica(uuid);
               }
            }
         }).runTaskTimer(Main.getInstance(), attesa, periodo);
         this.raffica.put(uuid, task);
      }
   }

   private void fermaRaffica(UUID uuid) {
      BukkitTask task = (BukkitTask)this.raffica.remove(uuid);
      if (task != null) {
         task.cancel();
      }

   }

   private int sparaUnColpo(Player player, boolean inRaffica) {
      UUID uuid = player.getUniqueId();
      if (!this.inMira.contains(uuid)) {
         return 2;
      } else if (AmmanettaListener.isAmmanettato(uuid)) {
         return 2;
      } else if (FumogenoListener.nonPuoiSparare(player)) {
         return 2;
      } else {
         ItemStack armaItem = player.getInventory().getItemInOffHand();
         TipoArma tipo = this.arma(armaItem);
         if (tipo != TIPO) {
            return 2;
         } else {
            long ora = System.currentTimeMillis();
            if (!inRaffica && ora - (Long)this.ultimoSparo.getOrDefault(uuid, 0L) < 500L) {
               return 2;
            } else {
               StatoArmi.Stato stato = StatoArmi.di(armaItem);
               if (stato.haCaricatore && stato.munizioni > 0) {
                  this.ultimoSparo.put(uuid, ora);
                  --stato.munizioni;
                  this.spara(player, tipo);
                  return 0;
               } else {
                  player.getWorld().playSound(player.getLocation(), "weapons.empty", SoundCategory.PLAYERS, 1.0F, 1.0F);
                  this.ultimoSparo.put(uuid, ora);
                  return 1;
               }
            }
         }
      }
   }

   private void spara(Player player, TipoArma tipo) {
      Location loc = player.getEyeLocation();
      Vector dir = loc.getDirection().normalize();
      double gittata = tipo.getGittata();
      player.getWorld().playSound(loc, tipo.getSuonoSparo(), SoundCategory.PLAYERS, 1.0F, 1.0F);
      Bossolo.espelli(player);
      Vetri.colpisci(player, tipo, loc, dir, gittata);
      RayTraceResult result = player.getWorld().rayTraceEntities(loc, dir, gittata, 0.3, (entity) -> entity instanceof LivingEntity && !entity.equals(player));
      double distanzaVolo = result != null ? result.getHitPosition().distance(loc.toVector()) : gittata;
      Proiettile.lancia(loc, dir, distanzaVolo);
      if (result != null) {
         Entity var11 = result.getHitEntity();
         if (var11 instanceof LivingEntity) {
            LivingEntity target = (LivingEntity)var11;
            boolean headshot = this.isHeadshot(target, result.getHitPosition());
            Vector velocitaPrima = target.getVelocity();
            double danno = headshot ? tipo.getDannoTesta() : tipo.getDannoCorpo();
            if (target instanceof Player) {
               Player vittima = (Player)target;
               LogArmi.colpo(player, vittima, tipo);
            }

            Sparo.inCorso = true;

            try {
               target.setNoDamageTicks(0);
               target.damage(com.atlantisrp.items.Giubbotto.danno(target, tipo, headshot, danno), player);
            } finally {
               Sparo.inCorso = false;
            }

            target.setVelocity(velocitaPrima);
         }
      }

   }

   private boolean isHeadshot(LivingEntity target, Vector hitPosition) {
      double altezzaImpatto = hitPosition.getY() - target.getLocation().getY();
      return altezzaImpatto >= target.getHeight() * 0.85;
   }

   static {
      TIPO = TipoArma.AUG;
   }
}
