package com.star_bat.space.control.pawn.series;

public class Arena<T> {
  public T[] arr;
  int head = 0;
  int prev = 0;
  public int length;

  public Arena(T[] arr) {
    this.arr = arr;
    this.length = arr.length;
  }

  public T get() {
    prev = head;
    head += 1;
    if (head == arr.length) {
      head = 0;
    }
    return arr[prev];
  }
}
