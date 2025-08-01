package net.vspell.createportable.block;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.vspell.createportable.CreatePortable;
import net.vspell.createportable.block.winder.WinderBlockEntity;

public class ModBlockEntities {

    public static final BlockEntityEntry<WinderBlockEntity> WINDER_ENTITY_ENTRY = CreatePortable.REGISTRATE.blockEntity("winder", WinderBlockEntity::new)
            .validBlocks(ModBlocks.WINDER_ENTRY::get)
            .register();

    public static void register() {}
}
