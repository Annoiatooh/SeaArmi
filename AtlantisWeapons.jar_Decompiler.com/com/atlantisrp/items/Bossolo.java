package com.atlantisrp.items;

import com.atlantisrp.Main;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class Bossolo {
   private static final float SCALA = 0.25F;
   private static final int DURATA_TICK = 40;
   private static final double GRAVITA = 0.06;
   private static final float ROTAZIONE = 35.0F;

   public static void espelli(Player player) {
      Location occhi = player.getEyeLocation();
      Vector dir = occhi.getDirection().normalize();
      Vector destra = dir.clone().crossProduct(new Vector(0, 1, 0));
      if (destra.lengthSquared() < 1.0E-4) {
         destra = new Vector(1, 0, 0);
      }

      destra.normalize();
      final Location posizione = occhi.clone().add(dir.clone().multiply((double)0.5F)).add(destra.clone().multiply(0.3)).add((double)0.0F, -0.3, (double)0.0F);
      final ItemDisplay display = (ItemDisplay)posizione.getWorld().spawn(posizione, ItemDisplay.class, (d) -> {
         d.setItemStack(ItemFactory.createBossolo());
         d.setItemDisplayTransform(ItemDisplayTransform.FIXED);
         d.setBillboard(Billboard.FIXED);
         d.setTransformation(new Transformation(new Vector3f(0.0F, 0.0F, 0.0F), new AxisAngle4f(), new Vector3f(0.25F, 0.25F, 0.25F), new AxisAngle4f()));
         d.setViewRange(0.3F);
         d.setPersistent(false);
      });
      final Vector velocita = destra.clone().multiply(0.12).add(new Vector((double)0.0F, 0.15, (double)0.0F));
      (new BukkitRunnable() {
         int tick = 0;
         boolean atterrato = false;
         final Location pos = posizione.clone();

         public void run() {
            if (display.isValid() && this.tick < 40) {
               ++this.tick;
               if (!this.atterrato) {
                  velocita.setY(velocita.getY() - 0.06);
                  Location prossima = this.pos.clone().add(velocita);
                  Block blocco = prossima.getBlock();
                  if (blocco.getType().isSolid()) {
                     this.atterrato = true;
                  } else {
                     this.pos.setX(prossima.getX());
                     this.pos.setY(prossima.getY());
                     this.pos.setZ(prossima.getZ());
                     this.pos.setYaw(this.pos.getYaw() + 35.0F);
                     display.teleport(this.pos);
                  }
               }

            } else {
               display.remove();
               this.cancel();
            }
         }
      }).runTaskTimer(Main.getInstance(), 1L, 1L);
   }
}
