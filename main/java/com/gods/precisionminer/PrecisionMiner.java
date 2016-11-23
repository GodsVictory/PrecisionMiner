package com.gods.precisionminer;

import java.util.ArrayList;

import com.gods.precisionminer.client.KeyBindings;
import com.gods.precisionminer.client.KeyInputHandler;
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
	public static final String VERSION = "0.0.2";

	public static SimpleNetworkWrapper network;

	boolean reset = true;
	public static boolean enabled = true;
	public static boolean lastEnabled = false;
	public static boolean lastAttacking = false;
	public static boolean lastSneaking = false;

	public static ArrayList<Integer> playerArray = new ArrayList<Integer>();
	public static ArrayList<Integer> allowBreak = new ArrayList<Integer>();

	@Mod.Instance(MODID)
	public static PrecisionMiner instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
		network = NetworkRegistry.INSTANCE.newSimpleChannel("precisionminer");
		if (FMLCommonHandler.instance().getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
			KeyBindings.init();
		}
		network.registerMessage(ServerHandler.Handler.class, ServerHandler.class, 1, Side.SERVER);
	}

	@SubscribeEvent
	public void tickEvent(TickEvent.ClientTickEvent event) {
		if (!TickEvent.Phase.START.equals(event.phase))
			return;
		if (FMLCommonHandler.instance().getSide().isClient()) {
			Minecraft mc = Minecraft.getMinecraft();

			if (mc.theWorld != null && mc.inGameHasFocus && !mc.playerController.isInCreativeMode()) {
				if (lastEnabled != enabled) {
					lastEnabled = enabled;
					if (!enabled)
						network.sendToServer(new ServerHandler(0));
				}
				if (!enabled)
					return;

				boolean isAttacking = mc.gameSettings.keyBindAttack.isKeyDown();
				if (lastAttacking != isAttacking)
					if (!isAttacking)
						network.sendToServer(new ServerHandler(2));
				lastAttacking = isAttacking;

				boolean isSneaking = mc.thePlayer.isSneaking();
				if (lastSneaking != isSneaking)
					if (isSneaking)
						network.sendToServer(new ServerHandler(1));
					else
						network.sendToServer(new ServerHandler(0));
				lastSneaking = isSneaking;
			}

		}

	}

	@SubscribeEvent
	public void onbreak(BreakEvent event) {
		EntityPlayerMP entity = (EntityPlayerMP) event.getPlayer();
		if (entity != null)
			if (playerArray.contains(entity.getEntityId()))
				if (allowBreak.contains(entity.getEntityId()))
					event.setCanceled(true);
				else
					allowBreak.add(entity.getEntityId());
	}

}
