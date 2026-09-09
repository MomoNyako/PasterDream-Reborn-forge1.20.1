package com.pasterdream.pasterdreammod.worldgen.dimension;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.pasterdream.pasterdreammod.PasterDreamMod;
import com.pasterdream.pasterdreammod.worldgen.biome.ModBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ModLevelStems {

    //六维参数区间必须满足：每个群系的参数区间不能都是其他群系参数区间的子集，否则该群系永远无法出现。
    //满足上述条件的情况下如果某群系仍然不生成，则需进一步满足：六个参数区间中至少有一个与其他所有群系的对应区间交集为空。
    public static final ResourceKey<LevelStem> DYEDREAM_WORLD =
            ResourceKey.create(Registries.LEVEL_STEM,
                    ResourceLocation.fromNamespaceAndPath(PasterDreamMod.MOD_ID, "dyedream_world"));

    public static final ResourceKey<LevelStem> LAMP_SHADOW_WORLD =
            ResourceKey.create(Registries.LEVEL_STEM,
                    ResourceLocation.fromNamespaceAndPath(PasterDreamMod.MOD_ID, "lamp_shadow_world"));

    public static final ResourceKey<LevelStem> WIND_JOURNEY_WORLD =
            ResourceKey.create(Registries.LEVEL_STEM,
                    ResourceLocation.fromNamespaceAndPath(PasterDreamMod.MOD_ID, "wind_journey_world"));

    public static final ResourceKey<LevelStem> AARONCOS_ARENA_WORLD =
            ResourceKey.create(Registries.LEVEL_STEM,
                    ResourceLocation.fromNamespaceAndPath(PasterDreamMod.MOD_ID, "aaroncos_arena_world"));

    @SuppressWarnings("unchecked")
    private static MultiNoiseBiomeSource createMultiNoiseSource(
            Climate.ParameterList<Holder<Biome>> params) {
        try {
            Constructor<MultiNoiseBiomeSource> ctor = MultiNoiseBiomeSource.class
                    .getDeclaredConstructor(Either.class);
            ctor.setAccessible(true);
            return ctor.newInstance(Either.left(params));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create MultiNoiseBiomeSource", e);
        }
    }

    private static void addSurfacePoint(List<Pair<Climate.ParameterPoint, Holder<Biome>>> points,
                                         Climate.Parameter temp, Climate.Parameter humidity,
                                         Climate.Parameter continentalness, Climate.Parameter erosion,
                                         Climate.Parameter weirdness, Holder<Biome> biome) {
        points.add(Pair.of(new Climate.ParameterPoint(temp, humidity, continentalness, erosion,
                Climate.Parameter.point(0.0F), weirdness, 0L), biome));
        points.add(Pair.of(new Climate.ParameterPoint(temp, humidity, continentalness, erosion,
                Climate.Parameter.point(1.0F), weirdness, 0L), biome));
    }

    private static void addLandSurfacePoint(List<Pair<Climate.ParameterPoint, Holder<Biome>>> points,
                                             Climate.Parameter temp, Climate.Parameter humidity,
                                             Climate.Parameter continentalness, Climate.Parameter erosion,
                                             Climate.Parameter wNeg, Climate.Parameter wPos, Holder<Biome> biome) {
        addSurfacePoint(points, temp, humidity, continentalness, erosion, wNeg, biome);
        addSurfacePoint(points, temp, humidity, continentalness, erosion, wPos, biome);
    }

    private static void addCavePoint(List<Pair<Climate.ParameterPoint, Holder<Biome>>> points,
                                      Climate.Parameter temp, Climate.Parameter humidity,
                                      Climate.Parameter continentalness, Climate.Parameter erosion,
                                      Climate.Parameter weirdness, Holder<Biome> biome) {
        points.add(Pair.of(new Climate.ParameterPoint(temp, humidity, continentalness, erosion,
                Climate.Parameter.span(0.2F, 0.9F), weirdness, 0L), biome));
    }

    public static void bootstrap(BootstapContext<LevelStem> context) {
        // 获取依赖注册表的引用
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);

        // 引用染梦维度群系
        Holder<Biome> dyedreamFrozenOcean = biomes.getOrThrow(ModBiomes.DYEDREAM_FROZEN_OCEAN);
        Holder<Biome> dyedreamColdOcean = biomes.getOrThrow(ModBiomes.DYEDREAM_COLD_OCEAN);
        Holder<Biome> dyedreamOcean = biomes.getOrThrow(ModBiomes.DYEDREAM_OCEAN);
        Holder<Biome> dyedreamBeach = biomes.getOrThrow(ModBiomes.DYEDREAM_BEACH);
        Holder<Biome> dyedreamRiver = biomes.getOrThrow(ModBiomes.DYEDREAM_RIVER);
        Holder<Biome> dyedreamFrozenRiver = biomes.getOrThrow(ModBiomes.DYEDREAM_FROZEN_RIVER);
        Holder<Biome> dyedreamSnowyPeaks = biomes.getOrThrow(ModBiomes.DYEDREAM_SNOWY_PEAKS);
        Holder<Biome> dyedreamCherryGrove = biomes.getOrThrow(ModBiomes.DYEDREAM_CHERRY_GROVE);
        Holder<Biome> dyedreamSnowyPlains = biomes.getOrThrow(ModBiomes.DYEDREAM_SNOWY_PLAINS);
        Holder<Biome> dyedreamMushroomMountains = biomes.getOrThrow(ModBiomes.DYEDREAM_MUSHROOM_MOUNTAINS);
        Holder<Biome> dyedreamPlains = biomes.getOrThrow(ModBiomes.DYEDREAM_PLAINS);
        Holder<Biome> dyedreamFlowerField = biomes.getOrThrow(ModBiomes.DYEDREAM_FLOWER_FIELD);
        Holder<Biome> dyedreamForest = biomes.getOrThrow(ModBiomes.DYEDREAM_FOREST);
        Holder<Biome> dyedreamCaves = biomes.getOrThrow(ModBiomes.DYEDREAM_CAVES);
        Holder<Biome> dyedreamLushCaves = biomes.getOrThrow(ModBiomes.DYEDREAM_LUSH_CAVES);
        Holder<Biome> dyedreamDripstoneCaves = biomes.getOrThrow(ModBiomes.DYEDREAM_DRIPSTONE_CAVES);
        // 引用自定义的维度类型和噪声设置
        Holder<DimensionType> dimType = dimensionTypes.getOrThrow(ModDimensionTypes.DYEDREAM_WORLD);
        Holder<NoiseGeneratorSettings> dimNoise = noiseSettings.getOrThrow(ModNoiseSettings.DYEDREAM_WORLD);

        // 多噪声群系源 — 采用原版分档边界（温度/湿度/大陆性/侵蚀/山脊谷带），17 群系
        //  海洋类 C[-1.05,-0.19]；海岸带平坦侵蚀为沙滩、陡峭侵蚀归陡坡/山峰陆地群系
        //  河流占山脊谷带 W[-0.05,0.05]；陆地/沙滩排除谷带（W 双点）
        //  平原按山脊 W 正负分半 → 染梦平原 / 染梦花海（花海为平原 W 变体）
        //  染梦樱雪森林独享 冷×湿×陡坡 气候区（原染梦雪林已删除，由樱雪森林吃掉其区域）
        //  染梦雪原覆盖全部平坦冷区（原染梦雪针叶林已删除，其 冷×湿×平坦 气候由雪原吃掉）
        //  地表群系 depth 双点位 {0,1}；洞穴群系 depth [0.2,0.9]
        Climate.Parameter tCold = Climate.Parameter.span(-1.0F, -0.15F);
        Climate.Parameter tColdOcean0 = Climate.Parameter.span(-1.0F, -0.45F);
        Climate.Parameter tColdOcean1 = Climate.Parameter.span(-0.45F, -0.15F);
        Climate.Parameter tWarm = Climate.Parameter.span(-0.15F, 1.0F);
        Climate.Parameter tFull = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter hDry = Climate.Parameter.span(-1.0F, 0.1F);
        Climate.Parameter hWet = Climate.Parameter.span(0.1F, 1.0F);
        Climate.Parameter hFull = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter cOcean = Climate.Parameter.span(-1.05F, -0.19F);
        Climate.Parameter cCoast = Climate.Parameter.span(-0.19F, -0.11F);
        Climate.Parameter cLand = Climate.Parameter.span(-0.11F, 1.0F);
        Climate.Parameter cSteepLand = Climate.Parameter.span(-0.19F, 1.0F);
        Climate.Parameter cRiver = Climate.Parameter.span(-0.19F, 1.0F);
        Climate.Parameter eFull = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter ePeak = Climate.Parameter.span(-1.0F, -0.78F);
        Climate.Parameter eSlope = Climate.Parameter.span(-0.78F, -0.2225F);
        Climate.Parameter eFlat = Climate.Parameter.span(-0.2225F, 1.0F);
        Climate.Parameter eUpperMountain = Climate.Parameter.span(-1.0F, -0.375F);
        Climate.Parameter cNearInland = Climate.Parameter.span(-0.11F, 0.03F);
        Climate.Parameter cMidFarInland = Climate.Parameter.span(0.03F, 1.0F);
        // 原版 OverworldBiomeBuilder 的 Peak+High 怪异度切片（raw ridges）：|w| ∈ [0.4, 0.9333]，真正的峰顶
        Climate.Parameter wPeakPos = Climate.Parameter.span(0.4F, 0.9333F);
        Climate.Parameter wPeakNeg = Climate.Parameter.span(-0.9333F, -0.4F);
        Climate.Parameter eMountain = Climate.Parameter.span(-1.0F, -0.2225F);
        Climate.Parameter wFull = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter wValley = Climate.Parameter.span(-0.05F, 0.05F);
        Climate.Parameter wNeg = Climate.Parameter.span(-1.0F, -0.05F);
        Climate.Parameter wPos = Climate.Parameter.span(0.05F, 1.0F);
        Climate.Parameter cCaveNonInland = Climate.Parameter.span(-1.0F, 0.8F);
        Climate.Parameter cCaveInland = Climate.Parameter.span(0.8F, 1.0F);
        Climate.Parameter hCaveDry = Climate.Parameter.span(-1.0F, 0.7F);
        Climate.Parameter hCaveWet = Climate.Parameter.span(0.7F, 1.0F);

        List<Pair<Climate.ParameterPoint, Holder<Biome>>> dyedreamBiomePoints = new ArrayList<>();
        // 海洋（W 全区间）
        addSurfacePoint(dyedreamBiomePoints, tColdOcean0, hFull, cOcean, eFull, wFull, dyedreamFrozenOcean);
        addSurfacePoint(dyedreamBiomePoints, tColdOcean1, hFull, cOcean, eFull, wFull, dyedreamColdOcean);
        addSurfacePoint(dyedreamBiomePoints, tWarm, hFull, cOcean, eFull, wFull, dyedreamOcean);
        // 沙滩（独占海岸带平坦侵蚀 E[-0.2225,1]，排除河谷；陡峭海岸交还陆地群系）
        addLandSurfacePoint(dyedreamBiomePoints, tFull, hFull, cCoast, eFlat, wNeg, wPos, dyedreamBeach);
        // 河流（占山脊谷带，冷/暖按温度拆分；仅平坦侵蚀 E[-0.2225,1]——陡坡/山峰谷地高于海平面会成干河，交还陆地群系）
        addSurfacePoint(dyedreamBiomePoints, tWarm, hFull, cRiver, eFlat, wValley, dyedreamRiver);
        addSurfacePoint(dyedreamBiomePoints, tCold, hFull, cRiver, eFlat, wValley, dyedreamFrozenRiver);
        // 寒冷陆地（对照原版 OverworldBiomeBuilder：峰 = E0+E1 × 中/深内陆 × 高怪异度切片，真正的山顶）
        //  染梦雪山（冰尖峰）— 峰顶 E0+E1[-1,-0.375] × 中/深内陆 C[0.03,1] × |w|∈[0.4,0.9333]（原版 Peak+High 切片，只占山顶小范围）
        addLandSurfacePoint(dyedreamBiomePoints, tCold, hFull, cMidFarInland, eUpperMountain, wPeakNeg, wPeakPos, dyedreamSnowyPeaks);
        //  染梦樱雪森林 — 湿山坡 E1~E2[-0.78,-0.2225]（W 双点；峰顶湿侧高 W 处由雪山按注册顺序先占）
        addLandSurfacePoint(dyedreamBiomePoints, tCold, hWet, cSteepLand, eSlope, wNeg, wPos, dyedreamCherryGrove);
        //  染梦雪原 — 平坦冷区（原染梦雪针叶林删除，其 冷×湿×平坦 气候由雪原吃掉，湿度扩为全区间）
        addLandSurfacePoint(dyedreamBiomePoints, tCold, hFull, cLand, eFlat, wNeg, wPos, dyedreamSnowyPlains);
        //  染梦雪原（干山坡）— 原雪坡 E1~E2 干侧 [-0.78,-0.2225] 并入雪原（雪坡合并进雪原，山坡归雪原）
        addLandSurfacePoint(dyedreamBiomePoints, tCold, hDry, cLand, eSlope, wNeg, wPos, dyedreamSnowyPlains);
        //  染梦雪原（近内陆山麓 E0）— 近内陆 C[-0.11,0.03] 的 E0 低山/山麓归雪原，雪山只在中/深内陆高山
        addLandSurfacePoint(dyedreamBiomePoints, tCold, hFull, cNearInland, ePeak, wNeg, wPos, dyedreamSnowyPlains);
        // 温暖陆地（菇山陡坡下探到海岸带）
        addLandSurfacePoint(dyedreamBiomePoints, tWarm, hFull, cSteepLand, eMountain, wNeg, wPos, dyedreamMushroomMountains);
        // 染梦平原 / 染梦花海 — 同为 暖×干×内陆×平坦 气候，按山脊 W 正负分半（仿原版向日葵平原变体）
        addSurfacePoint(dyedreamBiomePoints, tWarm, hDry, cLand, eFlat, wNeg, dyedreamPlains);
        addSurfacePoint(dyedreamBiomePoints, tWarm, hDry, cLand, eFlat, wPos, dyedreamFlowerField);
        addLandSurfacePoint(dyedreamBiomePoints, tWarm, hWet, cLand, eFlat, wNeg, wPos, dyedreamForest);
        // 洞穴（depth [0.2,0.9]，按 大陆性 C=0.8 / 湿度 H=0.7 互补切分）
        addCavePoint(dyedreamBiomePoints, tFull, hCaveDry, cCaveNonInland, eFull, wFull, dyedreamCaves);
        addCavePoint(dyedreamBiomePoints, tFull, hCaveWet, cCaveNonInland, eFull, wFull, dyedreamLushCaves);
        addCavePoint(dyedreamBiomePoints, tFull, hFull, cCaveInland, eFull, wFull, dyedreamDripstoneCaves);

        Climate.ParameterList<Holder<Biome>> biomeParams = new Climate.ParameterList<>(dyedreamBiomePoints);
        MultiNoiseBiomeSource biomeSource = createMultiNoiseSource(biomeParams);
        ChunkGenerator chunkGenerator = new NoiseBasedChunkGenerator(biomeSource, dimNoise);

        context.register(DYEDREAM_WORLD, new LevelStem(dimType, chunkGenerator));

        // ===== 灯影之下维度 =====
        Holder<Biome> shadowNyliumWastes = biomes.getOrThrow(ModBiomes.SHADOW_NYLIUM_WASTES);
        Holder<Biome> shadowForest = biomes.getOrThrow(ModBiomes.SHADOW_FOREST);
        Holder<Biome> shadowRuins = biomes.getOrThrow(ModBiomes.SHADOW_RUINS);
        Holder<Biome> shadowOcean = biomes.getOrThrow(ModBiomes.SHADOW_OCEAN);
        Holder<DimensionType> lampShadowDimType = dimensionTypes.getOrThrow(ModDimensionTypes.LAMP_SHADOW_WORLD);
        Holder<NoiseGeneratorSettings> lampShadowNoise = noiseSettings.getOrThrow(ModNoiseSettings.LAMP_SHADOW_WORLD);

        Climate.ParameterList<Holder<Biome>> lampShadowBiomeParams = new Climate.ParameterList<>(List.<Pair<Climate.ParameterPoint, Holder<Biome>>>of(
                // shadow_ocean（阴影之海）：全温度/湿度，C[-2, -0.19]海洋大陆性，约占25%
                Pair.of(
                        new Climate.ParameterPoint(
                                Climate.Parameter.span(-2F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                Climate.Parameter.span(-2F, -0.19F),
                                Climate.Parameter.span(-2F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                0L
                        ),
                        shadowOcean
                ),
                // shadow_ruins（阴影古迹）：寒冷干燥 T[-2, 0] H[-2, 0] C[-0.19, 2]，约占18.75%
                Pair.of(
                        new Climate.ParameterPoint(
                                Climate.Parameter.span(-2F, 0F),
                                Climate.Parameter.span(-2F, 0F),
                                Climate.Parameter.span(-0.19F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                0L
                        ),
                        shadowRuins
                ),
                // shadow_forest（阴影森林）：寒冷潮湿 T[-2, 0] H[0, 2] C[-0.19, 2]，约占18.75%
                Pair.of(
                        new Climate.ParameterPoint(
                                Climate.Parameter.span(-2F, 0F),
                                Climate.Parameter.span(0F, 2F),
                                Climate.Parameter.span(-0.19F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                0L
                        ),
                        shadowForest
                ),
                // shadow_nylium_wastes（菌索荒原）：温暖 T[0, 2] H[-2, 2] C[-0.19, 2]，约占37.5%
                Pair.of(
                        new Climate.ParameterPoint(
                                Climate.Parameter.span(0F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                Climate.Parameter.span(-0.19F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                Climate.Parameter.span(-2F, 2F),
                                0L
                        ),
                        shadowNyliumWastes
                )
        ));
        MultiNoiseBiomeSource lampShadowBiomeSource = createMultiNoiseSource(lampShadowBiomeParams);
        ChunkGenerator lampShadowChunkGenerator = new NoiseBasedChunkGenerator(lampShadowBiomeSource, lampShadowNoise);

        context.register(LAMP_SHADOW_WORLD, new LevelStem(lampShadowDimType, lampShadowChunkGenerator));

        // ===== 风之旅途维度 =====
        Holder<Biome> windMoorArchipelago = biomes.getOrThrow(ModBiomes.WIND_MOOR_ARCHIPELAGO);
        Holder<Biome> mistyDreamCloudLayer = biomes.getOrThrow(ModBiomes.MISTY_DREAM_CLOUD_LAYER);
        Holder<DimensionType> windJourneyDimType = dimensionTypes.getOrThrow(ModDimensionTypes.WIND_JOURNEY_WORLD);
        Holder<NoiseGeneratorSettings> windJourneyNoise = noiseSettings.getOrThrow(ModNoiseSettings.WIND_JOURNEY_WORLD);

        // 双群系（原作 multi_noise 参数）：
        //  风泊群岛 T[0,1] H[0,1] C[-0.3,1] E[0,1]
        //  迷梦云层 T[-1,0] H[-1,0] C[-1,-0.3] E[-1,0]
        Climate.ParameterList<Holder<Biome>> windJourneyBiomeParams = new Climate.ParameterList<>(List.<Pair<Climate.ParameterPoint, Holder<Biome>>>of(
                Pair.of(
                        new Climate.ParameterPoint(
                                Climate.Parameter.span(0F, 1F),
                                Climate.Parameter.span(0F, 1F),
                                Climate.Parameter.span(-0.3F, 1F),
                                Climate.Parameter.span(0F, 1F),
                                Climate.Parameter.point(0.0F),
                                Climate.Parameter.span(-1F, 1F),
                                0L
                        ),
                        windMoorArchipelago
                ),
                Pair.of(
                        new Climate.ParameterPoint(
                                Climate.Parameter.span(-1F, 0F),
                                Climate.Parameter.span(-1F, 0F),
                                Climate.Parameter.span(-1F, -0.3F),
                                Climate.Parameter.span(-1F, 0F),
                                Climate.Parameter.point(0.0F),
                                Climate.Parameter.span(-1F, 1F),
                                0L
                        ),
                        mistyDreamCloudLayer
                )
        ));
        MultiNoiseBiomeSource windJourneyBiomeSource = createMultiNoiseSource(windJourneyBiomeParams);
        ChunkGenerator windJourneyChunkGenerator = new NoiseBasedChunkGenerator(windJourneyBiomeSource, windJourneyNoise);

        context.register(WIND_JOURNEY_WORLD, new LevelStem(windJourneyDimType, windJourneyChunkGenerator));

        // ===== 亚伦柯斯竞技场维度 =====
        Holder<Biome> aaroncosArenaBiome = biomes.getOrThrow(ModBiomes.AARONCOS_ARENA);
        Holder<DimensionType> aaroncosArenaDimType = dimensionTypes.getOrThrow(ModDimensionTypes.AARONCOS_ARENA_WORLD);
        Holder<NoiseGeneratorSettings> aaroncosArenaNoise = noiseSettings.getOrThrow(ModNoiseSettings.AARONCOS_ARENA_WORLD);

        // 单群系（原作 multi_noise 参数全 0；单群系 R-tree 恒返回唯一群系，虚空维度无覆盖问题）
        Climate.ParameterList<Holder<Biome>> aaroncosArenaBiomeParams = new Climate.ParameterList<>(List.<Pair<Climate.ParameterPoint, Holder<Biome>>>of(
                Pair.of(
                        new Climate.ParameterPoint(
                                Climate.Parameter.point(0.0F),
                                Climate.Parameter.point(0.0F),
                                Climate.Parameter.point(0.0F),
                                Climate.Parameter.point(0.0F),
                                Climate.Parameter.point(0.0F),
                                Climate.Parameter.point(0.0F),
                                0L
                        ),
                        aaroncosArenaBiome
                )
        ));
        MultiNoiseBiomeSource aaroncosArenaBiomeSource = createMultiNoiseSource(aaroncosArenaBiomeParams);
        ChunkGenerator aaroncosArenaChunkGenerator = new NoiseBasedChunkGenerator(aaroncosArenaBiomeSource, aaroncosArenaNoise);

        context.register(AARONCOS_ARENA_WORLD, new LevelStem(aaroncosArenaDimType, aaroncosArenaChunkGenerator));
    }
}
