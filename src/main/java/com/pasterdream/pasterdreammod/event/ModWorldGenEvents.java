package com.pasterdream.pasterdreammod.event;

import com.pasterdream.pasterdreammod.world.dimension.LampShadowWorldDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModWorldGenEvents {

    // 暮影之笼结构（下界基岩层上方）
    private static final ResourceLocation TWILIGHT_LANTERN =
            ResourceLocation.fromNamespaceAndPath("pasterdream", "twilight_lantern");
    private static final int TWILIGHT_LANTERN_Y = 128;
    private static final int TWILIGHT_LANTERN_RANGE = 2000;

    // 灯影之下出生点结构
    private static final ResourceLocation SHADOW_WORLD_SPAWN =
            ResourceLocation.fromNamespaceAndPath("pasterdream", "shadow_world_spawn");

    private static volatile boolean twilightLanternNeedsPlacement = false;
    private static volatile boolean lampShadowSpawnNeedsPlacement = false;

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;

        if (serverLevel.dimension().equals(Level.NETHER)) {
            TwilightLanternPlacedData data = TwilightLanternPlacedData.get(serverLevel);
            if (!data.isPlaced()) {
                twilightLanternNeedsPlacement = true;
            }
        }

        if (serverLevel.dimension().equals(LampShadowWorldDimension.LAMP_SHADOW_WORLD)) {
            LampShadowSpawnPlacedData data = LampShadowSpawnPlacedData.get(serverLevel);
            if (!data.isPlaced()) {
                lampShadowSpawnNeedsPlacement = true;
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (twilightLanternNeedsPlacement) {
            ServerLevel nether = event.getServer().getLevel(Level.NETHER);
            if (nether != null) {
                TwilightLanternPlacedData data = TwilightLanternPlacedData.get(nether);
                if (!data.isPlaced()) {
                    twilightLanternNeedsPlacement = false;
                    placeTwilightLantern(nether, data);
                } else {
                    twilightLanternNeedsPlacement = false;
                }
            }
        }

        if (lampShadowSpawnNeedsPlacement) {
            ServerLevel lampShadow = event.getServer().getLevel(LampShadowWorldDimension.LAMP_SHADOW_WORLD);
            if (lampShadow != null) {
                LampShadowSpawnPlacedData data = LampShadowSpawnPlacedData.get(lampShadow);
                if (!data.isPlaced()) {
                    lampShadowSpawnNeedsPlacement = false;
                    placeShadowWorldSpawn(lampShadow, data);
                } else {
                    lampShadowSpawnNeedsPlacement = false;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCheckSpawn(MobSpawnEvent.PositionCheck event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
        if (!serverLevel.dimension().equals(Level.NETHER)) return;
        MobSpawnType spawnType = event.getSpawnType();
        if (spawnType != MobSpawnType.NATURAL && spawnType != MobSpawnType.CHUNK_GENERATION) return;

        TwilightLanternPlacedData data = TwilightLanternPlacedData.get(serverLevel);
        if (!data.isPlaced()) return;

        if (data.isInBounds((int) event.getX(), (int) event.getY(), (int) event.getZ())) {
            event.setResult(net.minecraftforge.eventbus.api.Event.Result.DENY);
        }
    }

    private static void placeTwilightLantern(ServerLevel serverLevel, TwilightLanternPlacedData data) {
        StructureTemplate template = serverLevel.getStructureManager()
                .get(TWILIGHT_LANTERN).orElse(null);
        if (template == null) return;

        RandomSource random = serverLevel.getRandom();
        int x = random.nextIntBetweenInclusive(-TWILIGHT_LANTERN_RANGE, TWILIGHT_LANTERN_RANGE);
        int z = random.nextIntBetweenInclusive(-TWILIGHT_LANTERN_RANGE, TWILIGHT_LANTERN_RANGE);

        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        serverLevel.getChunkSource().getChunk(chunkX, chunkZ, true);

        BlockPos origin = new BlockPos(x - 22, TWILIGHT_LANTERN_Y, z - 21);
        StructurePlaceSettings settings = new StructurePlaceSettings();
        template.placeInWorld(serverLevel, origin, origin, settings, random, 3);

        data.setPlaced(x, z, origin, template.getSize());
        serverLevel.getDataStorage().save(); // 立即落盘，防止维度重载导致重复放置
    }

    private static void placeShadowWorldSpawn(ServerLevel serverLevel, LampShadowSpawnPlacedData data) {
        StructureTemplate template = serverLevel.getStructureManager()
                .get(SHADOW_WORLD_SPAWN).orElse(null);
        if (template == null) return;

        // Match the teleport logic in LampShadowWorldTeleporter:
        // if block at (0, 100, 0) is air → place at y=100, teleport to y=104
        // else → place at y=150, teleport to y=154
        boolean low = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, -9, -9) <= 100;
        int y = low ? 100 : 150;

        BlockPos origin = new BlockPos(-11, y, -9);
        StructurePlaceSettings settings = new StructurePlaceSettings();
        RandomSource random = serverLevel.getRandom();

        // Ensure chunk is loaded
        int chunkX = origin.getX() >> 4;
        int chunkZ = origin.getZ() >> 4;
        serverLevel.getChunkSource().getChunk(chunkX, chunkZ, true);

        template.placeInWorld(serverLevel, origin, origin, settings, random, 3);
        data.setPlaced();
        serverLevel.getDataStorage().save(); // 立即落盘，防止维度重载导致重复放置
    }

    public static class TwilightLanternPlacedData extends SavedData {
        private static final String DATA_NAME = "pasterdream_twilight_lantern";
        private boolean placed = false;
        private int posX;
        private int posZ;
        private int boundMinX, boundMinY, boundMinZ;
        private int boundMaxX, boundMaxY, boundMaxZ;

        public TwilightLanternPlacedData() {}

        public static TwilightLanternPlacedData load(CompoundTag tag) {
            TwilightLanternPlacedData data = new TwilightLanternPlacedData();
            data.placed = tag.getBoolean("placed");
            data.posX = tag.getInt("posX");
            data.posZ = tag.getInt("posZ");
            data.boundMinX = tag.getInt("boundMinX");
            data.boundMinY = tag.getInt("boundMinY");
            data.boundMinZ = tag.getInt("boundMinZ");
            data.boundMaxX = tag.getInt("boundMaxX");
            data.boundMaxY = tag.getInt("boundMaxY");
            data.boundMaxZ = tag.getInt("boundMaxZ");
            return data;
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            tag.putBoolean("placed", this.placed);
            tag.putInt("posX", this.posX);
            tag.putInt("posZ", this.posZ);
            tag.putInt("boundMinX", this.boundMinX);
            tag.putInt("boundMinY", this.boundMinY);
            tag.putInt("boundMinZ", this.boundMinZ);
            tag.putInt("boundMaxX", this.boundMaxX);
            tag.putInt("boundMaxY", this.boundMaxY);
            tag.putInt("boundMaxZ", this.boundMaxZ);
            return tag;
        }

        public static TwilightLanternPlacedData get(ServerLevel level) {
            return level.getDataStorage().computeIfAbsent(
                    TwilightLanternPlacedData::load, TwilightLanternPlacedData::new, DATA_NAME);
        }

        public boolean isPlaced() {
            return this.placed;
        }

        public int getPosX() {
            return posX;
        }

        public int getPosZ() {
            return posZ;
        }

        public boolean isInBounds(int x, int y, int z) {
            return boundMaxX > boundMinX
                && x >= boundMinX && x <= boundMaxX
                && y >= boundMinY && y <= boundMaxY
                && z >= boundMinZ && z <= boundMaxZ;
        }

        public void setPlaced(int x, int z, BlockPos origin, Vec3i size) {
            this.placed = true;
            this.posX = x;
            this.posZ = z;
            this.boundMinX = origin.getX();
            this.boundMinY = origin.getY();
            this.boundMinZ = origin.getZ();
            this.boundMaxX = origin.getX() + size.getX();
            this.boundMaxY = origin.getY() + size.getY();
            this.boundMaxZ = origin.getZ() + size.getZ();
            setDirty();
        }
    }

    public static class LampShadowSpawnPlacedData extends SavedData {
        private static final String DATA_NAME = "pasterdream_lamp_shadow_spawn";
        private boolean placed = false;

        public LampShadowSpawnPlacedData() {}

        public static LampShadowSpawnPlacedData load(CompoundTag tag) {
            LampShadowSpawnPlacedData data = new LampShadowSpawnPlacedData();
            data.placed = tag.getBoolean("placed");
            return data;
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            tag.putBoolean("placed", this.placed);
            return tag;
        }

        public static LampShadowSpawnPlacedData get(ServerLevel level) {
            return level.getDataStorage().computeIfAbsent(
                    LampShadowSpawnPlacedData::load, LampShadowSpawnPlacedData::new, DATA_NAME);
        }

        public boolean isPlaced() {
            return this.placed;
        }

        public void setPlaced() {
            this.placed = true;
            setDirty();
        }
    }
}
