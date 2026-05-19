package org.jakubmiczek.model;

public enum TransactionCategory {
    INCOME(false),
    ENTERTAINMENT(true),
    TRANSPORT(true),
    FOOD(true),
    SHOPPING(true),
    BILLS(true),
    HEALTH(true),
    SAVINGS(false),
    OTHER(true);

    private final boolean expense;

    TransactionCategory(boolean expense) {
        this.expense = expense;
    }

    public boolean isExpense() {
        return this.expense;
    }
}
