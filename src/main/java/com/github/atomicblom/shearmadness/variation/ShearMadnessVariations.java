package com.github.atomicblom.shearmadness.variation;

import com.github.atomicblom.shearmadness.api.modelmaker.IModelMaker;
import com.github.atomicblom.shearmadness.api.IVariationRegistry;
import com.github.atomicblom.shearmadness.api.ItemStackHelper;
import com.github.atomicblom.shearmadness.api.RegisterShearMadnessVariationEvent;
import com.github.atomicblom.shearmadness.api.behaviour.DamageBehaviour;
import com.github.atomicblom.shearmadness.api.behaviour.ExplosiveBehaviour;
import com.github.atomicblom.shearmadness.api.behaviour.FlightBehaviour;
import com.github.atomicblom.shearmadness.api.behaviour.PlaceInvisibleBlockBehaviour;
import com.github.atomicblom.shearmadness.configuration.Settings.Behaviours;
import com.github.atomicblom.shearmadness.modelmaker.DefaultChiselModelMaker;
import com.github.atomicblom.shearmadness.api.transformation.RailTransformations;
import com.github.atomicblom.shearmadness.api.transformation.StairTransformations;
import com.github.atomicblom.shearmadness.utility.BlockLibrary;
import com.github.atomicblom.shearmadness.utility.ChiselLibrary;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.api.carving.CarvingUtils;
import java.util.function.Function;

@SuppressWarnings({"MethodMayBeStatic", "AnonymousInnerClass"})
public enum ShearMadnessVariations
{
    INSTANCE;

    private static final IModelMaker DefaultChiselModelMaker = new DefaultChiselModelMaker();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @Optional.Method(modid = "shearmadness")
    public void onShearMadnessRegisterVariations(RegisterShearMadnessVariationEvent event) {
        final IVariationRegistry registry = event.getRegistry();

        //Java 8 Style Registration
        registry.registerVariation(
                itemStack -> CarvingUtils.getChiselRegistry().getVariation(itemStack) != null,
                DefaultChiselModelMaker
        );

        //Java 7 Style Registration
        //noinspection Convert2Lambda
        registry.registerVariation(
                new Function<ItemStack, Boolean>() {
                    @Override
                    public Boolean apply(ItemStack itemStack)
                    {
                        return ItemStackHelper.isStackForBlock(
                                itemStack,
                                Blocks.RAIL,
                                Blocks.ACTIVATOR_RAIL,
                                Blocks.DETECTOR_RAIL,
                                Blocks.GOLDEN_RAIL
                        );
                    }
                },
                new RailTransformations()
        );

        registry.registerVariation(
                itemStack -> ItemStackHelper.isStackForBlockSubclassOf(
                        itemStack,
                        BlockStairs.class
                        ),
                new StairTransformations()
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.CACTUS),
                entity -> new DamageBehaviour(entity, Behaviours::allowCactus, DamageSource.cactus)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.MAGMA),
                entity -> new DamageBehaviour(entity, Behaviours::allowFireDamage, DamageSource.hotFloor)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.REDSTONE_BLOCK),
                entity -> new PlaceInvisibleBlockBehaviour(entity, Behaviours::allowRedstone, BlockLibrary.invisibleRedstone.getDefaultState())
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.GLOWSTONE),
                entity -> new PlaceInvisibleBlockBehaviour(entity, Behaviours::allowGlowstone, BlockLibrary.invisibleGlowstone.getDefaultState())
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, Blocks.TNT),
                sheep -> new ExplosiveBehaviour(sheep, Behaviours::allowTNT)
        );

        registry.registerBehaviour(
                itemStack -> ItemStackHelper.isStackForBlock(itemStack, ChiselLibrary.technical, 4) ||
                        ItemStackHelper.isStackForBlock(itemStack, ChiselLibrary.technical1, 1),
                FlightBehaviour::new
        );
    }
}