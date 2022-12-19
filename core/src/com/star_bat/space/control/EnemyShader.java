package com.star_bat.space.control;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.star_bat.space.control.pawn.series.Assets;


public class EnemyShader implements Shader {
  ShaderProgram program;
  RenderContext context;
  Camera camera;
  int u_projViewTrans;
  int u_worldTrans;
  int u_sun;
  int u_eye;
  public Vector3 sun ;
  public Vector3 eye ;
	@Override
	public void init () {

    sun = new Vector3();
    eye  = new Vector3();

		program = Assets.shaders.get(Assets.ENEMY);
    if (!program.isCompiled()) {
      throw new GdxRuntimeException(program.getLog());
    }
    u_projViewTrans = program.getUniformLocation("u_projViewTrans");
		u_worldTrans = program.getUniformLocation("u_worldTrans");
		u_sun = program.getUniformLocation("u_sun");
		u_eye = program.getUniformLocation("u_eye");
  }
	@Override
	public void dispose () {
    program.dispose();
  }
	@Override
	public void begin (Camera camera, RenderContext context) {
    this.camera = camera;
    this.context = context;
    program.bind();
    program.setUniformMatrix(u_projViewTrans, camera.combined);
    program.setUniformf(u_sun, sun.x, sun.y, sun.z);
    program.setUniformf(u_eye, eye.x, eye.y, eye.z);
    context.setDepthTest(GL20.GL_LEQUAL);
		context.setCullFace(GL20.GL_BACK);
  }
	@Override
	public void render (Renderable renderable) {
    program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
    renderable.meshPart.render(program);
  }
	@Override
	public void end () {	}
	@Override
	public int compareTo (Shader other) {
		return 0;
	}
	@Override
	public boolean canRender (Renderable instance) {
		return true;
	}
}