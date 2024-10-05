package bigsir.animationlib.mixin;

import net.minecraft.client.render.model.ModelBase;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.core.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import bigsir.animationlib.animations.ItemAnimation;
import bigsir.animationlib.interfaces.IAnimations;

@Mixin(value = ModelBiped.class, remap = false)
public abstract class ModelBipedMixin extends ModelBase implements IAnimations {
	@Unique
	private EntityLiving entity;
	@Unique
	private ItemAnimation itemAnimation = ItemAnimation.noAnimation;
	@Unique
	private ItemAnimation.EntryPoint entryPoint;

	@Override
	public void bta_animationlib$setAnimatedEntity(EntityLiving entity) {
		this.entity = entity;
	}

	@Override
	public void bta_animationlib$setHoldingAnimation(ItemAnimation animation) {
		this.itemAnimation = animation;
	}

	@Inject(method = "setRotationAngles", at = @At(value = "HEAD"))
	public void getEntryPoint(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci){
		this.entryPoint = this.itemAnimation.getEntryPoint();
	}

	@Inject(method = "setRotationAngles", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/util/helper/MathHelper;sin(F)F", shift = At.Shift.AFTER, ordinal = 4))
	public void beforeSwing(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci){
		if(this.entryPoint == ItemAnimation.EntryPoint.BEFORE_SWING && this.itemAnimation.getApplyCondition(this.entity)){
			this.itemAnimation.applyTransformation(this.entity, (ModelBiped)(Object)this, limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		}
	}

	@Inject(method = "setRotationAngles", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/util/helper/MathHelper;sin(F)F", shift = At.Shift.BY, by = 2, ordinal = 5))
	public void beforeSneak(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci){
		if(this.entryPoint == ItemAnimation.EntryPoint.BEFORE_SNEAK && this.itemAnimation.getApplyCondition(this.entity)){
			this.itemAnimation.applyTransformation(this.entity, (ModelBiped)(Object)this, limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		}
	}

	@Inject(method = "setRotationAngles", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/util/helper/MathHelper;cos(F)F", shift = At.Shift.BY, by = -2, ordinal = 6))
	public void afterSneak(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci){
		if(this.entryPoint == ItemAnimation.EntryPoint.AFTER_SNEAK && this.itemAnimation.getApplyCondition(this.entity)){
			this.itemAnimation.applyTransformation(this.entity, (ModelBiped)(Object)this, limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		}
	}

}
