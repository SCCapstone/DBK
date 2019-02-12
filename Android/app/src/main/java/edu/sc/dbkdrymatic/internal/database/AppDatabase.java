package edu.sc.dbkdrymatic.internal.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import edu.sc.dbkdrymatic.internal.BoostBox;
import edu.sc.dbkdrymatic.internal.SiteInfo;

/**
 * Room Persistence Database manager for DBK Drymatic app.
 *
 * <p>This is the provider through which DAO objects should be instantiated, e.g.,
 * {@code SiteInfoDao}, {@code BoostBoxDao}, {@code JobDao}, and the method through which those
 * objects should be instantiated. Those objects are abstraction layers over this class, and cannot
 * exist on their own.
 *
 * <p>This class should be instantiated using a {@code RoomDatabase.Builder} generated using
 * {@code android.arch.persistence.room.Room.databaseBuilder()}. For documentation, see
 * {@code https://developer.android.com/reference/android/arch/persistence/room/Room.html}.
 */
@Database(entities = {SiteInfo.class, BoostBox.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
  /** Returns an abstraction for storage and retrieval of {@code SiteInfo} objects */
  public abstract SiteInfoDao siteInfoDao();

  /** Returns an abstraction for storage and retrieval of {@code BoostBox} objects.*/
  public abstract BoostBoxDao boostBoxDao();
}
