/*     */ package com.atlantisrp.gui;
/*     */ 
/*     */ import com.atlantisrp.items.ItemFactory;
/*     */ import com.atlantisrp.items.Munizione;
/*     */ import com.atlantisrp.items.TipoArma;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import net.kyori.adventure.text.TextComponent;
/*     */ import net.kyori.adventure.text.format.NamedTextColor;
/*     */ import net.kyori.adventure.text.format.TextColor;
/*     */ import net.kyori.adventure.text.format.TextDecoration;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ public class ArmiGUI implements InventoryHolder {
/*     */   public static final String TITOLO = "Armi Atlantis";
/*     */   private final Inventory inventory;
/*  24 */   private int pagina = 1;
/*     */   
/*     */   public ArmiGUI() {
/*  27 */     this.inventory = Bukkit.createInventory(this, 54, 
/*  28 */         Component.text("Armi Atlantis", (TextColor)NamedTextColor.DARK_GRAY)
/*  29 */         .decoration(TextDecoration.ITALIC, false));
/*  30 */     riempi();
/*     */   }
/*     */   
/*     */   private void riempi() {
/*  34 */     this.inventory.clear();
/*  35 */     if (this.pagina == 2) {
/*  36 */       riempiPagina2();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  41 */     this.inventory.setItem(53, ItemFactory.createPaginaSuccessiva());
/*     */ 
/*     */     
/*  44 */     this.inventory.setItem(3, conLore(ItemFactory.createGiubbottoLeggero(), List.of(clickPerRicevere())));
/*  45 */     this.inventory.setItem(5, conLore(ItemFactory.createGiubbottoPesante(), List.of(clickPerRicevere())));
/*  46 */     this.inventory.setItem(4, conLore(ItemFactory.createPassamontagna(), List.of(clickPerRicevere())));
/*  47 */     this.inventory.setItem(1, conLore(ItemFactory.createManette(), List.of(clickPerRicevere())));
/*  48 */     this.inventory.setItem(7, conLore(ItemFactory.createTronchesi(), List.of(clickPerRicevere())));
/*  49 */     this.inventory.setItem(25, conLore(ItemFactory.createCorda(), List.of(clickPerRicevere())));
/*  50 */     this.inventory.setItem(21, conLore(ItemFactory.createTaser(), List.of(clickPerRicevere())));
/*  51 */     this.inventory.setItem(2, conLore(ItemFactory.createVisore(), List.of(clickPerRicevere())));
/*  52 */     this.inventory.setItem(6, conLore(ItemFactory.createFumogeno(), List.of(clickPerRicevere())));
/*     */ 
/*     */     
/*  55 */     TipoArma[] riga1 = { TipoArma.MP5, TipoArma.PM12, TipoArma.PMX, TipoArma.M4A1, TipoArma.AUG, TipoArma.SC7090, TipoArma.HK416, TipoArma.ARX200 };
/*     */     
/*  57 */     for (int i = 0; i < riga1.length; i++) {
/*  58 */       this.inventory.setItem(9 + i, armaGui(riga1[i]));
/*     */     }
/*     */ 
/*     */     
/*  62 */     this.inventory.setItem(18, armaGui(TipoArma.GLOCK));
/*  63 */     this.inventory.setItem(19, armaGui(TipoArma.FS92));
/*  64 */     this.inventory.setItem(20, armaGui(TipoArma.PX4));
/*  65 */     this.inventory.setItem(22, armaGui(TipoArma.SPAS15));
/*  66 */     this.inventory.setItem(23, armaGui(TipoArma.BARRETT));
/*  67 */     this.inventory.setItem(24, armaGui(TipoArma.SAKO));
/*     */ 
/*     */     
/*  70 */     TipoArma[] caricatori = { TipoArma.GLOCK, TipoArma.FS92, TipoArma.PX4, TipoArma.MP5, TipoArma.PM12, TipoArma.PMX, TipoArma.M4A1, TipoArma.AUG, TipoArma.SC7090, TipoArma.HK416, TipoArma.ARX200, TipoArma.SPAS15, TipoArma.BARRETT, TipoArma.SAKO };
/*     */ 
/*     */     
/*  73 */     for (int j = 0; j < caricatori.length; j++) {
/*  74 */       this.inventory.setItem(27 + j, conLore(ItemFactory.createCaricatore(caricatori[j], 0), 
/*  75 */             List.of(clickPerRicevere())));
/*     */     }
/*     */ 
/*     */     
/*  79 */     this.inventory.setItem(50, conLore(ItemFactory.createDardoTaser(16), List.of(clickPerRicevere())));
/*     */ 
/*     */     
/*  82 */     Munizione[] munizioni = { Munizione.NOVE_MM, Munizione.NATO_556, Munizione.NATO_762, Munizione.POMPA, Munizione.CECCHINO };
/*     */     
/*  84 */     for (int k = 0; k < munizioni.length; k++) {
/*  85 */       this.inventory.setItem(45 + k, munizioneGui(munizioni[k]));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void riempiPagina2() {
/*  91 */     this.inventory.setItem(10, conLore(ItemFactory.createAsciaVdf(), List.of(clickPerRicevere())));
/*  92 */     this.inventory.setItem(11, conLore(ItemFactory.createAriete(), List.of(clickPerRicevere())));
/*  93 */     this.inventory.setItem(12, conLore(ItemFactory.createPiedeDiPorco(), List.of(clickPerRicevere())));
/*     */ 
/*     */     
/*  96 */     this.inventory.setItem(45, ItemFactory.createPaginaIndietro());
/*     */   }
/*     */ 
/*     */   
/*     */   private ItemStack armaGui(TipoArma tipo) {
/* 101 */     List<Component> lore = new ArrayList<>();
/* 102 */     lore.add(riga("Danno: ", NamedTextColor.GRAY, String.format(Locale.ROOT, "%.1f", new Object[] { Double.valueOf(tipo.getDannoTesta()) }), NamedTextColor.RED));
/* 103 */     lore.add(riga("Tipo: ", NamedTextColor.GRAY, tipo.isAutomatica() ? "Automatica" : "Semi-Auto", NamedTextColor.YELLOW));
/* 104 */     lore.add(riga("Capienza: ", NamedTextColor.GRAY, "" + tipo.getCapacita() + " colpi", NamedTextColor.AQUA));
/* 105 */     lore.add(Component.empty());
/* 106 */     lore.add(clickPerRicevere());
/* 107 */     return conLore(ItemFactory.createArma(tipo), lore);
/*     */   }
/*     */   
/*     */   private ItemStack munizioneGui(Munizione tipo) {
/* 111 */     List<String> armeCheLaUsano = new ArrayList<>();
/* 112 */     for (TipoArma t : TipoArma.values()) {
/* 113 */       if (t.getMunizione() == tipo) armeCheLaUsano.add(t.getNome());
/*     */     
/*     */     } 
/* 116 */     List<Component> lore = new ArrayList<>();
/* 117 */     lore.add(Component.text("Usata da:", (TextColor)NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
/* 118 */     lore.add(Component.text(String.join(", ", (Iterable)armeCheLaUsano), (TextColor)NamedTextColor.DARK_GRAY)
/* 119 */         .decoration(TextDecoration.ITALIC, false));
/* 120 */     lore.add(Component.empty());
/* 121 */     lore.add(Component.text("▶ Click per ricevere 64 munizioni", (TextColor)NamedTextColor.GREEN)
/* 122 */         .decoration(TextDecoration.ITALIC, false));
/* 123 */     return conLore(ItemFactory.createMunizioni(tipo, 64), lore);
/*     */   }
/*     */   
/*     */   private Component riga(String etichetta, NamedTextColor coloreEtichetta, String valore, NamedTextColor coloreValore) {
/* 127 */     return ((TextComponent)Component.text(etichetta, (TextColor)coloreEtichetta)
/* 128 */       .append((Component)Component.text(valore, (TextColor)coloreValore)))
/* 129 */       .decoration(TextDecoration.ITALIC, false);
/*     */   }
/*     */   
/*     */   private Component clickPerRicevere() {
/* 133 */     return Component.text("▶ Click per ricevere", (TextColor)NamedTextColor.GREEN)
/* 134 */       .decoration(TextDecoration.ITALIC, false);
/*     */   }
/*     */   
/*     */   private ItemStack conLore(ItemStack item, List<Component> lore) {
/* 138 */     ItemMeta meta = item.getItemMeta();
/* 139 */     meta.lore(lore);
/* 140 */     item.setItemMeta(meta);
/* 141 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   public void mostraPagina(int nuovaPagina) {
/* 146 */     this.pagina = Math.max(1, Math.min(2, nuovaPagina));
/* 147 */     riempi();
/*     */   }
/*     */   
/*     */   public int getPagina() {
/* 151 */     return this.pagina;
/*     */   }
/*     */ 
/*     */   
/*     */   public Inventory getInventory() {
/* 156 */     return this.inventory;
/*     */   }
/*     */   
/*     */   public static void apri(Player player) {
/* 160 */     player.openInventory((new ArmiGUI()).getInventory());
/*     */   }
/*     */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\gui\ArmiGUI.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */