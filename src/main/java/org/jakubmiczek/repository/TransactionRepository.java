package org.jakubmiczek.repository;

import org.jakubmiczek.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    void save(Transaction transaction);
    List<Transaction> findAll();
    Optional<Transaction> findById(UUID id);
    void update(Transaction transaction);
    void deleteById(UUID id);
}
