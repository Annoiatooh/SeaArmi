/*     */ package com.atlantisrp.commands;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import net.kyori.adventure.text.format.NamedTextColor;
/*     */ import net.kyori.adventure.text.format.TextColor;
/*     */ import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryDragEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RapinaCommand
/*     */   implements CommandExecutor, Listener
/*     */ {
/*     */   private static final double RAGGIO = 5.0D;
/*  38 */   private static final Map<UUID, UUID> RAPINE = new HashMap<>();
/*     */   
/*     */   public static class ControlloHolder
/*     */     implements InventoryHolder {
/*     */     final UUID bersaglio;
/*     */     Inventory inventory;
/*     */     
/*     */     ControlloHolder(UUID bersaglio) {
/*  46 */       this.bersaglio = bersaglio;
/*     */     }
/*     */ 
/*     */     
/*     */     public Inventory getInventory() {
/*  51 */       return this.inventory;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void avvia(JavaPlugin plugin) {
/*  56 */     Bukkit.getScheduler().runTaskTimer((Plugin)plugin, () -> { Iterator<Map.Entry<UUID, UUID>> it = RAPINE.entrySet().iterator(); while (it.hasNext()) { Map.Entry<UUID, UUID> e = it.next(); Player rapinatore = Bukkit.getPlayer(e.getKey()); Player vittima = Bukkit.getPlayer(e.getValue()); if (rapinatore == null || vittima == null) it.remove();  }  for (Player p : Bukkit.getOnlinePlayers()) { InventoryHolder patt0$temp = p.getOpenInventory().getTopInventory().getHolder(); if (patt0$temp instanceof ControlloHolder) { ControlloHolder h = (ControlloHolder)patt0$temp; Player v = Bukkit.getPlayer(h.bersaglio); if (v == null) { p.closeInventory(); continue; }  riempi(h.inventory, v); }  }  }10L, 10L);
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
/*     */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
/*     */     Player player;
/*  83 */     if (sender instanceof Player) { player = (Player)sender; }
/*  84 */     else { sender.sendMessage("Solo i giocatori possono usare questo comando.");
/*  85 */       return true; }
/*     */     
/*  87 */     if (args.length != 1) {
/*  88 */       errore(player, "Uso: /" + label + " (nick)");
/*  89 */       return true;
/*     */     } 
/*  91 */     Player bersaglio = Bukkit.getPlayerExact(args[0]);
/*  92 */     if (bersaglio == null) {
/*  93 */       errore(player, "Giocatore non trovato.");
/*  94 */       return true;
/*     */     } 
/*  96 */     if (bersaglio.equals(player)) {
/*  97 */       errore(player, "Non puoi farlo a te stesso.");
/*  98 */       return true;
/*     */     } 
/*     */     
/* 101 */     if (label.equalsIgnoreCase("rapina")) {
/* 102 */       if (!vicini(player, bersaglio)) {
/* 103 */         errore(player, bersaglio.getName() + " è troppo lontano (massimo 5 blocchi).");
/* 104 */         return true;
/*     */       } 
/* 106 */       RAPINE.put(player.getUniqueId(), bersaglio.getUniqueId());
/* 107 */       player.sendMessage((Component)Component.text("Hai iniziato a rapinare " + bersaglio.getName() + ".", (TextColor)NamedTextColor.GREEN));
/* 108 */       azione(player, "sta rapinando", bersaglio);
/* 109 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 114 */     boolean conRapina = bersaglio.getUniqueId().equals(RAPINE.get(player.getUniqueId()));
/* 115 */     boolean fdo = player.hasPermission("atlantisweapons.fdo");
/* 116 */     if (!conRapina && !fdo) {
/* 117 */       errore(player, "Prima devi iniziare una /rapina su " + bersaglio.getName() + ".");
/* 118 */       return true;
/*     */     } 
/* 120 */     if (!conRapina && !vicini(player, bersaglio)) {
/* 121 */       errore(player, bersaglio.getName() + " è troppo lontano (massimo 5 blocchi).");
/* 122 */       return true;
/*     */     } 
/* 124 */     azione(player, "sta controllando", bersaglio);
/*     */     
/* 126 */     ControlloHolder holder = new ControlloHolder(bersaglio.getUniqueId());
/* 127 */     holder.inventory = Bukkit.createInventory(holder, 54, 
/* 128 */         (Component)Component.text("Inventario di " + bersaglio.getName(), (TextColor)NamedTextColor.DARK_GRAY));
/* 129 */     riempi(holder.inventory, bersaglio);
/* 130 */     player.openInventory(holder.inventory);
/* 131 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void azione(Player attore, String verbo, Player bersaglio) {
/* 136 */     String comando = "azione " + nomeVisibile(attore) + " " + verbo + " " + nomeVisibile(bersaglio);
/* 137 */     Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), comando);
/*     */   }
/*     */   
/*     */   private static String nomeVisibile(Player p) {
/* 141 */     return PlainTextComponentSerializer.plainText()
/* 142 */       .serialize(p.displayName());
/*     */   }
/*     */   
/*     */   private static void riempi(Inventory inv, Player v) {
/* 146 */     PlayerInventory pi = v.getInventory();
/* 147 */     inv.clear();
/*     */     int i;
/* 149 */     for (i = 9; i < 36; ) { inv.setItem(i - 9, copia(pi.getItem(i))); i++; }
/* 150 */      for (i = 0; i < 9; ) { inv.setItem(27 + i, copia(pi.getItem(i))); i++; }
/*     */     
/* 152 */     inv.setItem(45, copia(pi.getHelmet()));
/* 153 */     inv.setItem(46, copia(pi.getChestplate()));
/* 154 */     inv.setItem(47, copia(pi.getLeggings()));
/* 155 */     inv.setItem(48, copia(pi.getBoots()));
/* 156 */     inv.setItem(49, copia(pi.getItemInOffHand()));
/*     */   }
/*     */   
/*     */   private static ItemStack copia(ItemStack i) {
/* 160 */     return (i == null || i.getType().isAir()) ? null : i.clone();
/*     */   }
/*     */   
/*     */   private static boolean vicini(Player a, Player b) {
/* 164 */     return (a.getWorld().equals(b.getWorld()) && a.getLocation().distance(b.getLocation()) <= 5.0D);
/*     */   }
/*     */   
/*     */   private static void errore(Player p, String testo) {
/* 168 */     p.sendMessage((Component)Component.text(testo, (TextColor)NamedTextColor.RED));
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onClick(InventoryClickEvent event) {
/* 174 */     if (event.getView().getTopInventory().getHolder() instanceof ControlloHolder) event.setCancelled(true); 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onDrag(InventoryDragEvent event) {
/* 179 */     if (event.getView().getTopInventory().getHolder() instanceof ControlloHolder) event.setCancelled(true); 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onQuit(PlayerQuitEvent event) {
/* 184 */     RAPINE.remove(event.getPlayer().getUniqueId());
/*     */   }
/*     */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\commands\RapinaCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */