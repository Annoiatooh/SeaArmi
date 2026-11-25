package com.atlantisrp.items;

import com.atlantisrp.listeners.ColpitoListener;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class Giubbotto {
   private static final double VITA = (double)20.0F;
   private static final int EXTRA_LEGGERO = 2;
   private static final int EXTRA_PESANTE = 3;
   private static final double MARGINE = 0.02;

   public static double danno(LivingEntity target, TipoArma tipo, boolean testa, double base) {
      if (!(target instanceof Player player)) {
         return base;
      } else {
         ColpitoListener.applica(player);
         byte livello = 0;
         double punti = (double)0.0F;
         double resistenza = (double)0.0F;
         ItemStack petto = player.getInventory().getChestplate();
         if (petto != null && petto.getItemMeta() != null) {
            String id = (String)petto.getItemMeta().getPersistentDataContainer().get(ItemFactory.KEY_TIPO, PersistentDataType.STRING);
            if ("giubbotto_leggero".equals(id)) {
               livello = 1;
               punti = (double)5.0F;
            } else if ("giubbotto_pesante".equals(id)) {
               livello = 2;
               punti = (double)8.0F;
               resistenza = (double)2.0F;
            }
         }

         int colpiTabella = tipo.colpiPerUccidere(testa, livello);
         double voluto;
         if (colpiTabella > 0) {
            voluto = (double)20.0F / (double)colpiTabella + 0.02;
         } else {
            if (livello == 0) {
               return base;
            }

            if (livello == 1 && tipo == TipoArma.HK416) {
               return base;
            }

            int colpi = (int)Math.ceil((double)20.0F / base - 1.0E-9);
            int extra = livello == 1 ? 2 : 3;
            voluto = (double)20.0F / (double)(colpi + extra) + 0.02;
         }

         if (livello == 0) {
            return voluto;
         } else {
            double x = voluto;

            for(int i = 0; i < 5; ++i) {
               double riduzione = Math.min((double)20.0F, Math.max(punti / (double)5.0F, punti - x / ((double)2.0F + resistenza / (double)4.0F))) / (double)25.0F;
               x = voluto / ((double)1.0F - riduzione);
            }

            return x;
         }
      }
   }
}
