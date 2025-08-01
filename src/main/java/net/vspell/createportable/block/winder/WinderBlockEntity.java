package net.vspell.createportable.block.winder;

import com.simibubi.create.content.kinetics.base.DirectionalShaftHalvesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vspell.createportable.CreatePortable;

public class WinderBlockEntity extends DirectionalShaftHalvesBlockEntity {

    public WinderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();

        float speed = getSpeed();
        if (speed != 0){
            //TODO Charging logic
            CreatePortable.LOGGER.info("A WINDER  IS BEING POWERED HOORAAAY YIPPPEEE");
        }

    }


}
