package shadows.placebo.util;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;

/**
 * A Util class to edit spawners in world.  Constructing one will create a spawner if not present.
 * @author Shadows
 *
 */
public class SpawnerEditor {

	//Default variables for configurable spawner stats.
	public static final int SPAWN_DELAY = 20;
	public static final int MIN_SPAWN_DELAY = 200;
	public static final int MAX_SPAWN_DELAY = 800;
	public static final int SPAWN_COUNT = 4;
	public static final int MAX_NEARBY_ENTITIES = 6;
	public static final int PLAYER_RANGE = 16;
	public static final int SPAWN_RANGE = 4;

	protected SpawnerBlockEntity spawner;

	public SpawnerEditor(LevelAccessor world, BlockPos pos) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof SpawnerBlockEntity) {
			this.spawner = (SpawnerBlockEntity) te;
		} else {
			world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);
			te = world.getBlockEntity(pos);
			this.spawner = (SpawnerBlockEntity) te;
		}
	}

	/**
	 * Sets the mob type of the first spawn (or all spawns if potentials are not set).
	 */
	public SpawnerEditor setType(EntityType<? extends Entity> entity) {
		this.spawner.getSpawner().setEntityId(entity);
		return this;
	}

	/**
	 * Sets the delay before the first spawn. Set to -1 to skip first spawn.
	 */
	public SpawnerEditor setDelay(int delay) {
		this.spawner.getSpawner().spawnDelay = delay;
		return this;
	}

	/**
	 * Sets min spawn delay.
	 */
	public SpawnerEditor setMinDelay(int min) {
		this.spawner.getSpawner().minSpawnDelay = min;
		return this;
	}

	/**
	 * Sets max spawn delay.
	 */
	public SpawnerEditor setMaxDelay(int max) {
		this.spawner.getSpawner().maxSpawnDelay = max;
		return this;
	}

	/**
	 * Sets min and max spawn delays.
	 */
	public SpawnerEditor setMinAndMaxDelay(int min, int max) {
		this.setMinDelay(min);
		this.setMaxDelay(max);
		return this;
	}

	/**
	 * Sets the number of spawn attempts.
	 */
	public SpawnerEditor setSpawnCount(int count) {
		this.spawner.getSpawner().spawnCount = count;
		return this;
	}

	/**
	 * Sets the max nearby entities.
	 */
	public SpawnerEditor setMaxNearbyEntities(int max) {
		this.spawner.getSpawner().maxNearbyEntities = max;
		return this;
	}

	/**
	 * Sets the required player radius (in blocks) to activate.
	 */
	public SpawnerEditor setPlayerRange(int range) {
		this.spawner.getSpawner().requiredPlayerRange = range;
		return this;
	}

	/**
	 * Sets the spawn radius (in blocks).
	 */
	public SpawnerEditor setSpawnRange(int range) {
		this.spawner.getSpawner().spawnRange = range;
		return this;
	}

	/**
	 * Sets the additional NBT data for the first mob spawned (or all, if potentials are not set). <br>
	 * @param data An entity, written to NBT, in the format read by AnvilChunkLoader.readWorldEntity()
	 */
	public SpawnerEditor setSpawnData(int weight, @Nullable CompoundTag data) {
		return this.setSpawnData(data == null ? null : new SpawnData(weight, data));
	}

	/**
	 * Sets the additional NBT data for the first mob spawned (or all, if potentials are not set). <br>
	 * @param data An entity, written to NBT, in the format read by AnvilChunkLoader.readWorldEntity()
	 */
	public SpawnerEditor setSpawnData(@Nullable SpawnData entity) {
		if (entity == null) this.spawner.getSpawner().nextSpawnData = new SpawnData();
		else this.spawner.getSpawner().nextSpawnData = entity;
		return this;
	}

	/*
	 * Sets the list of entities the mob spawner will choose from. <br>
	 * Does not change the currently spawned mob.  Should probably be followed up by a call to setSpawnData.
	 */
	public SpawnerEditor setPotentials(SpawnData... entries) {
		this.spawner.getSpawner().spawnPotentials = WeightedRandomList.create(entries);
		return this;
	}
}