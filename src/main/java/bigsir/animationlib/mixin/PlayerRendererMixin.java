package bigsir.animationlib.mixin;

import bigsir.animationlib.interfaces.IAnimations;
import net.minecraft.client.render.entity.LivingRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.core.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerRenderer.class, remap = false)
public abstract class PlayerRendererMixin<T extends EntityPlayer> extends LivingRenderer<T> {

	@Shadow
	private ModelBiped modelBipedMain;

	@Shadow
	@Final
	private ModelBiped modelArmorChestplate;

	@Shadow
	@Final
	private ModelBiped modelArmor;

	public PlayerRendererMixin(ModelBase model, float shadowSize) {
		super(model, shadowSize);
	}

	@Inject(method = "drawFirstPersonHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelBiped;setRotationAngles(FFFFFF)V", shift = At.Shift.BEFORE))
	public void noRotation(EntityPlayer player, boolean isLeft, CallbackInfo ci){
		((IAnimations)this.modelBipedMain).bta_animationlib$noRotation();
	}

	@Inject(method = "render(Lnet/minecraft/core/entity/player/EntityPlayer;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/EntityPlayer;isPassenger()Z", shift = At.Shift.AFTER))
	public void copyMainModel(EntityPlayer entity, double x, double y, double z, float yaw, float partialTick, CallbackInfo ci){
		((IAnimations)modelBipedMain).bta_animationlib$setHoldingAnimation(((IAnimations)entity).bta_animationlib$getHoldingAnimation());
		((IAnimations)modelBipedMain).bta_animationlib$setAnimatedEntity(entity);
		((IAnimations)this.modelArmorChestplate).bta_animationlib$copyAttributes(this.modelBipedMain);
		((IAnimations)this.modelArmor).bta_animationlib$copyAttributes(this.modelBipedMain);
	}

	@Inject(method = "render(Lnet/minecraft/core/entity/player/EntityPlayer;DDDFF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V", shift = At.Shift.BEFORE))
	public void clearCopied(EntityPlayer entity, double x, double y, double z, float yaw, float partialTick, CallbackInfo ci){
		((IAnimations)this.modelArmorChestplate).bta_animationlib$clearAttributes();
		((IAnimations)this.modelArmor).bta_animationlib$clearAttributes();
	}
}
