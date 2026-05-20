package org.jakubmiczek.exceptions;

import java.util.UUID;

public class TransactionNotFoundException extends BudgetException {
    public TransactionNotFoundException(UUID id) {
        super("Transaction with id " + id + " not found");
    }
}
