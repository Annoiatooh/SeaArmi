/*    */ package com.atlantisrp.logs;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class UltimoColpo
/*    */   extends Record
/*    */ {
/*    */   private final String tiratore;
/*    */   private final String arma;
/*    */   private final long quando;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/atlantisrp/logs/LogArmi$UltimoColpo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/atlantisrp/logs/LogArmi$UltimoColpo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/atlantisrp/logs/LogArmi$UltimoColpo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/atlantisrp/logs/LogArmi$UltimoColpo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/atlantisrp/logs/LogArmi$UltimoColpo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #33	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/atlantisrp/logs/LogArmi$UltimoColpo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   private UltimoColpo(String tiratore, String arma, long quando) {
/* 33 */     this.tiratore = tiratore; this.arma = arma; this.quando = quando; } public String tiratore() { return this.tiratore; } public String arma() { return this.arma; } public long quando() { return this.quando; }
/*    */ 
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\logs\LogArmi$UltimoColpo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */