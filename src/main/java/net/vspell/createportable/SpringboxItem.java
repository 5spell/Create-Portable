package net.vspell.createportable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.vspell.createportable.component.ModComponents;

@EventBusSubscriber
public class SpringboxItem extends Item {
    public static final String STORED_SU_KEY = "stored_su";

    public SpringboxItem(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("DataFlowIssue")
    public static int getStoredSU(ItemStack stack){
        if (stack.has(ModComponents.STORED_SU)) {
            return stack.get(ModComponents.STORED_SU.get());
        }
        return 0;
    }

    private static void setStoredSU(ItemStack stack, int su){
        stack.set(ModComponents.STORED_SU.get(), su);
    }


    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event){
        event.getToolTip().add(Component.literal("Stored SU/t: " + getStoredSU(event.getItemStack())));
    }

}
