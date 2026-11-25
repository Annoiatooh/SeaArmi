package com.atlantisrp.commands;

import com.atlantisrp.listeners.AmmanettaListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffLegaturaCommand implements CommandExecutor {
   private static final String PERMESSO = "atlantisweapons.admin";

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (!sender.isOp() && !sender.hasPermission("atlantisweapons.admin")) {
         sender.sendMessage(Component.text("Non hai il permesso.", NamedTextColor.RED));
         return true;
      } else if (args.length != 1) {
         sender.sendMessage(Component.text("Uso: /" + label + " (nick)", NamedTextColor.RED));
         return true;
      } else {
         Player target = Bukkit.getPlayerExact(args[0]);
         if (target == null) {
            sender.sendMessage(Component.text("Giocatore non trovato.", NamedTextColor.RED));
            return true;
         } else {
            boolean corda = label.equalsIgnoreCase("adminscorda");
            String tipo = AmmanettaListener.tipoLegatura(target.getUniqueId());
            if (tipo == null) {
               sender.sendMessage(Component.text(target.getName() + " non è ammanettato né legato.", NamedTextColor.RED));
               return true;
            } else if (corda && !"corda".equals(tipo)) {
               sender.sendMessage(Component.text(target.getName() + " è ammanettato: usa /adminsmanetta.", NamedTextColor.RED));
               return true;
            } else if (!corda && !"manette".equals(tipo)) {
               sender.sendMessage(Component.text(target.getName() + " è legato con la corda: usa /adminscorda.", NamedTextColor.RED));
               return true;
            } else {
               AmmanettaListener.liberaStaff(target);
               String var10001 = target.getName();
               sender.sendMessage(Component.text("Hai liberato " + var10001 + ".", NamedTextColor.GREEN));
               target.sendMessage(Component.text(corda ? "Ti hanno slegato." : "Ti hanno tolto le manette.", NamedTextColor.GREEN));
               return true;
            }
         }
      }
   }
}
