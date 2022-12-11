package com.star_bat.control;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.star_bat.control.entity.Pilot;
import com.star_bat.control.entity.pawn.Ship;
import com.star_bat.control.entity.pawn.series.Assets;

public class WaveBuilder {

  
	static Ship fly;
  public WaveBuilder() {
		fly = new Ship(0);
		fly.guns.add(new Ship.Gun(Assets.BALL, 0.5f));
	}

  public Array<Pilot>[] build() {
	  Array<Pilot>[] waves = new Array[1];
		for (int i = 0; i < waves.length; ++i) {
			waves[i] = new Array(2);
		}
		waves[0].add(new Pilot(fly, new Vector3(10, 0, -50)));
		waves[0].add(new Pilot(fly, new Vector3(-10, 0, -50)));
    return waves;

  }
}
