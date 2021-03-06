package edu.sc.dbkdrymatic.internal.viewmodels;

import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import edu.sc.dbkdrymatic.internal.BoostBox;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.internal.database.BoostBoxDao;
import edu.sc.dbkdrymatic.internal.database.SiteInfoDao;

public class SelectedJobModel extends ViewModel implements Observer<Job> {

  private SiteInfoDao siDao;
  private BoostBoxDao bbDao;

  private MutableLiveData<Job> selectedJob;
  private int selectedJobId;

  public SelectedJobModel(SiteInfoDao siDao, BoostBoxDao bbDao) {
    this.siDao = siDao;
    this.bbDao = bbDao;
  }

  /**
   * Change the selected {@code Job} monitored by {@code getSelectedJob}.
   */
  public void setSelectedJob(Job job) {
    if (selectedJob == null) {
      selectedJob = new MutableLiveData<>();
    }

    // Stop observing the old "current job"
    LiveData<Job> currentJob = this.siDao.getJob(this.selectedJobId);
    if (currentJob != null) {
      this.siDao.getJob(this.selectedJobId).removeObserver(this);
    }

    // Update and begin observing the new current job.
    this.selectedJob.setValue(job);
    this.selectedJobId = job.getSiteInfo().getId();
    this.siDao.getJob(this.selectedJobId).observeForever(this);
  }

  /**
   * Update the currently selected job in the database.
   *
   * Note, this operation is only valid if the job passed in is the currently selected job. This
   * is enforced by the primary key found at {@code Job.getSiteInfo().getId()}. The program will
   * crash with an assertion failure if this is not respected.
   */
  public void update(Job job) {
    AsyncTask.execute(() -> {
      this.siDao.update(job.getSiteInfo());
    });
  }

  /**
   * Returns a {@code LiveData<Job>} which will always be up-to-date as long as it exists.
   *
   * <p>The returned object will send updates to observers if the selected {@code Job} is updated in
   * the database or if a different {@code Job} is selected.
   */
  public LiveData<Job> getSelectedJob() {
    if (this.selectedJob == null) {
      this.selectedJob = new MutableLiveData<>();
    }
    return this.selectedJob;
  }

  public void addBoostBox(BluetoothDevice device) {
    BoostBox box = BoostBox.getInstance(selectedJob.getValue(), device);
    AsyncTask.execute(() -> {
      this.bbDao.insertAll(box);
    });
  }

  public void updateBoostBox(BoostBox box) {
    bbDao.update(box);
  }

  @Override
  public void onChanged(@Nullable Job job) {
    selectedJob.postValue(job);
  }

  /**
   * Factory for creating {@code SelectedJobModel}s with the DAOs they require.
   *
   * <p>This method is used to avoid the {@code SelectedJobModel} having to know about the
   * Application Context, helping with separation of concerns.
   */
  public static class Factory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final SiteInfoDao siteInfoDao;
    @NonNull
    private final BoostBoxDao bbDao;

    public Factory(@NonNull SiteInfoDao siteInfoDao, @NonNull BoostBoxDao bbDao) {
      this.siteInfoDao = siteInfoDao;
      this.bbDao = bbDao;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
      return (T) new SelectedJobModel(siteInfoDao, bbDao);
    }
  }

    /*
    //Deletes the selected job from the SiteInfoDao, and removes it from the UI so the
    //user doesn't see it.
    public void deleteJob (SiteInfoDao delete) {
      this.selectedJob = null;

    }

    //Updates the name of the job, allowing the user to change how they see their job
    public void updateJob (SiteInfoDao update) {
      this.selectedJob(siDao.update);
      return this.selectedJob;
    }
    */
}
