package shadows.placebo.loot;

import java.util.function.Consumer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import shadows.placebo.Placebo;
import shadows.placebo.recipe.RecipeHelper;
import shadows.placebo.util.json.ItemAdapter;

public class StackLootEntry extends LootPoolSingletonContainer {
	public static final Serializer SERIALIZER = new Serializer();
	public static final LootPoolEntryType STACKLOOTENTRYTYPE = Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, new ResourceLocation(Placebo.MODID, "stack_entry"), new LootPoolEntryType(SERIALIZER));

	private final ItemStack stack;
	private final int min;
	private final int max;

	public StackLootEntry(ItemStack stack, int min, int max, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
		super(weight, quality, conditions, functions);
		this.stack = stack;
		this.min = min;
		this.max = max;
	}

	public StackLootEntry(ItemStack stack, int min, int max, int weight, int quality) {
		this(stack, min, max, weight, quality, new LootItemCondition[0], new LootItemFunction[0]);
	}

	public StackLootEntry(IForgeRegistryEntry<?> thing, int min, int max, int weight, int quality) {
		this(RecipeHelper.makeStack(thing), min, max, weight, quality);
	}

	public StackLootEntry(ItemStack stack) {
		this(stack, 1, 1, 1, 0);
	}

	@Override
	protected void createItemStack(Consumer<ItemStack> list, LootContext ctx) {
		ItemStack s = this.stack.copy();
		s.setCount(Mth.nextInt(ctx.getRandom(), this.min, this.max));
		list.accept(s);
	}

	@Override
	public LootPoolEntryType getType() {
		return STACKLOOTENTRYTYPE;
	}

	public static class Serializer extends LootPoolSingletonContainer.Serializer<StackLootEntry> {

		@Override
		protected StackLootEntry deserialize(JsonObject jsonObject, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] lootConditions, LootItemFunction[] lootFunctions) {
			int min = GsonHelper.getAsInt(jsonObject, "min", 1);
			int max = GsonHelper.getAsInt(jsonObject, "max", 1);
			ItemStack stack = ItemAdapter.ITEM_READER.fromJson(jsonObject.get("stack"), ItemStack.class);
			return new StackLootEntry(stack, min, max, weight, quality);
		}

		@Override
		public void serializeCustom(JsonObject object, StackLootEntry e, JsonSerializationContext conditions) {
			super.serializeCustom(object, e, conditions);
			object.addProperty("min", e.min);
			object.addProperty("max", e.max);
			object.add("stack", ItemAdapter.ITEM_READER.toJsonTree(e.stack));
		}

	}
}
