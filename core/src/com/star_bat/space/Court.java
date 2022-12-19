package com.star_bat.space;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.star_bat.space.control.Enemies;
import com.star_bat.space.control.Enemies.Pilot;
import com.star_bat.space.control.Player;
import com.star_bat.space.control.pawn.Bullet;
import com.star_bat.space.control.pawn.series.Arena;
import com.star_bat.space.control.pawn.series.Assets;
import com.star_bat.space.control.pawn.series.Body;


class Planet {
  public ModelInstance inst;
  Matrix4 world = new Matrix4().idt();
  Matrix4 view = new Matrix4().idt();
  Matrix4 proj = new Matrix4().idt();
  Vector3 coords = new Vector3();
  Vector3 pos = new Vector3();
  Vector3 up = new Vector3();
  Vector3 nextPos = new Vector3();
  Vector3 v0 = new Vector3();
  Matrix4 m0 = new Matrix4();

  public Planet() {
    inst = new ModelInstance(Assets.scenes.get(Assets.BUG_PLANET).scene.model);
    inst.transform.trn(0, 0, 0);
    inst.transform.scl(1000, 1000, 1000);
    coords.x = 1100;

  }

  public void move(float dt) {
    coords.y += Math.PI * 2 * dt / 960;
    coords.z += Math.PI * 2 * dt / 240;
    float xzr = (float)Math.sin(coords.z);
    pos.set(nextPos);
    nextPos.x = (float)Math.cos(coords.y) * xzr * coords.x;
    nextPos.y = (float)Math.cos(coords.z) * coords.x;
    nextPos.z = (float)Math.sin(coords.y) * xzr * coords.x;
    up.set(pos).nor();

  view.setToLookAt(pos, nextPos, up);
  world.set(view);
   Matrix4.inv(world.val);
  // trans.setToRotation(Vector3.Y, up);
  //   trans.trn(pos);




    // trans.mul(m0);
  }

	public void moveBody(Body body) {
		body.update();
		body.inst.transform.mulLeft(world);
	}

}

public class Court {
	public PerspectiveCamera cam = new PerspectiveCamera();
	Player player;
	Planet planet;
	Enemies enemies;
	Environment environment = new Environment();

  public Court() {
    planet = new Planet();

		cam.fieldOfView = 67;

		cam.near = 1f;
		cam.far = 1000f;
		cam.update();

		environment.set(
				new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(.8f, .8f, .8f, -1f, .8f, -.2f));

    switch (Gdx.app.getType()) {
			case iOS:
			case Android:
				player = new Player(new Player.Touchstick());
				break;
			default:
				player = new Player(new Player.Keyboard());
				break;
		}

    enemies = Enemies.makeBugs(player.bulletMat);
    enemies.stepWave();
    player.enemyBulletMat = enemies.bulletMat;

  }


  public void move(float dt) {
    planet.move(dt);

    cam.position.set(0, 32, 32);
    cam.direction.set(0, 0, 0);
    cam.up.set(Vector3.Y);

    // custom cam update
    cam.view.setToLookAt(cam.position, cam.direction, cam.up);
    cam.view.mul(planet.view );
		cam.combined.set(cam.projection);
		Matrix4.mul(cam.combined.val, cam.view.val);



    // cam.update(false);

    // cam.view.setToRotation(Vector3.X, 45f);
    // cam.view.trn(0, 32f, 32f);
    // cam.view.mulLeft(planet.trans);
    // Matrix4.inv(cam.view.val);


    player.move(dt);
    planet.moveBody(player.body);

		for (Arena<Bullet> bullets : player.bulletMat) {
			for (Bullet bullet : bullets.arr) {
				bullet.update(dt);
				planet.moveBody(bullet.body);
			}
		}

		for (Arena<Bullet> bullets : enemies.bulletMat) {
			for (Bullet bullet : bullets.arr) {
				bullet.update(dt);
				planet.moveBody(bullet.body);

			}
		}


		Array<Pilot> pilots = enemies.getPilots();
		for(Pilot pilot : pilots) {
			pilot.update(dt);
			planet.moveBody(pilot.ship.body);
		}
  }

  public void collide() {

  }

  public void render(ModelBatch mb) {
    mb.render(planet.inst, environment);
    mb.render(player.body.inst, environment);

		for (Arena<Bullet> bullets : player.bulletMat) {
			for (Bullet bullet : bullets.arr) {
        mb.render(bullet.body.inst, environment);
			}
		}

		for (Arena<Bullet> bullets : enemies.bulletMat) {
			for (Bullet bullet : bullets.arr) {
        mb.render(bullet.body.inst, environment);
			}
		}

		Array<Pilot> pilots = enemies.getPilots();
		for(Pilot pilot : pilots) {
      mb.render(pilot.ship.body.inst, environment);
		}
		// inst.transform.getTranslation(v0);
		// if (cam.frustum.pointInFrustum(v0)) {
		// }
  }

  public void draw(SpriteBatch sb) {
		for (Sprite sprite : player.sprites) {
			sprite.draw(sb);
		}
  }

}