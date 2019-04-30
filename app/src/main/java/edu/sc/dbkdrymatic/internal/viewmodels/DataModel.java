package edu.sc.dbkdrymatic.internal.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jscience.physics.amount.Amount;

import java.util.List;

import edu.sc.dbkdrymatic.internal.Damage;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.internal.database.SiteInfoDao;

public class DataModel extends ViewModel {

  private SiteInfoDao siDao;

  public DataModel(SiteInfoDao siDao) {
    this.siDao = siDao;
  }

  /**
   * Fetch a monitor of the jobs in the application database.
   */
  public LiveData<List<Job>> getJobs() {
    return this.siDao.getAllJobs();
  }

  /**
   * Creates a new Job with the given name and empty parameters, adding it to the database.
   *
   * TODO: Return the job and automatically switch to it.
   */
  public void createWithName(String name, Settings settings) {
    final SiteInfo siteInfo = new SiteInfo(
        Amount.valueOf(0.0, settings.getVolumeUnit()),
        Amount.valueOf(0, settings.getTemperatureUnit()),
        Amount.valueOf(0, settings.getTemperatureUnit()),
        Amount.valueOf(0, settings.getTemperatureUnit()),
        Amount.valueOf(0, settings.getTemperatureUnit()),
        0.0,
        Damage.CLASS1,
        settings.getCountry(),
        name);

    new Thread() {
      @Override
      public void run() {
        siDao.insertAll(siteInfo);
      }
    }.start();
  }



  public void deleteJob(String name, Settings settings) {










  }
  /**
   * Factory for creating {@code DataModel}s with the DAOs they require.
   *
   * <p>This method is used to avoid the {@code DataModel} having to know about the Application
   * Context, helping with separation of concerns.
   */
  public static class Factory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final SiteInfoDao siteInfoDao;

    public Factory(SiteInfoDao siteInfoDao) {
      this.siteInfoDao = siteInfoDao;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
      return (T) new DataModel(siteInfoDao);
    }
  }

}
