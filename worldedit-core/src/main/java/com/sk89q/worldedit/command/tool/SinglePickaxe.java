/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.world.World;

/**
 * A super pickaxe mode that removes one block.
 */
public class SinglePickaxe implements BlockTool {

    @Override
    public boolean canUse(Actor player) {
        return player.hasPermission("worldedit.superpickaxe");
    }

    @Override
    public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session, com.sk89q.worldedit.util.Location clicked) {
        World world = (World) clicked.getExtent();
        final int blockType = world.getBlockType(clicked.toVector());
        if (blockType == BlockID.BEDROCK
                && !player.canDestroyBedrock()) {
            return true;
        }

        EditSession editSession = session.createEditSession(player);
        editSession.getSurvivalExtent().setToolUse(config.superPickaxeDrop);

        try {
            editSession.setBlock(clicked.toVector(), WorldEdit.getInstance().getBaseBlockFactory().getBaseBlock(BlockID.AIR));
        } catch (MaxChangedBlocksException e) {
            player.printError("Max blocks change limit reached.");
        } finally {
            editSession.flushQueue();
        }

        world.playEffect(clicked.toVector(), 2001, blockType);

        return true;
    }

}
