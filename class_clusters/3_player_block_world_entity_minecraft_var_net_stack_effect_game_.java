package com.ankamagames.wakfu.client.core.game.protector.event;

import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;

public class ProtectorChallengeProposalEvent extends ProtectorChallengeEvent
{
    private String m_title;
    private String m_itemLink;
    
    @Override
    public ProtectorMood getProtectorMood() {
        return ProtectorMood.NEUTRAL;
    }
    
    public void setReward(final String itemLink) {
        this.m_itemLink = itemLink;
    }
    
    public void setTitle(final String title) {
        this.m_title = title;
    }
    
    @Override
    public String[] getParams() {
        final String[] params = { this.m_title, this.m_itemLink };
        return params;
    }
}

--------------------

package squeek.veganoption.integration.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import squeek.veganoption.blocks.BlockRettable;
import squeek.veganoption.helpers.LangHelper;

import java.util.List;

public class ProviderRettable implements IWailaDataProvider
{
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> toolTip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return toolTip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> toolTip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		BlockRettable blockRettable = (BlockRettable) accessor.getBlock();
		float rettingPercent = BlockRettable.getRettingPercent(accessor.getWorld(), accessor.getPosition());
		if (rettingPercent >= 1)
			toolTip.add(LangHelper.translate("info.retted"));
		else
		{
			if (blockRettable.canRet(accessor.getWorld(), accessor.getPosition()))
				toolTip.add(LangHelper.contextString("waila", "retting", Math.round(rettingPercent * 100F)));
			else
				toolTip.add(LangHelper.translate("info.retting.not.submerged"));
		}
		return toolTip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> toolTip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return toolTip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos)
	{
		return null;
	}
}

--------------------

package minestrapteam.mods.minestrappolation.item.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockPermaFrost extends ItemBlock
{

	public ItemBlockPermaFrost(Block block)
	{
		super(block);
		this.setHasSubtypes(false);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return 11052399;
	}

	@Override
	public String getUnlocalizedName(ItemStack item)
	{
		return "permafrost";
	}
}

--------------------

