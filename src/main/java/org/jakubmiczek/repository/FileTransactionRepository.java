package org.jakubmiczek.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jakubmiczek.model.Transaction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileTransactionRepository implements TransactionRepository {

    private final ObjectMapper objectMapper;
    private final File file;

    public FileTransactionRepository(String fileName) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.file = new File(fileName);
    }

    @Override
    public void save(Transaction transaction) {
        List<Transaction> transactions = findAll();
        transactions.removeIf(t -> t.getId().equals(transaction.getId()));
        transactions.add(transaction);
        writeAll(transactions);
    }

    @Override
    public List<Transaction> findAll() {
        if(!file.exists()) return new ArrayList<>();
        try {
            return objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("[SYSTEM]: Cannot read transactions file", e);
        }
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return findAll().stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    @Override
    public void update(Transaction transaction) {
        save(transaction);
    }

    @Override
    public void deleteById(UUID id) {
        List<Transaction> transactions = findAll();
        transactions.removeIf(transaction -> transaction.getId().equals(id));
        writeAll(transactions);
    }

    private void writeAll(List<Transaction> transactions) {
        try {
            objectMapper.writeValue(file, transactions);
        } catch (IOException e) {
            throw new RuntimeException("[SYSTEM]: Cannot write transactions to file", e);
        }
    }
}
