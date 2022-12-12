package com.star_bat.control.entity.pawn.series;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Sensor{
  BoundingBox bBox;
  public Vector3 pos;
  public float radius;

  public Sensor(BoundingBox bBox, Vector3 pos) {
    this.bBox = new BoundingBox(bBox);
    this.pos = pos;
    radius = bBox.max.len();
  }

  public boolean contains(Sensor aabb) {
    // i swear this is smart
    bBox.max.add(pos);
    bBox.min.add(pos);
    boolean res = bBox.contains(aabb.bBox);
    bBox.max.sub(pos);
    bBox.min.sub(pos);
    return res;
  }
}