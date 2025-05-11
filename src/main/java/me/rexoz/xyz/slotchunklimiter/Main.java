package me.rexoz.xyz.slotchunklimiter;

import me.rexoz.xyz.slotchunklimiter.BlockReplace.ChunkBlockLimiter;
import me.rexoz.xyz.slotchunklimiter.Redstone.ChunkRedstoneLimiter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("ChunkLimiter aktif");
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChunkRedstoneLimiter(getConfig()), this);
        getServer().getPluginManager().registerEvents(new ChunkBlockLimiter(getConfig()), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("ChunkLimiter devre dışı");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("chunklimiter") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            sender.sendMessage("§aChunkLimiter config dosyası yeniden yüklendi.");
            return true;
        }
        return false;
    }
}
