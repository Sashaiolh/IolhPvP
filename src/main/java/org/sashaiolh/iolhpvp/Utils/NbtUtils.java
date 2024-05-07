package org.sashaiolh.iolhpvp.Utils;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

public class NbtUtils {
    public static void setPersistentTag(Player player, String tag, Tag value) {
        CompoundTag data = player.getPersistentData();
        CompoundTag persistent;

        if (!data.contains(Player.PERSISTED_NBT_TAG)) {
            data.put(Player.PERSISTED_NBT_TAG, (persistent = new CompoundTag()));
        } else {
            persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
        }

        persistent.put(tag, value);
    }

    public static Tag getPersistentTag(Player player, String tag, Tag expectedValue) {
        CompoundTag data = player.getPersistentData();
        CompoundTag persistent;

        if (!data.contains(Player.PERSISTED_NBT_TAG)) {
            data.put(Player.PERSISTED_NBT_TAG, (persistent = new CompoundTag()));
        } else {
            persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
        }

        if (persistent.contains(tag))
            return persistent.get(tag);
        else
            //persistent.put(tag, expectedValue);
            return expectedValue;
    }

    public static boolean getPersistentBoolean(Player player, String tag, boolean expectedValue) {
        Tag theTag = getPersistentTag(player, tag, ByteTag.valueOf(expectedValue));
        return theTag instanceof ByteTag ? ((ByteTag) theTag).getAsByte() != 0 : expectedValue;
    }

    public static void setPersistentBoolean(Player player, String tag, boolean value) {
        setPersistentTag(player, tag, ByteTag.valueOf(value));
    }



    public static boolean hasPersistentTag(Player player, String tag) {
        CompoundTag data = player.getPersistentData();
        CompoundTag persistent;

        if (!data.contains(Player.PERSISTED_NBT_TAG)) {
            data.put(Player.PERSISTED_NBT_TAG, (persistent = new CompoundTag()));
        } else {
            persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
        }

        if (persistent.contains(tag))
            return true;
        else
            return false;

    }

}
