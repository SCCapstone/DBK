package edu.sc.dbkdrymatic.internal.database;

import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

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
