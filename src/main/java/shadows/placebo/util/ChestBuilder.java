package shadows.placebo.util;

import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import shadows.placebo.loot.StackLootEntry;

/**
 * Utils for loot chests. Uses the Placebo loot system.
 * @author Shadows
 *
 */
public class ChestBuilder {

	protected Random random;
	protected ChestBlockEntity chest;
	protected boolean isValid;
	protected BlockPos position;
	protected LevelAccessor iWorld;

	public ChestBuilder(LevelAccessor world, Random rand, BlockPos pos) {
		BlockEntity tileEntity = world.getBlockEntity(pos);
		if (tileEntity instanceof ChestBlockEntity) {
			this.random = rand;
			this.chest = (ChestBlockEntity) tileEntity;
			this.isValid = true;
			this.position = pos;
			this.iWorld = world;
		}
	}

	public void fill(ResourceLocation loot) {
		if (this.iWorld != null) {
			RandomizableContainerBlockEntity.setLootTable(this.iWorld, this.random, this.position, loot);
		} else {
			this.chest.setLootTable(loot, this.random.nextLong());
		}
	}

	public static LootPoolEntryContainer loot(Item item, int min, int max, int weight, int quality) {
		return loot(new ItemStack(item), min, max, weight, quality);
	}

	public static LootPoolEntryContainer loot(Block block, int min, int max, int weight, int quality) {
		return loot(new ItemStack(block), min, max, weight, quality);
	}

	public static LootPoolEntryContainer loot(ItemStack item, int min, int max, int weight, int quality) {
		return new StackLootEntry(item, min, max, weight, quality);
	}

	public static void place(LevelAccessor world, Random random, BlockPos pos, ResourceLocation loot) {
		world.setBlock(pos, Blocks.CHEST.defaultBlockState(), 2);
		ChestBuilder chest = new ChestBuilder(world, random, pos);
		if (chest.isValid) {
			chest.fill(loot);
		}
	}

	public static void placeTrapped(LevelAccessor world, Random random, BlockPos pos, ResourceLocation loot) {
		world.setBlock(pos, Blocks.TRAPPED_CHEST.defaultBlockState(), 2);
		ChestBuilder chest = new ChestBuilder(world, random, pos);
		if (chest.isValid) {
			chest.fill(loot);
		}
	}

	public static class EnchantedEntry extends StackLootEntry {

		protected final LootItemFunction func = EnchantRandomlyFunction.randomApplicableEnchantment().build();
		protected Item i;

		public EnchantedEntry(Item i, int weight) {
			super(i, 1, 1, weight, 5);
			this.i = i;
		}

		@Override
		protected void createItemStack(Consumer<ItemStack> list, LootContext ctx) {
			list.accept(this.func.apply(new ItemStack(this.i), ctx));
		}

	}
}