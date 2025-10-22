package ru.iot;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.iot.message.MessageRepo;
import ru.iot.network.MessagePacketPayload;

@Getter
public class IOTMod implements ModInitializer {

    public static final String MOD_ID = "iotmod";

    private static @Getter IOTMod instance;

    private final Logger logger = LoggerFactory.getLogger(MOD_ID);
    private MessageRepo messageRepo;

    public IOTMod() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        MessagePacketPayload.register();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            if (messageRepo == null) {
                messageRepo = new MessageRepo();
            }
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (messageRepo != null) {
                messageRepo.close();
            }
        });
    }
}