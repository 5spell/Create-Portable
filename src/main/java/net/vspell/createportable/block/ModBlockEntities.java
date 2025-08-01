package net.vspell.createportable.block;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.vspell.createportable.CreatePortable;
import net.vspell.createportable.block.winder.WinderBlockEntity;
import net.vspell.createportable.block.winder.WinderRenderer;
import net.vspell.createportable.block.winder.WinderVisual;

public class ModBlockEntities {

    public static final BlockEntityEntry<WinderBlockEntity> WINDER_ENTITY_ENTRY = CreatePortable.REGISTRATE
            .blockEntity("winder", WinderBlockEntity::new)
            .visual(() -> WinderVisual::new)
            .validBlocks(ModBlocks.WINDER_ENTRY::get)
            .renderer(() -> WinderRenderer::new)
            .register();

    public static void register() {}
}
