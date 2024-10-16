package bigsir.animationlib.animations;

/**
 * Entry points for the animation.
 * <li>{@link #BEFORE_SWING} - applies before swing transformation</li>
 * <li>{@link #BEFORE_SNEAK} - applies between swing and sneak transformations</li>
 * <li>{@link #AFTER_SNEAK} - applies after sneak transformation</li>
 */
public enum AnimationEntryPoint {
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
