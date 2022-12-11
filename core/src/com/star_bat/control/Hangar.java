package com.star_bat.control;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.star_bat.control.entity.Pilot;
import com.star_bat.control.entity.pawn.Bullet;
import com.star_bat.control.entity.pawn.series.Arena;
import com.star_bat.control.entity.pawn.series.Assets;

import java.util.Arrays;
import java.util.Random;

public class Hangar {
	public Arena<Bullet> balls;
	public Arena<Bullet> cones;

	int wave = -1;
	Arena<ModelInstance>[] instanceMat;
	Array<Pilot>[] waves;
	Random random = new Random();
	static String[] ENEMY_NAMES = { Assets.FLY, Assets.WASP, Assets.DRAGONFLY, Assets.LADYBUG, Assets.MOTH };

	public Hangar(Array<Pilot>[] waves) {
		this.waves = waves;

		balls = Bullet.buildArena(Assets.SCENES.get(Assets.BALL).scene.model, 1000);
		cones = Bullet.buildArena(Assets.SCENES.get(Assets.CONE).scene.model, 200);

		int[] maxAmts = new int[8];
		int[] waveAmts = new int[8];
		Arrays.fill(maxAmts, 0);
		Arrays.fill(waveAmts, 0);

		for (Array<Pilot> wave : waves) {
			for (Pilot pilot : wave) {
				waveAmts[pilot.ship.index]++;
			}
			for (int i = 0; i < maxAmts.length; ++i) {
				maxAmts[i] = Math.max(maxAmts[i], waveAmts[i]);
			}
			Arrays.fill(waveAmts, 0);
		}
		
		instanceMat = new Arena[8];
		for (int i = 0; i < maxAmts.length; ++i) {
			ModelInstance[] instances = new ModelInstance[maxAmts[i]];
			for(int j = 0; j < maxAmts[i]; ++j) {
				System.out.println(ENEMY_NAMES[i]);
				instances[j] = new ModelInstance(Assets.SCENES.get(ENEMY_NAMES[i]).scene.model);
				instances[j].transform.val[Matrix4.M03] = (random.nextInt(2) * 2 - 1) * 200;
				instances[j].transform.val[Matrix4.M13] = 100;
			}
			instanceMat[i] = new Arena(instances);
		}
	}

	public Array<Pilot> getWave() {
		return waves[wave];
	}

	public void nextWave() {
		++wave;
		for (Pilot pilot : waves[wave]) {
			pilot.use(instanceMat[pilot.ship.index].get());
		}
	}

	public void update(float dt) {
		for (Pilot pilot : waves[wave]) {
			pilot.update(dt);
		}
	}
}
