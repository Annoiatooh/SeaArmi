/*    */ package com.atlantisrp.items;
/*    */ 
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.block.data.BlockData;
/*    */ import org.bukkit.scheduler.BukkitTask;
/*    */ 
/*    */ final class InAttesa extends Record {
/*    */   private final Block blocco;
/*    */   private final BlockData dati;
/*    */   private final BukkitTask task;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/atlantisrp/items/Vetri$InAttesa;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/atlantisrp/items/Vetri$InAttesa;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/atlantisrp/items/Vetri$InAttesa;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/atlantisrp/items/Vetri$InAttesa;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/atlantisrp/items/Vetri$InAttesa;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/atlantisrp/items/Vetri$InAttesa;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   private InAttesa(Block blocco, BlockData dati, BukkitTask task) {
/* 25 */     this.blocco = blocco; this.dati = dati; this.task = task; } public Block blocco() { return this.blocco; } public BlockData dati() { return this.dati; } public BukkitTask task() { return this.task; }
/*    */ 
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\items\Vetri$InAttesa.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */