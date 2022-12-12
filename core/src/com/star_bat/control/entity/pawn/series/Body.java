package com.star_bat.control.entity.pawn.series;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Body {
  public Vector3 pos = new Vector3();
  public Vector3 scl = new Vector3();
  public Quaternion rot = new Quaternion();
  public ModelInstance inst;

  public Body(Model model) {
    inst = new ModelInstance(model);
  }

  public void update() {
    inst.transform.set(pos, rot, scl);
  }
}
