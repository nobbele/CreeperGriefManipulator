package dev.nobbele.creepergriefmanipulator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class EntityExplodeEventHandler implements Listener {
    private final Random random = new Random();

    @EventHandler(priority = EventPriority.NORMAL)
    public void explodeEvent(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof Creeper))
            return;

        List<Block> blocks = event.blockList();
        List<Block> toDestroy = new ArrayList<>();

        for (Block block : blocks) {
            if (!shouldPreserveBlock(event, block)) {
                toDestroy.add(block);
            }
        }

        for (Block block : toDestroy) {
            Collection<ItemStack> drops = block.getDrops();
            ItemStack itemStack = null;
            if (!drops.isEmpty()) {
                itemStack = drops.iterator().next();
                int amount = random.nextInt(itemStack.getAmount());
                itemStack.setAmount(amount);
            }

            block.breakNaturally(itemStack);
        }

        event.blockList().clear();
    }

    private boolean shouldPreserveBlock(EntityExplodeEvent event, Block block) {
        Vector dir = event.getLocation().toVector().subtract(block.getLocation().toVector());
        Vector dirFull = new Vector(dir.getX() > 0 ? 1 : 0, dir.getY() > 0 ? 1 : 0, dir.getZ() > 0 ? 1 : 0);
        if (block.getType() != Material.GRASS_BLOCK) {
            Block hitBlock = block.getLocation().add(dirFull).getBlock();

            if (hitBlock != null) {
                if (hitBlock.getType() == Material.GRASS_BLOCK) {
                    return true;
                }
            }
        } else {
            if (random.nextBoolean()) {
                return true;
            }
        }

        return false;
    }
}
