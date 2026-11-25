/*    */ package com.atlantisrp.items;
/*    */ 
/*    */ import com.atlantisrp.Main;
/*    */ import java.util.ArrayList;
/*    */ import org.bukkit.FluidCollisionMode;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.Sound;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.block.data.BlockData;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.scheduler.BukkitTask;
/*    */ import org.bukkit.util.RayTraceResult;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ public final class Vetri {
/*    */   private static final long RIGENERA_TICK = 200L;
/*    */   private static final boolean SOLO_PISTOLE = true;
/*    */   
/*    */   private static final class InAttesa extends Record { private final Block blocco;
/*    */     private final BlockData dati;
/*    */     private final BukkitTask task;
/*    */     
/* 25 */     private InAttesa(Block blocco, BlockData dati, BukkitTask task) { this.blocco = blocco; this.dati = dati; this.task = task; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/atlantisrp/items/Vetri$InAttesa;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 25 */       //   0	7	0	this	Lcom/atlantisrp/items/Vetri$InAttesa; } public Block blocco() { return this.blocco; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/atlantisrp/items/Vetri$InAttesa;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/atlantisrp/items/Vetri$InAttesa; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/atlantisrp/items/Vetri$InAttesa;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/atlantisrp/items/Vetri$InAttesa;
/* 25 */       //   0	8	1	o	Ljava/lang/Object; } public BlockData dati() { return this.dati; } public BukkitTask task() { return this.task; }
/* 26 */      } private static final List<InAttesa> LISTA = new ArrayList<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public static void colpisci(Player player, TipoArma tipo, Location occhi, Vector dir, double gittata) {
/* 31 */     if (!tipo.isPistola())
/*    */       return; 
/* 33 */     RayTraceResult r = player.getWorld().rayTraceBlocks(occhi, dir, gittata, FluidCollisionMode.NEVER, false);
/* 34 */     if (r == null || r.getHitBlock() == null)
/* 35 */       return;  Block b = r.getHitBlock();
/* 36 */     if (!isVetro(b.getType()))
/*    */       return; 
/* 38 */     BlockData dati = b.getBlockData();
/* 39 */     Location centro = b.getLocation().add(0.5D, 0.5D, 0.5D);
/* 40 */     b.getWorld().playSound(centro, Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
/* 41 */     b.getWorld().spawnParticle(Particle.BLOCK, centro, 25, 0.3D, 0.3D, 0.3D, dati);
/* 42 */     b.setType(Material.AIR, false);
/*    */     
/* 44 */     InAttesa[] voce = new InAttesa[1];
/* 45 */     BukkitTask task = Bukkit.getScheduler().runTaskLater((Plugin)Main.getInstance(), () -> { ripristina(voce[0]); LISTA.remove(voce[0]); }200L);
/*    */ 
/*    */ 
/*    */     
/* 49 */     voce[0] = new InAttesa(b, dati, task);
/* 50 */     LISTA.add(voce[0]);
/*    */   }
/*    */   
/*    */   private static void ripristina(InAttesa v) {
/* 54 */     if (v == null)
/* 55 */       return;  if (v.blocco().getType().isAir()) v.blocco().setBlockData(v.dati(), false); 
/*    */   }
/*    */   
/*    */   public static void ripristinaTutti() {
/* 59 */     for (InAttesa v : new ArrayList(LISTA)) {
/* 60 */       v.task().cancel();
/* 61 */       ripristina(v);
/*    */     } 
/* 63 */     LISTA.clear();
/*    */   }
/*    */   
/*    */   private static boolean isVetro(Material m) {
/* 67 */     if (!m.isBlock()) return false; 
/* 68 */     String n = m.name();
/* 69 */     return (n.endsWith("GLASS") || n.endsWith("GLASS_PANE"));
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\items\Vetri.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */