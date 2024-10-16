package bigsir.animationlib.animations;

import bigsir.animationlib.interfaces.IAnimations;
import bigsir.animationlib.utils.AnimationValueStorage;
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
	private static final Map<Class<? extends ItemAnimation>, String> fieldLinkMap = new HashMap<>();
	protected AnimationValueStorage tempValueStorage;
	protected EntityLiving attached;

	/**
	 * Used to get the entry point the animation will target. For entry point descriptions see {@link EntryPoint}
	 * @return targeted entry point
	 */
	public AnimationEntryPoint getEntryPoint(){
		return AnimationEntryPoint.BEFORE_SNEAK;
	}

	/**
	 * Method that exposes ModelBiped and all needed parameters needed to change rotations.
	 */
	public void applyTransformation(EntityLiving entity, ModelBiped modelBiped, float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale){
	}

	public boolean isAnimated(){
		return false;
	}

	public void initValues(EntityLiving entity){
	}

	public void updateValues(EntityLiving entity){
	}

	public final void loadTempValues(AnimationValueStorage avs){
		tempValueStorage = avs;
	}

	public final void clearTempValues(){
		tempValueStorage = null;
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

	public final void attachEntity(EntityLiving entity){
		this.attached = entity;
	}

	public final void clearEntity(){
		this.attached = null;
	}

	protected final void defineFloat(String key, float startValue) {
		if(!isAnimated()) return;
		tempValueStorage.setFloat(key, startValue, AnimationValueStorage.Type.ALL);
	}

	protected final void setFloat(String key, float f){
		if(!isAnimated()) return;
		tempValueStorage.setFloat(key, f, AnimationValueStorage.Type.CURRENT);
	}

	protected final float getFloat(String key){
		if(!isAnimated()) return 0.0F;
		return tempValueStorage.getFloat(key, AnimationValueStorage.Type.CURRENT);
	}

	protected final float getPartialFloat(String key){
		if(!isAnimated()) return 0.0F;
		return ((IAnimations)attached).bta_animationlib$getAVS().getFloat(key, AnimationValueStorage.Type.PARTIAL);
	}

	public static void linkField(String fieldName, Class<? extends ItemAnimation> animationClass){
		fieldLinkMap.put(animationClass, fieldName);
	}

	protected final Object getFieldValue(String string) {
		Object o = null;
		try {
			o = this.attached.getClass().getField(string).get(this.attached);
		} catch (NoSuchFieldException | IllegalAccessException ignored){}
		return o;
	}
}
