package com.star_bat.space.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.star_bat.space.control.pawn.Bullet;
import com.star_bat.space.control.pawn.series.Arena;
import com.star_bat.space.control.pawn.series.Assets;
import com.star_bat.space.control.pawn.series.Body;

public class Player {
	public Array<Sprite> sprites = new Array();

	final int PRIMARY = 0;
	public Arena<Bullet>[] bulletMat = new Arena[1];
	public Arena<Bullet>[] enemyBulletMat;

	Vector2 input;
	Keyboard keyboard;
	Touchstick touchstick;
	final float VEL = 32f;
	float primaryCD = 0f;
	public Body body;
	BoundingBox bBox = new BoundingBox(new Vector3(1f, 1f, 1f), new Vector3(-1f, -1f, -1f));

	Vector3 v0 = new Vector3();
	Vector3 v1 = new Vector3();
	Quaternion q0 = new Quaternion();

	public Player(Keyboard keyboard) {
		this.keyboard = keyboard;
		init();
	}

	public Player(Touchstick touchstick) {
		this.touchstick = touchstick;
		sprites.add(touchstick.socket);
		sprites.add(touchstick.stick);
		init();
	}

	void init() {

		bulletMat[PRIMARY] = Bullet.buildArena(Assets.scenes.get(Assets.BOLT).scene.model, 100);
		this.body = new Body(Assets.scenes.get(Assets.BAT).scene.model);
		body.scl.set(7f, 7f, 7f);
	}

	public void move(float dt) {
		if (touchstick != null) {
			input = this.touchstick.getInput();
		} else {
			input = this.keyboard.getInput();
		}
		input.scl(VEL * dt);

		v0.set(input.x, 0f, input.y);
		body.pos.add(v0);

		v1.set(Vector3.Y).crs(v0);
		v1.x = 0;
		q0.setFromAxisRad(v1, .4f);
		body.rot.slerp(q0, dt * 8f);

		v1.set(0f, 0f, -300f);

		primaryCD += dt;
		if (primaryCD > 0.08f) {
			v0.set(body.pos);
			bulletMat[PRIMARY].get().spawn(v0.add(1f, 0f, -2f), v1);
			bulletMat[PRIMARY].get().spawn(v0.add(-2f, 0f, 0f), v1);
			primaryCD = 0;
		}
	}

	public void collide() {

	}

	static public class Touchstick {
		static final float DEAD_ZONE = 50;
		static final float LIMIT = 200;

		Vector2 input = new Vector2();
		Vector2 origin = new Vector2();
		Sprite stick;
		Sprite socket;
		Vector2 spriteOrigin = new Vector2(600, 200);
		Vector2 v0 = new Vector2();

		public Touchstick() {
			this.stick = new Sprite(Assets.textures.get(Assets.STICK));
			this.socket = new Sprite(Assets.textures.get(Assets.STICK));

			this.socket.setSize(300, 300);
			this.socket.setPosition(spriteOrigin.x - 70, spriteOrigin.y - 70);

			this.stick.setSize(160, 160);
			this.stick.setPosition(spriteOrigin.x, spriteOrigin.y);
		}

		public Vector2 getInput() {
			if (Gdx.input.isTouched()) {
				if (origin.x == -1) {
					origin.set(Gdx.input.getX(), Gdx.input.getY());
				} else {
					input.set(Gdx.input.getX(), Gdx.input.getY());
					input.sub(origin);
					float len = input.len();
					if (len < DEAD_ZONE) {
						input.setZero();
					} else {
						input.scl(1 / Math.max(len, LIMIT));
					}
				}
			} else {
				if (origin.x != -1) {
					origin.x = -1;
					input.setZero();
				}
			}
			v0.set(input).scl(100);
			this.stick.setPosition(spriteOrigin.x + v0.x, spriteOrigin.y - v0.y);
			return input;
		}

	}

	static public class Keyboard {
		Vector2 input = new Vector2();

		public Vector2 getInput() {
			input.setZero();
			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				input.y += 1;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.W)) {
				input.y -= 1;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				input.x += 1;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
				input.x -= 1;
			}
			return input.nor();
		}
	}

}
