package com.gods.precisionminer.packets;

import com.gods.precisionminer.PrecisionMiner;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientHandler implements IMessage {
	    private boolean allowBreak;

	    public ClientHandler() { }

	    public ClientHandler(boolean allowBreak) {
	        this.allowBreak = allowBreak;
	    }

	    @Override
	    public void fromBytes(ByteBuf buf) {
	    	allowBreak = buf.readBoolean(); // this class is very useful in general for writing more complex objects
	    }

	    @Override
	    public void toBytes(ByteBuf buf) {
	    	buf.writeBoolean(allowBreak);
	    }

	    public static class Handler implements IMessageHandler<ClientHandler, IMessage> {

	        public IMessage onMessage(final ClientHandler message, final MessageContext ctx) {

	                	PrecisionMiner.allowBreak = message.allowBreak;

	            return null; // no response in this case
	        }

	    }

}
