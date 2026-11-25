/*    */ package com.atlantisrp.commands;
/*    */ 
/*    */ import com.atlantisrp.listeners.AmmanettaListener;
/*    */ import net.kyori.adventure.text.Component;
/*    */ import net.kyori.adventure.text.format.NamedTextColor;
/*    */ import net.kyori.adventure.text.format.TextColor;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class StaffLegaturaCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   private static final String PERMESSO = "atlantisweapons.admin";
/*    */   
/*    */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
/* 19 */     if (!sender.isOp() && !sender.hasPermission("atlantisweapons.admin")) {
/* 20 */       sender.sendMessage((Component)Component.text("Non hai il permesso.", (TextColor)NamedTextColor.RED));
/* 21 */       return true;
/*    */     } 
/* 23 */     if (args.length != 1) {
/* 24 */       sender.sendMessage((Component)Component.text("Uso: /" + label + " (nick)", (TextColor)NamedTextColor.RED));
/* 25 */       return true;
/*    */     } 
/* 27 */     Player target = Bukkit.getPlayerExact(args[0]);
/* 28 */     if (target == null) {
/* 29 */       sender.sendMessage((Component)Component.text("Giocatore non trovato.", (TextColor)NamedTextColor.RED));
/* 30 */       return true;
/*    */     } 
/*    */     
/* 33 */     boolean corda = label.equalsIgnoreCase("adminscorda");
/* 34 */     String tipo = AmmanettaListener.tipoLegatura(target.getUniqueId());
/* 35 */     if (tipo == null) {
/* 36 */       sender.sendMessage((Component)Component.text(target.getName() + " non è ammanettato né legato.", (TextColor)NamedTextColor.RED));
/* 37 */       return true;
/*    */     } 
/* 39 */     if (corda && !"corda".equals(tipo)) {
/* 40 */       sender.sendMessage((Component)Component.text(target.getName() + " è ammanettato: usa /adminsmanetta.", (TextColor)NamedTextColor.RED));
/* 41 */       return true;
/*    */     } 
/* 43 */     if (!corda && !"manette".equals(tipo)) {
/* 44 */       sender.sendMessage((Component)Component.text(target.getName() + " è legato con la corda: usa /adminscorda.", (TextColor)NamedTextColor.RED));
/* 45 */       return true;
/*    */     } 
/*    */     
/* 48 */     AmmanettaListener.liberaStaff(target);
/* 49 */     sender.sendMessage((Component)Component.text("Hai liberato " + target.getName() + ".", (TextColor)NamedTextColor.GREEN));
/* 50 */     target.sendMessage((Component)Component.text(corda ? "Ti hanno slegato." : "Ti hanno tolto le manette.", (TextColor)NamedTextColor.GREEN));
/* 51 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\commands\StaffLegaturaCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */