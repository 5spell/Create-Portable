package net.vspell.createportable.item;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Rarity;
import net.vspell.createportable.CreatePortable;
import net.vspell.createportable.SpringboxItem;

public class ModItems {

    public static final ItemEntry<SpringboxItem> SPRINGBOX_ENTRY = CreatePortable.REGISTRATE.item("springbox", SpringboxItem::new)
            .properties(p -> p.stacksTo(1).rarity(Rarity.EPIC))
            .register();

    public static void register() {}
}
