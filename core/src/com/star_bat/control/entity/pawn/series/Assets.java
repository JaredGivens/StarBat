package com.star_bat.control.entity.pawn.series;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Assets {
	public static final AssetManager MANAGER = new AssetManager();
	public static final String BAT = "models/Bat.gltf";
	public static final String FLY = "models/Fly.gltf";
	public static final String WASP = "models/Wasp.gltf";
	public static final String DRAGONFLY = "models/Dragonfly.gltf";
	public static final String LADYBUG = "models/Ladybug.gltf";
	public static final String MOTH = "models/Moth.gltf";
	public static final String BOLT = "models/Bolt.gltf";
	public static final String BALL = "models/Ball.gltf";
	public static final String CONE = "models/Cone.gltf";
	public static final String STICK = "images/Stick.png";
	public static final HashMap<String, SceneAsset> SCENES = new HashMap();
	public static final HashMap<String, Texture> TEXTURES = new HashMap();

	public static void load() {
		MANAGER.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());

		MANAGER.load(BAT, SceneAsset.class);
		MANAGER.load(FLY, SceneAsset.class);
		MANAGER.load(WASP, SceneAsset.class);
		MANAGER.load(DRAGONFLY, SceneAsset.class);
		MANAGER.load(LADYBUG, SceneAsset.class);
		MANAGER.load(MOTH, SceneAsset.class);
		MANAGER.load(BOLT, SceneAsset.class);
		MANAGER.load(BALL, SceneAsset.class);
		MANAGER.load(CONE, SceneAsset.class);
		MANAGER.load(STICK, Texture.class);

		Assets.MANAGER.finishLoading();

		SCENES.put(BAT, MANAGER.get(BAT, SceneAsset.class));
		SCENES.put(FLY, MANAGER.get(FLY, SceneAsset.class));
		SCENES.put(WASP, MANAGER.get(WASP, SceneAsset.class));
		SCENES.put(DRAGONFLY, MANAGER.get(DRAGONFLY, SceneAsset.class));
		SCENES.put(LADYBUG, MANAGER.get(LADYBUG, SceneAsset.class));
		SCENES.put(MOTH, MANAGER.get(MOTH, SceneAsset.class));
		SCENES.put(BOLT, MANAGER.get(BOLT, SceneAsset.class));
		SCENES.put(BALL, MANAGER.get(BALL, SceneAsset.class));
		SCENES.put(CONE, MANAGER.get(CONE, SceneAsset.class));
		TEXTURES.put(STICK, MANAGER.get(STICK, Texture.class));

	}

	public static void dispose() {
		MANAGER.dispose();
		for (SceneAsset asset : SCENES.values()) {
			asset.dispose();
		}
	}
}
