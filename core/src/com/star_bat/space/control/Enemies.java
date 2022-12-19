package com.star_bat.space.control;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.star_bat.space.control.pawn.Bullet;
import com.star_bat.space.control.pawn.series.Arena;
import com.star_bat.space.control.pawn.series.Assets;
import com.star_bat.space.control.pawn.series.Body;
import com.star_bat.space.control.pawn.series.Sensor;

public class Enemies {

	static public Enemies makeBugs(Arena<Bullet>[] playerBulletMat) {

		final int BALL = 0;
		final int CONE = 1;

		Enemies enemies = new Enemies(2);

		enemies.bulletMat[BALL] = Bullet.buildArena(Assets.scenes.get(Assets.BALL).scene.model, 100);
		enemies.bulletMat[CONE] = Bullet.buildArena(Assets.scenes.get(Assets.CONE).scene.model, 10);

		Spec fly = new Spec();
		fly.setModel(Assets.scenes.get(Assets.FLY).scene.model);
		fly.bulletMat = enemies.bulletMat;
		fly.playerBulletMat = playerBulletMat;
		fly.hp = 100f;
		fly.scl = new Vector3(3f, 3f, 3f);
		fly.guns.add(new Gun(BALL, 3.2f, new Vector3(0f, 0f, 10f)));

		Array<Pilot>[] waves = new Array[1];
		for (int i = 0; i < waves.length; ++i) {
			waves[i] = new Array(2);
		}
		waves[0].add(new Pilot(fly, new Vector3(10f, 0f, -50f)));
		waves[0].add(new Pilot(fly, new Vector3(-10f, 0f, -50f)));
		enemies.setWaves(waves);
		return enemies;

	}

	int wave = -1;
	HashMap<Spec, Arena<Ship>> shipMap = new HashMap();
	Array<Pilot>[] waves;
	public Arena<Bullet>[] bulletMat;

	Enemies(int bulletTypes) {
		bulletMat = new Arena[bulletTypes];
	}

	void setWaves(Array<Pilot>[] waves) {
		this.waves = waves;

		HashMap<Spec, Integer> maxAmts = new HashMap();
		HashMap<Spec, Integer> waveAmts = new HashMap();

		for (Array<Pilot> wave : waves) {
			for (Pilot pilot : wave) {
				waveAmts.put(pilot.spec, waveAmts.getOrDefault(pilot.spec, 0) + 1);
			}

			for (HashMap.Entry<Spec, Integer> e : waveAmts.entrySet()) {
				maxAmts.put(e.getKey(), Math.max(e.getValue(), maxAmts.getOrDefault(e.getKey(), 0)));
			}
			waveAmts.clear();
		}

		for (HashMap.Entry<Spec, Integer> e : maxAmts.entrySet()) {
			Ship[] ships = new Ship[e.getValue()];
			for (int j = 0; j < ships.length; ++j) {
				ships[j] = new Ship(e.getKey());
			}
			shipMap.put(e.getKey(), new Arena(ships));
		}
	}

	public Array<Pilot> getWave() {
		return waves[wave];
	}

	public void stepWave() {
		++wave;
		for (Pilot pilot : waves[wave]) {
			pilot.use(shipMap.get(pilot.spec).get());
			pilot.reset();
		}
	}

	public Array<Pilot> getPilots() {
		return waves[wave];
	}

	static public class Pilot {
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
							if (bullet.intersects(ship.sensor)) {
								ship.hp -= bullet.dmg;
								if (ship.hp < 0) {
								}
							}
						}
					}

					for (int i = 0; i < spec.guns.size; ++i) {
						ship.cds[i] -= dt;
						if (ship.cds[i] < 0) {
							Gun gun = spec.guns.get(i);
							ship.cds[i] = gun.cd;
							ship.v0.set(ship.body.pos).mul(gun.tsf);
							spec.bulletMat[gun.index].get().spawn(ship.v0, gun.vel);
						}
					}

					ship.body.update();
				}

			}
		}
	}

	static public class Ship {
		public boolean arrived = false;
		public float hp;
		public Body body;
		public Sensor sensor;
		public Vector3 v0 = new Vector3();
		public float[] cds;

		public Ship(Spec spec) {
			body = new Body(spec.model);
			body.scl.set(spec.scl);
			sensor = new Sensor(spec.bBox, body.pos);
			cds = new float[spec.guns.size];
			for (int i = 0; i < cds.length; ++i) {
				cds[i] = spec.guns.get(i).cd;
			}
		}

	}
}

class Spec {
	public Arena<Bullet>[] bulletMat;
	public Arena<Bullet>[] playerBulletMat;
	public Array<Gun> guns = new Array();
	public Vector3 scl;
	public Model model;
	BoundingBox bBox;
	public float hp;

	public void setModel(Model model) {
		this.model = model;
		bBox = new BoundingBox();
		model.calculateBoundingBox(bBox);
	}

}

class Gun {
	public Matrix4 tsf = new Matrix4().idt();
	public Vector3 vel;
	int index;
	float cd;

	public Gun(int index, float cd, Vector3 vel) {
		this.index = index;
		this.cd = cd;
		this.vel = vel;
	}

}
