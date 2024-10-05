package bigsir.animationlib;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimationLib implements ModInitializer {
    public static final String MOD_ID = "animationlib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("AnimationLib initialized.");
    }
}
