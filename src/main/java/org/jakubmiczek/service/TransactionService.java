package org.jakubmiczek.service;

import org.jakubmiczek.exceptions.TransactionNotFoundException;
import org.jakubmiczek.model.MonthlyBudget;
import org.jakubmiczek.model.Transaction;
import org.jakubmiczek.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class TransactionService {

    private final TransactionRepository repository;
    private MonthlyBudget monthlyBudget;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public void setMonthlyBudget(BigDecimal budget) {
        this.monthlyBudget = MonthlyBudget.builder().year(LocalDate.now().getYear()).month(LocalDate.now().getMonthValue()).limit(budget).build();
    }

    public void addTransaction(Transaction transaction) {
        repository.save(transaction);
        checkBudgetAlert();
    }

    private Optional<String> checkBudgetAlert() {
        if (monthlyBudget == null) return Optional.empty();

        BigDecimal budget = monthlyBudget.getLimit();

        BigDecimal expense = repository.findAll().stream()
                .filter(t -> t.getDate().getMonthValue() == monthlyBudget.getMonth())
                .filter(Transaction::isExpense)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if(expense.compareTo(budget) > 0) {
            return Optional.of(String.format("[SYSTEM]: Monthly limit was %s and was exceeded", budget));
        }

        return Optional.empty();
    }

    public void deleteTransaction(UUID id) {
        if(repository.findById(id).isPresent()) {
            repository.deleteById(id);
        }
        else {
            throw new TransactionNotFoundException(id);
        }
    }

    public Transaction findTransaction(UUID id) {
        return  repository.findById(id).orElse(null);
    }

    public List<Transaction> getTransactions() {
        return repository.findAll();
    }

    public void updateTransaction(Transaction transaction) {
        repository.update(transaction);
    }
}
