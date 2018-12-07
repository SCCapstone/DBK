package edu.sc.dbkdrymatic.internal;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {SiteInfo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
  public abstract SiteInfoDao siteInfoDao();
}
