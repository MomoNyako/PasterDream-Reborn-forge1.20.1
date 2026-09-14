package com.pasterdream.pasterdreammod.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 1.20.1 中非主世界维度的 {@code ServerLevelData} 是 {@code DerivedLevelData}：天气 setter
 * （setRaining/setRainTime/setThunderTime/setThundering/setClearWeatherTime）均为空实现，getter 读取主世界，
 * 天气实际只在主世界存一份。因此 {@code /weather} 在自定义维度执行时（对命令来源所在维度调用
 * {@code setWeatherParameters}）改不动天气。
 * <p>
 * {@code setWeatherParameters} 只被 {@code /weather} 指令与 GameTest 调用，不在每 tick 的
 * {@code advanceWeatherCycle}/{@code resetWeatherCycle} 中（后者逐 tick 调用的是单个 setter），
 * 所以在这里把非主世界维度的设置转发到主世界即可，不会造成天气计时多倍递减。
 */
@Mixin(ServerLevel.class)
public class ServerLevelWeatherMixin {

    @Inject(method = "setWeatherParameters", at = @At("HEAD"), cancellable = true)
    private void pasterdream$applyWeatherGlobally(int clearTime, int rainTime, boolean raining,
                                                  boolean thundering, CallbackInfo ci) {
        ServerLevel self = (ServerLevel) (Object) this;
        MinecraftServer server = self.getServer();
        ServerLevel overworld = server == null ? null : server.overworld();
        if (overworld != null && overworld != self) {
            overworld.setWeatherParameters(clearTime, rainTime, raining, thundering);
            ci.cancel();
        }
    }
}
