/*     */ package com.atlantisrp.listeners;
/*     */ 
/*     */ import com.atlantisrp.Main;
/*     */ import com.atlantisrp.items.ItemFactory;
/*     */ import java.time.Duration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import net.kyori.adventure.text.format.NamedTextColor;
/*     */ import net.kyori.adventure.text.format.TextColor;
/*     */ import net.kyori.adventure.title.Title;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.Particle;
/*     */ import org.bukkit.SoundCategory;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Event;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.PlayerDeathEvent;
/*     */ import org.bukkit.event.player.PlayerAnimationEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.event.player.PlayerToggleSneakEvent;
/*     */ import org.bukkit.inventory.EquipmentSlot;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.persistence.PersistentDataType;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.potion.PotionEffect;
/*     */ import org.bukkit.potion.PotionEffectType;
/*     */ import org.bukkit.util.RayTraceResult;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaserListener
/*     */   implements Listener
/*     */ {
/*     */   private static final double PORTATA = 10.0D;
/*     */   private static final long COOLDOWN_MS = 667L;
/*     */   private static final int STUN_TICK = 100;
/*     */   private static final int LENTEZZA_STUN = 6;
/*     */   private static final String SUONO_SPARO = "minecraft:weapons.taser";
/*     */   private static final String SUONO_VUOTO = "weapons.empty";
/*  55 */   private final Map<UUID, Integer> slotMira = new HashMap<>();
/*  56 */   private final Set<UUID> inMira = new HashSet<>();
/*  57 */   private final Map<UUID, Long> ultimoSparo = new HashMap<>();
/*     */   
/*     */   private static boolean isTaser(ItemStack item) {
/*  60 */     return (item != null && item.getItemMeta() != null && "taser".equals(item.getItemMeta()
/*  61 */         .getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING)));
/*     */   }
/*     */   
/*     */   private static boolean isMirino(ItemStack item) {
/*  65 */     return (item != null && item.getItemMeta() != null && "mirino".equals(item.getItemMeta()
/*  66 */         .getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING)));
/*     */   }
/*     */   
/*     */   private static int cariche(ItemStack t) {
/*  70 */     return ((Integer)t.getItemMeta().getPersistentDataContainer()
/*  71 */       .getOrDefault(ItemFactory.KEY_CARICHE, PersistentDataType.INTEGER, Integer.valueOf(0))).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onInteract(PlayerInteractEvent event) {
/*  77 */     if (event.getHand() != EquipmentSlot.HAND)
/*  78 */       return;  Player p = event.getPlayer();
/*  79 */     PlayerInventory inv = p.getInventory();
/*  80 */     ItemStack t = inv.getItemInMainHand();
/*  81 */     if (!isTaser(t))
/*     */       return; 
/*  83 */     event.setCancelled(true);
/*  84 */     event.setUseItemInHand(Event.Result.DENY);
/*  85 */     event.setUseInteractedBlock(Event.Result.DENY);
/*  86 */     if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
/*  87 */       return;  if (AmmanettaListener.isAmmanettato(p.getUniqueId()))
/*     */       return; 
/*  89 */     int c = cariche(t);
/*  90 */     int mancanti = 4 - c;
/*  91 */     if (mancanti <= 0)
/*     */       return; 
/*  93 */     int presi = 0;
/*  94 */     for (int i = 0; i < inv.getSize() && presi < mancanti; i++) {
/*  95 */       ItemStack it = inv.getItem(i);
/*  96 */       if (it != null && it.getItemMeta() != null && "dardo_taser".equals(it.getItemMeta()
/*  97 */           .getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING))) {
/*  98 */         int quanti = Math.min(it.getAmount(), mancanti - presi);
/*  99 */         presi += quanti;
/* 100 */         it.setAmount(it.getAmount() - quanti);
/* 101 */         inv.setItem(i, (it.getAmount() <= 0) ? null : it);
/*     */       } 
/* 103 */     }  if (presi == 0) {
/* 104 */       HudArmi.avviso(p, "Non hai dardi per il taser");
/*     */       return;
/*     */     } 
/* 107 */     inv.setItemInMainHand(ItemFactory.taserStato(c + presi, true));
/* 108 */     p.getWorld().playSound(p.getLocation(), "weapons.reloadmagin", SoundCategory.PLAYERS, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onSneak(PlayerToggleSneakEvent event) {
/* 114 */     Player player = event.getPlayer();
/* 115 */     PlayerInventory inv = player.getInventory();
/*     */     
/* 117 */     if (event.isSneaking()) {
/* 118 */       if (AmmanettaListener.isAmmanettato(player.getUniqueId()))
/* 119 */         return;  ItemStack main = inv.getItemInMainHand();
/* 120 */       if (!isTaser(main)) {
/*     */         return;
/*     */       }
/* 123 */       ItemStack inSecondaMano = inv.getItemInOffHand();
/* 124 */       if (!inSecondaMano.getType().isAir()) {
/* 125 */         int libero = inv.firstEmpty();
/* 126 */         if (libero == -1)
/* 127 */           return;  inv.setItem(libero, inSecondaMano);
/* 128 */         inv.setItemInOffHand(null);
/*     */       } 
/*     */       
/* 131 */       int slot = inv.getHeldItemSlot();
/* 132 */       this.slotMira.put(player.getUniqueId(), Integer.valueOf(slot));
/* 133 */       this.inMira.add(player.getUniqueId());
/*     */       
/* 135 */       inv.setItemInOffHand(ItemFactory.armaInMira(main, true));
/* 136 */       inv.setItem(slot, ItemFactory.createMirino());
/*     */     } else {
/* 138 */       terminaMira(player);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void terminaMira(Player player) {
/* 143 */     UUID uuid = player.getUniqueId();
/* 144 */     if (!this.inMira.remove(uuid))
/*     */       return; 
/* 146 */     Integer slot = this.slotMira.remove(uuid);
/* 147 */     PlayerInventory inv = player.getInventory();
/* 148 */     ItemStack offhand = inv.getItemInOffHand();
/* 149 */     if (slot == null || !isTaser(offhand))
/*     */       return; 
/* 151 */     inv.setItem(slot.intValue(), ItemFactory.armaInMira(offhand, false));
/* 152 */     inv.setItemInOffHand(null);
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onQuit(PlayerQuitEvent event) {
/* 157 */     terminaMira(event.getPlayer());
/* 158 */     this.ultimoSparo.remove(event.getPlayer().getUniqueId());
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onDeath(PlayerDeathEvent event) {
/* 163 */     if (event.getKeepInventory()) {
/* 164 */       terminaMira(event.getEntity());
/*     */     } else {
/* 166 */       event.getDrops().removeIf(TaserListener::isMirino);
/* 167 */       this.inMira.remove(event.getEntity().getUniqueId());
/* 168 */       this.slotMira.remove(event.getEntity().getUniqueId());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onSwing(PlayerAnimationEvent event) {
/* 175 */     Player p = event.getPlayer();
/* 176 */     UUID uuid = p.getUniqueId();
/* 177 */     if (!this.inMira.contains(uuid))
/* 178 */       return;  if (AmmanettaListener.isAmmanettato(uuid))
/*     */       return; 
/* 180 */     PlayerInventory inv = p.getInventory();
/* 181 */     ItemStack t = inv.getItemInOffHand();
/* 182 */     if (!isTaser(t))
/*     */       return; 
/* 184 */     long ora = System.currentTimeMillis();
/* 185 */     if (ora - ((Long)this.ultimoSparo.getOrDefault(uuid, Long.valueOf(0L))).longValue() < 667L)
/* 186 */       return;  this.ultimoSparo.put(uuid, Long.valueOf(ora));
/*     */     
/* 188 */     int c = cariche(t);
/* 189 */     if (c <= 0) {
/* 190 */       p.getWorld().playSound(p.getLocation(), "weapons.empty", SoundCategory.PLAYERS, 1.0F, 1.0F);
/*     */       
/*     */       return;
/*     */     } 
/* 194 */     c--;
/* 195 */     inv.setItemInOffHand(ItemFactory.armaInMira(ItemFactory.taserStato(c, (c > 0)), true));
/* 196 */     p.getWorld().playSound(p.getLocation(), "minecraft:weapons.taser", SoundCategory.PLAYERS, 1.0F, 1.0F);
/*     */     
/* 198 */     Location occhi = p.getEyeLocation();
/* 199 */     Vector dir = occhi.getDirection();
/*     */ 
/*     */     
/* 202 */     double fino = 10.0D;
/* 203 */     RayTraceResult muro = p.getWorld().rayTraceBlocks(occhi, dir, 10.0D);
/* 204 */     if (muro != null) fino = occhi.toVector().distance(muro.getHitPosition());
/*     */     
/* 206 */     RayTraceResult r = p.getWorld().rayTraceEntities(occhi, dir, fino, 0.3D, e -> { if (e instanceof Player) { Player altro = (Player)e; if (!altro.equals(p) && altro.getGameMode() != GameMode.SPECTATOR); }
/*     */            return false;
/* 208 */         }); Player colpito = null;
/* 209 */     if (r != null) { Entity entity = r.getHitEntity(); if (entity instanceof Player) { Player bersaglio = (Player)entity;
/* 210 */         colpito = bersaglio;
/* 211 */         fino = occhi.toVector().distance(r.getHitPosition()); }
/*     */        }
/*     */     
/*     */     double d;
/* 215 */     for (d = 0.8D; d < fino; d += 0.4D) {
/* 216 */       Location l = occhi.clone().add(dir.clone().multiply(d)).subtract(0.0D, 0.15D, 0.0D);
/* 217 */       p.getWorld().spawnParticle(Particle.WHITE_SMOKE, l, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */     
/* 220 */     if (colpito != null) stordisci(colpito); 
/*     */   }
/*     */   
/*     */   private void stordisci(Player bersaglio) {
/* 224 */     PotionEffectType lentezza = PotionEffectType.getByKey(NamespacedKey.minecraft("slowness"));
/* 225 */     if (lentezza != null) {
/* 226 */       bersaglio.addPotionEffect(new PotionEffect(lentezza, 100, 6, false, false, false));
/*     */     }
/* 228 */     PotionEffectType cieco = PotionEffectType.getByKey(NamespacedKey.minecraft("blindness"));
/* 229 */     if (cieco != null) {
/* 230 */       bersaglio.addPotionEffect(new PotionEffect(cieco, 100, 0, false, false, false));
/*     */     }
/* 232 */     bersaglio.showTitle(Title.title(
/* 233 */           (Component)Component.text("⚡ ZzZzZ ⚡", (TextColor)NamedTextColor.YELLOW), 
/* 234 */           (Component)Component.text("Hai ricevuto una scossa elettrica!", (TextColor)NamedTextColor.WHITE), 
/* 235 */           Title.Times.times(Duration.ZERO, Duration.ofMillis(5000L), Duration.ofMillis(300L))));
/*     */ 
/*     */     
/* 238 */     Bukkit.getScheduler().runTaskLater((Plugin)Main.getInstance(), () -> { if (bersaglio.isOnline()) AmmanettaListener.riapplicaEffetti(bersaglio);  }102L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onPugno(EntityDamageByEntityEvent event) {
/* 246 */     Entity entity = event.getDamager(); if (entity instanceof Player) { Player p = (Player)entity; if (isTaser(p.getInventory().getItemInMainHand()) || this.inMira
/* 247 */         .contains(p.getUniqueId()))
/* 248 */         event.setCancelled(true);  }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\listeners\TaserListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */