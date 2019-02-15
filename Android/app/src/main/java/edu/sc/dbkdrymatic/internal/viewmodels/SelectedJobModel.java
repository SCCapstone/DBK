package edu.sc.dbkdrymatic.internal.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.database.SiteInfoDao;

public class SelectedJobModel extends ViewModel implements Observer<Job> {

  private SiteInfoDao siDao;

  private MutableLiveData<Job> selectedJob;
  private int selectedJobId;

  public SelectedJobModel(SiteInfoDao siDao) {
    this.siDao = siDao;
  }

  public void setSelectedJob(Job job) {
    this.selectedJobId = job.getSiteInfo().getId();
    this.siDao.getJob(this.selectedJobId).observeForever(this);
  }

  public LiveData<Job> getSelectedJob() {
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
