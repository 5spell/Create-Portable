package net.vspell.createportable.block.winder;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.DirectionalShaftHalvesBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.vspell.createportable.CreatePortable;


public class WinderBlockEntity extends KineticBlockEntity {

    public enum WinderMode {
        CHARGING,
        DISCHARGING
    }

    private WinderMode mode = WinderMode.CHARGING;
    boolean wasDischarging = false;
    public boolean isPowered = false;
    public int MaxStoredSU = 2000;
    public int StoredSU = 0;
    public int BlockStress = 20; // TODO: add variable stress
    public KineticNetwork network;
    public float NetworkStress;
    public float NetworkCapacity;
    public WinderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    @Override
    public boolean isSource() {
        // Crashes if true is returned
        return true;
    }

    private boolean readyToGenerate() {
        return mode == WinderMode.DISCHARGING && isPowered && StoredSU > 0;
    }

    @Override
    public void tick() {
        super.tick();

        //updating whether is receiving redstone
        assert level != null;
        isPowered = level.hasNeighborSignal(worldPosition);

        float speed = getSpeed();
        //Setting Charge-discharge modes
        if (speed != 0) {
            if (getBlockState().getValue(WinderBlock.FILLED)
                    && StoredSU < MaxStoredSU) {
                StoredSU += BlockStress;
            }
            setMode(WinderMode.CHARGING);

        }
        else {

            //Discharge logic
            setMode(WinderMode.DISCHARGING);
            //CreatePortable.LOGGER.info("DISCHARGING");
            network = getOrCreateNetwork();
            if (network != null) {
                NetworkStress = network.getActualStressOf(this);
                NetworkCapacity = network.getActualCapacityOf(this);
                StoredSU = Math.max(0, StoredSU - (int)(NetworkStress - NetworkCapacity));
            }
        }
        //Update kinetics if mode has changed
        if (wasDischarging != (mode == WinderMode.DISCHARGING))
        {
            //
            detachKinetics();
            attachKinetics();
            //CreatePortable.LOGGER.info("Winder now " + mode.toString());
        }
        wasDischarging = (mode == WinderMode.DISCHARGING);
    }

    // reading the mode from NBT
    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        mode = WinderMode.valueOf(compound.getString("Mode"));
        StoredSU = compound.getInt("StoredSU");
    }

    // saving the mode to NBT
    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putString("Mode", mode.name());
        compound.putInt("StoredSU", StoredSU);
    }

    @Override
    public float getGeneratedSpeed(){
        CreatePortable.LOGGER.info(
                "Mode: " + mode.toString() +
                "\nIsPowered: " + isPowered +
                "\nStoredSU: " + StoredSU);
        if (mode == WinderMode.DISCHARGING && isPowered && StoredSU > 0)
        {
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
        assert level != null;
        level.setBlock(worldPosition, getBlockState().setValue(WinderBlock.FILLED, true), 3);
        StoredSU = Springbox_Charge;
    }
}
