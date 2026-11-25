/*    */ package com.atlantisrp.items;
/*    */ 
/*    */ import com.atlantisrp.Main;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.configuration.ConfigurationSection;
/*    */ import org.bukkit.configuration.file.YamlConfiguration;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.ItemMeta;
/*    */ import org.bukkit.persistence.PersistentDataContainer;
/*    */ import org.bukkit.persistence.PersistentDataType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StatoArmi
/*    */ {
/*    */   public static class Stato
/*    */   {
/*    */     public int munizioni;
/*    */     public boolean haCaricatore;
/*    */     
/*    */     Stato(int munizioni, boolean haCaricatore) {
/* 28 */       this.munizioni = munizioni;
/* 29 */       this.haCaricatore = haCaricatore;
/*    */     }
/*    */   }
/*    */   
/* 33 */   private static final Map<String, Stato> STATI = new HashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public static Stato di(ItemStack arma) {
/* 38 */     if (arma == null || arma.getItemMeta() == null) return new Stato(0, false); 
/* 39 */     PersistentDataContainer pdc = arma.getItemMeta().getPersistentDataContainer();
/* 40 */     String id = (String)pdc.get(ItemFactory.KEY_ID, PersistentDataType.STRING);
/* 41 */     if (id == null) {
/*    */       
/* 43 */       TipoArma tipo = TipoArma.da((String)pdc.get(ItemFactory.KEY_TIPO, PersistentDataType.STRING));
/* 44 */       if (tipo == null) return new Stato(0, false);
/*    */       
/* 46 */       int munizioni = ((Integer)pdc.getOrDefault(ItemFactory.KEY_MUNIZIONI, PersistentDataType.INTEGER, Integer.valueOf(tipo.getCapacita()))).intValue();
/* 47 */       boolean ha = (((Byte)pdc.getOrDefault(ItemFactory.KEY_HA_CARICATORE, PersistentDataType.BYTE, Byte.valueOf((byte)1))).byteValue() == 1);
/*    */       
/* 49 */       id = UUID.randomUUID().toString();
/* 50 */       ItemMeta meta = arma.getItemMeta();
/* 51 */       meta.getPersistentDataContainer().set(ItemFactory.KEY_ID, PersistentDataType.STRING, id);
/* 52 */       arma.setItemMeta(meta);
/*    */       
/* 54 */       Stato nuovo = new Stato(munizioni, ha);
/* 55 */       STATI.put(id, nuovo);
/* 56 */       return nuovo;
/*    */     } 
/*    */     
/* 59 */     Stato s = STATI.get(id);
/* 60 */     if (s == null) {
/* 61 */       TipoArma tipo = TipoArma.da((String)pdc.get(ItemFactory.KEY_TIPO, PersistentDataType.STRING));
/* 62 */       s = new Stato((tipo == null) ? 0 : tipo.getCapacita(), true);
/* 63 */       STATI.put(id, s);
/*    */     } 
/* 65 */     return s;
/*    */   }
/*    */ 
/*    */   
/*    */   private static File file() {
/* 70 */     return new File(Main.getInstance().getDataFolder(), "stato_armi.yml");
/*    */   }
/*    */   
/*    */   public static void carica() {
/* 74 */     STATI.clear();
/* 75 */     File f = file();
/* 76 */     if (!f.exists())
/* 77 */       return;  YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
/* 78 */     ConfigurationSection sez = yml.getConfigurationSection("armi");
/* 79 */     if (sez == null)
/* 80 */       return;  for (String id : sez.getKeys(false)) {
/* 81 */       STATI.put(id, new Stato(sez.getInt(id + ".munizioni"), sez.getBoolean(id + ".caricatore")));
/*    */     }
/*    */   }
/*    */   
/*    */   public static void salva() {
/* 86 */     YamlConfiguration yml = new YamlConfiguration();
/* 87 */     for (Map.Entry<String, Stato> e : STATI.entrySet()) {
/* 88 */       yml.set("armi." + (String)e.getKey() + ".munizioni", Integer.valueOf(((Stato)e.getValue()).munizioni));
/* 89 */       yml.set("armi." + (String)e.getKey() + ".caricatore", Boolean.valueOf(((Stato)e.getValue()).haCaricatore));
/*    */     } 
/*    */     try {
/* 92 */       Main.getInstance().getDataFolder().mkdirs();
/* 93 */       yml.save(file());
/* 94 */     } catch (IOException ex) {
/* 95 */       Main.getInstance().getLogger().warning("Impossibile salvare stato_armi.yml: " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\items\StatoArmi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */