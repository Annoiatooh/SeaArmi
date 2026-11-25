package com.atlantisrp.items;

import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemFactory {
   public static final NamespacedKey KEY_TIPO = new NamespacedKey("atlantisweapons", "tipo");
   public static final NamespacedKey KEY_MUNIZIONI = new NamespacedKey("atlantisweapons", "munizioni");
   public static final NamespacedKey KEY_HA_CARICATORE = new NamespacedKey("atlantisweapons", "ha_caricatore");
   public static final NamespacedKey KEY_ID = new NamespacedKey("atlantisweapons", "id");
   public static final NamespacedKey KEY_ARMA_CARICATORE = new NamespacedKey("atlantisweapons", "arma_caricatore");
   public static final int CAPACITA_GLOCK;
   public static final int CAPACITA_FS92;
   public static final int CAPACITA_PX4;
   public static final int CAPACITA_MP5;
   public static final int CAPACITA_PM12;
   public static final int CAPACITA_M4A1;
   public static final int CAPACITA_PMX;
   public static final int CMD_MANETTE = 68;
   public static final int CMD_TRONCHESI = 291;
   public static final NamespacedKey KEY_USI;
   public static final int USI_TRONCHESI = 10;
   public static final int CMD_VISORE = 290;
   public static final int CMD_CORDA = 142;
   public static final int USI_CORDA = 10;
   public static final NamespacedKey KEY_CARICHE;
   public static final NamespacedKey KEY_CARICATO;
   public static final int CARICHE_TASER = 4;
   public static final int CMD_TASER_CARICO = 20;
   public static final int CMD_TASER_SPARATO = 21;
   public static final int CMD_DARDO_TASER = 398;
   public static final int CMD_FUMOGENO = 144;
   public static final int CMD_ASCIA_VDF = 1;
   public static final int CMD_PIEDE_DI_PORCO = 65;
   public static final int CMD_ARIETE = 66;

   public static ItemStack createArma(TipoArma tipo) {
      ItemStack item = new ItemStack(Material.CROSSBOW);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text(tipo.getNome(), NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      meta.setCustomModelData(tipo.getCmdArma());
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, tipo.getId());
      meta.getPersistentDataContainer().set(KEY_ID, PersistentDataType.STRING, UUID.randomUUID().toString());
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createCaricatore(TipoArma tipo, int munizioni) {
      ItemStack item = new ItemStack(Material.STICK);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Caricatore " + tipo.getNome(), NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      meta.setCustomModelData(tipo.getCmdCaricatore());
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "caricatore");
      meta.getPersistentDataContainer().set(KEY_ARMA_CARICATORE, PersistentDataType.STRING, tipo.getId());
      meta.getPersistentDataContainer().set(KEY_MUNIZIONI, PersistentDataType.INTEGER, munizioni);
      meta.setMaxStackSize(1);
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createMunizioni(Munizione tipo, int quantita) {
      ItemStack item = new ItemStack(Material.STICK, quantita);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text(tipo.getNome(), NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      meta.setCustomModelData(tipo.getCmd());
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, tipo.getId());
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createGlock() {
      return createArma(TipoArma.GLOCK);
   }

   public static ItemStack createCaricatoreGlock(int munizioni) {
      return createCaricatore(TipoArma.GLOCK, munizioni);
   }

   public static ItemStack createFS92() {
      return createArma(TipoArma.FS92);
   }

   public static ItemStack createCaricatoreFS92(int munizioni) {
      return createCaricatore(TipoArma.FS92, munizioni);
   }

   public static ItemStack createPX4() {
      return createArma(TipoArma.PX4);
   }

   public static ItemStack createCaricatorePX4(int munizioni) {
      return createCaricatore(TipoArma.PX4, munizioni);
   }

   public static ItemStack createMP5() {
      return createArma(TipoArma.MP5);
   }

   public static ItemStack createCaricatoreMP5(int munizioni) {
      return createCaricatore(TipoArma.MP5, munizioni);
   }

   public static ItemStack createPM12() {
      return createArma(TipoArma.PM12);
   }

   public static ItemStack createCaricatorePM12(int munizioni) {
      return createCaricatore(TipoArma.PM12, munizioni);
   }

   public static ItemStack createM4A1() {
      return createArma(TipoArma.M4A1);
   }

   public static ItemStack createCaricatoreM4A1(int munizioni) {
      return createCaricatore(TipoArma.M4A1, munizioni);
   }

   public static ItemStack createPMX() {
      return createArma(TipoArma.PMX);
   }

   public static ItemStack createCaricatorePMX(int munizioni) {
      return createCaricatore(TipoArma.PMX, munizioni);
   }

   public static ItemStack createMunizioni9mm(int quantita) {
      return createMunizioni(Munizione.NOVE_MM, quantita);
   }

   public static ItemStack createMunizioniCecchino(int quantita) {
      return createMunizioni(Munizione.CECCHINO, quantita);
   }

   public static ItemStack createMunizioni556(int quantita) {
      return createMunizioni(Munizione.NATO_556, quantita);
   }

   public static ItemStack createBossolo() {
      ItemStack item = new ItemStack(Material.STICK);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Bossolo", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      meta.setCustomModelData(555);
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "bossolo");
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createMirino() {
      ItemStack item = new ItemStack(Material.CROSSBOW);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Mirino", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
      meta.setCustomModelData(100);
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "mirino");
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack armaInMira(ItemStack arma, boolean inMira) {
      ItemStack copia = arma.clone();
      ItemMeta var4 = copia.getItemMeta();
      if (var4 instanceof CrossbowMeta meta) {
         if (inMira) {
            meta.addChargedProjectile(new ItemStack(Material.ARROW));
         } else {
            meta.setChargedProjectiles((List)null);
         }

         copia.setItemMeta(meta);
      }

      return copia;
   }

   public static ItemStack createPassamontagna() {
      ItemStack item = new ItemStack(Material.STICK);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Passamontagna", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      meta.setCustomModelData(64);
      meta.setMaxStackSize(1);
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "passamontagna");
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createGiubbottoLeggero() {
      ItemStack item = new ItemStack(Material.GOLDEN_CHESTPLATE);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Giubbotto Antiproiettile Leggero", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "giubbotto_leggero");
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createGiubbottoPesante() {
      ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Giubbotto Antiproiettile Pesante", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "giubbotto_pesante");
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createManette() {
      return stickSpeciale("Manette", 68, "manette");
   }

   public static ItemStack createTronchesi() {
      return tronchesiConUsi(10);
   }

   public static ItemStack tronchesiConUsi(int usi) {
      ItemStack item = stickSpeciale("Tronchesi", 291, "tronchesi");
      ItemMeta meta = item.getItemMeta();
      meta.getPersistentDataContainer().set(KEY_USI, PersistentDataType.INTEGER, usi);
      meta.lore(List.of((TextComponent)Component.text("Utilizzi: " + usi, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createCorda() {
      return cordaConUsi(10);
   }

   public static ItemStack cordaConUsi(int usi) {
      ItemStack item = stickSpeciale("Corda", 142, "corda");
      ItemMeta meta = item.getItemMeta();
      meta.getPersistentDataContainer().set(KEY_USI, PersistentDataType.INTEGER, usi);
      meta.lore(List.of((TextComponent)Component.text("Utilizzi: " + usi, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createTaser() {
      return taserStato(4, true);
   }

   public static ItemStack taserStato(int cariche, boolean caricato) {
      ItemStack item = new ItemStack(Material.CROSSBOW);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Taser", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      meta.setCustomModelData(caricato ? 20 : 21);
      meta.setMaxStackSize(1);
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "taser");
      meta.getPersistentDataContainer().set(KEY_CARICHE, PersistentDataType.INTEGER, cariche);
      meta.getPersistentDataContainer().set(KEY_CARICATO, PersistentDataType.BYTE, (byte)(caricato ? 1 : 0));
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createDardoTaser(int quantita) {
      ItemStack item = new ItemStack(Material.STICK, quantita);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Dardo Taser", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      meta.setCustomModelData(398);
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "dardo_taser");
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createVisore() {
      return stickSpeciale("Visore Notturno", 290, "visore");
   }

   public static ItemStack createFumogeno() {
      ItemStack item = new ItemStack(Material.STICK);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Fumogeno", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      meta.setCustomModelData(144);
      meta.setMaxStackSize(16);
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "fumogeno");
      item.setItemMeta(meta);
      return item;
   }

   private static ItemStack stickSpeciale(String nome, int cmd, String tipo) {
      ItemStack item = new ItemStack(Material.STICK);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text(nome, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      meta.setCustomModelData(cmd);
      meta.setMaxStackSize(1);
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, tipo);
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createAsciaVdf() {
      ItemStack item = new ItemStack(Material.IRON_AXE);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Ascia VDF", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
      meta.setCustomModelData(1);
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "ascia_vdf");
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createAriete() {
      return stickSpeciale("Ariete", 66, "ariete");
   }

   public static ItemStack createPiedeDiPorco() {
      return stickSpeciale("Piede di porco", 65, "piede_di_porco");
   }

   public static ItemStack createPaginaSuccessiva() {
      ItemStack item = new ItemStack(Material.ARROW);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Pagina successiva", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "pagina_successiva");
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack createPaginaIndietro() {
      ItemStack item = new ItemStack(Material.ARROW);
      ItemMeta meta = item.getItemMeta();
      meta.displayName(Component.text("Pagina indietro", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
      meta.getPersistentDataContainer().set(KEY_TIPO, PersistentDataType.STRING, "pagina_indietro");
      item.setItemMeta(meta);
      return item;
   }

   static {
      CAPACITA_GLOCK = TipoArma.GLOCK.getCapacita();
      CAPACITA_FS92 = TipoArma.FS92.getCapacita();
      CAPACITA_PX4 = TipoArma.PX4.getCapacita();
      CAPACITA_MP5 = TipoArma.MP5.getCapacita();
      CAPACITA_PM12 = TipoArma.PM12.getCapacita();
      CAPACITA_M4A1 = TipoArma.M4A1.getCapacita();
      CAPACITA_PMX = TipoArma.PMX.getCapacita();
      KEY_USI = new NamespacedKey("atlantisweapons", "usi");
      KEY_CARICHE = new NamespacedKey("atlantisweapons", "taser_cariche");
      KEY_CARICATO = new NamespacedKey("atlantisweapons", "taser_caricato");
   }
}
