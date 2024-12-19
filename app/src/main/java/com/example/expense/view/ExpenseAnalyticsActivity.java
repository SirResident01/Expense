package com.example.expense.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.expense.R;
import com.example.expense.model.Expense;
import com.example.expense.viewmodel.ExpenseViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseAnalyticsActivity extends AppCompatActivity {
    private LineChart lineChart;
    private Spinner spinnerTimePeriod;
    private ExpenseViewModel expenseViewModel;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_analytics);

        // Инициализация элементов
        lineChart = findViewById(R.id.lineChart);
        spinnerTimePeriod = findViewById(R.id.spinnerTimePeriod);
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        // Получение userId текущего пользователя
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userId", -1);

        // Настройка Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.time_periods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimePeriod.setAdapter(adapter);

        // Слушатель выбора временного периода
        spinnerTimePeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedPeriod = parent.getItemAtPosition(position).toString();
                updateChart(selectedPeriod);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ничего не делаем
            }
        });
    }

    private void updateChart(String timePeriod) {
        expenseViewModel.getExpensesForUser(currentUserId).observe(this, expenses -> {
            List<Entry> entries = filterExpensesByTimePeriod(expenses, timePeriod);
            LineDataSet dataSet = new LineDataSet(entries, "Расходы");
            dataSet.setColor(getResources().getColor(R.color.purple_500));
            dataSet.setValueTextColor(getResources().getColor(R.color.black));

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.invalidate(); // Обновление графика
        });
    }

    private List<Entry> filterExpensesByTimePeriod(List<Expense> expenses, String timePeriod) {
        List<Entry> entries = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date today = new Date();

        for (Expense expense : expenses) {
            try {
                Date expenseDate = dateFormat.parse(expense.getDate());
                if (expenseDate != null) {
                    float daysDifference = (today.getTime() - expenseDate.getTime()) / (1000 * 60 * 60 * 24);
                    if (isWithinTimePeriod(daysDifference, timePeriod)) {
                        entries.add(new Entry(daysDifference, (float) expense.getAmount()));
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return entries;
    }

    private boolean isWithinTimePeriod(float daysDifference, String timePeriod) {
        switch (timePeriod) {
            case "День":
                return daysDifference <= 1;
            case "Неделя":
                return daysDifference <= 7;
            case "Месяц":
                return daysDifference <= 30;
            default:
                return false;
        }
    }
}
