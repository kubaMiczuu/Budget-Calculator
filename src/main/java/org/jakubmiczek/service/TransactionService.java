package org.jakubmiczek.service;

import lombok.Getter;
import org.jakubmiczek.exceptions.BudgetException;
import org.jakubmiczek.exceptions.TransactionNotFoundException;
import org.jakubmiczek.model.MonthlyBudget;
import org.jakubmiczek.model.Transaction;
import org.jakubmiczek.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
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
        checkBudgetAlert().ifPresent(msg -> {throw new BudgetException(msg);
        });
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
        repository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        repository.deleteById(id);
    }

    public Optional<Transaction> findTransaction(UUID id) {
        return repository.findById(id);
    }

    public List<Transaction> getTransactions() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Transaction::getDate))
                .toList();
    }

    public void updateTransaction(Transaction transaction) {
        repository.update(transaction);
    }
}
