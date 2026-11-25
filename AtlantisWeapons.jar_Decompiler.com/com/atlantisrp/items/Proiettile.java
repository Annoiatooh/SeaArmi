package com.atlantisrp.items;

import com.atlantisrp.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class Proiettile {
   private static final int CMD = 91;
   private static final float SCALA = 0.5F;
   private static final double VELOCITA = (double)8.0F;
   private static final boolean INVERTI_PUNTA = false;

   public static void lancia(Location partenza, Vector dir, double distanza) {
      final Vector direzione = dir.clone().normalize();
      final Location inizio = partenza.clone().add(direzione.clone().multiply((double)1.0F)).add((double)0.0F, -0.15, (double)0.0F);
      inizio.setDirection(direzione);
      ItemStack stick = new ItemStack(Material.STICK);
      ItemMeta meta = stick.getItemMeta();
      meta.setCustomModelData(91);
      stick.setItemMeta(meta);
      final ItemDisplay display = (ItemDisplay)inizio.getWorld().spawn(inizio, ItemDisplay.class, (d) -> {
         d.setItemStack(stick);
         d.setItemDisplayTransform(ItemDisplayTransform.FIXED);
         d.setBillboard(Billboard.FIXED);
         float angolo = (float)Math.toRadians((double)90.0F);
         d.setTransformation(new Transformation(new Vector3f(0.0F, 0.0F, 0.0F), new AxisAngle4f(angolo, 1.0F, 0.0F, 0.0F), new Vector3f(0.5F, 0.5F, 0.5F), new AxisAngle4f()));
         d.setTeleportDuration(1);
         d.setViewRange(1.0F);
         d.setPersistent(false);
      });
      final double daPercorrere = Math.max((double)1.0F, distanza - (double)1.0F);
      (new BukkitRunnable() {
         double percorso = (double)0.0F;

         public void run() {
            if (display.isValid() && !(this.percorso >= daPercorrere)) {
               this.percorso = Math.min(daPercorrere, this.percorso + (double)8.0F);
               Location punto = inizio.clone().add(direzione.clone().multiply(this.percorso));
               punto.setDirection(direzione);
               display.teleport(punto);
            } else {
               display.remove();
               this.cancel();
            }
         }
      }).runTaskTimer(Main.getInstance(), 1L, 1L);
   }
}
