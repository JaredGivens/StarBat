
package com.star_bat.control.entity.pawn;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.star_bat.control.entity.pawn.series.Arena;
import com.star_bat.control.entity.pawn.series.Body;
import com.star_bat.control.entity.pawn.series.Sensor;

public class Bullet {
	public static Arena<Bullet> buildArena(Model model, int amt) {
		Bullet[] bullets = new Bullet[amt];

		for (int i = 0; i < amt; ++i) {
			bullets[i] = new Bullet(model, 5);
			bullets[i].hide();
		}
		return new Arena(bullets);
	}

	public float dmg;
	public Body body;
	Sensor sensor;
	public boolean active;
	Vector3 vel = new Vector3();

	public Bullet(Model model, float dmg) {
		this.dmg = dmg;
		body = new Body(model);
		BoundingBox bBox = new BoundingBox();
		body.inst.calculateBoundingBox(bBox);
		sensor = new Sensor(bBox, body.pos);
	}

	public void update() {
		if(!active) {
			return;
		}

		body.pos.add(vel);

		if (body.pos.len2() > 10_000) {
			hide();
			return;
		}

		body.update();
	}

	public void hide() {
		body.pos.set(10_000, 10_000, 10_000);
		body.update();
		active = false;
	}

	public void spawn(Vector3 origin, Vector3 vel) {
		this.vel.set(vel);
		body.pos.set(origin);
		body.update();
		active = true;
	}

	public boolean intersects(Sensor sensor) {
		boolean res = active;
		res &= this.sensor.pos.dst(sensor.pos) < (this.sensor.radius + sensor.radius);
		res &= this.sensor.intersects(sensor);
		return res;

	}

}
