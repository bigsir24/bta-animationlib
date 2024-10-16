package bigsir.animationlib.interfaces;

import bigsir.animationlib.utils.AnimationValueStorage;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.core.entity.EntityLiving;
import bigsir.animationlib.animations.ItemAnimation;

import java.util.Map;

public interface IAnimations {

	default void bta_animationlib$copyAttributes(ModelBiped modelBiped){
	}

	default void bta_animationlib$clearAttributes(){
	}

	default AnimationValueStorage bta_animationlib$getAVS(){
		return null;
	}

	default EntityLiving bta_animationlib$getEntity(){
		return null;
	}

	default ItemAnimation bta_animationlib$getItemAnimation(){
		return ItemAnimation.noAnimation;
	}

	default boolean bta_animationlib$getShouldRotate(){
		return false;
	}

	default void bta_animationlib$noRotation(){
	}

	default void bta_animationlib$setHoldingAnimation(ItemAnimation animation){
	}

	default void bta_animationlib$setAnimatedEntity(EntityLiving entity){
	}

	default ItemAnimation bta_animationlib$getHoldingAnimation(){
		return ItemAnimation.noAnimation;
	}
}
