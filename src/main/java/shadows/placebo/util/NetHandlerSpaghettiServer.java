package shadows.placebo.util;

import java.util.Set;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQuery;
import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.network.protocol.game.ServerboundEntityTagQuery;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.common.util.FakePlayer;

public class NetHandlerSpaghettiServer extends ServerGamePacketListenerImpl {

	public NetHandlerSpaghettiServer(FakePlayer player) {
		super(null, new Connection(PacketFlow.CLIENTBOUND), player);
	}

	@Override
	public void disconnect(Component textComponent) {
	}

	@Override
	public void onDisconnect(Component reason) {
	}

	@Override
	public void teleport(double x, double y, double z, float yaw, float pitch) {
	}

	@Override
	public void handleLockDifficulty(ServerboundLockDifficultyPacket p_217261_1_) {
	}

	@Override
	public void handleChangeDifficulty(ServerboundChangeDifficultyPacket p_217263_1_) {
	}

	@Override
	public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket p_217262_1_) {
	}

	@Override
	public void handleAnimate(ServerboundSwingPacket packetIn) {
	}

	@Override
	public void handleClientCommand(ServerboundClientCommandPacket packetIn) {
	}

	@Override
	public void handleMovePlayer(ServerboundMovePlayerPacket packetIn) {
	}

	@Override
	public void handleContainerButtonClick(ServerboundContainerButtonClickPacket packetIn) {
	}

	@Override
	public void handleContainerClose(ServerboundContainerClosePacket packetIn) {
	}

	@Override
	public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket packetIn) {
	}

	@Override
	public void handleMoveVehicle(ServerboundMoveVehiclePacket packetIn) {
	}

	@Override
	public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket packetIn) {
	}

	@Override
	public void handleResourcePackResponse(ServerboundResourcePackPacket packetIn) {
	}

	@Override
	public void handleChat(ServerboundChatPacket packetIn) {
	}

	@Override
	public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket packetIn) {
	}

	@Override
	public void handleClientInformation(ServerboundClientInformationPacket packetIn) {
	}

	@Override
	public void handleContainerClick(ServerboundContainerClickPacket packetIn) {
	}

	@Override
	public void handleCustomPayload(ServerboundCustomPayloadPacket packetIn) {
	}

	@Override
	public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket packetIn) {
	}

	@Override
	public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket packetIn) {
	}

	@Override
	public void handlePlayerCommand(ServerboundPlayerCommandPacket packetIn) {
	}

	@Override
	public void handlePlayerInput(ServerboundPlayerInputPacket packetIn) {
	}

	@Override
	public void handleEditBook(ServerboundEditBookPacket packetIn) {
	}

	@Override
	public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQuery packetIn) {
	}

	@Override
	public void handleSetCarriedItem(ServerboundSetCarriedItemPacket packetIn) {
	}

	@Override
	public void handlePlaceRecipe(ServerboundPlaceRecipePacket packetIn) {
	}

	@Override
	public void handleKeepAlive(ServerboundKeepAlivePacket packetIn) {
	}

	@Override
	public void handlePlayerAction(ServerboundPlayerActionPacket packetIn) {
	}

	@Override
	public void handleEntityTagQuery(ServerboundEntityTagQuery packetIn) {
	}

	@Override
	public void handleSelectTrade(ServerboundSelectTradePacket packetIn) {
	}

	@Override
	public void handlePickItem(ServerboundPickItemPacket packetIn) {
	}

	@Override
	public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket packetIn) {
	}

	@Override
	public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket packetIn) {
	}

	@Override
	public void handleUseItemOn(ServerboundUseItemOnPacket packetIn) {
	}

	@Override
	public void handlePaddleBoat(ServerboundPaddleBoatPacket packetIn) {
	}

	@Override
	public void handleSetCommandBlock(ServerboundSetCommandBlockPacket packetIn) {
	}

	@Override
	public void handleRenameItem(ServerboundRenameItemPacket packetIn) {
	}

	@Override
	public void handleSignUpdate(ServerboundSignUpdatePacket packetIn) {
	}

	@Override
	public void handleUseItem(ServerboundUseItemPacket packetIn) {
	}

	@Override
	public void handleInteract(ServerboundInteractPacket packetIn) {
	}

	@Override
	public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket packetIn) {
	}

	@Override
	public void handleSetStructureBlock(ServerboundSetStructureBlockPacket packetIn) {
	}

	@Override
	public void handleSetBeaconPacket(ServerboundSetBeaconPacket packetIn) {
	}

	@Override
	public void send(Packet<?> packetIn, GenericFutureListener<? extends Future<? super Void>> futureListeners) {
	}

	@Override
	public void teleport(double x, double y, double z, float yaw, float pitch, Set<RelativeArgument> relativeSet) {
	}

	@Override
	public void send(Packet<?> packetIn) {
	}

	@Override
	public void tick() {
	}

}
