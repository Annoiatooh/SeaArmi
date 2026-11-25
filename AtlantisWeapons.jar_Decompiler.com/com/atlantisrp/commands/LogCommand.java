package com.atlantisrp.commands;

import com.atlantisrp.logs.LogArmi;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class LogCommand implements TabExecutor {
   private static final String PERMESSO = "atlantisweapons.log";

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player player) {
         if (!player.hasPermission("atlantisweapons.log") && !player.isOp()) {
            player.sendMessage(Component.text("Non hai il permesso.", NamedTextColor.RED));
            return true;
         } else {
            if (args.length == 1 && args[0].equalsIgnoreCase("attiva")) {
               LogArmi.attiva(player);
               player.sendMessage(Component.text("Log attivati: vedrai spari e morti.", NamedTextColor.GREEN));
            } else if (args.length == 1 && args[0].equalsIgnoreCase("disattiva")) {
               LogArmi.disattiva(player);
               player.sendMessage(Component.text("Log disattivati.", NamedTextColor.YELLOW));
            } else {
               player.sendMessage(Component.text("Uso: /log <attiva|disattiva>", NamedTextColor.RED));
            }

            return true;
         }
      } else {
         sender.sendMessage("Solo i giocatori possono usare questo comando.");
         return true;
      }
   }

   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      return args.length == 1 ? List.of("attiva", "disattiva") : List.of();
   }
}
