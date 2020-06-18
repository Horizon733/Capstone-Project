package com.example.moneysaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.moneysaver.database.AppDatabase;
import com.example.moneysaver.database.DateConverter;
import com.example.moneysaver.database.Entry;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEntry extends AppCompatActivity {
    public static final String INSTANCE_ENTRY_ID = "instanceEntryId";
    public static final String EXTRA_ENTRY_ID = "extraEntryId";
    private int dayI, monthI, yearI;
    @BindView(R.id.date_picker)
    EditText date;
    @BindView(R.id.salary_edit)
    EditText salary;
    @BindView(R.id.expenses_edit)
    EditText expenses;
    @BindView(R.id.grid_layour)
    GridLayout gridLayout;
    private AppDatabase mDb;
    private static final int DEFAULT_ENTRY_ID = -1;
    private int mEntryId = DEFAULT_ENTRY_ID;
    public Date dateD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_ENTRY_ID)) {
            mEntryId = savedInstanceState.getInt(INSTANCE_ENTRY_ID, DEFAULT_ENTRY_ID);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        ButterKnife.bind(this);
        date.setOnClickListener(mClickListener);
        mDb = AppDatabase.getInstance(getApplicationContext());
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_ENTRY_ID)) {
            getSupportActionBar().setTitle("Update Entry");
            if (mEntryId == DEFAULT_ENTRY_ID) {
                mEntryId = intent.getIntExtra(EXTRA_ENTRY_ID, DEFAULT_ENTRY_ID);
                AddEntryViewModelFactory factory = new AddEntryViewModelFactory(mDb, mEntryId);
                final AddEntryViewModel viewModel = new ViewModelProvider(this, factory).get(AddEntryViewModel.class);
                viewModel.getEntry().observe(this, new Observer<Entry>() {
                    @Override
                    public void onChanged(Entry taskEntry) {
                        viewModel.getEntry().removeObserver(this);
                        populateUI(taskEntry);
                    }
                });
            }
        } else {
            getSupportActionBar().setTitle("Add Entry");
        }

    }

    private void populateUI(Entry entry) {
        salary.setText(Double.toString(entry.getSalary()));
        expenses.setText(Double.toString(entry.getExpenses()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String dateS = dateFormat.format(entry.getDate());
        date.setText(dateS);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateDialog();
        }
    };

    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                String dateS = dateFormat.format(fromTimestampFormat(dayOfMonth + "-" + monthOfYear + "-" + year));
                date.setText(dateS);
                dayI = dayOfMonth;
                monthI = monthOfYear;
                yearI =year;
            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(AddEntry.this, listener, yearI, monthI, dayI);
        dpDialog.getDatePicker().setMaxDate(DateConverter.toTimestamp(Calendar.getInstance().getTime()));
        dpDialog.getDatePicker().setMinDate(DateConverter.toTimestamp(fromTimestampFormat("1-1-2000")));
        dpDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
               saveSalary();

                return true;
            case R.id.action_delete:
                deleteEntry();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSalary() {
        String salryS = salary.getText().toString();
        String expenseS = expenses.getText().toString();
        String dateD = date.getText().toString();
        Date dateO = DateConverter.fromTimestamp(dateD);
        Log.e("date",""+dateO);
        if (dateD.equals("") && salryS.equals("") && expenseS.equals("")) {
            Snackbar.make(gridLayout, "Fill all the details", Snackbar.LENGTH_SHORT).show();
        } else if (dateD.equals("") ) {
            Snackbar.make(gridLayout, "Fill all the details", Snackbar.LENGTH_SHORT).show();
        } else if (dateD.equals("") && salryS.equals("")) {
            Snackbar.make(gridLayout, "Fill all the details", Snackbar.LENGTH_SHORT).show();
        } else if ( salryS.equals("") || expenseS.equals("")) {
            Snackbar.make(gridLayout, "Fill all the details", Snackbar.LENGTH_SHORT).show();
        } else {
            double salaryD = Double.parseDouble(salryS);
            double expensesD = Double.parseDouble(expenseS);
            double savings = Math.abs(salaryD - expensesD);
            final Entry entry = new Entry(dateO, salaryD, expensesD, savings);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mEntryId == DEFAULT_ENTRY_ID) {
                        mDb.entryDao().insertEntry(entry);
                    } else {
                        entry.setId(mEntryId);
                        mDb.entryDao().updateEntry(entry);
                    }
                    finish();
                }
            });
        }
    }
    private void deleteEntry(){
        if(mEntryId == DEFAULT_ENTRY_ID) {
            Toast.makeText(AddEntry.this,"Atleast save or add the record",Toast.LENGTH_LONG).show();
        }else {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.entryDao().deleteById(mEntryId);
                    finish();
                }
            });
        }
    }
    public static Date fromTimestampFormat(String value) {
        DateFormat df = new SimpleDateFormat("dd-mm-yyyy");
        if (value != null) {
            try {
                Log.e("Date formatted",""+df.parse(value));
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

}