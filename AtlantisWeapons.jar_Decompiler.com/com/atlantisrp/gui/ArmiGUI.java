package com.atlantisrp.gui;

import com.atlantisrp.items.ItemFactory;
import com.atlantisrp.items.Munizione;
import com.atlantisrp.items.TipoArma;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArmiGUI implements InventoryHolder {
   public static final String TITOLO = "Armi Atlantis";
   private final Inventory inventory;
   private int pagina = 1;

   public ArmiGUI() {
      this.inventory = Bukkit.createInventory(this, 54, Component.text("Armi Atlantis", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
      this.riempi();
   }

   private void riempi() {
      this.inventory.clear();
      if (this.pagina == 2) {
         this.riempiPagina2();
      } else {
         this.inventory.setItem(53, ItemFactory.createPaginaSuccessiva());
         this.inventory.setItem(3, this.conLore(ItemFactory.createGiubbottoLeggero(), List.of(this.clickPerRicevere())));
         this.inventory.setItem(5, this.conLore(ItemFactory.createGiubbottoPesante(), List.of(this.clickPerRicevere())));
         this.inventory.setItem(4, this.conLore(ItemFactory.createPassamontagna(), List.of(this.clickPerRicevere())));
         this.inventory.setItem(1, this.conLore(ItemFactory.createManette(), List.of(this.clickPerRicevere())));
         this.inventory.setItem(7, this.conLore(ItemFactory.createTronchesi(), List.of(this.clickPerRicevere())));
         this.inventory.setItem(25, this.conLore(ItemFactory.createCorda(), List.of(this.clickPerRicevere())));
         this.inventory.setItem(21, this.conLore(ItemFactory.createTaser(), List.of(this.clickPerRicevere())));
         this.inventory.setItem(2, this.conLore(ItemFactory.createVisore(), List.of(this.clickPerRicevere())));
         this.inventory.setItem(6, this.conLore(ItemFactory.createFumogeno(), List.of(this.clickPerRicevere())));
         TipoArma[] riga1 = new TipoArma[]{TipoArma.MP5, TipoArma.PM12, TipoArma.PMX, TipoArma.M4A1, TipoArma.AUG, TipoArma.SC7090, TipoArma.HK416, TipoArma.ARX200};

         for(int i = 0; i < riga1.length; ++i) {
            this.inventory.setItem(9 + i, this.armaGui(riga1[i]));
         }

         this.inventory.setItem(18, this.armaGui(TipoArma.GLOCK));
         this.inventory.setItem(19, this.armaGui(TipoArma.FS92));
         this.inventory.setItem(20, this.armaGui(TipoArma.PX4));
         this.inventory.setItem(22, this.armaGui(TipoArma.SPAS15));
         this.inventory.setItem(23, this.armaGui(TipoArma.BARRETT));
         this.inventory.setItem(24, this.armaGui(TipoArma.SAKO));
         TipoArma[] caricatori = new TipoArma[]{TipoArma.GLOCK, TipoArma.FS92, TipoArma.PX4, TipoArma.MP5, TipoArma.PM12, TipoArma.PMX, TipoArma.M4A1, TipoArma.AUG, TipoArma.SC7090, TipoArma.HK416, TipoArma.ARX200, TipoArma.SPAS15, TipoArma.BARRETT, TipoArma.SAKO};

         for(int i = 0; i < caricatori.length; ++i) {
            this.inventory.setItem(27 + i, this.conLore(ItemFactory.createCaricatore(caricatori[i], 0), List.of(this.clickPerRicevere())));
         }

         this.inventory.setItem(50, this.conLore(ItemFactory.createDardoTaser(16), List.of(this.clickPerRicevere())));
         Munizione[] munizioni = new Munizione[]{Munizione.NOVE_MM, Munizione.NATO_556, Munizione.NATO_762, Munizione.POMPA, Munizione.CECCHINO};

         for(int i = 0; i < munizioni.length; ++i) {
            this.inventory.setItem(45 + i, this.munizioneGui(munizioni[i]));
         }

      }
   }

   private void riempiPagina2() {
      this.inventory.setItem(10, this.conLore(ItemFactory.createAsciaVdf(), List.of(this.clickPerRicevere())));
      this.inventory.setItem(11, this.conLore(ItemFactory.createAriete(), List.of(this.clickPerRicevere())));
      this.inventory.setItem(12, this.conLore(ItemFactory.createPiedeDiPorco(), List.of(this.clickPerRicevere())));
      this.inventory.setItem(45, ItemFactory.createPaginaIndietro());
   }

   private ItemStack armaGui(TipoArma tipo) {
      List<Component> lore = new ArrayList();
      lore.add(this.riga("Danno: ", NamedTextColor.GRAY, String.format(Locale.ROOT, "%.1f", tipo.getDannoTesta()), NamedTextColor.RED));
      lore.add(this.riga("Tipo: ", NamedTextColor.GRAY, tipo.isAutomatica() ? "Automatica" : "Semi-Auto", NamedTextColor.YELLOW));
      lore.add(this.riga("Capienza: ", NamedTextColor.GRAY, tipo.getCapacita() + " colpi", NamedTextColor.AQUA));
      lore.add(Component.empty());
      lore.add(this.clickPerRicevere());
      return this.conLore(ItemFactory.createArma(tipo), lore);
   }

   private ItemStack munizioneGui(Munizione tipo) {
      List<String> armeCheLaUsano = new ArrayList();

      for(TipoArma t : TipoArma.values()) {
         if (t.getMunizione() == tipo) {
            armeCheLaUsano.add(t.getNome());
         }
      }

      List<Component> lore = new ArrayList();
      lore.add(Component.text("Usata da:", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
      lore.add(Component.text(String.join(", ", armeCheLaUsano), NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
      lore.add(Component.empty());
      lore.add(Component.text("▶ Click per ricevere 64 munizioni", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
      return this.conLore(ItemFactory.createMunizioni(tipo, 64), lore);
   }

   private Component riga(String etichetta, NamedTextColor coloreEtichetta, String valore, NamedTextColor coloreValore) {
      return ((TextComponent)Component.text(etichetta, coloreEtichetta).append(Component.text(valore, coloreValore))).decoration(TextDecoration.ITALIC, false);
   }

   private Component clickPerRicevere() {
      return Component.text("▶ Click per ricevere", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false);
   }

   private ItemStack conLore(ItemStack item, List<Component> lore) {
      ItemMeta meta = item.getItemMeta();
      meta.lore(lore);
      item.setItemMeta(meta);
      return item;
   }

   public void mostraPagina(int nuovaPagina) {
      this.pagina = Math.max(1, Math.min(2, nuovaPagina));
      this.riempi();
   }

   public int getPagina() {
      return this.pagina;
   }

   public Inventory getInventory() {
      return this.inventory;
   }

   public static void apri(Player player) {
      player.openInventory((new ArmiGUI()).getInventory());
   }
}
