package net.vspell.createportable.block;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.vspell.createportable.CreatePortable;
import net.vspell.createportable.block.winder.WinderBlock;

import static com.simibubi.create.foundation.data.BlockStateGen.axisBlock;

public class ModBlocks {

    public static final BlockEntry<WinderBlock> WINDER_ENTRY = CreatePortable.REGISTRATE.block("winder", WinderBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(properties -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PODZOL)
                    .noOcclusion())
            .transform(TagGen.axeOrPickaxe())
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(AllSpriteShifts.ANDESITE_CASING)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, AllSpriteShifts.ANDESITE_CASING,
                    (s, f) -> f.getAxis() == s.getValue(WinderBlock.AXIS))))
            .blockstate((c, p) -> axisBlock(c, p, $ -> AssetLookup.partialBaseModel(c, p), true))
            .register();

    public static void register() {}
}
