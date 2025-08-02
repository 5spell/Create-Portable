package net.vspell.createportable.block.winder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class WinderRenderer extends KineticBlockEntityRenderer<WinderBlockEntity> {

    public WinderRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @SuppressWarnings("DataFlowIssue") // NullPtrException GETOUT
    @Override
    protected void renderSafe(WinderBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (VisualizationManager.supportsVisualization(be.getLevel())) return;

        final BlockPos pos = be.getBlockPos();
        float time = AnimationTickHolder.getRenderTime(be.getLevel());

        final Direction shaftDirection = be.getBlockState().getValue(WinderBlock.FACING);
        final Direction.Axis axis = shaftDirection.getAxis();

        SuperByteBuffer shaft = CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), shaftDirection);
        float offset = getRotationOffsetForPosition(be, pos, axis);
        float angle = (time * be.getSpeed() * 3f / 10) % 360;

        if (be.getSpeed() != 0 && be.hasSource()) {
            BlockPos source = be.source.subtract(be.getBlockPos());
            Direction sourceFacing = Direction.getNearest(source.getX(), source.getY(), source.getZ());
            if (sourceFacing.getAxis() == shaftDirection.getAxis())
                angle *= sourceFacing == shaftDirection ? 1 : -1;
            else if (sourceFacing.getAxisDirection() == shaftDirection.getAxisDirection())
                angle *= -1;
        }
        angle += offset;
        angle = angle / 180f * (float) Math.PI;

        kineticRotationTransform(shaft, be, axis, angle, light);
        shaft.renderInto(ms, buffer.getBuffer(RenderType.solid()));
    }
}
