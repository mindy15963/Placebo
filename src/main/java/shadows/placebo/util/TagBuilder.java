package shadows.placebo.util;

import java.util.Arrays;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.PotionUtils;

/**
 * Util class to manipulate entities before they are spawned.
 * @author Shadows
 *
 */
@Deprecated
public class TagBuilder {

	public static final String HAND_ITEMS = "HandItems";
	public static final String ARMOR_ITEMS = "ArmorItems";
	public static final String PASSENGERS = "Passengers";
	public static final String PERSISTENT = "PersistenceRequired";
	public static final String HEALTH = "Health";
	public static final String OFFSET = "Offset";
	public static final String MOTION = "Motion";
	public static final String DIRECTION = "direction";
	public static final String ENTITY_FIRE = "Fire";
	public static final String ARROW_PICKUP = "pickup";
	public static final String ARROW_DAMAGE = "damage";
	public static final CompoundTag ARROW = getDefaultTag(EntityType.ARROW);
	public static final String EFFECTS = "ActiveEffects";
	public static final String TIME = "Time";
	public static final String DROP_ITEM = "DropItem";
	public static final String HURT_ENTITIES = "HurtEntities";
	public static final String FALL_HURT_AMOUNT = "FallHurtAmount";
	public static final String FALL_HURT_MAX = "FallHurtMax";
	public static final String TILE_ENTITY_DATA = "TileEntityData";
	public static final CompoundTag TNT = getDefaultTag(EntityType.TNT);
	public static final String FUSE = "Fuse";

	/**
	 * Creates a tag that will spawn this entity, with all default values.
	 */
	public static CompoundTag getDefaultTag(EntityType<? extends Entity> entity) {
		CompoundTag tag = new CompoundTag();
		tag.putString(SpawnerBuilder.ID, entity.getRegistryName().toString());
		return tag;
	}

	/**
	 * Sets this entity's hp.
	 */
	public static CompoundTag setHealth(CompoundTag entity, float health) {
		entity.putFloat(HEALTH, health);
		return entity;
	}

	/**
	 * Tells this entity to not despawn naturally.
	 */
	public static CompoundTag setPersistent(CompoundTag entity, boolean persistent) {
		entity.putBoolean(PERSISTENT, persistent);
		return entity;
	}

	/**
	 * Sets the equipment of a written entity.
	 * @param tag The entity tag, created by {@link Entity#writeToNBT}
	 * @param equipment Stacks to represent the equipment, in the order of EntityEquipmentSlot
	 */
	public static CompoundTag setEquipment(CompoundTag entity, ItemStack... equipment) {
		ItemStack[] stacks = fixStacks(equipment);
		ListTag tagListHands = new ListTag();
		for (int i = 0; i < 2; i++)
			tagListHands.add(new CompoundTag());

		ListTag tagListArmor = new ListTag();
		for (int i = 0; i < 4; i++)
			tagListArmor.add(new CompoundTag());

		for (EquipmentSlot s : EquipmentSlot.values()) {
			ItemStack stack = stacks[s.ordinal()];
			if (s.getType() == EquipmentSlot.Type.HAND && !stack.isEmpty()) {
				tagListHands.set(s.getIndex(), stack.save(new CompoundTag()));
			} else if (!stack.isEmpty()) {
				tagListArmor.set(s.getIndex(), stack.save(new CompoundTag()));
			}
		}

		entity.put(HAND_ITEMS, tagListHands);
		entity.put(ARMOR_ITEMS, tagListArmor);
		return entity;
	}

	private static ItemStack[] fixStacks(ItemStack[] unfixed) {
		ItemStack[] stacks = new ItemStack[] { ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, };
		for (int i = 0; i < unfixed.length; i++)
			if (unfixed[i] != null) stacks[i] = unfixed[i];
		return stacks;
	}

	/**
	 * Sets the equipment drop chances of a written entity.
	 * @param tag The entity tag, created by {@link Entity#writeToNBT}
	 * @param chances Drop chances for each slot, in the order of EntityEquipmentSlot.  If a chance is above 1, that slot can drop without requiring player damage.
	 */
	public static CompoundTag setDropChances(CompoundTag entity, float... chances) {
		float[] fixed = fixChances(chances);
		ListTag tagListHands = new ListTag();
		ListTag tagListArmor = new ListTag();

		for (EquipmentSlot s : EquipmentSlot.values()) {
			FloatTag chance = FloatTag.valueOf(fixed[s.ordinal()]);
			if (s.getType() == EquipmentSlot.Type.HAND) {
				tagListHands.set(s.getIndex(), chance);
			} else tagListArmor.set(s.getIndex(), chance);

		}

		entity.put(HAND_ITEMS, tagListHands);
		entity.put(ARMOR_ITEMS, tagListArmor);
		return entity;
	}

	private static float[] fixChances(float[] unfixed) {
		float[] chances = new float[6];
		for (int i = 0; i < unfixed.length; i++)
			chances[i] = unfixed[i];
		return chances;
	}

	/**
	 * Sets this entity to spawn with a given offset from the spawner, instead of using random coordinates.
	 */
	public static CompoundTag setOffset(CompoundTag entity, double x, double y, double z) {
		entity.put(OFFSET, doubleTagList(x, y, z));
		return entity;
	}

	/**
	 * Sets the motion of an entity.
	 */
	public static CompoundTag setMotion(CompoundTag entity, double x, double y, double z) {
		entity.put(MOTION, doubleTagList(x, y, z));
		return entity;
	}

	/**
	 * Sets fireball motion, because fireballs are stupid and use their own key.
	 */
	public static CompoundTag setFireballMotion(CompoundTag entity, double x, double y, double z) {
		entity.put(DIRECTION, doubleTagList(x, y, z));
		return entity;
	}

	public static ListTag doubleTagList(double... data) {
		ListTag tagList = new ListTag();
		for (double d : data)
			tagList.add(DoubleTag.valueOf(d));
		return tagList;
	}

	/**
	 * Adds a potion effect to the stack and returns it.
	 */
	public static ItemStack addPotionEffect(ItemStack stack, MobEffect potion, int duration, int amplifier) {
		return PotionUtils.setCustomEffects(stack, Arrays.asList(new MobEffectInstance(potion, duration, amplifier)));
	}

	/**
	 * Adds a potion effect to this entity.
	 */
	public static void addPotionEffect(CompoundTag tag, MobEffect potion, int amplifier) {
		TagBuilder.addPotionEffect(tag, potion, Integer.MAX_VALUE, amplifier, false);
	}

	/**
	 * Adds a potion effect to this entity.
	 */
	public static void addPotionEffect(CompoundTag tag, MobEffect potion, int amplifier, boolean showParticles) {
		TagBuilder.addPotionEffect(tag, potion, Integer.MAX_VALUE, amplifier, showParticles);
	}

	/**
	 * Adds a potion effect to this entity.
	 */
	public static void addPotionEffect(CompoundTag tag, MobEffect potion, int duration, int amplifier) {
		TagBuilder.addPotionEffect(tag, potion, duration, amplifier, false);
	}

	/**
	 * Adds a potion effect to this entity.
	 */
	public static CompoundTag addPotionEffect(CompoundTag entity, MobEffect potion, int duration, int amplifier, boolean showParticles) {
		ListTag effects = entity.getList(EFFECTS, 10);
		MobEffectInstance fx = new MobEffectInstance(potion, duration, amplifier, false, showParticles);
		effects.add(fx.save(new CompoundTag()));
		entity.put(EFFECTS, effects);
		return entity;
	}

	/**
	 * Makes a falling block tag.
	 */
	public static CompoundTag fallingBlock(BlockState state, int time) {
		return TagBuilder.fallingBlock(state, time, false, 2, 40, false, null);
	}

	/**
	 * Makes a falling block tag.
	 */
	public static CompoundTag fallingBlock(BlockState state, int time, float fallDamage) {
		return TagBuilder.fallingBlock(state, time, true, fallDamage, 40, false, null);
	}

	/**
	 * Makes a falling block.
	 * @param state The state to show.
	 * @param time How long we have been falling.  Despawn after 600 ticks, or if starting below 0, once we hit 0.
	 * @param hurtEntities If we damage entities.
	 * @param fallDamage How much we damage entities for, if hurtEntities is true.
	 * @param maxFallDamage The max amount we can damage an entity for.
	 * @param dropItem If we drop our block on despawn.
	 * @param tileData Tile entity data, to be set if we hit the ground/
	 * @return An CompoundNBT containing a falling block.
	 */
	public static CompoundTag fallingBlock(BlockState state, int time, boolean hurtEntities, float fallDamage, int maxFallDamage, boolean dropItem, CompoundTag tileData) {
		CompoundTag tag = getDefaultTag(EntityType.FALLING_BLOCK);
		tag.put("BlockState", NbtUtils.writeBlockState(state));
		tag.putInt(TIME, time);
		tag.putBoolean(DROP_ITEM, dropItem);
		tag.putBoolean(HURT_ENTITIES, hurtEntities);
		tag.putFloat(FALL_HURT_AMOUNT, fallDamage);
		tag.putInt(FALL_HURT_MAX, maxFallDamage);
		if (tileData != null) tag.put(TILE_ENTITY_DATA, tileData);
		return tag;
	}

	/**
	 * Converts a standard entity tag into one of tnt riding the entity.
	 * @param tag An entity written to NBT.
	 * @return The provided entity, now with TNT on it's head.
	 */
	public static CompoundTag applyTNTHat(CompoundTag tag) {
		TagBuilder.setMotion(tag, 0.0, 0.3, 0.0);
		TagBuilder.addPotionEffect(tag, MobEffects.MOVEMENT_SPEED, 1);
		TagBuilder.addPotionEffect(tag, MobEffects.DAMAGE_RESISTANCE, -6);
		addPassengers(tag, TNT.copy());
		return tag;
	}

	/**
	 * Sets the provided passengers to ride on the given entity.
	 */
	public static CompoundTag addPassengers(CompoundTag entity, CompoundTag... passengers) {
		ListTag list = entity.getList(PASSENGERS, 10);
		if (list.isEmpty()) entity.put(PASSENGERS, list);
		for (CompoundTag nbt : passengers)
			list.add(nbt);
		return entity;
	}

	/**
	 * Forcibly gives skeletons bows.
	 */
	public static CompoundTag checkForSkeleton(CompoundTag entity) {
		if (entity.getString(SpawnerBuilder.ID).contains("skeleton")) {
			TagBuilder.setEquipment(entity, new ItemStack(Items.BOW));
		}
		return entity;
	}

	public static CompoundTag checkForCreeper(CompoundTag entity) {
		if (entity.getString(SpawnerBuilder.ID).contains("creeper")) {
			ListTag effects = entity.getList(EFFECTS, 10);
			for (Tag nbt : effects) {
				((CompoundTag) nbt).putInt("Duration", 300);
			}
			return entity;
		}
		return entity;
	}

}