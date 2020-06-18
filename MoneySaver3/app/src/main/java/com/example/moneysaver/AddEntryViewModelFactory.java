package com.example.moneysaver;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.moneysaver.database.AppDatabase;


public class AddEntryViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mEntryId;

    public AddEntryViewModelFactory(AppDatabase database, int entryId){
        mDb = database;
        mEntryId = entryId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddEntryViewModel(mDb, mEntryId);
    }
}
