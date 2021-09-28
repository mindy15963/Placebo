package shadows.placebo.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * A Util class to create TileEntityMobSpawner nbt tags.
 * @author Shadows
 *
 */
public class SpawnerBuilder {

	public static final String SPAWN_DELAY = "Delay";
	public static final String SPAWN_POTENTIALS = "SpawnPotentials";
	public static final String SPAWN_DATA = "SpawnData";
	public static final String MIN_SPAWN_DELAY = "MinSpawnDelay";
	public static final String MAX_SPAWN_DELAY = "MaxSpawnDelay";
	public static final String SPAWN_COUNT = "SpawnCount";
	public static final String MAX_NEARBY_ENTITIES = "MaxNearbyEntities";
	public static final String REQUIRED_PLAYER_RANGE = "RequiredPlayerRange";
	public static final String SPAWN_RANGE = "SpawnRange";
	public static final String ID = "id";
	public static final String ENTITY = "Entity"; //WeightedSpawnerEntity's internal entity tag
	public static final CompoundTag BASE_TAG;
	static {
		SpawnerBlockEntity te = (SpawnerBlockEntity) ((EntityBlock) Blocks.SPAWNER).newBlockEntity(null, null);
		te.getSpawner().setEntityId(EntityType.PIG);
		BASE_TAG = te.save(new CompoundTag());
		BASE_TAG.getList(SPAWN_POTENTIALS, NBT.TAG_COMPOUND).clear();
	}

	CompoundTag tag = BASE_TAG.copy();
	boolean hasPotentials = false;
	SpawnData baseEntity = new SpawnData();

	public SpawnerBuilder() {
		this.tag.put(SPAWN_DATA, this.baseEntity.getTag());
	}

	/**
	 * Sets the mob type of the first spawn (or all spawns if potentials are not set).
	 */
	public SpawnerBuilder setType(EntityType<? extends Entity> entity) {
		return this.setType(entity.getRegistryName());
	}

	/**
	 * Sets the mob type of the first spawn (or all spawns if potentials are not set).
	 */
	public SpawnerBuilder setType(ResourceLocation entity) {
		this.baseEntity.getTag().putString(ID, entity.toString());
		return this;
	}

	/**
	 * Sets the delay before the first spawn. Set to -1 to skip first spawn.
	 */
	public SpawnerBuilder setDelay(int delay) {
		this.tag.putShort(SPAWN_DELAY, (short) delay);
		return this;
	}

	/**
	 * Sets min spawn delay.
	 */
	public SpawnerBuilder setMinDelay(int delay) {
		this.tag.putShort(MIN_SPAWN_DELAY, (short) delay);
		return this;
	}

	/**
	 * Sets max spawn delay.
	 */
	public SpawnerBuilder setMaxDelay(int delay) {
		this.tag.putShort(MAX_SPAWN_DELAY, (short) delay);
		return this;
	}

	/**
	 * Sets min and max spawn delays.
	 */
	public SpawnerBuilder setMinAndMaxDelay(int min, int max) {
		this.setMinDelay(min);
		this.setMaxDelay(max);
		return this;
	}

	/**
	 * Sets the number of spawn attempts.
	 */
	public SpawnerBuilder setSpawnCount(int count) {
		this.tag.putShort(SPAWN_COUNT, (short) count);
		return this;
	}

	/**
	 * Sets the max nearby entities.
	 */
	public SpawnerBuilder setMaxNearbyEntities(int max) {
		this.tag.putShort(MAX_NEARBY_ENTITIES, (short) max);
		return this;
	}

	/**
	 * Sets the required player radius (in blocks) to activate.
	 */
	public SpawnerBuilder setPlayerRange(int range) {
		this.tag.putShort(REQUIRED_PLAYER_RANGE, (short) range);
		return this;
	}

	/**
	 * Sets the spawn radius (in blocks).
	 */
	public SpawnerBuilder setSpawnRange(int range) {
		this.tag.putShort(SPAWN_RANGE, (short) range);
		return this;
	}

	/**
	 * Sets the additional NBT data for the first mob spawned (or all, if potentials are not set).
	 * @param data An entity, written to NBT, in the format read by AnvilChunkLoader.readWorldEntity()
	 */
	public SpawnerBuilder setSpawnData(CompoundTag data) {
		if (data == null) {
			data = new CompoundTag();
			data.putString(ID, "minecraft:pig");
		}
		this.baseEntity.tag = data.copy();
		return this;
	}

	/*
	 * Sets the list of entities the mob spawner will choose from.
	 */
	public SpawnerBuilder setPotentials(SpawnData... entries) {
		this.hasPotentials = true;
		this.tag.put(SPAWN_POTENTIALS, new ListTag());
		ListTag list = this.tag.getList(SPAWN_POTENTIALS, 10);
		for (SpawnData e : entries)
			list.add(e.save());
		return this;
	}

	/*
	 * Adds to the list of entities the mob spawner will choose from.
	 */
	public SpawnerBuilder addPotentials(SpawnData... entries) {
		this.hasPotentials = true;
		ListTag list = this.tag.getList(SPAWN_POTENTIALS, 10);
		for (SpawnData e : entries)
			list.add(e.save());
		return this;
	}

	/**
	 * @return The spawn data, represented as an entity nbt tag.
	 */
	public CompoundTag getSpawnData() {
		return this.tag.getCompound(SPAWN_DATA);
	}

	/**
	 * @return The spawn data, represented as an entity nbt tag.
	 */
	public ListTag getPotentials() {
		return this.tag.getList(SPAWN_POTENTIALS, 10);
	}

	public void build(LevelAccessor world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() != Blocks.SPAWNER) {
			world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);
		}
		BlockEntity s = world.getBlockEntity(pos);
		s.load(this.tag);
	}
}