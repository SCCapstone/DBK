package edu.sc.dbkdrymatic.internal;

public interface Subscriber<T> {
  void publish(T data);
}
