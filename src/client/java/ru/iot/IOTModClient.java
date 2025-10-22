package ru.iot;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screens.PauseScreen;
import ru.iot.gui.MessageScreen;

public class IOTModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.screen instanceof PauseScreen) {
                client.setScreen(new MessageScreen());
            }
        });
    }
}