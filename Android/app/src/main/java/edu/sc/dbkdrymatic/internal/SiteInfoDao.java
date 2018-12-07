package edu.sc.dbkdrymatic.internal;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SiteInfoDao {
  @Query("SELECT * FROM SiteInfo")
  List<SiteInfo> getAll();

  @Query("SELECT * FROM SiteInfo WHERE name LIKE :name LIMIT 1")
  SiteInfo getByName(String name);

  @Insert
  void insertAll(SiteInfo... infos);

  @Delete
  void delete(SiteInfo siteInfo);

  @Update
  void update(SiteInfo siteInfo);
}
