package com.gods.precisionminer.client;

import com.gods.precisionminer.PrecisionMiner;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		// checking inGameHasFocus prevents your keys from firing when the player is typing a chat message
		// NOTE that the KeyInputEvent will NOT be posted when a gui screen such as the inventory is open
		if (FMLClientHandler.instance().getClient().inGameHasFocus) {
			if (KeyBindings.precisionMiner.isPressed()) {
				PrecisionMiner.enabled = !PrecisionMiner.enabled;
				TextComponentString msg;
				if (PrecisionMiner.enabled)
					msg = new TextComponentString("PrecisionMiner: " + TextFormatting.GREEN + "ENABLED");
				else
					msg = new TextComponentString("PrecisionMiner: " + TextFormatting.RED + "DISABLED");
				FMLClientHandler.instance().getClientPlayerEntity().addChatComponentMessage(msg);
			}
		}
	}
}
