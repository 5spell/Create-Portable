package net.vspell.createportable.block.winder;

import com.simibubi.create.content.kinetics.KineticNetwork;
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
        DISCHARGING
    }

    private WinderMode mode = WinderMode.CHARGING;

    public boolean isPowered = false;
    public int StoredSU = 0;
    public int MaxStoredSU = 2000;
    public int BlockStress = 20; // TODO: add variable stress
    public KineticNetwork network;
    public float NetworkStress;
    public float NetworkCapacity;

    public WinderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    @Override
    public boolean isSource() {
        return source != null && mode == WinderMode.DISCHARGING && isPowered && StoredSU > 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (source == null) {
            CreatePortable.LOGGER.warn("Winder source is null on tick.");
        }

        assert level != null;
        isPowered = level.hasNeighborSignal(worldPosition);

        if (source == null || !hasNetwork()) {
            return;
        }

        float speed = getSpeed();

        if (speed != 0) {
            boolean isFilled = getBlockState().getValue(WinderBlock.FILLED);
            if (isFilled && StoredSU < MaxStoredSU) {
                StoredSU += BlockStress;
            }
        } else {
            setMode(WinderMode.DISCHARGING);
            network = getOrCreateNetwork();

            if (network != null) {
                NetworkStress = network.getActualStressOf(this);
                NetworkCapacity = network.getActualCapacityOf(this);
                StoredSU -= (int)(NetworkStress - NetworkCapacity);
            }
        }
    }

    // reading the mode from NBT
    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
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
        CreatePortable.LOGGER.info(
                "Mode: " + mode.toString() +
                "\nIsPowered: " + isPowered +
                "\nStoredSU: " + StoredSU);
        if (level == null || source == null) {
            return 0;
        }
        if (mode == WinderMode.DISCHARGING && isPowered && StoredSU > 0)
        { //TODO: fix crashes
            CreatePortable.LOGGER.info("Winder should be generating");
            return 16;
        }
        CreatePortable.LOGGER.info("Winder should not be generating");
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

    public void InsertSpringbox(int Springbox_Charge)
    {
        getBlockState().setValue(WinderBlock.FILLED, true);
        StoredSU = Springbox_Charge;
    }
}
