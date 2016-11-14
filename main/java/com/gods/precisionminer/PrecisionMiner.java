package com.gods.precisionminer;

import java.util.ArrayList;

import com.gods.precisionminer.client.KeyBindings;
import com.gods.precisionminer.client.KeyInputHandler;
import com.gods.precisionminer.packets.ClientHandler;
import com.gods.precisionminer.packets.ServerHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = PrecisionMiner.MODID, version = PrecisionMiner.VERSION, name = PrecisionMiner.NAME)
public class PrecisionMiner {
	public static final String MODID = "precisionminer";
	public static final String NAME = "precisionminer";
	public static final String VERSION = "0.0.1";

	public static SimpleNetworkWrapper network;

	public static boolean allowBreak = true;
	public static boolean enabled = true;

	public static ArrayList<Integer> playerArray = new ArrayList<Integer>();

	@Mod.Instance(MODID)
	public static PrecisionMiner instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
		network = NetworkRegistry.INSTANCE.newSimpleChannel("precisionminer");
		if (FMLCommonHandler.instance().getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
			KeyBindings.init();
			network.registerMessage(ClientHandler.Handler.class, ClientHandler.class, 0, Side.CLIENT);
		}
		network.registerMessage(ServerHandler.Handler.class, ServerHandler.class, 1, Side.SERVER);
	}

	@SubscribeEvent
	public void tickEvent(TickEvent.ClientTickEvent event) {
		if (!TickEvent.Phase.START.equals(event.phase))
			return;

		if (FMLCommonHandler.instance().getSide().isClient()) {
			if (!enabled)
				network.sendToServer(new ServerHandler(false));
			Minecraft mc = Minecraft.getMinecraft();
			boolean isAttacking = mc.gameSettings.keyBindAttack.isKeyDown();
			if (!isAttacking && mc.inGameHasFocus && !mc.isGamePaused() && enabled) {
				network.sendToServer(new ServerHandler(true));
			}
		}
	}

	@SubscribeEvent
	public void onbreak(BreakEvent event) {
		EntityPlayerMP entity = (EntityPlayerMP) event.getPlayer();
		if (playerArray.contains(entity.getEntityId()))
			if (allowBreak) {
				event.setCanceled(false);
				allowBreak = false;
			} else
				event.setCanceled(true);
	}

}
