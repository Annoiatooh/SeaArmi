package com.atlantisrp.commands;

import com.atlantisrp.gui.ArmiGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArmiCommand implements CommandExecutor {
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player player) {
         if (!player.isOp()) {
            player.sendMessage(Component.text("Non hai il permesso.", NamedTextColor.RED));
            return true;
         } else {
            ArmiGUI.apri(player);
            return true;
         }
      } else {
         sender.sendMessage("Solo i giocatori possono usare questo comando.");
         return true;
      }
   }
}
