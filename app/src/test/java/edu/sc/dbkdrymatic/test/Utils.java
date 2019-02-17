package edu.sc.dbkdrymatic.test;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Utils {
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
