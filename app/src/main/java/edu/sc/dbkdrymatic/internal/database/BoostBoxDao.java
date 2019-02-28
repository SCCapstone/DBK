package edu.sc.dbkdrymatic.internal.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.sc.dbkdrymatic.internal.BoostBox;

@Dao
public interface BoostBoxDao {
  @Query("SELECT * FROM BoostBox")
  List<BoostBox> getAll();

  @Query("SELECT * FROM BoostBox WHERE address LIKE :address LIMIT 1")
  BoostBox getByAddress(String address);

  @Insert
  void insertAll(BoostBox... boxes);

  @Delete
  void delete(BoostBox boostBox);

  @Update
  void update(BoostBox boostBox);
}
