package shadows.placebo.util;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class ItemTier implements Tier {

	protected int durability, level, enchantability;
	protected float efficiency, damage;
	protected Ingredient repair;

	public ItemTier(int level, int durability, float efficiency, float damage, int enchantability, Ingredient repair) {
		this.level = level;
		this.durability = durability;
		this.efficiency = efficiency;
		this.damage = damage;
		this.enchantability = enchantability;
		this.repair = repair;
	}

	@Override
	public int getUses() {
		return this.durability;
	}

	@Override
	public float getSpeed() {
		return this.efficiency;
	}

	@Override
	public float getAttackDamageBonus() {
		return this.damage;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repair;
	}

}
