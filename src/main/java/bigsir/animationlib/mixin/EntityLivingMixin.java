package bigsir.animationlib.mixin;

import bigsir.animationlib.utils.AnimationValueStorage;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import bigsir.animationlib.animations.ItemAnimation;
import bigsir.animationlib.interfaces.IAnimations;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = EntityLiving.class, remap = false)
public abstract class EntityLivingMixin extends Entity implements IAnimations {
	public EntityLivingMixin(World world) {
		super(world);
	}

	@Shadow
	public abstract ItemStack getHeldItem();
	@Unique
	public AnimationValueStorage avs = new AnimationValueStorage();
	@Unique
	public ItemAnimation animation;
	@Unique
	public ItemAnimation prevAnimation;

	@Override
	public AnimationValueStorage bta_animationlib$getAVS() {
		return this.avs;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void animationTick(CallbackInfo ci) {
		//Updating values here
		//We should not modify the animation here
		//Init both prev and current values
		this.animation = this.bta_animationlib$getHoldingAnimation();
		if (this.animation.isAnimated()) {
			EntityLiving entity = (EntityLiving) (Object) this;
			if (this.prevAnimation != this.animation) {
				this.avs.storageSetup(entity, this.animation);
			} else {
				this.avs.next(entity, this.animation);
			}
		}
		if(this.prevAnimation != this.animation) this.avs.clearIfNotEmpty();
		this.prevAnimation = this.animation;
	}

	@Override
	public ItemAnimation bta_animationlib$getHoldingAnimation() {
		ItemStack heldStack = this.getHeldItem();
		return heldStack != null ? ItemAnimation.getAnimation(heldStack.getItem()) : ItemAnimation.noAnimation;
	}
}
