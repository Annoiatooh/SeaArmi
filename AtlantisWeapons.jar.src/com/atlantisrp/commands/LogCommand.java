/*    */ package com.atlantisrp.commands;
/*    */ 
/*    */ import com.atlantisrp.logs.LogArmi;
/*    */ import java.util.List;
/*    */ import net.kyori.adventure.text.Component;
/*    */ import net.kyori.adventure.text.format.NamedTextColor;
/*    */ import net.kyori.adventure.text.format.TextColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.command.TabExecutor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class LogCommand
/*    */   implements TabExecutor
/*    */ {
/*    */   private static final String PERMESSO = "atlantisweapons.log";
/*    */   
/*    */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
/*    */     Player player;
/* 20 */     if (sender instanceof Player) { player = (Player)sender; }
/* 21 */     else { sender.sendMessage("Solo i giocatori possono usare questo comando.");
/* 22 */       return true; }
/*    */     
/* 24 */     if (!player.hasPermission("atlantisweapons.log") && !player.isOp()) {
/* 25 */       player.sendMessage((Component)Component.text("Non hai il permesso.", (TextColor)NamedTextColor.RED));
/* 26 */       return true;
/*    */     } 
/*    */     
/* 29 */     if (args.length == 1 && args[0].equalsIgnoreCase("attiva")) {
/* 30 */       LogArmi.attiva(player);
/* 31 */       player.sendMessage((Component)Component.text("Log attivati: vedrai spari e morti.", (TextColor)NamedTextColor.GREEN));
/* 32 */     } else if (args.length == 1 && args[0].equalsIgnoreCase("disattiva")) {
/* 33 */       LogArmi.disattiva(player);
/* 34 */       player.sendMessage((Component)Component.text("Log disattivati.", (TextColor)NamedTextColor.YELLOW));
/*    */     } else {
/* 36 */       player.sendMessage((Component)Component.text("Uso: /log <attiva|disattiva>", (TextColor)NamedTextColor.RED));
/*    */     } 
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
/* 43 */     return (args.length == 1) ? List.of("attiva", "disattiva") : List.of();
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\commands\LogCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */