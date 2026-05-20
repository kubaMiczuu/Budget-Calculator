package model;

import org.jakubmiczek.model.Transaction;
import org.jakubmiczek.model.TransactionCategory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class TransactionTest {
    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        assertThatThrownBy(() -> Transaction.builder().amount(BigDecimal.valueOf(-1)).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[SYSTEM]: Amount cannot be negative or null");
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNull() {
        assertThatThrownBy(() -> Transaction.builder().amount(null).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[SYSTEM]: Amount cannot be negative or null");
    }

    @Test
    void shouldThrowExceptionWhenDateIsAfterNow() {
        assertThatThrownBy(() -> Transaction.builder().date(LocalDate.now().plusDays(1)).amount(BigDecimal.valueOf(50)).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[SYSTEM]: Date must be before now");
    }

    @Test
    void shouldThrowExceptionWhenDateIsNull() {
        assertThatThrownBy(() -> Transaction.builder().date(null).amount(BigDecimal.valueOf(50)).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[SYSTEM]: Date must be before now");
    }

    @Test
    void shouldUseProvidedCategory() {
        TransactionCategory category = TransactionCategory.ENTERTAINMENT;

        Transaction transaction = Transaction.builder().category(category).date(LocalDate.now()).amount(BigDecimal.valueOf(50)).build();

        assertThat(transaction.getCategory()).isEqualTo(category);
    }

    @Test
    void shouldUseDefaultCategoryValueWhenCategoryIsNull() {
        Transaction transaction = Transaction.builder().category(null).date(LocalDate.now()).amount(BigDecimal.valueOf(50)).build();
        assertThat(transaction.getCategory()).isEqualTo(TransactionCategory.OTHER);
    }

    @Test
    void shouldUseProvidedId() {
        UUID id = UUID.randomUUID();

        Transaction transaction = Transaction.builder().id(id).category(TransactionCategory.INCOME).date(LocalDate.now()).amount(BigDecimal.valueOf(50)).build();

        assertThat(transaction.getId()).isEqualTo(id);
    }

    @Test
    void shouldGenerateRandomUUIDValueWhenIdIsNull() {
        Transaction transaction = Transaction.builder().category(TransactionCategory.INCOME).date(LocalDate.now()).amount(BigDecimal.valueOf(50)).build();
        assertThat(transaction.getId()).isNotNull();
    }

    @Test
    void shouldCreateCorrectTransaction() {
        Transaction transaction = Transaction.builder().amount(BigDecimal.valueOf(50)).date(LocalDate.now()).build();
        assertThat(transaction.getAmount()).isEqualTo(BigDecimal.valueOf(50));
        assertThat(transaction.getDate()).isEqualTo(LocalDate.now());
        assertThat(transaction.getCategory()).isEqualTo(TransactionCategory.OTHER);
    }

    @Test
    void shouldReturnCorrectIsExpenseValue() {
        Transaction transaction = Transaction.builder().category(TransactionCategory.FOOD).amount(BigDecimal.valueOf(50)).date(LocalDate.now()).build();
        assertThat(transaction.isExpense()).isTrue();
    }
}
