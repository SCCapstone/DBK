package edu.sc.dbkdrymatic.internal.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.database.SiteInfoDao;

public class SelectedJobModel extends ViewModel implements Observer<Job> {

  private SiteInfoDao siDao;

  private MutableLiveData<Job> selectedJob;
  private int selectedJobId;

  public SelectedJobModel(SiteInfoDao siDao) {
    this.siDao = siDao;
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
    this.siDao.update(job.getSiteInfo());
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

    public Factory(SiteInfoDao siteInfoDao) {
      this.siteInfoDao = siteInfoDao;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
      return (T) new SelectedJobModel(siteInfoDao);
    }
  }
}
