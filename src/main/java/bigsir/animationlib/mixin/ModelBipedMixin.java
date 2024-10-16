package bigsir.animationlib.mixin;

import bigsir.animationlib.animations.AnimationEntryPoint;
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
	protected EntityLiving entity;
	@Unique
	protected ItemAnimation itemAnimation = ItemAnimation.noAnimation;
	@Unique
	protected boolean isChild = false;
	@Unique
	boolean shouldRotate = true;
	@Unique
	protected AnimationEntryPoint entryPoint;

	@Override
	public void bta_animationlib$setAnimatedEntity(EntityLiving entity) {
		this.entity = entity;
	}

	@Override
	public void bta_animationlib$noRotation() {
		this.shouldRotate = false;
	}

	@Override
	public void bta_animationlib$copyAttributes(ModelBiped modelBiped) {
		this.entity = ((IAnimations)modelBiped).bta_animationlib$getEntity();
		this.itemAnimation = ((IAnimations)modelBiped).bta_animationlib$getItemAnimation();
		this.shouldRotate = ((IAnimations)modelBiped).bta_animationlib$getShouldRotate();
		this.isChild = true;
	}

	@Override
	public EntityLiving bta_animationlib$getEntity() {
		return this.entity;
	}

	@Override
	public ItemAnimation bta_animationlib$getItemAnimation() {
		return this.itemAnimation;
	}

	@Override
	public boolean bta_animationlib$getShouldRotate() {
		return this.shouldRotate;
	}

	@Override
	public void bta_animationlib$setHoldingAnimation(ItemAnimation animation) {
		//this.previousAnimation = this.itemAnimation;
		this.itemAnimation = animation;
	}

	@Override
	public void bta_animationlib$clearAttributes() {
		this.entity = null;
		this.itemAnimation = ItemAnimation.noAnimation;
		this.shouldRotate = false;
		this.isChild = false;
	}

	@Inject(method = "setRotationAngles", at = @At(value = "HEAD"))
	public void preAnimationSetup(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci){
		//this.itemAnimation = ((IAnimations)this.entity).bta_animationlib$getHoldingAnimation();
		this.entryPoint = this.itemAnimation.getEntryPoint();
		if(this.itemAnimation != ItemAnimation.noAnimation) {
			this.itemAnimation.attachEntity(this.entity);
		}
	}

	@Inject(method = "setRotationAngles", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/util/helper/MathHelper;sin(F)F", shift = At.Shift.AFTER, ordinal = 4))
	public void beforeSwing(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci){
		if(shouldApply(AnimationEntryPoint.BEFORE_SWING)){
			this.itemAnimation.applyTransformation(this.entity, (ModelBiped)(Object)this, limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		}
	}

	@Inject(method = "setRotationAngles", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/util/helper/MathHelper;sin(F)F", shift = At.Shift.BY, by = 2, ordinal = 5))
	public void beforeSneak(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci){
		if(shouldApply(AnimationEntryPoint.BEFORE_SNEAK)){
			this.itemAnimation.applyTransformation(this.entity, (ModelBiped)(Object)this, limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		}
	}

	@Inject(method = "setRotationAngles", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/util/helper/MathHelper;cos(F)F", shift = At.Shift.BY, by = -2, ordinal = 6))
	public void afterSneak(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci){
		if(shouldApply(AnimationEntryPoint.AFTER_SNEAK)){
			this.itemAnimation.applyTransformation(this.entity, (ModelBiped)(Object)this, limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
		}
	}

	@Inject(method = "setRotationAngles", at = @At("TAIL"))
	public void rotate(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale, CallbackInfo ci){
		//if(this.entity instanceof EntityOtherPlayerMP) System.out.println(((IAnimations)this.entity).bta_animationlib$getPartialValues());
		this.shouldRotate = true;
		this.itemAnimation.clearEntity();
	}

	@Unique
	private boolean shouldApply(AnimationEntryPoint entryPoint){
		return this.entryPoint == entryPoint && this.shouldRotate && this.itemAnimation.getApplyCondition(this.entity);
	}

}
