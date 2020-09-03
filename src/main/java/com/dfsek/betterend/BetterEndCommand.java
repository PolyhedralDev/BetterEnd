package com.dfsek.betterend;

import com.dfsek.betterend.config.ConfigUtil;
import com.dfsek.betterend.util.LangUtil;
import com.dfsek.betterend.util.Util;
import com.dfsek.betterend.world.EndBiomeGrid;
import com.dfsek.betterend.world.EndChunkGenerator;
import com.dfsek.betterend.world.EndProfiler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.WorldProfiler;
import org.polydev.gaea.tree.Tree;

import java.util.Random;


public class BetterEndCommand implements CommandExecutor {

    private final JavaPlugin main;
    public BetterEndCommand(JavaPlugin main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(args.length == 1 && args[0].equalsIgnoreCase("biome")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(LangUtil.prefix + LangUtil.playersOnly);
                return true;
            }
            Player p = (Player) sender;
            if(sender.hasPermission("betterend.checkbiome")) {
                if(p.getWorld().getGenerator() instanceof EndChunkGenerator) sender
                        .sendMessage(LangUtil.prefix + String.format(LangUtil.biomeCommand, EndBiomeGrid.fromWorld(p.getWorld()).getBiome(p.getLocation()).toString()));
                else sender.sendMessage(LangUtil.prefix + LangUtil.notBetterEndWorld);
            } else {
                sender.sendMessage(LangUtil.prefix + LangUtil.noPermission);
            }
            return true;
        } else if(args.length == 2 && args[0].equalsIgnoreCase("tpbiome")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(LangUtil.prefix + LangUtil.playersOnly);
                return true;
            }
            Player p = (Player) sender;
            if(p.hasPermission("betterend.gotobiome")) {
                if(p.getWorld().getGenerator() instanceof EndChunkGenerator) return Util.tpBiome(p, args);
                else sender.sendMessage(LangUtil.prefix + LangUtil.notBetterEndWorld);
            } else {
                sender.sendMessage(LangUtil.prefix + LangUtil.noPermission);
            }
            return true;
        } else if(args.length == 1 && args[0].equalsIgnoreCase("version")) {
            sender.sendMessage(LangUtil.prefix + String.format(LangUtil.versionCommand, main.getDescription().getVersion()));
            return true;
        } else if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(LangUtil.prefix + LangUtil.reloadConfig);
            ConfigUtil.loadConfig(main.getLogger(), main);
            sender.sendMessage(LangUtil.prefix + LangUtil.completeMessage);
            return true;
        } else if(args.length == 2 && args[0].equalsIgnoreCase("tree")) {
            if(sender.hasPermission("betterend.tree")) {
                try {
                    Tree.valueOf(args[1]).plant(((Player) sender).getLocation(), new Random(), false, main);
                    return true;
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("Invalid tree type.");
                }
            }
        } else if(args.length >= 1 && args[0].equalsIgnoreCase("profile")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(LangUtil.prefix + LangUtil.playersOnly);
                return true;
            }
            Player p = (Player) sender;
            if(p.getWorld().getGenerator() instanceof EndChunkGenerator) {
                WorldProfiler profile = EndProfiler.fromWorld(p.getWorld());
                if(args.length > 1 && "query".equals(args[1])) {
                    sender.sendMessage(profile.getResultsFormatted());
                    return true;
                } else if(args.length > 1 && "reset".equals(args[1])) {
                    profile.reset();
                    sender.sendMessage("Profiler has been reset.");
                    return true;
                } else if(args.length > 1 && "start".equals(args[1])) {
                    profile.setProfiling(true);
                    sender.sendMessage("Profiler has started.");
                    return true;
                } else if(args.length > 1 && "stop".equals(args[1])) {
                    profile.setProfiling(false);
                    sender.sendMessage("Profiler has stopped.");
                    return true;
                }
            }
            else sender.sendMessage(LangUtil.prefix + LangUtil.notBetterEndWorld);

        }
        return false;
    }
}
