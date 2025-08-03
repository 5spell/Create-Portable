package net.vspell.createportable.block.winder;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.DirectionalShaftHalvesBlockEntity;
import com.simibubi.create.foundation.item.KineticStats;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vspell.createportable.CreatePortable;


public class WinderBlockEntity extends DirectionalShaftHalvesBlockEntity {

    public enum WinderMode {
        CHARGING,
        DISCHARGING;
    }

    private WinderMode mode = WinderMode.CHARGING;

    public boolean isPowered = false;
    public int StoredSU = 0;
    public int MaxStoredSU = 2000;
    public int BlockStress = 20; // TODO: add variable stress
    public KineticNetwork network = this.getOrCreateNetwork();
    public float NetworkStress;
    public float NetworkCapacity;


    public WinderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    @Override
    public void tick() {
        super.tick();
        isPowered= level.hasNeighborSignal(worldPosition);
        float speed = getSpeed();
        if (speed != 0){

            boolean isFilled = getBlockState().getValue(WinderBlock.FILLED); // new FILLED property

            //CreatePortable.LOGGER.info("A WINDER IS BEING POWERED HOORAAAY YIPPPEEE");

            if  (isFilled && (StoredSU < MaxStoredSU)) {
                StoredSU = StoredSU + BlockStress;
            }
        }
        else
        {
            setMode(WinderMode.DISCHARGING);
            if(network != null)
            {
                NetworkStress = this.network.getActualStressOf(this);
                NetworkCapacity = this.network.getActualCapacityOf(this);
                StoredSU = StoredSU - (int)(NetworkStress - NetworkCapacity);
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
                "Mode: " +
                mode.toString() + " IsPowered: " +
                isPowered + " StoredSU: " + StoredSU);

        if (mode == WinderMode.DISCHARGING && isPowered && StoredSU > 0)
        { //TODO: figure out discharge conditions
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

    public WinderMode getMode() {
        return mode;
    }


    public void InsertSpringbox(int Springbox_Charge)
    {
        getBlockState().setValue(WinderBlock.FILLED, true);
        StoredSU = Springbox_Charge;
    }

    public void pop()
    {
        StoredSU = 0;
    }

}
