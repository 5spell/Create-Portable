package net.vspell.createportable.block.winder;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.function.Consumer;

public class WinderVisual extends KineticBlockEntityVisual<WinderBlockEntity> {

    protected final RotatingInstance shaftInstance;
    protected final Direction shaftDirection;
    protected final Direction.Axis axis;
    protected Direction sourceFacing;

    public WinderVisual(VisualizationContext context, WinderBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        shaftDirection = blockEntity.getBlockState().getValue(WinderBlock.FACING);
        axis = shaftDirection.getAxis();

        updateSourceFacing();

        var instancer = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF)); // No idea wtf this is but create has it

        RotatingInstance instance = instancer.createInstance();

        instance.setup(blockEntity, axis, getSpeed(shaftDirection))
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, shaftDirection)
                .setChanged();

        shaftInstance = instance;
    }




    private float getSpeed(Direction direction) {
        float speed = blockEntity.getSpeed();

        if (speed != 0 && sourceFacing != null) {
            if (sourceFacing.getAxis() == direction.getAxis())
                speed *= sourceFacing == direction ? 1 : -1;
            else if (sourceFacing.getAxisDirection() == direction.getAxisDirection())
                speed *= -1;
        }
        return speed;
    }


    protected void updateSourceFacing() {
        if (blockEntity.hasSource()) {
            BlockPos source = blockEntity.source.subtract(pos);
            sourceFacing = Direction.getNearest(source.getX(), source.getY(), source.getZ());
        } else {
            sourceFacing = null;
        }
    }

    @Override
    public void update(float pt) {
        shaftInstance.setup(blockEntity, shaftDirection.getAxis(), getSpeed(shaftDirection))
                .setChanged();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(shaftInstance);
    }

    @Override
    public void updateLight(float partialTick) {
        relight(shaftInstance);
    }

    @Override
    protected void _delete() {
        shaftInstance.delete();
    }
}
