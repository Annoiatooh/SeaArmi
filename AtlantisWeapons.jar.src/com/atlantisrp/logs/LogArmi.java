/*     */ package com.atlantisrp.logs;
/*     */ 
/*     */ import com.atlantisrp.items.TipoArma;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import net.kyori.adventure.text.TextComponent;
/*     */ import net.kyori.adventure.text.event.ClickEvent;
/*     */ import net.kyori.adventure.text.event.HoverEvent;
/*     */ import net.kyori.adventure.text.event.HoverEventSource;
/*     */ import net.kyori.adventure.text.format.NamedTextColor;
/*     */ import net.kyori.adventure.text.format.TextColor;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogArmi
/*     */ {
/*  26 */   private static final Set<UUID> ATTIVI = new HashSet<>();
/*     */ 
/*     */   
/*  29 */   private static final Map<UUID, UltimoColpo> ULTIMO = new HashMap<>(); private static final long VALIDITA_MS = 3000L;
/*     */   private static final class UltimoColpo extends Record {
/*     */     private final String tiratore; private final String arma; private final long quando;
/*     */     
/*  33 */     private UltimoColpo(String tiratore, String arma, long quando) { this.tiratore = tiratore; this.arma = arma; this.quando = quando; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/atlantisrp/logs/LogArmi$UltimoColpo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #33	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/atlantisrp/logs/LogArmi$UltimoColpo; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/atlantisrp/logs/LogArmi$UltimoColpo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #33	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/atlantisrp/logs/LogArmi$UltimoColpo; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/atlantisrp/logs/LogArmi$UltimoColpo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #33	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/atlantisrp/logs/LogArmi$UltimoColpo;
/*  33 */       //   0	8	1	o	Ljava/lang/Object; } public String tiratore() { return this.tiratore; } public String arma() { return this.arma; } public long quando() { return this.quando; }
/*     */   
/*     */   }
/*  36 */   public static void attiva(Player player) { ATTIVI.add(player.getUniqueId()); }
/*  37 */   public static void disattiva(Player player) { ATTIVI.remove(player.getUniqueId()); } public static boolean isAttivo(Player player) {
/*  38 */     return ATTIVI.contains(player.getUniqueId());
/*     */   }
/*     */ 
/*     */   
/*     */   public static void colpo(Player tiratore, Player vittima, TipoArma arma) {
/*  43 */     ULTIMO.put(vittima.getUniqueId(), new UltimoColpo(tiratore.getName(), arma.getNome(), System.currentTimeMillis()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     Component msg = prefisso().append((Component)Component.text(tiratore.getName(), (TextColor)NamedTextColor.WHITE)).append((Component)Component.text(" ")).append(coordinate(tiratore.getLocation())).append((Component)Component.text(" ha sparato a ", (TextColor)NamedTextColor.GRAY)).append((Component)Component.text(vittima.getName(), (TextColor)NamedTextColor.WHITE)).append((Component)Component.text(" ")).append(coordinate(vittima.getLocation())).append((Component)Component.text(" con " + arma.getNome(), (TextColor)NamedTextColor.GRAY));
/*  54 */     invia(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void morte(Player vittima) {
/*     */     String causa;
/*  60 */     UltimoColpo ultimo = ULTIMO.remove(vittima.getUniqueId());
/*     */     
/*  62 */     if (ultimo != null && System.currentTimeMillis() - ultimo.quando() <= 3000L) {
/*  63 */       causa = "ucciso da " + ultimo.tiratore() + " con " + ultimo.arma();
/*  64 */     } else if (vittima.getKiller() != null) {
/*  65 */       causa = "ucciso da " + vittima.getKiller().getName();
/*     */     } else {
/*  67 */       EntityDamageEvent ultimoDanno = vittima.getLastDamageCause();
/*  68 */       causa = (ultimoDanno == null) ? "causa sconosciuta" : traduci(ultimoDanno.getCause());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     Component msg = prefisso().append((Component)Component.text(vittima.getName(), (TextColor)NamedTextColor.WHITE)).append((Component)Component.text(" ")).append(coordinate(vittima.getLocation())).append((Component)Component.text(" è morto: ", (TextColor)NamedTextColor.RED)).append((Component)Component.text(causa, (TextColor)NamedTextColor.GRAY));
/*  77 */     invia(msg);
/*     */   }
/*     */   
/*     */   private static String traduci(EntityDamageEvent.DamageCause causa) {
/*  81 */     switch (causa) { case FALL: case FIRE: case FIRE_TICK: case LAVA: case DROWNING: case SUFFOCATION: case STARVATION: case VOID: case CONTACT: case ENTITY_ATTACK: case ENTITY_SWEEP_ATTACK: case PROJECTILE: case BLOCK_EXPLOSION: case ENTITY_EXPLOSION: case POISON: case WITHER: case MAGIC: case LIGHTNING: case SUICIDE: case FREEZE: case CRAMMING: case FLY_INTO_WALL: case HOT_FLOOR: case CUSTOM:  }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 103 */       causa.name().toLowerCase().replace('_', ' ');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Component prefisso() {
/* 109 */     return (Component)Component.text("[LOG] ", (TextColor)NamedTextColor.RED);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Component coordinate(Location l) {
/* 114 */     int x = l.getBlockX(), y = l.getBlockY(), z = l.getBlockZ();
/* 115 */     String comando = "/execute in " + String.valueOf(l.getWorld().getKey()) + " run tp @s " + x + " " + y + " " + z;
/* 116 */     return ((TextComponent)Component.text("[" + x + ", " + y + ", " + z + "]", (TextColor)NamedTextColor.AQUA)
/* 117 */       .clickEvent(ClickEvent.runCommand(comando)))
/* 118 */       .hoverEvent((HoverEventSource)HoverEvent.showText((Component)Component.text("Clicca per teletrasportarti", (TextColor)NamedTextColor.GRAY)));
/*     */   }
/*     */   
/*     */   private static void invia(Component msg) {
/* 122 */     for (Player p : Bukkit.getOnlinePlayers()) {
/* 123 */       if (ATTIVI.contains(p.getUniqueId())) p.sendMessage(msg); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\logs\LogArmi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */