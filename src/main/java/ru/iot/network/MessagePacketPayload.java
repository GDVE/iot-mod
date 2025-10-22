package ru.iot.network;

import com.google.protobuf.InvalidProtocolBufferException;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import ru.iot.IOTMod;
import ru.iot.message.MessageEntity;
import ru.iot.proto.MessageOuterClass;

public record MessagePacketPayload(MessageOuterClass.Message message) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.tryBuild(IOTMod.MOD_ID, "message_packet");

    public static final Type<MessagePacketPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, MessagePacketPayload> CODEC =
            StreamCodec.of(MessagePacketPayload::serialize, MessagePacketPayload::deserialize);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static void serialize(FriendlyByteBuf buf, MessagePacketPayload packet) {
        byte[] bytes = packet.message.toByteArray();
        buf.writeVarInt(bytes.length);
        buf.writeBytes(bytes);
    }

    private static MessagePacketPayload deserialize(FriendlyByteBuf buf) {
        byte[] bytes = new byte[buf.readVarInt()];
        buf.readBytes(bytes);
        try {
            return new MessagePacketPayload(MessageOuterClass.Message.parseFrom(bytes));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("Failed to parse Protobuf message!", e);
        }
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(MessagePacketPayload.TYPE, MessagePacketPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> {
            var entity = new MessageEntity();
            entity.setText(payload.message.toString());
            entity.setUuid(context.player().getUUID());

            IOTMod.getInstance().getMessageRepo().save(entity);
        });
    }
}