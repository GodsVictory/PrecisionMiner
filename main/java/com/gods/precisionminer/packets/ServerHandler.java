package com.gods.precisionminer.packets;

import com.gods.precisionminer.PrecisionMiner;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerHandler implements IMessage {
	private int code;

	public ServerHandler() {
	}

	public ServerHandler(int i) {
		this.code = i;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		code = buf.readInt(); // this class is very useful in general
										// for writing more complex objects
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(code);
	}

	public static class Handler implements IMessageHandler<ServerHandler, IMessage> {

		public IMessage onMessage(ServerHandler message, MessageContext ctx) {
			int player = ctx.getServerHandler().playerEntity.getEntityId();
			if (message.code == 0) {
				PrecisionMiner.playerArray.remove(new Integer(player));
				PrecisionMiner.allowBreak.remove(new Integer(player));
			} else if (message.code == 1) {
				if (!PrecisionMiner.playerArray.contains(player))
					PrecisionMiner.playerArray.add(player);
			} else if (message.code == 2) {
				PrecisionMiner.allowBreak.remove(new Integer(player));
			}

			return null;
		}

	}
}
