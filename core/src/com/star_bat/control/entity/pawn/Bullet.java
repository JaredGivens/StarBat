
package com.star_bat.control.entity.pawn;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.star_bat.control.entity.pawn.series.Arena;

public class Bullet {
	public static Arena<Bullet> buildArena(Model model, int amt) {
		Bullet[] bullets = new Bullet[amt];
		for (int i = 0; i < amt; ++i) {
			bullets[i] = new Bullet(model);
			bullets[i].hide();
		}
		return new Arena(bullets);
	}


	public ModelInstance instance;
	public boolean active;
	Vector3 velocity = new Vector3();

	public Bullet(Model model) {
		instance = new ModelInstance(model);
	}

	public void update() {
		instance.transform.translate(velocity);
		if (Math.abs(instance.transform.val[Matrix4.M03]) > 100
				|| Math.abs(instance.transform.val[Matrix4.M13]) > 100
				|| Math.abs(instance.transform.val[Matrix4.M23]) > 100) {
			hide();
		}
	}

	public void hide() {
		instance.transform.setToTranslation(10_000, 10_000, 10_000);
		active = false;
	}

	public void spawn(Vector3 origin, Vector3 velocity) {
		this.velocity.set(velocity);
		instance.transform.setToTranslation(origin);
		active = true;
	}

}
