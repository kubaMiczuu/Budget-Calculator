package org.jakubmiczek.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class Transaction {
    private final UUID id;
    private final String description;
    private final BigDecimal amount;
    private final LocalDate date;
    private final TransactionCategory category;

    public boolean isExpense() {
        return category.isExpense();
    }

    public static class TransactionBuilder {
        public Transaction build() {
            if(amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("[SYSTEM]: Amount cannot be negative or null");
            }
            if(date == null || date.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("[SYSTEM]: Date must be before now");
            }
            TransactionCategory finalCategory = category == null ? TransactionCategory.OTHER : category;
            UUID finalId = id == null ? UUID.randomUUID() : id;

            return new Transaction(finalId, description, amount, date, finalCategory);
        }
    }
}
