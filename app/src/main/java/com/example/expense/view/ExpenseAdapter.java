package com.example.expense.view;


import android.view.LayoutInflater;
import android.view.View;
import com.example.expense.model.Expense;
import com.example.expense.R;

import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expenses = new ArrayList<>();
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onEditClick(Expense expense);
        void onDeleteClick(Expense expense);
    }

    public ExpenseAdapter(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense currentExpense = expenses.get(position);
        holder.tvCategory.setText(currentExpense.getCategory());
        holder.tvAmount.setText(String.valueOf(currentExpense.getAmount()));
        holder.btnEdit.setOnClickListener(v -> onItemClickListener.onEditClick(currentExpense));
        holder.btnDelete.setOnClickListener(v -> onItemClickListener.onDeleteClick(currentExpense));
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategory;
        private final TextView tvAmount;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
