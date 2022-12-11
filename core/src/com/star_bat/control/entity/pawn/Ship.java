package com.star_bat.control.entity.pawn;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Ship {
  public static class Gun {
    public Matrix4 transform = new Matrix4();
    public Vector3 velocity = new Vector3();
    String path;
    float cd;
    public Gun(String path, float cd) {
      this.path = path;
      this.cd = cd;
    }
  }
  public Array<Gun> guns = new Array();
  public int index;
  public Ship(int index) {
    this.index = index;
  }

}
