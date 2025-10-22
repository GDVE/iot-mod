package ru.iot.gui;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.iot.network.MessagePacketPayload;
import ru.iot.proto.MessageOuterClass;

public class MessageScreen extends Screen {

    private EditBox input;

    public MessageScreen() {
        super(Component.translatable("message_screen.title"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        int centerX = width / 2;
        int centerY = height / 2;

        addRenderableWidget(input = buildInput(centerX, centerY));
        addRenderableWidget(buildButton(centerX, centerY));
    }

    private EditBox buildInput(int centerX, int centerY) {
        EditBox box = new EditBox(this.font,
                centerX - 100,
                centerY - 15,
                200,
                20,
                Component.translatable("message_screen.editbox"));

        box.setMaxLength(256);
        return box;
    }

    private Button buildButton(int centerX, int centerY) {
        return Button
                .builder(Component.translatable("message_screen.button"), this::onButtonPress)
                .pos(centerX - Button.DEFAULT_WIDTH / 2, centerY + Button.DEFAULT_HEIGHT / 2)
                .build();
    }

    private void onButtonPress(Button button) {
        String text = input.getValue();
        if (text.isEmpty()) return;

        ClientPlayNetworking.send(new MessagePacketPayload(MessageOuterClass.Message.newBuilder()
                .setText(text)
                .build()));
    }
}
