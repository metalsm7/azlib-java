package com.mparang.azlib;

public interface AZCallback {
  public void callback();
  public static interface Callback<T> { public void callback(T value); }
}
