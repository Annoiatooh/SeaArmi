/*    */ package com.atlantisrp.commands;
/*    */ 
/*    */ import com.atlantisrp.gui.ArmiGUI;
/*    */ import net.kyori.adventure.text.Component;
/*    */ import net.kyori.adventure.text.format.NamedTextColor;
/*    */ import net.kyori.adventure.text.format.TextColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class ArmiCommand
/*    */   implements CommandExecutor {
/*    */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
/*    */     Player player;
/* 16 */     if (sender instanceof Player) { player = (Player)sender; }
/* 17 */     else { sender.sendMessage("Solo i giocatori possono usare questo comando.");
/* 18 */       return true; }
/*    */ 
/*    */     
/* 21 */     if (!player.isOp()) {
/* 22 */       player.sendMessage((Component)Component.text("Non hai il permesso.", (TextColor)NamedTextColor.RED));
/* 23 */       return true;
/*    */     } 
/*    */     
/* 26 */     ArmiGUI.apri(player);
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\signo\Downloads\AtlantisWeapons.jar!\com\atlantisrp\commands\ArmiCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */