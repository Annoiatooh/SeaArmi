package com.atlantisrp.items;

import com.atlantisrp.Main;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class StatoArmi {
   private static final Map<String, Stato> STATI = new HashMap();

   public static Stato di(ItemStack arma) {
      if (arma != null && arma.getItemMeta() != null) {
         PersistentDataContainer pdc = arma.getItemMeta().getPersistentDataContainer();
         String id = (String)pdc.get(ItemFactory.KEY_ID, PersistentDataType.STRING);
         if (id == null) {
            TipoArma tipo = TipoArma.da((String)pdc.get(ItemFactory.KEY_TIPO, PersistentDataType.STRING));
            if (tipo == null) {
               return new Stato(0, false);
            } else {
               int munizioni = (Integer)pdc.getOrDefault(ItemFactory.KEY_MUNIZIONI, PersistentDataType.INTEGER, tipo.getCapacita());
               boolean ha = (Byte)pdc.getOrDefault(ItemFactory.KEY_HA_CARICATORE, PersistentDataType.BYTE, (byte)1) == 1;
               id = UUID.randomUUID().toString();
               ItemMeta meta = arma.getItemMeta();
               meta.getPersistentDataContainer().set(ItemFactory.KEY_ID, PersistentDataType.STRING, id);
               arma.setItemMeta(meta);
               Stato nuovo = new Stato(munizioni, ha);
               STATI.put(id, nuovo);
               return nuovo;
            }
         } else {
            Stato s = (Stato)STATI.get(id);
            if (s == null) {
               TipoArma tipo = TipoArma.da((String)pdc.get(ItemFactory.KEY_TIPO, PersistentDataType.STRING));
               s = new Stato(tipo == null ? 0 : tipo.getCapacita(), true);
               STATI.put(id, s);
            }

            return s;
         }
      } else {
         return new Stato(0, false);
      }
   }

   private static File file() {
      return new File(Main.getInstance().getDataFolder(), "stato_armi.yml");
   }

   public static void carica() {
      STATI.clear();
      File f = file();
      if (f.exists()) {
         YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
         ConfigurationSection sez = yml.getConfigurationSection("armi");
         if (sez != null) {
            for(String id : sez.getKeys(false)) {
               STATI.put(id, new Stato(sez.getInt(id + ".munizioni"), sez.getBoolean(id + ".caricatore")));
            }

         }
      }
   }

   public static void salva() {
      YamlConfiguration yml = new YamlConfiguration();

      for(Map.Entry<String, Stato> e : STATI.entrySet()) {
         yml.set("armi." + (String)e.getKey() + ".munizioni", ((Stato)e.getValue()).munizioni);
         yml.set("armi." + (String)e.getKey() + ".caricatore", ((Stato)e.getValue()).haCaricatore);
      }

      try {
         Main.getInstance().getDataFolder().mkdirs();
         yml.save(file());
      } catch (IOException ex) {
         Main.getInstance().getLogger().warning("Impossibile salvare stato_armi.yml: " + ex.getMessage());
      }

   }

   public static class Stato {
      public int munizioni;
      public boolean haCaricatore;

      Stato(int munizioni, boolean haCaricatore) {
         this.munizioni = munizioni;
         this.haCaricatore = haCaricatore;
      }
   }
}
