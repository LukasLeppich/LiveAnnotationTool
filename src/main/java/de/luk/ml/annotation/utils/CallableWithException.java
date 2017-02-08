package de.luk.ml.annotation.utils;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/8/17.
 */
@FunctionalInterface
public interface CallableWithException<V> {
  V call() throws Throwable;
}
