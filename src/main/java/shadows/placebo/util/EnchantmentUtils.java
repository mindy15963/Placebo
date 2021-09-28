package shadows.placebo.util;

import java.util.List;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Enchantment Utility class used by OpenMods.  Replicated here under the permissions of the MIT Licenses.
 * @author boq
 *
 */
public class EnchantmentUtils {

	/**
	 * Be warned, minecraft doesn't update experienceTotal properly, so we have
	 * to do this.
	 *
	 * @param player
	 * @return
	 */
	public static int getPlayerXP(Player player) {
		return (int) (EnchantmentUtils.getExperienceForLevel(player.experienceLevel) + player.experienceProgress * player.getXpNeededForNextLevel());
	}

	public static void addPlayerXP(Player player, int amount) {
		int experience = getPlayerXP(player) + amount;
		player.totalExperience = experience;
		player.experienceLevel = EnchantmentUtils.getLevelForExperience(experience);
		int expForLevel = EnchantmentUtils.getExperienceForLevel(player.experienceLevel);
		player.experienceProgress = (float) (experience - expForLevel) / (float) player.getXpNeededForNextLevel();
	}

	public static int xpBarCap(int level) {
		if (level >= 30) return 112 + (level - 30) * 9;

		if (level >= 15) return 37 + (level - 15) * 5;

		return 7 + level * 2;
	}

	private static int sum(int n, int a0, int d) {
		return n * (2 * a0 + (n - 1) * d) / 2;
	}

	public static int getExperienceForLevel(int level) {
		if (level == 0) return 0;
		if (level <= 15) return sum(level, 7, 2);
		if (level <= 30) return 315 + sum(level - 15, 37, 5);
		return 1395 + sum(level - 30, 112, 9);
	}

	public static int getXpToNextLevel(int level) {
		int levelXP = EnchantmentUtils.getLevelForExperience(level);
		int nextXP = EnchantmentUtils.getExperienceForLevel(level + 1);
		return nextXP - levelXP;
	}

	public static int getLevelForExperience(int targetXp) {
		int level = 0;
		while (true) {
			final int xpToNextLevel = xpBarCap(level);
			if (targetXp < xpToNextLevel) return level;
			level++;
			targetXp -= xpToNextLevel;
		}
	}

	public static float getPower(Level world, BlockPos position) {
		float power = 0;

		for (int deltaZ = -1; deltaZ <= 1; ++deltaZ) {
			for (int deltaX = -1; deltaX <= 1; ++deltaX) {
				if ((deltaZ != 0 || deltaX != 0) && world.isEmptyBlock(position.offset(deltaX, 0, deltaZ)) && world.isEmptyBlock(position.offset(deltaX, 1, deltaZ))) {
					power += getEnchantPower(world, position.offset(deltaX * 2, 0, deltaZ * 2));
					power += getEnchantPower(world, position.offset(deltaX * 2, 1, deltaZ * 2));
					if (deltaX != 0 && deltaZ != 0) {
						power += getEnchantPower(world, position.offset(deltaX * 2, 0, deltaZ));
						power += getEnchantPower(world, position.offset(deltaX * 2, 1, deltaZ));
						power += getEnchantPower(world, position.offset(deltaX, 0, deltaZ * 2));
						power += getEnchantPower(world, position.offset(deltaX, 1, deltaZ * 2));
					}
				}
			}
		}
		return power;
	}

	static float getEnchantPower(Level world, BlockPos pos) {
		return world.getBlockState(pos).getEnchantPowerBonus(world, pos);
	}

	public static void addAllBooks(Enchantment enchantment, List<ItemStack> items) {
		for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++)
			items.add(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i)));
	}
}