package shadows.placebo.util;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class NetworkUtils {

	/**
	 * Helper message to send a packet to all players watching a chunk.
	 */
	public static void sendToTracking(SimpleChannel channel, Object packet, ServerLevel world, BlockPos pos) {
		world.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(p -> {
			channel.sendTo(packet, p.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		});
	}

	/**
	 * Helper message to send a packet to all players watching a chunk.
	 */
	public static void sendTo(SimpleChannel channel, Object packet, Player player) {
		channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
	}

	/**
	 * Registers a message directly using the base FML systems.  Requires providing each method individually.
	 * @param channel Channel to register for.
	 * @param id Message id.
	 * @param messageType Class of the message object.
	 * @param encoder Method to write the method to buf.
	 * @param decoder Method to read the message from buf.
	 * @param messageConsumer Executor for the read message.
	 */
	public static <MSG> void registerMessage(SimpleChannel channel, int id, Class<MSG> messageType, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
		channel.registerMessage(id, messageType, encoder, decoder, messageConsumer);
	}

	/**
	 * Registers a message using {@link MessageProvider}.
	 * @param channel Channel to register for.
	 * @param id Message id.
	 * @param prov An instance of the message provider.  Note that this object will be kept around, so try to keep all fields uninitialized if possible.
	 */
	public static <T> void registerMessage(SimpleChannel channel, int id, MessageProvider<T> prov) {
		channel.registerMessage(id, prov.getMsgClass(), prov::write, prov::read, prov::handle);
	}

	/**
	 * MessageProvider is basically a marker for easier registration of messages.  It forces the implementer to override all methods a packet needs.
	 *
	 * @param <T> The message type.  Usually the implementing class.
	 * If {@link T} and {@link this#getClass()} are different, {@link MessageProvider#getMsgClass()} must be overridden.
	 */
	public static abstract class MessageProvider<T> {

		@SuppressWarnings("unchecked")
		public Class<T> getMsgClass() {
			return (Class<T>) this.getClass();
		}

		public abstract void write(T msg, FriendlyByteBuf buf);

		public abstract T read(FriendlyByteBuf buf);

		public abstract void handle(T msg, Supplier<NetworkEvent.Context> ctx);
	}

	/**
	 * Handles a packet.  Automatically calls {@link NetworkEvent.Context#setPacketHandled(boolean)}
	 * @param r Code to run to handle the packet.  Uses a supplier to try and combat some classloading stuff.
	 * @param ctx Context object.  Available from {@link MessageProvider#handle(Object, Supplier)}
	 */
	public static void handlePacket(Supplier<Runnable> r, NetworkEvent.Context ctx) {
		ctx.enqueueWork(r.get());
		ctx.setPacketHandled(true);
	}

}
