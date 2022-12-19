package com.star_bat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.star_bat.space.control.pawn.series.Assets;
import com.star_bat.space.Court;

public class StarBat extends ApplicationAdapter {


	SpriteBatch batch;
	SpriteBatch spriteBatch;
	ModelBatch modelBatch;
	Court court;

	ExtendViewport view;
	Stage stage;
	Vector3 v0 = new Vector3();

	@Override
	public void create() {
		Assets.load();


		modelBatch = new ModelBatch();
		spriteBatch = new SpriteBatch();

		court = new Court();
		view = new ExtendViewport(1080f, 2160f, court.cam);

		// fitView = new FitViewport(8f, 16f, cam);

		// enemyShader = new EnemyShader();
		// enemyShader.init();
		// enemyShader.eye = cam.position;

		// stage = new Stage(view, spriteBatch);
		// Gdx.input.setInputProcessor(stage);



	}



	@Override
	public void render() {
		Gdx.gl.glClearColor(.2f, .2f, .2f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		court.move(Gdx.graphics.getDeltaTime());
		court.collide();

		modelBatch.begin(court.cam);
		court.render(modelBatch);
		modelBatch.end();

		spriteBatch.begin();
		court.draw(spriteBatch);
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		// fitView.update(width, height);
		view.update(width, height);
	}

	@Override
	public void dispose() {
		Assets.dispose();
		modelBatch.dispose();
		stage.dispose();
	}
}
