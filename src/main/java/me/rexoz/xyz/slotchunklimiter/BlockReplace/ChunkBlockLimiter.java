package me.rexoz.xyz.slotchunklimiter.BlockReplace;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class ChunkBlockLimiter implements Listener {

    private final FileConfiguration config;

    public ChunkBlockLimiter(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onChunkEnter(PlayerMoveEvent event) {
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) return;

        Player player = event.getPlayer();
        Chunk chunk = event.getTo().getChunk();

        Map<Material, List<Block>> blockMap = new HashMap<>();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < chunk.getWorld().getMaxHeight(); y++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    Material type = block.getType();

                    if (config.getConfigurationSection("block-limits") == null) continue;
                    if (!config.getConfigurationSection("block-limits").getKeys(false).contains(type.name())) continue;

                    blockMap.putIfAbsent(type, new ArrayList<>());
                    blockMap.get(type).add(block);
                }
            }
        }

        for (String materialName : config.getConfigurationSection("block-limits").getKeys(false)) {
            Material mat = Material.matchMaterial(materialName);
            if (mat == null || !blockMap.containsKey(mat)) continue;

            int limit = config.getInt("block-limits." + materialName + ".limit");
            String replaceWith = config.getString("block-limits." + materialName + ".replace-with");
            Material replaceMat = Material.matchMaterial(replaceWith);

            List<Block> blocks = blockMap.get(mat);
            if (blocks.size() > limit) {
                int excess = blocks.size() - limit;
                Collections.shuffle(blocks);
                for (int i = 0; i < excess; i++) {
                    blocks.get(i).setType(replaceMat);
                }
                player.sendMessage("§cBu chunk'ta " + mat.name() + " limiti aşıldı. Fazla bloklar " + replaceMat.name() + " ile değiştirildi.");
            }
        }
    }
}
