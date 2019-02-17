package edu.sc.dbkdrymatic.test;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * A collection of utilities that are universally helpful in testing classes throughout the program.
 */
public class Utils {

  /**
   * Useful for testing {@code LiveData}. It is normally difficult to test, as it requires an
   * observer to send data to when it is updated, which is usually called in a separate thread.
   * Typically, tests have ended with success before this thread has an opportunity to run.
   *
   * This code forces the data to poll synchronously by waiting in the main thread until an observer
   * has been notified of the state.
   *
   * This requires an {@code @Rule InstantTaskExecutorRule} in the test environment.
   *
   * Example:
   *     LifecycleOwner owner = Mockito.mock(LifecycleOwner.class);
   *     LifecycleRegistry registry = new LifecycleRegistry(owner);
   *     Mockito.when(owner.getLifeCycle()).thenReturn(registry);
   *
   *     LiveData data = //...
   *     Utils.observe(data, owner, Mockito.mock(Observer.class));
   *     Assert.assertEquals(foo, data.getValue());
   *
   * Note: This is not appropriate for fetching from database.
   */
  public static void observe(LiveData data, LifecycleOwner owner, Observer observer)
      throws ExecutionException, InterruptedException {
    FutureTask<Void> task = new FutureTask<Void>(() -> {
      data.observe(owner, observer);
      return null;
    });

    ArchTaskExecutor.getInstance().executeOnMainThread(task);
    task.get();
  }
}
