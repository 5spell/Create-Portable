package net.vspell.createportable.block;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.AllTags.AllBlockTags;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.vspell.createportable.CreatePortable;
import net.vspell.createportable.block.winder.WinderBlock;

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
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.ANDESITE_CASING)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.ANDESITE_CASING,
                    (s, f) -> f.getAxis() == s.getValue(WinderBlock.AXIS))))
            .register();

    public static void register() {}
}
