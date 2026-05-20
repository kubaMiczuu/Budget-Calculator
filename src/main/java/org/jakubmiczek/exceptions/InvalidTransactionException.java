package org.jakubmiczek.exceptions;

public class InvalidTransactionException extends BudgetException {
    public InvalidTransactionException(String message) {
        super(message);
    }
}
