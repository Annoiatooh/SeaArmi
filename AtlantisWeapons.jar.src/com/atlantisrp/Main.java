/*     */ package com.atlantisrp;
/*     */ 
/*     */ import com.atlantisrp.commands.ArmiCommand;
/*     */ import com.atlantisrp.commands.LogCommand;
/*     */ import com.atlantisrp.commands.RapinaCommand;
/*     */ import com.atlantisrp.commands.SmanettaCommand;
/*     */ import com.atlantisrp.commands.StaffLegaturaCommand;
/*     */ import com.atlantisrp.gui.GUIListener;
/*     */ import com.atlantisrp.items.StatoArmi;
/*     */ import com.atlantisrp.items.Vetri;
/*     */ import com.atlantisrp.listeners.ARX200Listener;
/*     */ import com.atlantisrp.listeners.AUGListener;
/*     */ import com.atlantisrp.listeners.AmmanettaListener;
/*     */ import com.atlantisrp.listeners.BarrettListener;
/*     */ import com.atlantisrp.listeners.ColpitoListener;
/*     */ import com.atlantisrp.listeners.FS92Listener;
/*     */ import com.atlantisrp.listeners.FumogenoListener;
/*     */ import com.atlantisrp.listeners.GiubbottoPesanteListener;
/*     */ import com.atlantisrp.listeners.GunListener;
/*     */ import com.atlantisrp.listeners.HK416Listener;
/*     */ import com.atlantisrp.listeners.HudArmi;
/*     */ import com.atlantisrp.listeners.M4A1Listener;
/*     */ import com.atlantisrp.listeners.MP5Listener;
/*     */ import com.atlantisrp.listeners.PM12Listener;
/*     */ import com.atlantisrp.listeners.PMXListener;
/*     */ import com.atlantisrp.listeners.PX4Listener;
/*     */ import com.atlantisrp.listeners.PassamontagnaListener;
/*     */ import com.atlantisrp.listeners.SC7090Listener;
/*     */ import com.atlantisrp.listeners.SPAS15Listener;
/*     */ import com.atlantisrp.listeners.SakoListener;
/*     */ import com.atlantisrp.listeners.TaserListener;
/*     */ import com.atlantisrp.listeners.WeaponProtectionListener;
/*     */ import com.atlantisrp.logs.MorteLogListener;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ 
/*     */ public class Main extends JavaPlugin {
/*     */   public void onEnable() {
/*  41 */     instance = this;
/*  42 */     StatoArmi.carica();
/*     */     
/*  44 */     getServer().getScheduler().runTaskTimer((Plugin)this, StatoArmi::salva, 6000L, 6000L);
/*  45 */     getCommand("armi").setExecutor((CommandExecutor)new ArmiCommand());
/*  46 */     getCommand("log").setExecutor((CommandExecutor)new LogCommand());
/*  47 */     getCommand("smanetta").setExecutor((CommandExecutor)new SmanettaCommand());
/*  48 */     StaffLegaturaCommand staff = new StaffLegaturaCommand();
/*  49 */     getCommand("adminsmanetta").setExecutor((CommandExecutor)staff);
/*  50 */     getCommand("adminscorda").setExecutor((CommandExecutor)staff);
/*  51 */     RapinaCommand rapina = new RapinaCommand();
/*  52 */     getCommand("rapina").setExecutor((CommandExecutor)rapina);
/*  53 */     getCommand("controllo").setExecutor((CommandExecutor)rapina);
/*  54 */     getServer().getPluginManager().registerEvents((Listener)rapina, (Plugin)this);
/*  55 */     RapinaCommand.avvia(this);
/*  56 */     getServer().getPluginManager().registerEvents((Listener)new MorteLogListener(), (Plugin)this);
/*     */     
/*  58 */     getServer().getPluginManager().registerEvents((Listener)new GUIListener(), (Plugin)this);
/*  59 */     getServer().getPluginManager().registerEvents((Listener)new WeaponProtectionListener(), (Plugin)this);
/*     */ 
/*     */     
/*  62 */     getServer().getPluginManager().registerEvents((Listener)new GunListener(), (Plugin)this);
/*  63 */     getServer().getPluginManager().registerEvents((Listener)new FS92Listener(), (Plugin)this);
/*  64 */     getServer().getPluginManager().registerEvents((Listener)new PX4Listener(), (Plugin)this);
/*  65 */     getServer().getPluginManager().registerEvents((Listener)new MP5Listener(), (Plugin)this);
/*  66 */     getServer().getPluginManager().registerEvents((Listener)new PM12Listener(), (Plugin)this);
/*  67 */     getServer().getPluginManager().registerEvents((Listener)new PMXListener(), (Plugin)this);
/*  68 */     getServer().getPluginManager().registerEvents((Listener)new M4A1Listener(), (Plugin)this);
/*  69 */     getServer().getPluginManager().registerEvents((Listener)new AUGListener(), (Plugin)this);
/*  70 */     getServer().getPluginManager().registerEvents((Listener)new SC7090Listener(), (Plugin)this);
/*  71 */     getServer().getPluginManager().registerEvents((Listener)new HK416Listener(), (Plugin)this);
/*  72 */     getServer().getPluginManager().registerEvents((Listener)new ARX200Listener(), (Plugin)this);
/*  73 */     getServer().getPluginManager().registerEvents((Listener)new SPAS15Listener(), (Plugin)this);
/*  74 */     getServer().getPluginManager().registerEvents((Listener)new BarrettListener(), (Plugin)this);
/*  75 */     getServer().getPluginManager().registerEvents((Listener)new SakoListener(), (Plugin)this);
/*     */     
/*  77 */     HudArmi.avvia(this);
/*     */ 
/*     */     
/*  80 */     getServer().getPluginManager().registerEvents((Listener)new PassamontagnaListener(), (Plugin)this);
/*  81 */     PassamontagnaListener.avvia(this);
/*     */ 
/*     */     
/*  84 */     getServer().getPluginManager().registerEvents((Listener)new AmmanettaListener(), (Plugin)this);
/*  85 */     AmmanettaListener.avvia(this);
/*     */     
/*  87 */     getServer().getPluginManager().registerEvents((Listener)new FumogenoListener(), (Plugin)this);
/*  88 */     getServer().getPluginManager().registerEvents((Listener)new GiubbottoPesanteListener(), (Plugin)this);
/*  89 */     GiubbottoPesanteListener.avvia(this);
/*  90 */     FumogenoListener.avvia(this);
/*  91 */     getServer().getPluginManager().registerEvents((Listener)new TaserListener(), (Plugin)this);
/*  92 */     getServer().getPluginManager().registerEvents((Listener)new ColpitoListener(), (Plugin)this);
/*     */ 
/*     */     
/*  95 */     StringBuilder nomi = new StringBuilder();
/*  96 */     for (Plugin pl : getServer().getPluginManager().getPlugins()) {
/*  97 */       if (nomi.length() > 0) nomi.append(", "); 
/*  98 */       nomi.append(pl.getName());
/*     */     } 
/* 100 */     getLogger().info("Plugin presenti sul server: " + String.valueOf(nomi));
/* 101 */     getLogger().info("AtlantisWeapons build cooldown-listener: 14 listener armi registrati");
/*     */   }
/*     */   private static Main instance;
/*     */   
/*     */   public void onDisable() {
/* 106 */     StatoArmi.salva();
/* 107 */     PassamontagnaListener.ripristinaTutti();
/* 108 */     FumogenoListener.rimuoviTutti();
/* 109 */     SmanettaCommand.rimuoviTutti();
/* 110 */     Vetri.ripristinaTutti();
/*     */   }
/*     */   
/*     */   public static Main getInstance() {
/* 114 */     return instance;
/*     */   }
/*     */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\Main.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */