/*     */ package com.atlantisrp.listeners;
/*     */ import com.atlantisrp.items.ItemFactory;
/*     */ import com.atlantisrp.items.StatoArmi;
/*     */ import com.atlantisrp.items.TipoArma;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.kyori.adventure.key.Key;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import net.kyori.adventure.text.TextComponent;
/*     */ import net.kyori.adventure.text.format.NamedTextColor;
/*     */ import net.kyori.adventure.text.format.ShadowColor;
/*     */ import net.kyori.adventure.text.format.TextColor;
/*     */ import net.kyori.adventure.util.ARGBLike;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.persistence.PersistentDataContainer;
/*     */ import org.bukkit.persistence.PersistentDataType;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public class HudArmi {
/*  26 */   private static final Map<String, Integer> CAPACITA = new HashMap<>(); private static final int CAPACITA_DEFAULT = 17;
/*     */   
/*     */   static {
/*  29 */     for (TipoArma tipo : TipoArma.values()) {
/*  30 */       CAPACITA.put(tipo.getId(), Integer.valueOf(tipo.getCapacita()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   private static final Set<UUID> mostrando = new HashSet<>();
/*     */   
/*  40 */   private static final Key FONT_BARRA = Key.key("atlantis", "armi");
/*  41 */   private static final Key FONT_SPAZI = Key.key("mythichud", "spaces");
/*     */   
/*     */   private static final char SINISTRA = '';
/*     */   
/*  45 */   private static final char[] CENTRI = new char[] { '', '', '', '', '', '', '', '', '' };
/*     */   
/*     */   private static final char DESTRA = '';
/*     */   
/*     */   private static final char MENO_UNO = '';
/*     */   
/*     */   private static final int BARRA_W = 120;
/*     */   private static final int PREFISSO_W = 68;
/*     */   private static final int NUMERI_W = 30;
/*     */   private static final int SCARICO_W = 38;
/*     */   
/*     */   public static void avvia(JavaPlugin plugin) {
/*  57 */     (new BukkitRunnable()
/*     */       {
/*     */         public void run() {
/*  60 */           for (Player player : Bukkit.getOnlinePlayers()) {
/*  61 */             HudArmi.aggiorna(player);
/*     */           }
/*     */           
/*  64 */           HudArmi.mostrando.removeIf(uuid -> (Bukkit.getPlayer(uuid) == null));
/*     */         }
/*  66 */       }).runTaskTimer((Plugin)plugin, 0L, 4L);
/*     */   }
/*     */ 
/*     */   
/*  70 */   private static final Map<UUID, Long> AVVISO_FINO = new HashMap<>(); private static final int AVVISO_BARRA_W = 230; private static final int AVVISO_PX_PER_CARATTERE = 5;
/*     */   
/*     */   public static void avviso(Player player, String testo) {
/*  73 */     avviso(player, testo, NamedTextColor.RED);
/*     */   }
/*     */   
/*     */   public static void avviso(Player player, String testo, NamedTextColor colore) {
/*  77 */     AVVISO_FINO.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis() + 1500L));
/*  78 */     mostrando.add(player.getUniqueId());
/*  79 */     player.sendActionBar(costruisciAvviso(testo, colore));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Component costruisciAvviso(String testo, NamedTextColor colore) {
/*  86 */     int larghezzaTesto = testo.length() * 5;
/*  87 */     int margine = Math.max(0, (230 - larghezzaTesto) / 2);
/*     */ 
/*     */     
/*  90 */     Component sfondo = ((TextComponent)Component.text(disegnaSfondo(230), (TextColor)NamedTextColor.WHITE).font(FONT_BARRA)).shadowColor((ARGBLike)ShadowColor.none());
/*  91 */     return ((TextComponent)((TextComponent)((TextComponent)Component.empty()
/*  92 */       .append(sfondo))
/*  93 */       .append(spazio(-(230 - margine))))
/*  94 */       .append((Component)Component.text(testo, (TextColor)colore)))
/*  95 */       .append(spazio(230 - margine - larghezzaTesto));
/*     */   }
/*     */   
/*     */   private static void aggiorna(Player player) {
/*  99 */     Long fino = AVVISO_FINO.get(player.getUniqueId());
/* 100 */     if (fino != null) {
/* 101 */       if (System.currentTimeMillis() < fino.longValue())
/* 102 */         return;  AVVISO_FINO.remove(player.getUniqueId());
/* 103 */       player.sendActionBar((Component)Component.empty());
/*     */     } 
/*     */     
/* 106 */     ItemStack inMano = player.getInventory().getItemInMainHand();
/* 107 */     if (inMano.getItemMeta() != null && "mirino".equals(inMano.getItemMeta().getPersistentDataContainer()
/* 108 */         .get(ItemFactory.KEY_TIPO, PersistentDataType.STRING))) {
/* 109 */       inMano = player.getInventory().getItemInOffHand();
/*     */     }
/* 111 */     if (inMano.getItemMeta() != null && "taser".equals(inMano.getItemMeta().getPersistentDataContainer()
/* 112 */         .get(ItemFactory.KEY_TIPO, PersistentDataType.STRING))) {
/* 113 */       mostrando.add(player.getUniqueId());
/*     */       
/* 115 */       int cariche = ((Integer)inMano.getItemMeta().getPersistentDataContainer().getOrDefault(ItemFactory.KEY_CARICHE, PersistentDataType.INTEGER, Integer.valueOf(0))).intValue();
/* 116 */       player.sendActionBar(costruisciBarra(true, cariche, 4));
/*     */       
/*     */       return;
/*     */     } 
/* 120 */     ItemStack item = trovaItem(player);
/* 121 */     if (item == null) {
/*     */       
/* 123 */       if (mostrando.remove(player.getUniqueId())) {
/* 124 */         player.sendActionBar((Component)Component.empty());
/*     */       }
/*     */       return;
/*     */     } 
/* 128 */     mostrando.add(player.getUniqueId());
/*     */     
/* 130 */     PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
/* 131 */     String tipo = (String)pdc.get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
/* 132 */     int munizioni = ((Integer)pdc.getOrDefault(ItemFactory.KEY_MUNIZIONI, PersistentDataType.INTEGER, Integer.valueOf(0))).intValue();
/*     */ 
/*     */     
/* 135 */     if ("caricatore".equals(tipo)) {
/* 136 */       String arma = (String)pdc.get(ItemFactory.KEY_ARMA_CARICATORE, PersistentDataType.STRING);
/* 137 */       int i = (arma == null) ? 17 : ((Integer)CAPACITA.getOrDefault(arma, Integer.valueOf(17))).intValue();
/* 138 */       player.sendActionBar(costruisciBarra(true, munizioni, i));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 143 */     int capacita = ((Integer)CAPACITA.getOrDefault(tipo, Integer.valueOf(17))).intValue();
/*     */ 
/*     */     
/* 146 */     StatoArmi.Stato stato = StatoArmi.di(item);
/* 147 */     boolean carica = (stato.haCaricatore && stato.munizioni > 0);
/*     */     
/* 149 */     player.sendActionBar(costruisciBarra(carica, stato.munizioni, capacita));
/*     */   }
/*     */   private static Component costruisciBarra(boolean carica, int munizioni, int capacita) {
/*     */     TextComponent textComponent;
/* 153 */     int larghezzaTesto = 68 + (carica ? 30 : 38);
/* 154 */     int margine = Math.max(0, (120 - larghezzaTesto) / 2);
/*     */ 
/*     */ 
/*     */     
/* 158 */     Component sfondo = ((TextComponent)Component.text(disegnaSfondo(120), (TextColor)NamedTextColor.WHITE).font(FONT_BARRA)).shadowColor((ARGBLike)ShadowColor.none());
/*     */ 
/*     */     
/* 161 */     if (carica) {
/* 162 */       textComponent = Component.text("" + munizioni + "/" + munizioni, (TextColor)NamedTextColor.WHITE);
/*     */     } else {
/* 164 */       textComponent = Component.text("Scarico", (TextColor)NamedTextColor.WHITE);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     Component testo = ((TextComponent)((TextComponent)Component.text("Caricatore ", (TextColor)NamedTextColor.GRAY).append((Component)Component.text("»", (TextColor)NamedTextColor.DARK_GRAY))).append((Component)Component.text(" "))).append((Component)textComponent);
/*     */     
/* 173 */     return ((TextComponent)((TextComponent)((TextComponent)Component.empty()
/* 174 */       .append(sfondo))
/* 175 */       .append(spazio(-(120 - margine))))
/* 176 */       .append(testo))
/* 177 */       .append(spazio(120 - margine - larghezzaTesto));
/*     */   }
/*     */   
/*     */   private static String disegnaSfondo(int larghezza) {
/* 181 */     StringBuilder sb = new StringBuilder();
/* 182 */     sb.append('').append('');
/*     */     
/* 184 */     int centro = larghezza - 10;
/* 185 */     for (int i = CENTRI.length - 1; i >= 0; i--) {
/* 186 */       if ((centro & 1 << i) != 0) {
/* 187 */         sb.append(CENTRI[i]).append('');
/*     */       }
/*     */     } 
/*     */     
/* 191 */     sb.append('').append('');
/* 192 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static Component spazio(int n) {
/* 196 */     if (n == 0) return (Component)Component.empty(); 
/* 197 */     char c = (char)(((n > 0) ? 57344 : 61440) + Math.abs(n));
/* 198 */     return Component.text(String.valueOf(c)).font(FONT_SPAZI);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ItemStack trovaItem(Player player) {
/* 203 */     ItemStack main = player.getInventory().getItemInMainHand();
/* 204 */     if (isArma(main) || isCaricatore(main)) return main;
/*     */     
/* 206 */     ItemStack off = player.getInventory().getItemInOffHand();
/* 207 */     if (isArma(off) || isCaricatore(off)) return off;
/*     */     
/* 209 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean isArma(ItemStack item) {
/* 213 */     if (item == null || item.getItemMeta() == null) return false;
/*     */     
/* 215 */     String tipo = (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
/* 216 */     return (tipo != null && CAPACITA.containsKey(tipo));
/*     */   }
/*     */   
/*     */   private static boolean isCaricatore(ItemStack item) {
/* 220 */     if (item == null || item.getItemMeta() == null) return false;
/*     */     
/* 222 */     String tipo = (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
/* 223 */     return "caricatore".equals(tipo);
/*     */   }
/*     */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\listeners\HudArmi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */