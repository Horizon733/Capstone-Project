package com.example.moneysaver.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneysaver.database.AppDatabase;
import com.example.moneysaver.database.Entry;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<List<Entry>> entry;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
         entry = database.entryDao().loadAllEntries();
    }

    public LiveData<List<Entry>> getEntries() {
        return entry;
    }
}