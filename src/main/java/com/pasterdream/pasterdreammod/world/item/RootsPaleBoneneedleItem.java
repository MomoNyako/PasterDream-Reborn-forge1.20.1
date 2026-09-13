package com.pasterdream.pasterdreammod.world.item;

import com.pasterdream.pasterdreammod.helper.DreamDimensionHelper;
import com.pasterdream.pasterdreammod.init.ModCriteriaTriggers;
import com.pasterdream.pasterdreammod.init.ModParticleTypes;
import com.pasterdream.pasterdreammod.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RootsPaleBoneneedleItem extends Item {

    public RootsPaleBoneneedleItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.pasterdream.roots_pale_boneneedle.1"));
        tooltip.add(Component.translatable("tooltip.pasterdream.roots_pale_boneneedle.2"));
        tooltip.add(Component.translatable("tooltip.pasterdream.roots_pale_boneneedle.3"));
        tooltip.add(Component.translatable("tooltip.pasterdream.roots_pale_boneneedle.4"));
        tooltip.add(Component.translatable("tooltip.pasterdream.roots_pale_boneneedle.5"));
        tooltip.add(Component.translatable("tooltip.pasterdream.roots_pale_boneneedle.6"));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        Level level = context.getLevel();
        if (level.dimension() != Level.OVERWORLD) {
            if (!level.isClientSide()) {
                player.displayClientMessage(
                        Component.translatable("message.pasterdream.roots_pale_boneneedle.overworld_only"), true);
            }
            return InteractionResult.FAIL;
        }

        ItemStack itemstack = context.getItemInHand();
        BlockPos pos = context.getClickedPos().above();

        itemstack.getOrCreateTag().putBoolean("switch", true);
        itemstack.getOrCreateTag().putDouble("x", pos.getX());
        itemstack.getOrCreateTag().putDouble("y", pos.getY());
        itemstack.getOrCreateTag().putDouble("z", pos.getZ());

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ModParticleTypes.DUST_0_PARTICLE.get(),
                    pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                    64, 1, 1, 1, 0.15);
        }
        player.displayClientMessage(Component.translatable("message.pasterdream.roots_pale_boneneedle.waypoint_set"), true);
        level.playSound(null, BlockPos.containing(pos.getX(), pos.getY(), pos.getZ()),
                SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 1.0f, 1.0f);
        player.getCooldowns().addCooldown(this, 20);

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean hasWaypoint = itemstack.getOrCreateTag().getBoolean("switch");

        player.swing(hand, true);
        player.hurt(player.level().damageSources().generic(), 1.0f);

        if (DreamDimensionHelper.isDreamDimension(level) && level instanceof ServerLevel serverLevel) {
            boolean wasFalling = player.fallDistance > 10;

            serverLevel.sendParticles(ModParticleTypes.DUST_0_PARTICLE.get(),
                    player.getX(), player.getY(), player.getZ(),
                    64, 0.1, 1, 0.1, 0.2);
            level.playSound(null, BlockPos.containing(player.getX(), player.getY(), player.getZ()),
                    ModSounds.AWAKE.get(), SoundSource.NEUTRAL, 0.5f, 1.0f);

            PaleBoneneedleItem.scheduleDelayed(() -> {
                PaleBoneneedleItem.teleportToOverworldAndSpawn(serverLevel, player);
                if (hasWaypoint) {
                    // 已设置标记点：返回标记位置
                    teleportToWaypoint(itemstack, player);
                } else {
                    // 未设置标记点：默认返回重生点（与普通苍白骨针一致）
                    player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0));
                }
                if (player instanceof ServerPlayer sp) {
                    // 授予进度：使用苍白骨针（"哦，疼！"）
                    ModCriteriaTriggers.USE_BONE_NEEDLE.trigger(sp, false);
                    // 挑战进度：回主世界后授予（梦境中跌落>10格使用骨针 —— "人类坠出梦境"）
                    if (wasFalling) {
                        ModCriteriaTriggers.USE_BONE_NEEDLE.trigger(sp, true);
                    }
                }
                player.getCooldowns().addCooldown(this, 100);
            });
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    static void teleportToWaypoint(ItemStack itemstack, Player player) {
        if (player.level().dimension() != Level.OVERWORLD) return;
        double x = itemstack.getOrCreateTag().getDouble("x");
        double y = itemstack.getOrCreateTag().getDouble("y");
        double z = itemstack.getOrCreateTag().getDouble("z");
        player.teleportTo(x, y, z);
        player.fallDistance = 0;
        if (player instanceof ServerPlayer sp) {
            sp.connection.teleport(x, y, z, sp.getYRot(), sp.getXRot());
        }
    }
}
