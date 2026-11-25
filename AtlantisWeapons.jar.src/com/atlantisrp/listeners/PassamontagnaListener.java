/*     */ package com.atlantisrp.listeners;
/*     */ import com.atlantisrp.Main;
/*     */ import com.atlantisrp.items.ItemFactory;
/*     */ import io.papermc.paper.event.player.AsyncChatEvent;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import net.kyori.adventure.text.format.NamedTextColor;
/*     */ import net.kyori.adventure.text.format.TextColor;
/*     */ import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.entity.Display;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.entity.TextDisplay;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.inventory.ClickType;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryType;
/*     */ import org.bukkit.event.player.AsyncPlayerChatEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.persistence.PersistentDataType;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scoreboard.Scoreboard;
/*     */ import org.bukkit.scoreboard.Team;
/*     */ import org.bukkit.util.Transformation;
/*     */ import org.joml.AxisAngle4f;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class PassamontagnaListener implements Listener {
/*     */   private static final String NOME_TEAM = "atl_passamontagna";
/*     */   private static final int SLOT_ELMO = 39;
/*  42 */   private static final Map<UUID, String> NASCOSTI = new HashMap<>();
/*     */ 
/*     */   
/*  45 */   private static final Map<UUID, Component> NOME_ORIGINALE = new HashMap<>();
/*     */   
/*     */   private static final String NOME_ANONIMO = "Anonimo";
/*     */   
/*     */   private static final float ALTEZZA_NICK = 0.4F;
/*     */   
/*     */   public static void avvia(JavaPlugin plugin) {
/*  52 */     Team vecchio = scoreboard().getTeam("atl_passamontagna");
/*  53 */     if (vecchio != null) vecchio.unregister();
/*     */     
/*  55 */     Bukkit.getScheduler().runTaskTimer((Plugin)plugin, () -> { for (Player p : Bukkit.getOnlinePlayers()) { boolean haIlPassamontagna = isIndossabileTesta(p.getInventory().getHelmet()); boolean nascosto = NASCOSTI.containsKey(p.getUniqueId()); if (haIlPassamontagna && !nascosto) { nascondi(p); continue; }  if (!haIlPassamontagna && nascosto) { mostra(p); continue; }  if (haIlPassamontagna) { Team team = scoreboard().getTeam("atl_passamontagna"); Team attuale = scoreboard().getEntryTeam(p.getName()); if (team != null && attuale != team) team.addEntry(p.getName());  nascondiTagAltrui(p); controllaEtichetta(p); if (!"Anonimo".equals(PlainTextComponentSerializer.plainText().serialize(p.displayName()))) p.displayName((Component)Component.text("Anonimo", (TextColor)NamedTextColor.WHITE));  }  }  }10L, 10L);
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
/*     */   public static void ripristinaTutti() {
/*  81 */     for (Player p : Bukkit.getOnlinePlayers()) {
/*  82 */       if (NASCOSTI.containsKey(p.getUniqueId())) mostra(p); 
/*     */     } 
/*  84 */     Team t = scoreboard().getTeam("atl_passamontagna");
/*  85 */     if (t != null) t.unregister(); 
/*     */   }
/*     */   
/*     */   private static Scoreboard scoreboard() {
/*  89 */     return Bukkit.getScoreboardManager().getMainScoreboard();
/*     */   }
/*     */   
/*     */   private static void nascondi(Player p) {
/*  93 */     Scoreboard sb = scoreboard();
/*  94 */     Team team = sb.getTeam("atl_passamontagna");
/*  95 */     if (team == null) {
/*  96 */       team = sb.registerNewTeam("atl_passamontagna");
/*  97 */       team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
/*     */     } 
/*  99 */     Team precedente = sb.getEntryTeam(p.getName());
/* 100 */     NASCOSTI.put(p.getUniqueId(), (precedente == null) ? null : precedente.getName());
/* 101 */     team.addEntry(p.getName());
/*     */     
/* 103 */     creaEtichetta(p);
/* 104 */     nascondiTagAltrui(p);
/*     */ 
/*     */     
/* 107 */     NOME_ORIGINALE.put(p.getUniqueId(), p.displayName());
/* 108 */     p.displayName((Component)Component.text("Anonimo", (TextColor)NamedTextColor.WHITE));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   private static final Map<UUID, TextDisplay> ETICHETTE = new HashMap<>();
/*     */   
/*     */   private static void creaEtichetta(Player p) {
/* 116 */     rimuoviEtichetta(p);
/* 117 */     TextDisplay d = (TextDisplay)p.getWorld().spawn(p.getLocation(), TextDisplay.class, t -> {
/*     */           t.text((Component)Component.text("Anonimo", (TextColor)NamedTextColor.WHITE));
/*     */           
/*     */           t.setBillboard(Display.Billboard.CENTER);
/*     */           
/*     */           t.setPersistent(false);
/*     */           t.setTransformation(new Transformation(new Vector3f(0.0F, 0.4F, 0.0F), new AxisAngle4f(), new Vector3f(1.0F, 1.0F, 1.0F), new AxisAngle4f()));
/*     */         });
/* 125 */     p.addPassenger((Entity)d);
/* 126 */     p.hideEntity((Plugin)Main.getInstance(), (Entity)d);
/* 127 */     ETICHETTE.put(p.getUniqueId(), d);
/*     */   }
/*     */   
/*     */   private static void controllaEtichetta(Player p) {
/* 131 */     TextDisplay d = ETICHETTE.get(p.getUniqueId());
/* 132 */     if (d == null || !d.isValid() || !p.getPassengers().contains(d)) creaEtichetta(p); 
/*     */   }
/*     */   
/*     */   private static void rimuoviEtichetta(Player p) {
/* 136 */     TextDisplay d = ETICHETTE.remove(p.getUniqueId());
/* 137 */     if (d != null) d.remove();
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private static void tab(Player p, boolean nascondi) {
/*     */     try {
/* 144 */       Class<?> api = Class.forName("me.neznamy.tab.api.TabAPI");
/* 145 */       Object tab = api.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
/* 146 */       Object tp = api.getMethod("getPlayer", new Class[] { UUID.class }).invoke(tab, new Object[] { p.getUniqueId() });
/* 147 */       Object nt = api.getMethod("getNameTagManager", new Class[0]).invoke(tab, new Object[0]);
/* 148 */       if (tp == null || nt == null)
/* 149 */         return;  Class<?> ntc = Class.forName("me.neznamy.tab.api.nametag.NameTagManager");
/* 150 */       Class<?> tpc = Class.forName("me.neznamy.tab.api.TabPlayer");
/* 151 */       ntc.getMethod(nascondi ? "hideNameTag" : "showNameTag", new Class[] { tpc }).invoke(nt, new Object[] { tp });
/* 152 */     } catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void nascondiTagAltrui(Player p) {
/* 158 */     tab(p, true);
/* 159 */     TextDisplay nostra = ETICHETTE.get(p.getUniqueId());
/* 160 */     for (Entity e : p.getPassengers()) {
/* 161 */       if (e.equals(nostra) || !(e instanceof Display))
/* 162 */         continue;  for (Player v : Bukkit.getOnlinePlayers()) {
/* 163 */         if (!v.equals(p)) v.hideEntity((Plugin)Main.getInstance(), e); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void mostraTagAltrui(Player p) {
/* 169 */     tab(p, false);
/* 170 */     for (Entity e : p.getPassengers()) {
/* 171 */       if (!(e instanceof Display))
/* 172 */         continue;  for (Player v : Bukkit.getOnlinePlayers()) {
/* 173 */         if (!v.equals(p)) v.showEntity((Plugin)Main.getInstance(), e); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void mostra(Player p) {
/* 179 */     rimuoviEtichetta(p);
/* 180 */     mostraTagAltrui(p);
/* 181 */     Scoreboard sb = scoreboard();
/* 182 */     Team team = sb.getTeam("atl_passamontagna");
/* 183 */     if (team != null) team.removeEntry(p.getName());
/*     */ 
/*     */     
/* 186 */     Component originale = NOME_ORIGINALE.remove(p.getUniqueId());
/* 187 */     p.displayName((originale == null) ? (Component)Component.text(p.getName()) : originale);
/*     */ 
/*     */     
/* 190 */     String precedente = NASCOSTI.remove(p.getUniqueId());
/* 191 */     if (precedente != null) {
/* 192 */       Team t = sb.getTeam(precedente);
/* 193 */       if (t != null) t.addEntry(p.getName()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onQuit(PlayerQuitEvent event) {
/* 199 */     if (NASCOSTI.containsKey(event.getPlayer().getUniqueId())) mostra(event.getPlayer());
/*     */   
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onInteract(PlayerInteractEvent event) {
/* 205 */     if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
/*     */       return; 
/* 207 */     ItemStack item = event.getItem();
/* 208 */     if (!isIndossabileTesta(item))
/*     */       return; 
/* 210 */     event.setCancelled(true);
/*     */     
/* 212 */     Player player = event.getPlayer();
/* 213 */     if (AmmanettaListener.isAmmanettato(player.getUniqueId()))
/* 214 */       return;  PlayerInventory inv = player.getInventory();
/* 215 */     ItemStack elmoVecchio = inv.getHelmet();
/*     */     
/* 217 */     inv.setHelmet(item.clone());
/*     */     
/* 219 */     if (event.getHand() == EquipmentSlot.OFF_HAND) {
/* 220 */       inv.setItemInOffHand(elmoVecchio);
/*     */     } else {
/* 222 */       inv.setItemInMainHand(elmoVecchio);
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onClick(InventoryClickEvent event) {
/*     */     Player player;
/* 229 */     HumanEntity humanEntity = event.getWhoClicked(); if (humanEntity instanceof Player) { player = (Player)humanEntity; }
/*     */     else { return; }
/* 231 */      if (event.getView().getTopInventory().getHolder() instanceof com.atlantisrp.gui.ArmiGUI)
/*     */       return; 
/* 233 */     if (AmmanettaListener.isAmmanettato(player.getUniqueId()))
/* 234 */       return;  PlayerInventory inv = player.getInventory();
/* 235 */     if (event.getClickedInventory() != inv) {
/*     */       return;
/*     */     }
/* 238 */     if ((event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.SHIFT_LEFT) && event
/* 239 */       .getSlotType() != InventoryType.SlotType.ARMOR && 
/* 240 */       isGiubbotto(event.getCurrentItem())) {
/* 241 */       event.setCancelled(true);
/* 242 */       ItemStack vecchio = inv.getChestplate();
/* 243 */       inv.setChestplate(event.getCurrentItem().clone());
/* 244 */       event.setCurrentItem(vecchio);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 249 */     if ((event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.SHIFT_LEFT) && event
/* 250 */       .getSlotType() != InventoryType.SlotType.ARMOR && 
/* 251 */       isIndossabileTesta(event.getCurrentItem())) {
/* 252 */       event.setCancelled(true);
/* 253 */       ItemStack elmoVecchio = inv.getHelmet();
/* 254 */       inv.setHelmet(event.getCurrentItem().clone());
/* 255 */       event.setCurrentItem(elmoVecchio);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 260 */     if (event.getSlotType() == InventoryType.SlotType.ARMOR && event.getSlot() == 39) {
/* 261 */       ItemStack inCursore = event.getCursor();
/* 262 */       if (isIndossabileTesta(inCursore)) {
/* 263 */         event.setCancelled(true);
/* 264 */         ItemStack elmoVecchio = inv.getHelmet();
/* 265 */         inv.setHelmet(inCursore.clone());
/* 266 */         event.setCursor(elmoVecchio);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 271 */       if (event.getClick() == ClickType.NUMBER_KEY) {
/* 272 */         ItemStack dallaHotbar = inv.getItem(event.getHotbarButton());
/* 273 */         if (isIndossabileTesta(dallaHotbar)) {
/* 274 */           event.setCancelled(true);
/* 275 */           ItemStack elmoVecchio = inv.getHelmet();
/* 276 */           inv.setHelmet(dallaHotbar.clone());
/* 277 */           inv.setItem(event.getHotbarButton(), elmoVecchio);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isIndossabileTesta(ItemStack item) {
/* 285 */     if (item == null || item.getItemMeta() == null) return false; 
/* 286 */     String t = (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
/* 287 */     return ("passamontagna".equals(t) || "visore".equals(t));
/*     */   }
/*     */   
/*     */   private static boolean isGiubbotto(ItemStack item) {
/* 291 */     if (item == null || item.getItemMeta() == null) return false; 
/* 292 */     String t = (String)item.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
/* 293 */     return ("giubbotto_leggero".equals(t) || "giubbotto_pesante".equals(t));
/*     */   }
/*     */   
/*     */   private static boolean isPassamontagna(ItemStack item) {
/* 297 */     if (item == null || item.getItemMeta() == null) return false; 
/* 298 */     return "passamontagna".equals(item.getItemMeta().getPersistentDataContainer()
/* 299 */         .get(ItemFactory.KEY_TIPO, PersistentDataType.STRING));
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onChatPaper(AsyncChatEvent event) {
/* 305 */     if (NASCOSTI.containsKey(event.getPlayer().getUniqueId())) {
/* 306 */       event.renderer(ChatRenderer.viewerUnaware((source, nome, messaggio) -> Component.text("Anonimo: ", (TextColor)NamedTextColor.WHITE).append(messaggio)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onChat(AsyncPlayerChatEvent event) {
/* 313 */     if (NASCOSTI.containsKey(event.getPlayer().getUniqueId())) {
/* 314 */       event.setFormat("Anonimo: %2$s");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onJoin(PlayerJoinEvent event) {
/* 321 */     Player p = event.getPlayer();
/* 322 */     Bukkit.getScheduler().runTask((Plugin)Main.getInstance(), () -> {
/*     */           if (p.isOnline() && isIndossabileTesta(p.getInventory().getHelmet()) && !NASCOSTI.containsKey(p.getUniqueId()))
/*     */             nascondi(p); 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\listeners\PassamontagnaListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */