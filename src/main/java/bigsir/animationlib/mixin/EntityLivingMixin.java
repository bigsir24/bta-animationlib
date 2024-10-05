package bigsir.animationlib.mixin;

import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import bigsir.animationlib.animations.ItemAnimation;
import bigsir.animationlib.interfaces.IAnimations;

@Mixin(value = EntityLiving.class, remap = false)
public abstract class EntityLivingMixin implements IAnimations {
	@Shadow
	public abstract ItemStack getHeldItem();

	@Override
	public ItemAnimation bta_animationlib$getHoldingAnimation() {
		ItemStack heldStack = this.getHeldItem();
		return heldStack != null ? ItemAnimation.getAnimation(heldStack.getItem()) : ItemAnimation.noAnimation;
	}
}
