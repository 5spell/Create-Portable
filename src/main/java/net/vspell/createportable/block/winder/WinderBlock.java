package net.vspell.createportable.block.winder;

import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.vspell.createportable.CreatePortable;
import net.vspell.createportable.SpringboxItem;
import net.vspell.createportable.block.ModBlockEntities;
import net.vspell.createportable.item.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class WinderBlock extends RotatedPillarKineticBlock implements IBE<WinderBlockEntity> {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;


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

    // IRotate
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

    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        BlockEntity itemstack = level.getBlockEntity(pos);
        CreatePortable.LOGGER.info("A WINDER HAS BEEN CLICKED");
        if (itemstack instanceof WinderBlockEntity) {
            ItemStack item_inhand = player.getItemInHand(hand);
            if (item_inhand.is(ModItems.SPRINGBOX_ENTRY)) {
                if (!level.isClientSide) {
                    return ItemInteractionResult.SUCCESS;
                }
                CreatePortable.LOGGER.info("A WINDER HAS BEEN CLICKED by a springbox");
                return ItemInteractionResult.CONSUME;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
