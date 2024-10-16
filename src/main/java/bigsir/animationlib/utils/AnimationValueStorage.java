package bigsir.animationlib.utils;

import bigsir.animationlib.animations.ItemAnimation;
import net.minecraft.core.entity.EntityLiving;

import javax.swing.text.html.parser.Entity;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnimationValueStorage {
	protected Map<String, Float[]> values = new HashMap<>();

	public AnimationValueStorage(){

	}

	public void clearIfNotEmpty(){
		if(!this.values.isEmpty()) this.values.clear();
	}

	public float getFloat(String key, Type type){
		Float[] floats = values.get(key);
		return floats != null && floats.length == 3 ? floats[type.id] : 0;
	}

	public void setFloat(String key, float value, Type type){
		Float[] floats = values.getOrDefault(key, new Float[]{0F,0F,0F});
		if(floats == null || floats.length != 3) return;

		if(type == Type.ALL){
            Arrays.fill(floats, value);
		}else{
			floats[type.id] = value;
			values.put(key, floats);
		}
	}

	public void calculatePartial(float partialTick){
		for (Map.Entry<String, Float[]> entry : values.entrySet()) {
			Float[] floats = entry.getValue();
			float prev = floats[0];
			float curr = floats[1];
			floats[2] = prev + (curr-prev) * partialTick;
			values.put(entry.getKey(), floats);
		}
	}

	public void next(EntityLiving entity, ItemAnimation animation){
		animation.loadTempValues(this);
		for (Map.Entry<String, Float[]> entry : values.entrySet()) {
			Float[] floats = entry.getValue();
			floats[0] = floats[1];
			values.put(entry.getKey(), floats);
		}
		animation.updateValues(entity);
		animation.clearTempValues();
	}

	public void storageSetup(EntityLiving entity, ItemAnimation animation){
		this.values.clear();
		animation.loadTempValues(this);
		animation.initValues(entity);
		this.next(entity, animation);
		animation.clearTempValues();
	}

	public enum Type {
		PREVIOUS(0),
		CURRENT(1),
		PARTIAL(2),
		ALL(3);
		public final int id;

		Type(int id){
			this.id = id;
		}
	}
}
