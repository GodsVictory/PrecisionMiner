package com.gods.precisionminer.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {
	public static KeyBinding precisionMiner;

	public static void init() {
		precisionMiner = new KeyBinding("Toggle", Keyboard.KEY_BACKSLASH, "PrecisionMiner");
		ClientRegistry.registerKeyBinding(precisionMiner);
	}
}
