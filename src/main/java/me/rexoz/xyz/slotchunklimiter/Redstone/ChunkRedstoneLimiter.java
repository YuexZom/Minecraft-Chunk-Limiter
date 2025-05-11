package me.rexoz.xyz.slotchunklimiter.Redstone;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.ArrayList;
import java.util.List;

public class ChunkRedstoneLimiter implements Listener {

    private final FileConfiguration config;

    public ChunkRedstoneLimiter(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onRedstoneActivate(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        Chunk chunk = block.getChunk();

        int redstoneCount = 0;
        List<Block> redstoneBlocks = new ArrayList<>();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < chunk.getWorld().getMaxHeight(); y++) {
                for (int z = 0; z < 16; z++) {
                    Block b = chunk.getBlock(x, y, z);
                    if (b.getType() == Material.REDSTONE_WIRE) {
                        redstoneCount++;
                        redstoneBlocks.add(b);
                    }
                }
            }
        }

        int limit = config.getInt("redstone-limit", -1);
        if (limit != -1 && redstoneCount > limit) {
            event.setNewCurrent(0);
        }
    }
}
