package model;

import org.jakubmiczek.model.MonthlyBudget;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class MothlyBudgetTest {
    @Test
    void shouldThrowExceptionWhenYearIsLowerThanZero() {
        assertThatThrownBy(() -> MonthlyBudget.builder().year(-1).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[SYSTEM]: Year must be between 0 and now");
    }

    @Test
    void shouldThrowExceptionWhenYearIsGraterThanCurrentYear() {
        assertThatThrownBy(() -> MonthlyBudget.builder().year(LocalDate.now().plusYears(1).getYear()).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[SYSTEM]: Year must be between 0 and now");
    }

    @Test
    void shouldThrowExceptionWhenMonthIsLowerThanZero() {
        assertThatThrownBy(() -> MonthlyBudget.builder().year(2026).month(-2).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[SYSTEM]: Month must be between 0 and 12");
    }

    @Test
    void shouldThrowExceptionWhenMonthIsGreaterThanTwelve() {
        assertThatThrownBy(() -> MonthlyBudget.builder().year(2026).month(15).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[SYSTEM]: Month must be between 0 and 12");
    }

    @Test
    void shouldThrowExceptionWhenLimitIsLowerThanZero() {
        assertThatThrownBy(() -> MonthlyBudget.builder().year(2026).month(5).limit(BigDecimal.valueOf(-20)).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[SYSTEM]: Limit must be greater than 0");
    }

    @Test
    void shouldThrowExceptionWhenLimitIsNull() {
        assertThatThrownBy(() -> MonthlyBudget.builder().year(2026).month(5).limit(null).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[SYSTEM]: Limit must be greater than 0");
    }

    @Test
    void shouldCreateCorrectMonthlyBudget() {
        MonthlyBudget monthlyBudget = MonthlyBudget.builder().year(2026).month(5).limit(BigDecimal.valueOf(5000)).build();
        assertThat(monthlyBudget.getMonth()).isEqualTo(5);
        assertThat(monthlyBudget.getYear()).isEqualTo(2026);
        assertThat(monthlyBudget.getLimit()).isEqualTo(BigDecimal.valueOf(5000));
    }
}
