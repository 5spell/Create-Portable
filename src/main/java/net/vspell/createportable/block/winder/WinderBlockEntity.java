package net.vspell.createportable.block.winder;

import com.simibubi.create.content.kinetics.base.DirectionalShaftHalvesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vspell.createportable.CreatePortable;


public class WinderBlockEntity extends DirectionalShaftHalvesBlockEntity {

    public enum WinderMode {
        CHARGING,
        DISCHARGING,
        EMPTY;
    }

    private WinderMode mode = WinderMode.EMPTY;


    public WinderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    @Override
    public void tick() {
        super.tick();

        float speed = getSpeed();
        if (speed != 0){
            //TODO Charging logic
            CreatePortable.LOGGER.info("A WINDER IS BEING POWERED HOORAAAY YIPPPEEE");
        }
    }

    // reading the mode from NBT
    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        mode = WinderMode.valueOf(compound.getString("Mode"));
    }

    // saving the mode to NBT
    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putString("Mode", mode.name());
    }

    @Override
    public float getGeneratedSpeed(){
        if (false) { // temp
            return 16;
        }
        return 0;
    }

    @Override
    public float getTheoreticalSpeed() {
        return super.getTheoreticalSpeed();
    }

    public void setMode(WinderMode modeArg) {
        mode = modeArg;
        setChanged();
        sendData();
    }

    public void toggleMode() {
        mode = mode == WinderMode.CHARGING ? WinderMode.DISCHARGING : WinderMode.CHARGING;
        setChanged();
        sendData();
    }

    public WinderMode getMode() {
        return mode;
    }

}
