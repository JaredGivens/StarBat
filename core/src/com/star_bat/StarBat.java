package com.star_bat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.star_bat.control.Hangar;
import com.star_bat.control.Keyboard;
import com.star_bat.control.Player;
import com.star_bat.control.Touchstick;
import com.star_bat.control.WaveBuilder;
import com.star_bat.control.entity.Pilot;
import com.star_bat.control.entity.pawn.Bullet;
import com.star_bat.control.entity.pawn.series.Arena;
import com.star_bat.control.entity.pawn.series.Assets;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class StarBat extends ApplicationAdapter {
	PerspectiveCamera cam;
	SpriteBatch batch;
	ModelBatch modelBatch;
	SpriteBatch spriteBatch;
	Environment environment;
	FileHandle starBatPath;
	Player player;
	FitViewport fitView;
	ExtendViewport view;
	Stage stage;
	Vector3 v0 = new Vector3();
	Hangar hangar;

	public void create() {
		Assets.load();

		environment = new Environment();
		environment.set(
				new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(.8f, .8f, .8f, -1f, .8f, -.2f));

		modelBatch = new ModelBatch();
		spriteBatch = new SpriteBatch();

		cam = new PerspectiveCamera();
		cam.fieldOfView = 67;
		cam.position.set(0f, 64f, 32f);
		cam.lookAt(0f, 0f, 0f);

		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		// fitView = new FitViewport(8f, 16f, cam);
		view = new ExtendViewport(1080f, 2160f, cam);

		// stage = new Stage(view, spriteBatch);
		// Gdx.input.setInputProcessor(stage);

		switch (Gdx.app.getType()) {
		case iOS:
		case Android:
			player = new Player(cam, new Touchstick(stage));
			break;
		default:
			player = new Player(cam, new Keyboard());
			break;
		}

		hangar = new Hangar(new WaveBuilder().build());
		hangar.nextWave();

	}

	void renderInstance(ModelInstance instance) {
		instance.transform.getTranslation(v0);
		if (cam.frustum.pointInFrustum(v0)) {
			modelBatch.render(instance, environment);
		}
	}

	void renderBullets(Arena<Bullet> bullets) {
	}

	public void render() {

		float dt = Gdx.graphics.getDeltaTime();
		player.update(dt);
		hangar.update(dt);

		Gdx.gl.glClearColor(.2f, .2f, .2f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);

		renderInstance(player.instance);
		for (Bullet bullet : player.primaryArena.arr) {
			renderInstance(bullet.instance);
		}

		for (Bullet bullet : hangar.balls.arr) {
			renderInstance(bullet.instance);
		}
		for (Bullet bullet : hangar.cones.arr) {
			renderInstance(bullet.instance);
		}
		for(Pilot pilot : hangar.getWave()) {
			renderInstance(pilot.instance);
		}

		modelBatch.end();

		spriteBatch.begin();
		for (Sprite sprite : player.sprites) {
			sprite.draw(spriteBatch);
		}
		spriteBatch.end();
	}

	public void resize(int width, int height) {
		// fitView.update(width, height);
		view.update(width, height);
	}

	public void dispose() {
		Assets.dispose();
		modelBatch.dispose();
		stage.dispose();
	}
}
