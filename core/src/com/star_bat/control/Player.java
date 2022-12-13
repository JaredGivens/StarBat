package com.star_bat.control;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.star_bat.control.entity.pawn.Bullet;
import com.star_bat.control.entity.pawn.series.Arena;
import com.star_bat.control.entity.pawn.series.Assets;

public class Player {
	public Array<Sprite> sprites = new Array();
	public ModelInstance inst;

	final int PRIMARY = 0;
	public Arena<Bullet>[] bulletMat = new Arena[1];
	Arena<Bullet>[] enemyBulletMat;

	PerspectiveCamera cam;
	Vector2 input;
	Keyboard keyboard;
	Touchstick touchstick;
	final float VEL = 1f;
	float primaryCD = 0f;
	Vector3 position = new Vector3();
	BoundingBox bBox = new BoundingBox(new Vector3(1f, 1f, 1f), new Vector3(-1f, -1f, -1f));

	Quaternion rotation = new Quaternion();
	Vector3 v0 = new Vector3();
	Vector3 v1 = new Vector3();
	Quaternion q0 = new Quaternion();

	public Player(PerspectiveCamera cam, Keyboard keyboard) {
		this.cam = cam;
		this.inst = new ModelInstance(Assets.SCENES.get(Assets.BAT).scene.model);
		this.keyboard = keyboard;
		bulletMat[PRIMARY] = Bullet.buildArena(Assets.SCENES.get(Assets.BOLT).scene.model, 100);
	}

	public Player(PerspectiveCamera cam, Touchstick touchstick) {
		this.cam = cam;
		this.inst = new ModelInstance(Assets.SCENES.get(Assets.BAT).scene.model);
		this.touchstick = touchstick;
		sprites.add(touchstick.socket);
		sprites.add(touchstick.stick);
		bulletMat[PRIMARY] = Bullet.buildArena(Assets.SCENES.get(Assets.BOLT).scene.model, 100);
	}

	public void setEnemyBulletMat(Arena<Bullet>[] enemyBulletMat) {
		this.enemyBulletMat = enemyBulletMat;
	}

	public void update(float dt) {
		if (touchstick != null) {
			input = this.touchstick.getInput();
		} else {
			input = this.keyboard.getInput();
		}
		input.scl(VEL);

		v0.set(input.x, 0f, input.y);
		position.add(v0);

		v1.set(Vector3.Y).crs(v0);
		q0.setFromAxisRad(v1, .4f);
		rotation.slerp(q0, dt * 8f);
		inst.transform.set(position, rotation);

		v1.set(0f, 0f, -3f);

		primaryCD += dt;
		if (primaryCD > 0.08f) {
			v0.set(position);
			bulletMat[PRIMARY].get().spawn(v0.add(1f, 0f, -2f), v1);
			bulletMat[PRIMARY].get().spawn(v0.add(-2f, 0f, 0f), v1);
			primaryCD = 0;
		}

		for (Arena<Bullet> bullets : bulletMat) {
			for (Bullet bullet : bullets.arr) {
				bullet.update();
			}
		}

	}

}
