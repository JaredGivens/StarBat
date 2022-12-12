package com.star_bat.control;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.star_bat.control.entity.Pilot;
import com.star_bat.control.entity.Pilot.Ship;
import com.star_bat.control.entity.Pilot.Spec;
import com.star_bat.control.entity.pawn.Bullet;
import com.star_bat.control.entity.pawn.series.Arena;
import com.star_bat.control.entity.pawn.series.Assets;

import java.util.Arrays;
import java.util.HashMap;

public class Hangar {

	int wave = -1;
	HashMap<Spec, Arena<Ship>> shipMap = new HashMap();
	Array<Pilot>[] waves;

	public Hangar(Array<Pilot>[] waves) {
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
			System.out.println(e.getValue());
			for (int j = 0; j < ships.length; ++j) {
				ships[j] = new Ship(e.getKey());
			}
			shipMap.put(e.getKey(), new Arena(ships));
		}
	}

	public Array<Pilot> getWave() {
		return waves[wave];
	}

	public void nextWave() {
		++wave;
		for (Pilot pilot : waves[wave]) {
			pilot.use(shipMap.get(pilot.spec).get());
			pilot.reset();
		}
	}

	public void update(float dt) {
		for (Pilot pilot : waves[wave]) {
			pilot.update(dt);
		}
	}
}
