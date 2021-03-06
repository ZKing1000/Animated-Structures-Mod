package com.awesomeholden.packets;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.proxies.ClientProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RemoveEditorClient implements IMessage{
	
	public int x,y,z;
	
	public RemoveEditorClient(){}
	public RemoveEditorClient(int x,int y,int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}
	
	public static class Handler implements IMessageHandler<RemoveEditorClient,IMessage>{
		
		public static void deleteController(int[] coords){
			for(int i=ClientProxy.AnimationControllers.size()-1;i>-1;i--){
				if(Main.compareArrays(coords, ClientProxy.AnimationControllers.get(i).coords)){
					ClientProxy.AnimationControllers.remove(i);
					break;
				}
			}
			
		}

		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(RemoveEditorClient message, MessageContext ctx) {
			
			World w = Minecraft.getMinecraft().theWorld;
			
			TileentityAnimationEditorClient c = (TileentityAnimationEditorClient)w.getTileEntity(message.x, message.y, message.z);
			int[] cc = c.controller.coords;
				w.setBlock(message.x, message.y, message.z, Blocks.air);
				//Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Client destroyed it."+Main.indexOf(ClientProxy.AnimationControllers,(Object) c.controller)));
								
				deleteController(cc);
				
			return null;
		}
		
	}

}
