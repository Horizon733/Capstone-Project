package com.example.moneysaver;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneysaver.database.AppDatabase;
import com.example.moneysaver.database.Entry;

public class AddEntryViewModel extends ViewModel {
    private LiveData<Entry> task;

    public AddEntryViewModel(AppDatabase database, int entryId){
        task = database.entryDao().loadEntriesById(entryId);
    }

    public LiveData<Entry> getEntry(){
        return task;
    }
}
