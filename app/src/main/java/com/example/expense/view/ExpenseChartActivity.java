package com.example.expense.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.expense.R;
import com.example.expense.model.Expense;
import com.example.expense.viewmodel.ExpenseViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseChartActivity extends AppCompatActivity {

    private PieChart pieChart;
    private ExpenseViewModel expenseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_chart);

        pieChart = findViewById(R.id.pieChart);
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        // Получение userId текущего пользователя из SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("userId", -1);

        // Наблюдение за расходами только текущего пользователя
        expenseViewModel.getExpensesForUser(currentUserId).observe(this, this::setUpPieChart);
    }

    private void setUpPieChart(List<Expense> expenses) {
        Map<String, Double> expenseCategoryTotals = new HashMap<>();
        double totalAmount = 0.0;

        // Суммируем расходы по категориям и общую сумму
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            totalAmount += amount;
            expenseCategoryTotals.put(category, expenseCategoryTotals.getOrDefault(category, 0.0) + amount);
        }

        // Ограничиваем количество категорий до 8, остальные объединяем в "Other"
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(expenseCategoryTotals.entrySet());
        sortedEntries.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())); // Сортируем по убыванию

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        int[] predefinedColors = {
                android.graphics.Color.RED,
                android.graphics.Color.BLUE,
                android.graphics.Color.GREEN,
                android.graphics.Color.YELLOW,
                android.graphics.Color.MAGENTA,
                android.graphics.Color.CYAN,
                android.graphics.Color.LTGRAY,
                android.graphics.Color.DKGRAY
        };

        double otherTotal = 0.0;
        for (int i = 0; i < sortedEntries.size(); i++) {
            if (i < 8) {
                Map.Entry<String, Double> entry = sortedEntries.get(i);
                entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
                colors.add(predefinedColors[i % predefinedColors.length]);
            } else {
                otherTotal += sortedEntries.get(i).getValue();
            }
        }

        // Добавляем "Other", если есть категории сверх лимита
        if (otherTotal > 0) {
            entries.add(new PieEntry((float) otherTotal, "Other"));
            colors.add(android.graphics.Color.GRAY);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Расходы по категориям");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(android.graphics.Color.BLACK);

        PieData data = new PieData(dataSet);

        // Устанавливаем формат для значений на диаграмме (только цифры)
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f", value);
            }
        });

        // Настройка диаграммы
        pieChart.setData(data);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(android.graphics.Color.BLACK);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setExtraOffsets(20, 30, 20, 20);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();

        // Создаем легенду
        setUpLegend(pieChart, entries, colors);
    }

    private void setUpLegend(PieChart pieChart, List<PieEntry> entries, List<Integer> colors) {
        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            LegendEntry legendEntry = new LegendEntry();
            legendEntry.label = entries.get(i).getLabel();
            legendEntry.formColor = colors.get(i);
            legendEntries.add(legendEntry);
        }

        Legend legend = pieChart.getLegend();
        legend.setCustom(legendEntries);
        legend.setTextSize(14f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setWordWrapEnabled(true);
        legend.setYOffset(0f);
        legend.setXOffset(10f);
    }
}
