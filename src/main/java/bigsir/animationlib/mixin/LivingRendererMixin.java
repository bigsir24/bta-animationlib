package bigsir.animationlib.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingRenderer;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import bigsir.animationlib.interfaces.IAnimations;

@Mixin(value = LivingRenderer.class, remap = false)
public abstract class LivingRendererMixin<T extends EntityLiving> extends EntityRenderer<T> {

	@Shadow
	protected ModelBase mainModel;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingRenderer;getSwingProgress(Lnet/minecraft/core/entity/EntityLiving;F)F"))
	public void sp(T entity, double x, double y, double z, float yaw, float partialTick, CallbackInfo ci){
		if(entity instanceof EntityPlayer){
			((IAnimations)mainModel).bta_animationlib$setHoldingAnimation(((IAnimations)entity).bta_animationlib$getHoldingAnimation());
			((IAnimations)mainModel).bta_animationlib$setAnimatedEntity(entity);
		}
	}
}
