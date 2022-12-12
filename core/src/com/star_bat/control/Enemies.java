package com.star_bat.control;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.star_bat.control.entity.Pilot;
import com.star_bat.control.entity.Pilot.Ship;
import com.star_bat.control.entity.Pilot.Spec;
import com.star_bat.control.entity.Pilot.Spec.Gun;
import com.star_bat.control.entity.pawn.Bullet;
import com.star_bat.control.entity.pawn.series.Arena;
import com.star_bat.control.entity.pawn.series.Assets;

public class Enemies {

	static Spec fly;
	final int BALL = 0;
	final int CONE = 1;
	public Arena<Bullet>[] bulletMat = new Arena[2];

	public Enemies(Arena<Bullet>[] playerBulletMat) {
		bulletMat[BALL] = Bullet.buildArena(Assets.SCENES.get(Assets.BALL).scene.model, 100);
		bulletMat[CONE] = Bullet.buildArena(Assets.SCENES.get(Assets.CONE).scene.model, 10);

		fly = new Spec(Assets.SCENES.get(Assets.FLY).scene.model)
				.setBulletMat(bulletMat)
				.setPlayerBulletMat(playerBulletMat)
				.setHp(100);
		fly.guns.add(new Gun(0, 0.5f));

	}

	public Array<Pilot>[] genWaves() {
		Array<Pilot>[] waves = new Array[1];
		for (int i = 0; i < waves.length; ++i) {
			waves[i] = new Array(2);
		}
		waves[0].add(new Pilot(fly, new Vector3(10, 0, -50)));
		waves[0].add(new Pilot(fly, new Vector3(-10, 0, -50)));
		return waves;

	}
}
