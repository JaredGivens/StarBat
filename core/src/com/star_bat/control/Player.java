package com.star_bat.control;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.star_bat.control.entity.pawn.Bullet;
import com.star_bat.control.entity.pawn.series.Arena;
import com.star_bat.control.entity.pawn.series.Assets;

public class Player {
	public Array<Sprite> sprites = new Array();
	public ModelInstance instance;
	public Arena<Bullet> primaryArena;

	PerspectiveCamera cam;
	Vector2 input;
	Keyboard keyboard;
	Touchstick touchstick;
	final float VEL = 1f;
	float primaryCD = 0f;
	Vector3 translation = new Vector3();
	Quaternion rotation = new Quaternion();
	Vector3 v0 = new Vector3();
	Vector3 v1 = new Vector3();
	Quaternion q0 = new Quaternion();

	public Player(PerspectiveCamera cam, Keyboard keyboard) {
		this.cam = cam;
		this.instance = new ModelInstance(Assets.SCENES.get(Assets.BAT).scene.model);
		this.keyboard = keyboard;
		primaryArena = Bullet.buildArena(Assets.SCENES.get(Assets.BOLT).scene.model, 100);
	}

	public Player(PerspectiveCamera cam, Touchstick touchstick) {
		this.cam = cam;
		this.instance = new ModelInstance(Assets.SCENES.get(Assets.BAT).scene.model);
		this.touchstick = touchstick;
		sprites.add(touchstick.socket);
		sprites.add(touchstick.stick);
		primaryArena = Bullet.buildArena(Assets.SCENES.get(Assets.BOLT).scene.model, 100);
	}

	public void update(float dt) {
		if (touchstick != null) {
			input = this.touchstick.getInput();
		} else {
			input = this.keyboard.getInput();
		}
		input.scl(VEL);

		v0.set(input.x, 0f, input.y);
		translation.add(v0);

		v1.set(Vector3.Y).crs(v0);
		q0.setFromAxisRad(v1, .4f);
		rotation.slerp(q0, dt * 8f);
		instance.transform.set(translation, rotation);

		v1.set(0f, 0f, -3f);

		primaryCD += dt;
		if (primaryCD > 0.08f) {
			v0.set(translation);
			primaryArena.get().spawn(v0.add(1f, 0f, -2f), v1);
			primaryArena.get().spawn(v0.add(-2f, 0f, 0f), v1);
			primaryCD = 0;
		}

		for (Bullet bullet: primaryArena.arr) {
			bullet.update();
		}

	}

}
