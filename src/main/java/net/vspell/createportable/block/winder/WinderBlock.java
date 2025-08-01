package net.vspell.createportable.block.winder;

import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.vspell.createportable.block.ModBlockEntities;

public class WinderBlock extends RotatedPillarKineticBlock implements IBE<WinderBlockEntity> {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    //TODO Visual shafts (WinderBlockRenderer and/or WinderBlockVisual)
    public WinderBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face){
        return face == state.getValue(FACING);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public Class<WinderBlockEntity> getBlockEntityClass() {
        return WinderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends WinderBlockEntity> getBlockEntityType() {
        return ModBlockEntities.WINDER_ENTITY_ENTRY.get();
    }
}
