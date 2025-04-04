package org.sam.fletchingtable.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

public class FletchingTableClient implements ClientModInitializer {

    private static final KeyBinding dupeKey = new KeyBinding("key.dupe", GLFW.GLFW_KEY_V, "category.dupe"); // Keybind 'V'

    @Override
    public void onInitializeClient() {
        // Register keybinding
        KeyBindingHelper.registerKeyBinding(dupeKey);

        // Register the client tick event to check if the key is pressed
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (dupeKey.isPressed()) {
                dupe();
            }
        });
    }

    public static void dupe() {
        MinecraftClient client = MinecraftClient.getInstance();

        // Check if the player is holding a trident
        if (client.player == null || client.player.getMainHandStack().getItem() != Items.TRIDENT) {
            return; // Exit if the player is not holding a trident
        }

        // Now swap the trident to slot 1 (the first hotbar slot)
        client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, 1, 0, SlotActionType.SWAP, client.player);

        // Send the packet to simulate the trident throw
        PlayerActionC2SPacket packet = new PlayerActionC2SPacket(
                PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN, 0);
        client.getNetworkHandler().sendPacket(packet);
    }
}
