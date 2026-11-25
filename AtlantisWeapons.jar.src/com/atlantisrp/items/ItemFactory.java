/*     */ package com.atlantisrp.items;
/*     */ import java.util.List;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import net.kyori.adventure.text.format.NamedTextColor;
/*     */ import net.kyori.adventure.text.format.TextColor;
/*     */ import net.kyori.adventure.text.format.TextDecoration;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.CrossbowMeta;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.persistence.PersistentDataType;
/*     */ 
/*     */ public class ItemFactory {
/*  15 */   public static final NamespacedKey KEY_TIPO = new NamespacedKey("atlantisweapons", "tipo");
/*  16 */   public static final NamespacedKey KEY_MUNIZIONI = new NamespacedKey("atlantisweapons", "munizioni");
/*  17 */   public static final NamespacedKey KEY_HA_CARICATORE = new NamespacedKey("atlantisweapons", "ha_caricatore");
/*  18 */   public static final NamespacedKey KEY_ID = new NamespacedKey("atlantisweapons", "id");
/*  19 */   public static final NamespacedKey KEY_ARMA_CARICATORE = new NamespacedKey("atlantisweapons", "arma_caricatore");
/*     */ 
/*     */   
/*  22 */   public static final int CAPACITA_GLOCK = TipoArma.GLOCK.getCapacita();
/*  23 */   public static final int CAPACITA_FS92 = TipoArma.FS92.getCapacita();
/*  24 */   public static final int CAPACITA_PX4 = TipoArma.PX4.getCapacita();
/*  25 */   public static final int CAPACITA_MP5 = TipoArma.MP5.getCapacita();
/*  26 */   public static final int CAPACITA_PM12 = TipoArma.PM12.getCapacita();
/*  27 */   public static final int CAPACITA_M4A1 = TipoArma.M4A1.getCapacita();
/*  28 */   public static final int CAPACITA_PMX = TipoArma.PMX.getCapacita(); public static final int CMD_MANETTE = 68;
/*     */   public static final int CMD_TRONCHESI = 291;
/*     */   
/*     */   public static ItemStack createArma(TipoArma tipo) {
/*  32 */     ItemStack item = new ItemStack(Material.CROSSBOW);
/*  33 */     ItemMeta meta = item.getItemMeta();
/*     */     
/*  35 */     meta.displayName(Component.text(tipo.getNome(), (TextColor)NamedTextColor.GRAY)
/*  36 */         .decoration(TextDecoration.ITALIC, false));
/*  37 */     meta.setCustomModelData(Integer.valueOf(tipo.getCmdArma()));
/*     */     
/*  39 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, tipo.getId());
/*     */ 
/*     */     
/*  42 */     meta.getPersistentDataContainer().set(KEY_ID, PersistentDataType.STRING, UUID.randomUUID().toString());
/*     */     
/*  44 */     item.setItemMeta(meta);
/*  45 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ItemStack createCaricatore(TipoArma tipo, int munizioni) {
/*  50 */     ItemStack item = new ItemStack(Material.STICK);
/*  51 */     ItemMeta meta = item.getItemMeta();
/*     */     
/*  53 */     meta.displayName(Component.text("Caricatore " + tipo.getNome(), (TextColor)NamedTextColor.GRAY)
/*  54 */         .decoration(TextDecoration.ITALIC, false));
/*  55 */     meta.setCustomModelData(Integer.valueOf(tipo.getCmdCaricatore()));
/*     */     
/*  57 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "caricatore");
/*  58 */     meta.getPersistentDataContainer().set(KEY_ARMA_CARICATORE, PersistentDataType.STRING, tipo.getId());
/*  59 */     meta.getPersistentDataContainer().set(KEY_MUNIZIONI, PersistentDataType.INTEGER, Integer.valueOf(munizioni));
/*     */ 
/*     */     
/*  62 */     meta.setMaxStackSize(Integer.valueOf(1));
/*     */     
/*  64 */     item.setItemMeta(meta);
/*  65 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ItemStack createMunizioni(Munizione tipo, int quantita) {
/*  70 */     ItemStack item = new ItemStack(Material.STICK, quantita);
/*  71 */     ItemMeta meta = item.getItemMeta();
/*     */     
/*  73 */     meta.displayName(Component.text(tipo.getNome(), (TextColor)NamedTextColor.GRAY)
/*  74 */         .decoration(TextDecoration.ITALIC, false));
/*  75 */     meta.setCustomModelData(Integer.valueOf(tipo.getCmd()));
/*     */     
/*  77 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, tipo.getId());
/*     */     
/*  79 */     item.setItemMeta(meta);
/*  80 */     return item;
/*     */   }
/*     */   
/*     */   public static ItemStack createGlock() {
/*  84 */     return createArma(TipoArma.GLOCK); } public static ItemStack createCaricatoreGlock(int munizioni) {
/*  85 */     return createCaricatore(TipoArma.GLOCK, munizioni);
/*     */   }
/*  87 */   public static ItemStack createFS92() { return createArma(TipoArma.FS92); } public static ItemStack createCaricatoreFS92(int munizioni) {
/*  88 */     return createCaricatore(TipoArma.FS92, munizioni);
/*     */   }
/*  90 */   public static ItemStack createPX4() { return createArma(TipoArma.PX4); } public static ItemStack createCaricatorePX4(int munizioni) {
/*  91 */     return createCaricatore(TipoArma.PX4, munizioni);
/*     */   }
/*  93 */   public static ItemStack createMP5() { return createArma(TipoArma.MP5); } public static ItemStack createCaricatoreMP5(int munizioni) {
/*  94 */     return createCaricatore(TipoArma.MP5, munizioni);
/*     */   }
/*  96 */   public static ItemStack createPM12() { return createArma(TipoArma.PM12); } public static ItemStack createCaricatorePM12(int munizioni) {
/*  97 */     return createCaricatore(TipoArma.PM12, munizioni);
/*     */   }
/*  99 */   public static ItemStack createM4A1() { return createArma(TipoArma.M4A1); } public static ItemStack createCaricatoreM4A1(int munizioni) {
/* 100 */     return createCaricatore(TipoArma.M4A1, munizioni);
/*     */   }
/* 102 */   public static ItemStack createPMX() { return createArma(TipoArma.PMX); } public static ItemStack createCaricatorePMX(int munizioni) {
/* 103 */     return createCaricatore(TipoArma.PMX, munizioni);
/*     */   }
/* 105 */   public static ItemStack createMunizioni9mm(int quantita) { return createMunizioni(Munizione.NOVE_MM, quantita); }
/* 106 */   public static ItemStack createMunizioniCecchino(int quantita) { return createMunizioni(Munizione.CECCHINO, quantita); } public static ItemStack createMunizioni556(int quantita) {
/* 107 */     return createMunizioni(Munizione.NATO_556, quantita);
/*     */   }
/*     */   
/*     */   public static ItemStack createBossolo() {
/* 111 */     ItemStack item = new ItemStack(Material.STICK);
/* 112 */     ItemMeta meta = item.getItemMeta();
/*     */     
/* 114 */     meta.displayName(Component.text("Bossolo", (TextColor)NamedTextColor.GRAY)
/* 115 */         .decoration(TextDecoration.ITALIC, false));
/* 116 */     meta.setCustomModelData(Integer.valueOf(555));
/*     */     
/* 118 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "bossolo");
/*     */     
/* 120 */     item.setItemMeta(meta);
/* 121 */     return item;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack createMirino() {
/* 127 */     ItemStack item = new ItemStack(Material.CROSSBOW);
/* 128 */     ItemMeta meta = item.getItemMeta();
/*     */     
/* 130 */     meta.displayName(Component.text("Mirino", (TextColor)NamedTextColor.DARK_GRAY)
/* 131 */         .decoration(TextDecoration.ITALIC, false));
/* 132 */     meta.setCustomModelData(Integer.valueOf(100));
/*     */     
/* 134 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "mirino");
/*     */     
/* 136 */     item.setItemMeta(meta);
/* 137 */     return item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack armaInMira(ItemStack arma, boolean inMira) {
/* 144 */     ItemStack copia = arma.clone();
/* 145 */     ItemMeta itemMeta = copia.getItemMeta(); if (itemMeta instanceof CrossbowMeta) { CrossbowMeta meta = (CrossbowMeta)itemMeta;
/* 146 */       if (inMira) {
/* 147 */         meta.addChargedProjectile(new ItemStack(Material.ARROW));
/*     */       } else {
/* 149 */         meta.setChargedProjectiles(null);
/*     */       } 
/* 151 */       copia.setItemMeta((ItemMeta)meta); }
/*     */     
/* 153 */     return copia;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ItemStack createPassamontagna() {
/* 158 */     ItemStack item = new ItemStack(Material.STICK);
/* 159 */     ItemMeta meta = item.getItemMeta();
/*     */     
/* 161 */     meta.displayName(Component.text("Passamontagna", (TextColor)NamedTextColor.GRAY)
/* 162 */         .decoration(TextDecoration.ITALIC, false));
/* 163 */     meta.setCustomModelData(Integer.valueOf(64));
/* 164 */     meta.setMaxStackSize(Integer.valueOf(1));
/*     */     
/* 166 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "passamontagna");
/*     */     
/* 168 */     item.setItemMeta(meta);
/* 169 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ItemStack createGiubbottoLeggero() {
/* 174 */     ItemStack item = new ItemStack(Material.GOLDEN_CHESTPLATE);
/* 175 */     ItemMeta meta = item.getItemMeta();
/*     */     
/* 177 */     meta.displayName(Component.text("Giubbotto Antiproiettile Leggero", (TextColor)NamedTextColor.GRAY)
/* 178 */         .decoration(TextDecoration.ITALIC, false));
/*     */     
/* 180 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "giubbotto_leggero");
/*     */     
/* 182 */     item.setItemMeta(meta);
/* 183 */     return item;
/*     */   }
/*     */   
/*     */   public static ItemStack createGiubbottoPesante() {
/* 187 */     ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
/* 188 */     ItemMeta meta = item.getItemMeta();
/*     */     
/* 190 */     meta.displayName(Component.text("Giubbotto Antiproiettile Pesante", (TextColor)NamedTextColor.GRAY)
/* 191 */         .decoration(TextDecoration.ITALIC, false));
/*     */     
/* 193 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "giubbotto_pesante");
/*     */     
/* 195 */     item.setItemMeta(meta);
/* 196 */     return item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack createManette() {
/* 204 */     return stickSpeciale("Manette", 68, "manette");
/*     */   }
/*     */   
/* 207 */   public static final NamespacedKey KEY_USI = new NamespacedKey("atlantisweapons", "usi"); public static final int USI_TRONCHESI = 10; public static final int CMD_VISORE = 290;
/*     */   public static final int CMD_CORDA = 142;
/*     */   public static final int USI_CORDA = 10;
/*     */   
/*     */   public static ItemStack createTronchesi() {
/* 212 */     return tronchesiConUsi(10);
/*     */   }
/*     */   
/*     */   public static ItemStack tronchesiConUsi(int usi) {
/* 216 */     ItemStack item = stickSpeciale("Tronchesi", 291, "tronchesi");
/* 217 */     ItemMeta meta = item.getItemMeta();
/* 218 */     meta.getPersistentDataContainer().set(KEY_USI, PersistentDataType.INTEGER, Integer.valueOf(usi));
/* 219 */     meta.lore(List.of(Component.text("Utilizzi: " + usi, (TextColor)NamedTextColor.GRAY)
/* 220 */           .decoration(TextDecoration.ITALIC, false)));
/* 221 */     item.setItemMeta(meta);
/* 222 */     return item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack createCorda() {
/* 230 */     return cordaConUsi(10);
/*     */   }
/*     */   
/*     */   public static ItemStack cordaConUsi(int usi) {
/* 234 */     ItemStack item = stickSpeciale("Corda", 142, "corda");
/* 235 */     ItemMeta meta = item.getItemMeta();
/* 236 */     meta.getPersistentDataContainer().set(KEY_USI, PersistentDataType.INTEGER, Integer.valueOf(usi));
/* 237 */     meta.lore(List.of(Component.text("Utilizzi: " + usi, (TextColor)NamedTextColor.GRAY)
/* 238 */           .decoration(TextDecoration.ITALIC, false)));
/* 239 */     item.setItemMeta(meta);
/* 240 */     return item;
/*     */   }
/*     */ 
/*     */   
/* 244 */   public static final NamespacedKey KEY_CARICHE = new NamespacedKey("atlantisweapons", "taser_cariche");
/* 245 */   public static final NamespacedKey KEY_CARICATO = new NamespacedKey("atlantisweapons", "taser_caricato");
/*     */   public static final int CARICHE_TASER = 4;
/*     */   public static final int CMD_TASER_CARICO = 20;
/*     */   public static final int CMD_TASER_SPARATO = 21;
/*     */   public static final int CMD_DARDO_TASER = 398;
/*     */   
/*     */   public static ItemStack createTaser() {
/* 252 */     return taserStato(4, true);
/*     */   }
/*     */   public static final int CMD_FUMOGENO = 144; public static final int CMD_ASCIA_VDF = 1; public static final int CMD_PIEDE_DI_PORCO = 65; public static final int CMD_ARIETE = 66;
/*     */   public static ItemStack taserStato(int cariche, boolean caricato) {
/* 256 */     ItemStack item = new ItemStack(Material.CROSSBOW);
/* 257 */     ItemMeta meta = item.getItemMeta();
/* 258 */     meta.displayName(Component.text("Taser", (TextColor)NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
/* 259 */     meta.setCustomModelData(Integer.valueOf(caricato ? 20 : 21));
/* 260 */     meta.setMaxStackSize(Integer.valueOf(1));
/* 261 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "taser");
/* 262 */     meta.getPersistentDataContainer().set(KEY_CARICHE, PersistentDataType.INTEGER, Integer.valueOf(cariche));
/* 263 */     meta.getPersistentDataContainer().set(KEY_CARICATO, PersistentDataType.BYTE, Byte.valueOf((byte)(caricato ? 1 : 0)));
/* 264 */     item.setItemMeta(meta);
/* 265 */     return item;
/*     */   }
/*     */   
/*     */   public static ItemStack createDardoTaser(int quantita) {
/* 269 */     ItemStack item = new ItemStack(Material.STICK, quantita);
/* 270 */     ItemMeta meta = item.getItemMeta();
/* 271 */     meta.displayName(Component.text("Dardo Taser", (TextColor)NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
/* 272 */     meta.setCustomModelData(Integer.valueOf(398));
/* 273 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "dardo_taser");
/* 274 */     item.setItemMeta(meta);
/* 275 */     return item;
/*     */   }
/*     */   
/*     */   public static ItemStack createVisore() {
/* 279 */     return stickSpeciale("Visore Notturno", 290, "visore");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack createFumogeno() {
/* 285 */     ItemStack item = new ItemStack(Material.STICK);
/* 286 */     ItemMeta meta = item.getItemMeta();
/* 287 */     meta.displayName(Component.text("Fumogeno", (TextColor)NamedTextColor.GRAY)
/* 288 */         .decoration(TextDecoration.ITALIC, false));
/* 289 */     meta.setCustomModelData(Integer.valueOf(144));
/* 290 */     meta.setMaxStackSize(Integer.valueOf(16));
/* 291 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "fumogeno");
/* 292 */     item.setItemMeta(meta);
/* 293 */     return item;
/*     */   }
/*     */   
/*     */   private static ItemStack stickSpeciale(String nome, int cmd, String tipo) {
/* 297 */     ItemStack item = new ItemStack(Material.STICK);
/* 298 */     ItemMeta meta = item.getItemMeta();
/*     */     
/* 300 */     meta.displayName(Component.text(nome, (TextColor)NamedTextColor.GRAY)
/* 301 */         .decoration(TextDecoration.ITALIC, false));
/* 302 */     meta.setCustomModelData(Integer.valueOf(cmd));
/* 303 */     meta.setMaxStackSize(Integer.valueOf(1));
/*     */     
/* 305 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, tipo);
/*     */     
/* 307 */     item.setItemMeta(meta);
/* 308 */     return item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack createAsciaVdf() {
/* 317 */     ItemStack item = new ItemStack(Material.IRON_AXE);
/* 318 */     ItemMeta meta = item.getItemMeta();
/* 319 */     meta.displayName(Component.text("Ascia VDF", (TextColor)NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
/* 320 */     meta.setCustomModelData(Integer.valueOf(1));
/* 321 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "ascia_vdf");
/* 322 */     item.setItemMeta(meta);
/* 323 */     return item;
/*     */   }
/*     */   
/*     */   public static ItemStack createAriete() {
/* 327 */     return stickSpeciale("Ariete", 66, "ariete");
/*     */   }
/*     */   
/*     */   public static ItemStack createPiedeDiPorco() {
/* 331 */     return stickSpeciale("Piede di porco", 65, "piede_di_porco");
/*     */   }
/*     */ 
/*     */   
/*     */   public static ItemStack createPaginaSuccessiva() {
/* 336 */     ItemStack item = new ItemStack(Material.ARROW);
/* 337 */     ItemMeta meta = item.getItemMeta();
/*     */     
/* 339 */     meta.displayName(Component.text("Pagina successiva", (TextColor)NamedTextColor.WHITE)
/* 340 */         .decoration(TextDecoration.ITALIC, false));
/* 341 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "pagina_successiva");
/*     */     
/* 343 */     item.setItemMeta(meta);
/* 344 */     return item;
/*     */   }
/*     */   
/*     */   public static ItemStack createPaginaIndietro() {
/* 348 */     ItemStack item = new ItemStack(Material.ARROW);
/* 349 */     ItemMeta meta = item.getItemMeta();
/*     */     
/* 351 */     meta.displayName(Component.text("Pagina indietro", (TextColor)NamedTextColor.WHITE)
/* 352 */         .decoration(TextDecoration.ITALIC, false));
/* 353 */     meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "pagina_indietro");
/*     */     
/* 355 */     item.setItemMeta(meta);
/* 356 */     return item;
/*     */   }
/*     */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\items\ItemFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */