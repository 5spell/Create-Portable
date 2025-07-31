package net.vspell.createportable.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vspell.createportable.CreatePortable;

import java.util.function.UnaryOperator;

public class ModComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENT_TYPE_REGISTER =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CreatePortable.MOD_ID);


    // ------------- Components -------------

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> STORED_SU = register("stored_su",
            builder -> builder.persistent(Codec.INT)
    );

    // -------------

    // Register the components
    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderUnaryOperator){
        return COMPONENT_TYPE_REGISTER.register(name, () -> builderUnaryOperator.apply(DataComponentType.builder()).build());
    }

    // Register method for the register to register the register
    public static void register(IEventBus eventBus) {
        COMPONENT_TYPE_REGISTER.register(eventBus);
    }
}
