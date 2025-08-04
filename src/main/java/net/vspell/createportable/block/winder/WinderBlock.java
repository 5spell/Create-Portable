package net.vspell.createportable.block.winder;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.vspell.createportable.SpringboxItem;
import net.vspell.createportable.block.ModBlockEntities;
import net.vspell.createportable.component.ModComponents;
import net.vspell.createportable.item.ModItems;
import org.jetbrains.annotations.NotNull;

public class WinderBlock extends DirectionalKineticBlock implements IBE<WinderBlockEntity> {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public static final BooleanProperty FILLED = BooleanProperty.create("filled");


    public WinderBlock(Properties properties) {
        super(properties);
        registerDefaultState(super.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(FILLED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(
                FILLED
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
//        Direction dir = context.getClickedFace().getOpposite();
//        return defaultBlockState().setValue(FACING, dir);
        Direction preferred = getPreferredFacing(context);
        if ((context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown()) || preferred == null)
            return super.getStateForPlacement(context);
        return defaultBlockState().setValue(FACING, preferred);
    }


    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face){
        return face == state.getValue(FACING);
    }

    // IRotate
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public Class<WinderBlockEntity> getBlockEntityClass() {
        return WinderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends WinderBlockEntity> getBlockEntityType() {
        return ModBlockEntities.WINDER_ENTITY_ENTRY.get();
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof WinderBlockEntity) {
            ItemStack itemInHand = player.getItemInHand(hand);

            if (itemInHand.is(ModItems.SPRINGBOX_ENTRY.get()))
            {
                //CreatePortable.LOGGER.info("A WINDER HAS BEEN CLICKED by a springbox");

                int storedSU = SpringboxItem.getStoredSU(itemInHand);
                if(!state.getValue(FILLED)) // FILLED is the new filled property
                {
                    level.setBlock(pos, state.setValue(FILLED, true), 3);

                    ((WinderBlockEntity) blockEntity).insertSpringbox(SpringboxItem.getStoredSU(itemInHand));

                    itemInHand.shrink(1);

                    return ItemInteractionResult.SUCCESS;
                } else {
                    return ItemInteractionResult.FAIL;
                }


            }
            else if (itemInHand.isEmpty())
            {
                //CreatePortable.LOGGER.info("A WINDER HAS BEEN CLICKED by an empty hand");
                if(state.getValue(FILLED))
                {
                    ItemStack itemToGive = new ItemStack(ModItems.SPRINGBOX_ENTRY.get());
                    player.addItem(itemToGive);

                    itemToGive.set(ModComponents.STORED_SU.get(), ((WinderBlockEntity) blockEntity).removeSpringbox());
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public Direction getPreferredFacing(BlockPlaceContext context) {
        Direction prefferedSide = null;
        for (Direction side : Iterate.directions) {
            BlockState blockState = context.getLevel()
                    .getBlockState(context.getClickedPos()
                            .relative(side));
            if (blockState.getBlock() instanceof IRotate) {
                if (((IRotate) blockState.getBlock()).hasShaftTowards(context.getLevel(), context.getClickedPos()
                        .relative(side), blockState, side.getOpposite()))
                    if (prefferedSide != null && prefferedSide.getAxis() != side.getAxis()) {
                        prefferedSide = null;
                        break;
                    } else {
                        prefferedSide = side;
                    }
            }
        }
        return prefferedSide;
    }

}
