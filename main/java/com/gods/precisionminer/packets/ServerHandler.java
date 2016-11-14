package com.gods.precisionminer.packets;

import com.gods.precisionminer.PrecisionMiner;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerHandler implements IMessage {
	private boolean allowBreak;

	public ServerHandler() {
	}

	public ServerHandler(boolean allowBreak) {
		this.allowBreak = allowBreak;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		allowBreak = buf.readBoolean(); // this class is very useful in general
										// for writing more complex objects
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(allowBreak);
	}

	public static class Handler implements IMessageHandler<ServerHandler, IMessage> {

		public IMessage onMessage(ServerHandler message, MessageContext ctx) {
			int player = ctx.getServerHandler().playerEntity.getEntityId();
			if (message.allowBreak) {
				PrecisionMiner.allowBreak = message.allowBreak;
				if (!PrecisionMiner.playerArray.contains(player))
					PrecisionMiner.playerArray.add(player);
			} else {
				PrecisionMiner.playerArray.remove(new Integer(player));
			}

			return null;
		}

	}
}
