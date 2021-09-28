package shadows.placebo.util.json;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemAdapter implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {

	public static final ItemAdapter INSTANCE = new ItemAdapter();

	public static final Gson ITEM_READER = new GsonBuilder().registerTypeAdapter(ItemStack.class, INSTANCE).registerTypeAdapter(CompoundTag.class, NBTAdapter.INSTANCE).create();

	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		ResourceLocation id = new ResourceLocation(obj.get("item").getAsString());
		Item item = ForgeRegistries.ITEMS.getValue(id);
		if (item == Items.AIR && !id.equals(Items.AIR.getRegistryName())) throw new JsonParseException("Failed to read item " + id);
		int count = obj.has("count") ? obj.get("count").getAsInt() : 1;
		CompoundTag tag = obj.has("nbt") ? ctx.deserialize(obj.get("nbt"), CompoundTag.class) : null;
		CompoundTag capTag = obj.has("cap_nbt") ? ctx.deserialize(obj.get("cap_nbt"), CompoundTag.class) : null;
		ItemStack stack = new ItemStack(item, count, capTag);
		stack.setTag(tag);
		return stack;
	}

	@Override
	public JsonElement serialize(ItemStack stack, Type typeOfSrc, JsonSerializationContext ctx) {
		CompoundTag written = stack.save(new CompoundTag());
		JsonObject obj = new JsonObject();
		obj.add("item", ctx.serialize(stack.getItem().getRegistryName().toString()));
		obj.add("count", ctx.serialize(stack.getCount()));
		if (stack.hasTag()) obj.add("nbt", ctx.serialize(stack.getTag()));
		if (written.contains("ForgeCaps")) obj.add("cap_nbt", ctx.serialize(written.getCompound("ForgeCaps")));
		return obj;
	}

}