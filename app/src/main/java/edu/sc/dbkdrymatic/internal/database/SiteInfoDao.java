package edu.sc.dbkdrymatic.internal.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.SiteInfo;

@Dao
public interface SiteInfoDao {
  @Query("SELECT * FROM SiteInfo")
  LiveData<List<SiteInfo>> getAll();

  @Transaction
  @Query("SELECT * FROM SiteInfo")
  LiveData<List<Job>> getAllJobs();

  @Transaction
  @Query("SELECT * FROM SiteInfo WHERE id = :id LIMIT 1")
  LiveData<Job> getJob(int id);

  @Query("SELECT * FROM SiteInfo WHERE id = :id LIMIT 1")
  LiveData<SiteInfo> getById(int id);

  @Insert
  void insertAll(SiteInfo... infos);

  @Update
  void update(SiteInfo siteInfo);

  @Delete
  void delete(SiteInfo siteInfo);
}
