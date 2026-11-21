package io.github.thebusybiscuit.privatestorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

class SlimefunChestTest {

    @Test
    void isOwnerHandlesNullWithoutNpe() {
        UUID uuid = UUID.randomUUID();
        assertFalse(SlimefunChest.isOwner(null, uuid));
        assertFalse(SlimefunChest.isOwner(uuid.toString(), null));
        assertTrue(SlimefunChest.isOwner(uuid.toString(), uuid));
        assertFalse(SlimefunChest.isOwner(UUID.randomUUID().toString(), uuid));
    }

    @Test
    void drainChestMenuHandlesNullMenu() {
        assertTrue(SlimefunChest.drainChestMenu(null, 9).isEmpty());
    }

    @Test
    void drainChestMenuClosesViewersAndClearsSlots() {
        BlockMenu menu = mock(BlockMenu.class);
        Inventory inv = mock(Inventory.class);
        HumanEntity viewer = mock(HumanEntity.class);
        Material diamondMat = mock(Material.class);
        when(diamondMat.isAir()).thenReturn(false);
        Material airMat = mock(Material.class);
        when(airMat.isAir()).thenReturn(true);

        ItemStack diamond = mock(ItemStack.class);
        when(diamond.getType()).thenReturn(diamondMat);
        ItemStack air = mock(ItemStack.class);
        when(air.getType()).thenReturn(airMat);

        when(menu.toInventory()).thenReturn(inv);
        when(inv.getViewers()).thenReturn(List.of(viewer));
        when(menu.getItemInSlot(0)).thenReturn(diamond);
        when(menu.getItemInSlot(1)).thenReturn(air);

        List<ItemStack> drained = SlimefunChest.drainChestMenu(menu, 9);

        assertEquals(1, drained.size());
        assertEquals(diamond, drained.get(0));
        verify(viewer).closeInventory();
        verify(menu).replaceExistingItem(0, null);
    }
}
