package com.star_bat.control.entity;

import java.util.Random;

import com.star_bat.control.entity.pawn.Bullet;
import com.star_bat.control.entity.pawn.series.Arena;
import com.star_bat.control.entity.pawn.series.Body;
import com.star_bat.control.entity.pawn.series.Sensor;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class Pilot {
	public static class Spec {
		public static class Gun {
			public Matrix4 transform = new Matrix4();
			public Vector3 velocity = new Vector3();
			int index;
			float cd;

			public Gun(int index, float cd) {
				this.index = index;
				this.cd = cd;
			}
		}

		public Arena<Bullet>[] bulletMat;
		public Arena<Bullet>[] playerBulletMat;
		public Array<Gun> guns = new Array();
		public Model model;
		BoundingBox bBox = new BoundingBox();
		float radius2;
		public float hp;

		public Spec(Model model) {
			this.model = model;
			model.calculateBoundingBox(bBox);
			radius2 = bBox.max.len2();
		}

		public Spec setBulletMat(Arena<Bullet>[] bulletMat) {
			this.bulletMat = bulletMat;
			return this;
		}

		public Spec setPlayerBulletMat(Arena<Bullet>[] playerBulletMat) {
			this.playerBulletMat = playerBulletMat;
			return this;
		}

		public Spec setHp(float hp) {
			this.hp = hp;
			return this;
		}
	}

	public static class Ship {

		public boolean arrived = false;
		public float hp;
		public Body body;
		public Sensor sensor;
		public Vector3 v0 = new Vector3();
		public float[] cds;

		public Ship(Spec spec) {
			body = new Body(spec.model);
			sensor = new Sensor(spec.bBox, body.pos);
			cds = new float[spec.guns.size];
			for (int i = 0; i < cds.length; ++i) {
				cds[i] = spec.guns.get(i).cd;
			}
		}

	}

	static Random random = new Random();

	public Vector3 origin;
	public Ship ship;
	public Spec spec;

	public Pilot(Spec spec, Vector3 origin) {
		this.spec = spec;
		this.origin = origin;
	}

	public void use(Ship ship) {
		this.ship = ship;
	}

	public void reset() {
		ship.hp = spec.hp;
		ship.arrived = false;

		ship.body.pos.x = (random.nextInt(2) * 2 - 1) * 200f;
		Double theta = random.nextDouble() * Math.PI * 0.5;
		ship.body.pos.y = (float) Math.cos(theta) * 100f;
		ship.body.pos.z = (float) Math.sin(theta) * -100f;
		ship.body.update();
	}

	public void update(float dt) {
		if (ship != null) {
			// fly in
			if (!ship.arrived) {
				ship.v0.set(origin).sub(ship.body.pos);
				float len = ship.v0.len();
				if (len < dt * 100) {
					ship.body.pos.set(origin);
					ship.arrived = true;
				} else {
					ship.body.pos.add(ship.v0.scl(dt * 100 / len));
				}
			} else {

				// hit detection
				for (Arena<Bullet> arena : spec.playerBulletMat) {
					for (Bullet bullet : arena.arr) {
						// if( bullet.contains(ship.sensor)) {
						// ship.hp -= bullet.dmg;
						// if (ship.hp < 0) {
						// System.out.println("dead");
						// }
						// }
					}
				}

				for (int i = 0; i < spec.guns.size; ++i) {
					ship.cds[i] -= dt;
					if (ship.cds[i] < 0) {
						Spec.Gun gun = spec.guns.get(i);
						ship.cds[i] = gun.cd;
						ship.v0.set(ship.body.pos).mul(gun.transform);
						spec.bulletMat[gun.index].get().spawn(ship.v0, gun.velocity);
					}
				}

				ship.body.update();
			}

		}
	}
}
