package bigsir.animationlib.interfaces;

import net.minecraft.core.entity.EntityLiving;
import bigsir.animationlib.animations.ItemAnimation;

public interface IAnimations {

	default void bta_animationlib$setHoldingAnimation(ItemAnimation animation){
	}

	default void bta_animationlib$setAnimatedEntity(EntityLiving entity){
	}

	default ItemAnimation bta_animationlib$getHoldingAnimation(){
		return ItemAnimation.noAnimation;
	}
}
