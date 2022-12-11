package com.star_bat.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class Keyboard {
	Vector2 input = new Vector2();

	public Vector2 getInput() {
		input.setZero();
		if(Gdx.input.isKeyPressed(Keys.S)) {
			input.y += 1;
		}
		if(Gdx.input.isKeyPressed(Keys.W)) {
			input.y -= 1;
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			input.x += 1;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			input.x -= 1;
		}
		return input.nor();
	}
}
