package org.jakubmiczek.repository;

import org.jakubmiczek.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository {
    void save(Transaction transaction);
    List<Transaction> findAll();
    void deleteById(UUID id);
}
