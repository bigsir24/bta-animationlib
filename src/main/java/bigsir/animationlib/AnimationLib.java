package bigsir.animationlib;

import bigsir.animationlib.animations.ItemAnimation;
import bigsir.animationlib.animations.ItemAnimationBow;
import bigsir.animationlib.animations.ItemAnimationFood;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimationLib implements ModInitializer {
    public static final String MOD_ID = "animationlib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        LOGGER.info("AnimationLib initialized.");
		/*
		ItemAnimation.register(Item.toolBow, ItemAnimationBow.class);
		ItemAnimation.register(Item.foodPorkchopRaw, ItemAnimationFood.class);
		ItemAnimation.register(Item.foodPorkchopCooked, ItemAnimationFood.class);
		 */
    }
}
