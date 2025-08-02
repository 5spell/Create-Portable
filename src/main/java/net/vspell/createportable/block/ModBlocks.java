package net.vspell.createportable.block;

import com.simibubi.create.AllTags.AllBlockTags;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.vspell.createportable.CreatePortable;
import net.vspell.createportable.block.winder.WinderBlock;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ModBlocks {

    public static final BlockEntry<WinderBlock> WINDER_ENTRY = CreatePortable.REGISTRATE.block("winder", WinderBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(properties -> BlockBehaviour.Properties.of()
                    .strength(2.0F)
                    .mapColor(MapColor.PODZOL)
                    .noOcclusion())
            .tag(AllBlockTags.SAFE_NBT.tag)
            .tag(AllBlockTags.COPYCAT_DENY.tag)
            .tag(AllBlockTags.NON_HARVESTABLE.tag)
            .transform(TagGen.axeOrPickaxe())
            .item()
                .build()
            .register();

    public static void register() {}
}
