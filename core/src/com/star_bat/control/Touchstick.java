package com.star_bat.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star_bat.control.entity.pawn.series.Assets;

public class Touchstick {
	Stage stage;
	Vector2 input = new Vector2();
	Vector2 origin = new Vector2();
	Sprite stick;
	Sprite socket;
	Vector2 spriteOrigin = new Vector2(600, 200);
	static final float DEAD_ZONE = 50;
	static final float LIMIT = 200;
	Vector2 v0 = new Vector2();

	public Touchstick(Stage stage) {
		this.stage = stage;
		this.stick = new Sprite(Assets.TEXTURES.get(Assets.STICK));
		this.socket = new Sprite(Assets.TEXTURES.get(Assets.STICK));

		this.socket.setSize(300, 300);
		this.socket.setPosition(spriteOrigin.x - 70, spriteOrigin.y - 70);

		this.stick.setSize(160, 160);
		this.stick.setPosition(spriteOrigin.x, spriteOrigin.y);
	}

	public Vector2 getInput() {
		if(Gdx.input.isTouched()) {
			if(origin.x == -1) {
				origin.set(Gdx.input.getX(), Gdx.input.getY());
			} else {
				input.set(Gdx.input.getX(), Gdx.input.getY());
				input.sub(origin);
				float len = input.len();
				if(len < DEAD_ZONE) {
					input.setZero();
				} else {
					input.scl(1 / Math.max(len, LIMIT));
				}
			}
		} else {
			if(origin.x != -1) {
				origin.x = -1;
				input.setZero();
			}
		}
		v0.set(input).scl(100);
		this.stick.setPosition(spriteOrigin.x + v0.x, spriteOrigin.y - v0.y);
		return input;
	}


}
