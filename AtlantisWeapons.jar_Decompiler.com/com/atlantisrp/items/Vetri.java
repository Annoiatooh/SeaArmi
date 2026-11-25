package com.atlantisrp.items;

import com.atlantisrp.Main;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class Vetri {
   private static final long RIGENERA_TICK = 200L;
   private static final boolean SOLO_PISTOLE = true;
   private static final List<InAttesa> LISTA = new ArrayList();

   private Vetri() {
   }

   public static void colpisci(Player player, TipoArma tipo, Location occhi, Vector dir, double gittata) {
      if (tipo.isPistola()) {
         RayTraceResult r = player.getWorld().rayTraceBlocks(occhi, dir, gittata, FluidCollisionMode.NEVER, false);
         if (r != null && r.getHitBlock() != null) {
            Block b = r.getHitBlock();
            if (isVetro(b.getType())) {
               BlockData dati = b.getBlockData();
               Location centro = b.getLocation().add((double)0.5F, (double)0.5F, (double)0.5F);
               b.getWorld().playSound(centro, Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
               b.getWorld().spawnParticle(Particle.BLOCK, centro, 25, 0.3, 0.3, 0.3, dati);
               b.setType(Material.AIR, false);
               InAttesa[] voce = new InAttesa[1];
               BukkitTask task = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                  ripristina(voce[0]);
                  LISTA.remove(voce[0]);
               }, 200L);
               voce[0] = new InAttesa(b, dati, task);
               LISTA.add(voce[0]);
            }
         }
      }
   }

   private static void ripristina(InAttesa v) {
      if (v != null) {
         if (v.blocco().getType().isAir()) {
            v.blocco().setBlockData(v.dati(), false);
         }

      }
   }

   public static void ripristinaTutti() {
      for(InAttesa v : new ArrayList(LISTA)) {
         v.task().cancel();
         ripristina(v);
      }

      LISTA.clear();
   }

   private static boolean isVetro(Material m) {
      if (!m.isBlock()) {
         return false;
      } else {
         String n = m.name();
         return n.endsWith("GLASS") || n.endsWith("GLASS_PANE");
      }
   }

   private static record InAttesa(Block blocco, BlockData dati, BukkitTask task) {
   }
}
