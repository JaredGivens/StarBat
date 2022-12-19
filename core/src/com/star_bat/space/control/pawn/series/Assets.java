package com.star_bat.space.control.pawn.series;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader.ShaderProgramParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Assets {
	static ShaderProgramParameter buildShaderParameter(String path) {
		ShaderProgramParameter p = new ShaderProgramParameter();
		p.fragmentFile = path + ".frag";
		p.vertexFile = path + ".vert";
		return p;
	}

	public static AssetManager manager = new AssetManager();
	public static final String BAT = "models/Bat.gltf";
	public static final String FLY = "models/Fly.gltf";
	public static final String WASP = "models/Wasp.gltf";
	public static final String DRAGONFLY = "models/Dragonfly.gltf";
	public static final String LADYBUG = "models/Ladybug.gltf";
	public static final String MOTH = "models/Moth.gltf";
	public static final String BOLT = "models/Bolt.gltf";
	public static final String BALL = "models/Ball.gltf";
	public static final String CONE = "models/Cone.gltf";
	public static final String BUG_PLANET = "models/BugPlanet.gltf";
	public static final String STICK = "images/Stick.png";
	public static final String ENEMY = "shaders/Enemy";
	public static ShaderProgramParameter enemyParameter = buildShaderParameter(ENEMY);
	public static HashMap<String, SceneAsset> scenes = new HashMap();
	public static HashMap<String, Texture> textures = new HashMap();
	public static HashMap<String, ShaderProgram> shaders = new HashMap();

	public static void load() {
		manager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
		manager.setLoader(ShaderProgram.class, new ShaderProgramLoader(new InternalFileHandleResolver()));

		manager.load(BAT, SceneAsset.class);
		manager.load(FLY, SceneAsset.class);
		manager.load(WASP, SceneAsset.class);
		manager.load(DRAGONFLY, SceneAsset.class);
		manager.load(LADYBUG, SceneAsset.class);
		manager.load(MOTH, SceneAsset.class);
		manager.load(BOLT, SceneAsset.class);
		manager.load(BALL, SceneAsset.class);
		manager.load(CONE, SceneAsset.class);
		manager.load(BUG_PLANET, SceneAsset.class);

		manager.load(STICK, Texture.class);

		manager.load(ENEMY, ShaderProgram.class, enemyParameter);

		manager.finishLoading();

		// while (!manager.update()) {
		// 	System.out.println(manager.getProgress() * 100 + "%");
		// }

		scenes.put(BAT, manager.get(BAT, SceneAsset.class));
		scenes.put(FLY, manager.get(FLY, SceneAsset.class));
		scenes.put(WASP, manager.get(WASP, SceneAsset.class));
		scenes.put(DRAGONFLY, manager.get(DRAGONFLY, SceneAsset.class));
		scenes.put(LADYBUG, manager.get(LADYBUG, SceneAsset.class));
		scenes.put(MOTH, manager.get(MOTH, SceneAsset.class));
		scenes.put(BOLT, manager.get(BOLT, SceneAsset.class));
		scenes.put(BALL, manager.get(BALL, SceneAsset.class));
		scenes.put(CONE, manager.get(CONE, SceneAsset.class));
		scenes.put(BUG_PLANET, manager.get(BUG_PLANET, SceneAsset.class));

		textures.put(STICK, manager.get(STICK, Texture.class));

		shaders.put(ENEMY, manager.get(ENEMY, ShaderProgram.class));
	}

	public static void dispose() {
		manager.dispose();
		for (SceneAsset asset : scenes.values()) {
			asset.dispose();
		}
	}
}
