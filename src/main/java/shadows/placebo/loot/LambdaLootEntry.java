package shadows.placebo.loot;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import shadows.placebo.Placebo;

@Deprecated(since = "3.0.0", forRemoval = true)
public class LambdaLootEntry extends LootPoolSingletonContainer {
	public static final Serializer SERIALIZER = new Serializer();
	public static final LootPoolEntryType LAMBDALOOTENTRYTYPE = Registry.register(Registry.LOOT_POOL_ENTRY_TYPE, new ResourceLocation(Placebo.MODID, "lambda_entry"), new LootPoolEntryType(SERIALIZER));

	private final BiConsumer<Consumer<ItemStack>, LootContext> loot;

	public LambdaLootEntry(BiConsumer<Consumer<ItemStack>, LootContext> loot, int weight, int quality) {
		super(weight, quality, new LootItemCondition[0], new LootItemFunction[0]);
		this.loot = loot;
	}

	@Override
	protected void createItemStack(Consumer<ItemStack> list, LootContext ctx) {
		this.loot.accept(list, ctx);
	}

	@Override
	public LootPoolEntryType getType() {
		return LAMBDALOOTENTRYTYPE;
	}

	public static class Serializer extends LootPoolSingletonContainer.Serializer<LambdaLootEntry> {

		@Override
		protected LambdaLootEntry deserialize(JsonObject jsonObject, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] lootConditions, LootItemFunction[] lootFunctions) {
			throw new UnsupportedOperationException();
		}

	}
}
