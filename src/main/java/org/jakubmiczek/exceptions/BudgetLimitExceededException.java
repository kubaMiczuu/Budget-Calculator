package org.jakubmiczek.exceptions;

import org.jakubmiczek.model.TransactionCategory;

import java.math.BigDecimal;

public class BudgetLimitExceededException extends BudgetException {
    public BudgetLimitExceededException(TransactionCategory category, BigDecimal limit, BigDecimal amount) {
        super(String.format("Monthly limit for category %s was %s and was exceeded by transaction of amount %s",
                category, limit, amount));
    }
}
