package service;

import org.jakubmiczek.exceptions.BudgetException;
import org.jakubmiczek.exceptions.TransactionNotFoundException;
import org.jakubmiczek.model.Transaction;
import org.jakubmiczek.model.TransactionCategory;
import org.jakubmiczek.repository.TransactionRepository;
import org.jakubmiczek.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    public void shouldSetMonthlyBudget() {
        BigDecimal budget = BigDecimal.valueOf(5000);

        transactionService.setMonthlyBudget(budget);

        assertThat(transactionService.getMonthlyBudget().getLimit()).isEqualTo(budget);
    }

    @Test
    public void shouldExceedMonthlyBudgetLimit() {
        transactionService.setMonthlyBudget(BigDecimal.valueOf(5000));

        Transaction newTransaction = Transaction.builder().category(TransactionCategory.FOOD).amount(BigDecimal.valueOf(6000)).date(LocalDate.now()).build();

        when(transactionRepository.findAll()).thenReturn(List.of(newTransaction));

        assertThatThrownBy(() -> transactionService.addTransaction(newTransaction))
                .isInstanceOf(BudgetException.class)
                .hasMessage(String.format("[SYSTEM]: Monthly limit was %s and was exceeded", transactionService.getMonthlyBudget().getLimit()));
    }

    @Test
    public void shouldThrowTransactionNotFoundExceptionWhenIdDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(transactionRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> transactionService.deleteTransaction(id))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessage("Transaction with id " + id + " not found");
    }

    @Test
    public void shouldSaveTransaction() {
        Transaction newTransaction = Transaction.builder().category(TransactionCategory.FOOD).amount(BigDecimal.valueOf(50)).date(LocalDate.now()).build();

        transactionService.addTransaction(newTransaction);

        verify(transactionRepository, times(1)).save(newTransaction);
    }

    @Test
    public void shouldDeleteTransaction() {
        UUID id = UUID.randomUUID();

        Transaction newTransaction = Transaction.builder().id(id).category(TransactionCategory.FOOD).amount(BigDecimal.valueOf(6000)).date(LocalDate.now()).build();
        transactionService.addTransaction(newTransaction);

        when(transactionRepository.findById(id)).thenReturn(Optional.of(newTransaction));

        transactionService.deleteTransaction(id);

        verify(transactionRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldFindTransactionById() {
        UUID id = UUID.randomUUID();

        Transaction newTransaction = Transaction.builder().id(id).category(TransactionCategory.FOOD).amount(BigDecimal.valueOf(6000)).date(LocalDate.now()).build();
        transactionService.addTransaction(newTransaction);

        when(transactionRepository.findById(id)).thenReturn(Optional.of(newTransaction));

        assertThat(transactionService.findTransaction(id)).isEqualTo(Optional.of(newTransaction));
    }

    @Test
    public void shouldReturnListOfAllTransactions() {
        Transaction newTransaction1 = Transaction.builder().category(TransactionCategory.FOOD).amount(BigDecimal.valueOf(6000)).date(LocalDate.now()).build();
        Transaction newTransaction2 = Transaction.builder().category(TransactionCategory.ENTERTAINMENT).amount(BigDecimal.valueOf(200)).date(LocalDate.now()).build();
        Transaction newTransaction3 = Transaction.builder().category(TransactionCategory.SHOPPING).amount(BigDecimal.valueOf(500)).date(LocalDate.now()).build();

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(newTransaction1, newTransaction2, newTransaction3));

        assertThat(transactionService.getTransactions()).hasSize(3);
    }

    @Test
    public void shouldUpdateTransaction() {
        UUID id = UUID.randomUUID();
        Transaction oldTransaction = Transaction.builder().id(id).category(TransactionCategory.FOOD).amount(BigDecimal.valueOf(50)).date(LocalDate.now()).build();

        transactionService.addTransaction(oldTransaction);

        Transaction newTransaction = Transaction.builder().id(id).category(TransactionCategory.ENTERTAINMENT).amount(BigDecimal.valueOf(500)).date(LocalDate.now()).build();

        when(transactionRepository.findById(id)).thenReturn(Optional.of(newTransaction));

        transactionService.updateTransaction(newTransaction);

        assertThat(transactionService.findTransaction(id)).isEqualTo(Optional.of(newTransaction));
    }
}
