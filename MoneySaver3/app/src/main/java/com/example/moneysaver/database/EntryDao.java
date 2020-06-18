package com.example.moneysaver.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;
@Dao
public interface EntryDao {
    @Query("SELECT * FROM entry")
    LiveData<List<Entry>> loadAllEntries();

    @Insert
    void insertEntry(Entry entry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(Entry entry);

    @Query("SELECT * FROM entry WHERE id = :id")
    LiveData<Entry> loadEntriesById(int id);

    @Query("DELETE FROM entry")
    void deleteEntries();

    @Query("DELETE FROM entry WHERE id = :id")
    void deleteById(int id);

    //@Query("SELECT  id,strftime('%Y',date) as date, count(salary) as salary, count(expenses) as expenses ,count(savings) as savings FROM entry GROUP BY date")
    @Query("SELECT id,SUM(salary) as salary,SUM(expenses) as expenses,SUM(savings) as savings,date FROM entry WHERE date BETWEEN :dateStart AND :dateEnd")
    //@Query("SELECT * FROM entry WHERE strfttime('%y', date) = :date")
    LiveData<List<Entry>> yearlyEntries(Date dateStart, Date dateEnd);
}
