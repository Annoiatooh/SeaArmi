/*     */ package com.atlantisrp.listeners;
/*     */ 
/*     */ import com.atlantisrp.Main;
/*     */ import com.atlantisrp.items.ItemFactory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.entity.Display;
/*     */ import org.bukkit.entity.ItemDisplay;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.entity.Projectile;
/*     */ import org.bukkit.entity.Snowball;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.entity.ProjectileHitEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.inventory.EquipmentSlot;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.LeatherArmorMeta;
/*     */ import org.bukkit.persistence.PersistentDataType;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.potion.PotionEffect;
/*     */ import org.bukkit.potion.PotionEffectType;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ import org.bukkit.util.Transformation;
/*     */ import org.joml.AxisAngle4f;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ 
/*     */ public class FumogenoListener
/*     */   implements Listener
/*     */ {
/*     */   private static final int DURATA_TICK = 400;
/*     */   private static final int CMD_FUMO = 3;
/*     */   private static final int QUANTI = 3;
/*     */   private static final float SCALA = 1.0F;
/*     */   private static final float SCALA_FINE = 2.5F;
/*     */   private static final int MOVIMENTO_TICK = 30;
/*     */   private static final double DERIVA = 0.9D;
/*     */   private static final double SALITA = 0.6D;
/*     */   private static final double DISPERSIONE = 1.2D;
/*     */   private static final double VELOCITA = 1.2D;
/*     */   private static final long COOLDOWN_MS = 20000L;
/*     */   private static final double RAGGIO = 3.5D;
/*     */   private static final int CIECITA_DOPO_USCITA_TICK = 60;
/*  59 */   private static final Map<UUID, Long> COOLDOWN = new HashMap<>();
/*  60 */   private static final Map<UUID, Long> ACCECATO_FINO = new HashMap<>();
/*  61 */   private static final Set<UUID> CON_VISIONE = new HashSet<>(); private static final class Nube extends Record {
/*     */     private final Location centro; private final long fineMs;
/*  63 */     private Nube(Location centro, long fineMs) { this.centro = centro; this.fineMs = fineMs; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/atlantisrp/listeners/FumogenoListener$Nube;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #63	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/atlantisrp/listeners/FumogenoListener$Nube; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/atlantisrp/listeners/FumogenoListener$Nube;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #63	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/atlantisrp/listeners/FumogenoListener$Nube; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/atlantisrp/listeners/FumogenoListener$Nube;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #63	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/atlantisrp/listeners/FumogenoListener$Nube;
/*  63 */       //   0	8	1	o	Ljava/lang/Object; } public Location centro() { return this.centro; } public long fineMs() { return this.fineMs; }
/*  64 */      } private static final List<Nube> NUBI = new ArrayList<>();
/*     */   
/*     */   private static PotionEffectType eff(String nome) {
/*  67 */     return PotionEffectType.getByKey(NamespacedKey.minecraft(nome));
/*     */   }
/*     */   
/*     */   private static boolean visore(Player p) {
/*  71 */     ItemStack h = p.getInventory().getHelmet();
/*  72 */     return (h != null && h.getItemMeta() != null && "visore".equals(h.getItemMeta()
/*  73 */         .getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING)));
/*     */   }
/*     */   
/*     */   private static boolean dentroNube(Player p) {
/*  77 */     long ora = System.currentTimeMillis();
/*  78 */     for (Nube n : NUBI) {
/*  79 */       if (n.fineMs() > ora && n.centro().getWorld().equals(p.getWorld()) && n
/*  80 */         .centro().distanceSquared(p.getLocation()) <= 12.25D) return true; 
/*     */     } 
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean nonPuoiSparare(Player p) {
/*  87 */     Long fino = ACCECATO_FINO.get(p.getUniqueId());
/*  88 */     if (fino == null || System.currentTimeMillis() >= fino.longValue() || visore(p)) return false; 
/*  89 */     HudArmi.avviso(p, "Non puoi sparare dentro a un fumogeno");
/*  90 */     return true;
/*     */   }
/*     */   
/*     */   public static void avvia(JavaPlugin plugin) {
/*  94 */     Bukkit.getScheduler().runTaskTimer((Plugin)plugin, () -> { long ora = System.currentTimeMillis(); NUBI.removeIf(()); PotionEffectType cieco = eff("blindness"); PotionEffectType notte = eff("night_vision"); for (Player p : Bukkit.getOnlinePlayers()) { UUID id = p.getUniqueId(); boolean conVisore = visore(p); if (notte != null) if (conVisore) { PotionEffect a = p.getPotionEffect(notte); if (a == null || a.getDuration() != -1) p.addPotionEffect(new PotionEffect(notte, -1, 0, false, false, false));  CON_VISIONE.add(id); } else if (CON_VISIONE.remove(id)) { p.removePotionEffect(notte); }   if (cieco == null) continue;  if (!conVisore && dentroNube(p)) { ACCECATO_FINO.put(id, Long.valueOf(ora + 3000L)); p.addPotionEffect(new PotionEffect(cieco, 70, 0, false, false, false)); continue; }  if (conVisore && ACCECATO_FINO.remove(id) != null) { p.removePotionEffect(cieco); continue; }  Long fino = ACCECATO_FINO.get(id); if (fino != null && fino.longValue() <= ora) ACCECATO_FINO.remove(id);  }  }20L, 5L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   private static final List<ItemDisplay> ATTIVI = new ArrayList<>();
/* 132 */   private final Random random = new Random();
/*     */   
/*     */   private NamespacedKey chiave() {
/* 135 */     return new NamespacedKey((Plugin)Main.getInstance(), "fumogeno_proiettile");
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onLancio(PlayerInteractEvent event) {
/* 140 */     if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
/* 141 */       return;  if (event.getHand() != EquipmentSlot.HAND)
/*     */       return; 
/* 143 */     Player p = event.getPlayer();
/* 144 */     ItemStack item = p.getInventory().getItemInMainHand();
/* 145 */     if (item.getItemMeta() == null || !"fumogeno".equals(item.getItemMeta().getPersistentDataContainer()
/* 146 */         .get(ItemFactory.KEY_TIPO, PersistentDataType.STRING)))
/*     */       return; 
/* 148 */     event.setCancelled(true);
/* 149 */     if (AmmanettaListener.isAmmanettato(p.getUniqueId()))
/*     */       return; 
/* 151 */     long adesso = System.currentTimeMillis();
/* 152 */     long fine = ((Long)COOLDOWN.getOrDefault(p.getUniqueId(), Long.valueOf(0L))).longValue();
/* 153 */     if (adesso < fine) {
/* 154 */       long sec = (fine - adesso + 999L) / 1000L;
/* 155 */       HudArmi.avviso(p, "Fumogeno in ricarica: " + sec + "s");
/*     */       return;
/*     */     } 
/* 158 */     COOLDOWN.put(p.getUniqueId(), Long.valueOf(adesso + 20000L));
/*     */     
/* 160 */     ItemStack visuale = ItemFactory.createFumogeno();
/* 161 */     p.launchProjectile(Snowball.class, p.getEyeLocation().getDirection().multiply(1.2D), s -> {
/*     */           s.setItem(visuale);
/*     */           s.getPersistentDataContainer().set(chiave(), PersistentDataType.BYTE, Byte.valueOf((byte)1));
/*     */         });
/* 165 */     p.getWorld().playSound(p.getLocation(), "minecraft:entity.snowball.throw", 1.0F, 1.0F);
/*     */     
/* 167 */     if (p.getGameMode() != GameMode.CREATIVE) {
/* 168 */       item.setAmount(item.getAmount() - 1);
/* 169 */       p.getInventory().setItemInMainHand((item.getAmount() <= 0) ? null : item);
/*     */     } 
/*     */   }
/*     */   @EventHandler
/*     */   public void onImpatto(ProjectileHitEvent event) {
/*     */     Snowball s;
/* 175 */     Projectile projectile = event.getEntity(); if (projectile instanceof Snowball) { s = (Snowball)projectile; } else { return; }
/* 176 */      if (!s.getPersistentDataContainer().has(chiave(), PersistentDataType.BYTE))
/*     */       return; 
/* 178 */     Location pos = s.getLocation();
/*     */     
/* 180 */     event.setCancelled(true);
/* 181 */     s.remove();
/* 182 */     fumo(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   private Vector3f scalaPulsante() {
/* 187 */     float f = 2.5F * (0.92F + this.random.nextFloat() * 0.16F);
/* 188 */     return new Vector3f(f, f, f);
/*     */   }
/*     */   
/*     */   private void fumo(Location centro) {
/* 192 */     NUBI.add(new Nube(centro.clone(), System.currentTimeMillis() + 20000L));
/* 193 */     List<ItemDisplay> questi = new ArrayList<>();
/* 194 */     for (int i = 0; i < 3; i++) {
/* 195 */       Location l = centro.clone().add((this.random
/* 196 */           .nextDouble() - 0.5D) * 2.0D * 1.2D, 0.6D + this.random
/* 197 */           .nextDouble() * 0.4D, (this.random
/* 198 */           .nextDouble() - 0.5D) * 2.0D * 1.2D);
/* 199 */       l.setYaw(this.random.nextFloat() * 360.0F);
/*     */       
/* 201 */       ItemDisplay d = (ItemDisplay)centro.getWorld().spawn(l, ItemDisplay.class, disp -> {
/*     */             ItemStack corpetto = new ItemStack(Material.LEATHER_CHESTPLATE);
/*     */             
/*     */             LeatherArmorMeta meta = (LeatherArmorMeta)corpetto.getItemMeta();
/*     */             meta.setCustomModelData(Integer.valueOf(3));
/*     */             corpetto.setItemMeta((ItemMeta)meta);
/*     */             disp.setItemStack(corpetto);
/*     */             disp.setBillboard(Display.Billboard.FIXED);
/*     */             disp.setPersistent(false);
/*     */             disp.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(2.5F, 2.5F, 2.5F), new AxisAngle4f()));
/*     */           });
/* 212 */       questi.add(d);
/* 213 */       ATTIVI.add(d);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     BukkitTask movimento = Bukkit.getScheduler().runTaskTimer((Plugin)Main.getInstance(), () -> { for (ItemDisplay d : questi) { if (!d.isValid()) continue;  d.setInterpolationDelay(0); d.setInterpolationDuration(30); d.setTransformation(new Transformation(new Vector3f((float)((this.random.nextDouble() - 0.5D) * 2.0D * 0.9D), (float)(0.6D + (this.random.nextDouble() - 0.5D) * 0.6D), (float)((this.random.nextDouble() - 0.5D) * 2.0D * 0.9D)), new AxisAngle4f((float)Math.toRadians((this.random.nextInt(180) - 90)), 0.0F, 1.0F, 0.0F), scalaPulsante(), new AxisAngle4f())); }  }2L, 30L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     Bukkit.getScheduler().runTaskLater((Plugin)Main.getInstance(), () -> { movimento.cancel(); for (ItemDisplay d : questi) { d.remove(); ATTIVI.remove(d); }  }400L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rimuoviTutti() {
/* 243 */     NUBI.clear();
/* 244 */     for (ItemDisplay d : ATTIVI) d.remove(); 
/* 245 */     ATTIVI.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\listeners\FumogenoListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */