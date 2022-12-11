package com.star_bat.control.entity;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.star_bat.control.entity.pawn.Ship;

public class Pilot {
	public Vector3 origin;
	public ModelInstance instance;
	public Ship ship;

	boolean arrived = false;
	Vector3 position = new Vector3();
	Quaternion rotation = new Quaternion();
	Vector3 v0 = new Vector3();

	public Pilot(Ship ship, Vector3 origin) {
		this.ship = ship;
		this.origin = origin;
	}

	public void use(ModelInstance instance) {
		this.instance = instance;
		instance.transform.getRotation(rotation);
		instance.transform.getTranslation(position);
	}

	public void update(float dt) {
		if (instance != null) {
			if (!arrived) {
				v0.set(origin).sub(position);
				float len = v0.len();
				if (len < dt * 100) {
					position.set(origin);
					arrived = true;
				} else {
					position.add(v0.scl(dt * 100 / len));
				}
			} 
			
			instance.transform.set(position, rotation);
		}
	}
}
