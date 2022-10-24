package fi.dy.masa.minihud.mixin.render.world_gen_overlay;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkGeneratorOverworld;

import fi.dy.masa.minihud.config.RendererToggle;
import fi.dy.masa.minihud.data.DataStorage;

@Mixin(ChunkGeneratorOverworld.class)
public abstract class ChunkGeneratorOverworldMixin
{
    @ModifyArg(method = "populate",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/world/gen/feature/WorldGenDungeons;generate(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"))
    private BlockPos onDungeonGenerationAttempt(BlockPos position)
    {
        if (RendererToggle.SPAWNER_POSITIONS.isRendererEnabled())
        {
            DataStorage.getInstance().addDungeonSpawnerPosition(position);
        }

        return position;
    }
}
