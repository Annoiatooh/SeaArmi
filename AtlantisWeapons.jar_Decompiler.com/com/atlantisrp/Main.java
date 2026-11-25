package com.atlantisrp;

import com.atlantisrp.commands.ArmiCommand;
import com.atlantisrp.commands.LogCommand;
import com.atlantisrp.commands.RapinaCommand;
import com.atlantisrp.commands.SmanettaCommand;
import com.atlantisrp.commands.StaffLegaturaCommand;
import com.atlantisrp.gui.GUIListener;
import com.atlantisrp.items.StatoArmi;
import com.atlantisrp.items.Vetri;
import com.atlantisrp.listeners.ARX200Listener;
import com.atlantisrp.listeners.AUGListener;
import com.atlantisrp.listeners.AmmanettaListener;
import com.atlantisrp.listeners.BarrettListener;
import com.atlantisrp.listeners.ColpitoListener;
import com.atlantisrp.listeners.FS92Listener;
import com.atlantisrp.listeners.FumogenoListener;
import com.atlantisrp.listeners.GiubbottoPesanteListener;
import com.atlantisrp.listeners.GunListener;
import com.atlantisrp.listeners.HK416Listener;
import com.atlantisrp.listeners.HudArmi;
import com.atlantisrp.listeners.M4A1Listener;
import com.atlantisrp.listeners.MP5Listener;
import com.atlantisrp.listeners.PM12Listener;
import com.atlantisrp.listeners.PMXListener;
import com.atlantisrp.listeners.PX4Listener;
import com.atlantisrp.listeners.PassamontagnaListener;
import com.atlantisrp.listeners.SC7090Listener;
import com.atlantisrp.listeners.SPAS15Listener;
import com.atlantisrp.listeners.SakoListener;
import com.atlantisrp.listeners.TaserListener;
import com.atlantisrp.listeners.WeaponProtectionListener;
import com.atlantisrp.logs.MorteLogListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
   private static Main instance;

   public void onEnable() {
      instance = this;
      StatoArmi.carica();
      this.getServer().getScheduler().runTaskTimer(this, StatoArmi::salva, 6000L, 6000L);
      this.getCommand("armi").setExecutor(new ArmiCommand());
      this.getCommand("log").setExecutor(new LogCommand());
      this.getCommand("smanetta").setExecutor(new SmanettaCommand());
      StaffLegaturaCommand staff = new StaffLegaturaCommand();
      this.getCommand("adminsmanetta").setExecutor(staff);
      this.getCommand("adminscorda").setExecutor(staff);
      RapinaCommand rapina = new RapinaCommand();
      this.getCommand("rapina").setExecutor(rapina);
      this.getCommand("controllo").setExecutor(rapina);
      this.getServer().getPluginManager().registerEvents(rapina, this);
      RapinaCommand.avvia(this);
      this.getServer().getPluginManager().registerEvents(new MorteLogListener(), this);
      this.getServer().getPluginManager().registerEvents(new GUIListener(), this);
      this.getServer().getPluginManager().registerEvents(new WeaponProtectionListener(), this);
      this.getServer().getPluginManager().registerEvents(new GunListener(), this);
      this.getServer().getPluginManager().registerEvents(new FS92Listener(), this);
      this.getServer().getPluginManager().registerEvents(new PX4Listener(), this);
      this.getServer().getPluginManager().registerEvents(new MP5Listener(), this);
      this.getServer().getPluginManager().registerEvents(new PM12Listener(), this);
      this.getServer().getPluginManager().registerEvents(new PMXListener(), this);
      this.getServer().getPluginManager().registerEvents(new M4A1Listener(), this);
      this.getServer().getPluginManager().registerEvents(new AUGListener(), this);
      this.getServer().getPluginManager().registerEvents(new SC7090Listener(), this);
      this.getServer().getPluginManager().registerEvents(new HK416Listener(), this);
      this.getServer().getPluginManager().registerEvents(new ARX200Listener(), this);
      this.getServer().getPluginManager().registerEvents(new SPAS15Listener(), this);
      this.getServer().getPluginManager().registerEvents(new BarrettListener(), this);
      this.getServer().getPluginManager().registerEvents(new SakoListener(), this);
      HudArmi.avvia(this);
      this.getServer().getPluginManager().registerEvents(new PassamontagnaListener(), this);
      PassamontagnaListener.avvia(this);
      this.getServer().getPluginManager().registerEvents(new AmmanettaListener(), this);
      AmmanettaListener.avvia(this);
      this.getServer().getPluginManager().registerEvents(new FumogenoListener(), this);
      this.getServer().getPluginManager().registerEvents(new GiubbottoPesanteListener(), this);
      GiubbottoPesanteListener.avvia(this);
      FumogenoListener.avvia(this);
      this.getServer().getPluginManager().registerEvents(new TaserListener(), this);
      this.getServer().getPluginManager().registerEvents(new ColpitoListener(), this);
      StringBuilder nomi = new StringBuilder();

      for(Plugin pl : this.getServer().getPluginManager().getPlugins()) {
         if (nomi.length() > 0) {
            nomi.append(", ");
         }

         nomi.append(pl.getName());
      }

      this.getLogger().info("Plugin presenti sul server: " + String.valueOf(nomi));
      this.getLogger().info("AtlantisWeapons build cooldown-listener: 14 listener armi registrati");
   }

   public void onDisable() {
      StatoArmi.salva();
      PassamontagnaListener.ripristinaTutti();
      FumogenoListener.rimuoviTutti();
      SmanettaCommand.rimuoviTutti();
      Vetri.ripristinaTutti();
   }

   public static Main getInstance() {
      return instance;
   }
}
