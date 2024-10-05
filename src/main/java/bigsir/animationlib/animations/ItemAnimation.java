package bigsir.animationlib.animations;

import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.Item;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Base animation class.
 * <p>
 * Extend this class to create your animation.
 * Animations need to be registered with {@link ItemAnimation#register(Item, Class)}
 * </p>
 * Registered animations will apply whenever the player is holding an item.
 */
public class ItemAnimation {
	public static final ItemAnimation noAnimation = new ItemAnimation();
	private static final Map<Item, ItemAnimation> itemAnimationMap = new HashMap<>();
	private static final Map<Class<? extends ItemAnimation>, ItemAnimation> animationMap = new HashMap<>();

	/**
	 * Used to get the entry point the animation will target. For entry point descriptions see {@link EntryPoint}
	 * @return targeted entry point
	 */
	public EntryPoint getEntryPoint(){
		return EntryPoint.BEFORE_SNEAK;
	}

	/**
	 * Method that exposes ModelBiped and all needed parameters needed to change rotations.
	 */
	public void applyTransformation(EntityLiving entity, ModelBiped modelBiped, float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale){
	}

	/**
	 * Method to evaluate if the animation should be applied.
	 * @param entity the entity to animate
	 */
	public boolean getApplyCondition(EntityLiving entity){
		return true;
	}

	public static ItemAnimation getAnimation(Item item){
		if(!itemAnimationMap.containsKey(item)) return noAnimation;
		return itemAnimationMap.get(item);
	}

	/**
	 * Use this method to register your animations.
	 * @param targetItem the item the animation will be applied to
	 * @param clazz the animation class to be assigned to the item
	 */
	@SuppressWarnings("unused")
	public static void register(@NotNull Item targetItem, @NotNull Class<? extends ItemAnimation> clazz){
		try{
			if(animationMap.containsKey(clazz)){
				itemAnimationMap.put(targetItem, animationMap.get(clazz));
			}else{
				ItemAnimation animation = clazz.getConstructor().newInstance();
				animationMap.put(clazz, animation);
				itemAnimationMap.put(targetItem, animation);
			}
		}catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
			throw new RuntimeException("Failed to load an animation");
		}
	}

	/**
	 * Entry points for the animation.
	 * <li>{@link #BEFORE_SWING} - applies before swing transformation</li>
	 * <li>{@link #BEFORE_SNEAK} - applies between swing and sneak transformations</li>
	 * <li>{@link #AFTER_SNEAK} - applies after sneak transformation</li>
	 */
	public enum EntryPoint {
		/**
		 * Applies before swing transformation.
		 */
		BEFORE_SWING,
		/**
		 * Applies between swing and sneak transformations.
		 */
		BEFORE_SNEAK,
		/**
		 * Applies after sneak transformation.
		 */
		AFTER_SNEAK
	}
}
