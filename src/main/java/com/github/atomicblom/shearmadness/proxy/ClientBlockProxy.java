package com.github.atomicblom.shearmadness.proxy;

import com.github.atomicblom.shearmadness.utility.Reference;
import com.github.atomicblom.shearmadness.utility.Reference.Blocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;

@SuppressWarnings({"MethodMayBeStatic", "unused"})
public class ClientBlockProxy extends CommonBlockProxy
{
    @Override
    protected Item configureItemBlock(ItemBlock block)
    {
        final Item item = super.configureItemBlock(block);

        ModelLoader.setCustomModelResourceLocation(
                item,
                0,
                new ModelResourceLocation(
                        block.getRegistryName(),
                        Blocks.NORMAL_VARIANT
                )
        );

        return item;
    }
}