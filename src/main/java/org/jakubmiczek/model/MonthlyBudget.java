package org.jakubmiczek.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
public class MonthlyBudget {
    private final int year;
    private final int month;
    private final BigDecimal limit;

    public static class MonthlyBudgetBuilder {
        public MonthlyBudget build() {
            if(year <=0 || year > LocalDate.now().getYear()) {
                throw new IllegalArgumentException("[SYSTEM]: Year must be between 0 and now");
            }
            if(month <= 0 || month > 12) {
                throw new IllegalArgumentException("[SYSTEM]: Month must be between 0 and 12");
            }

            return new MonthlyBudget(year,month, limit);
        }
    }
}
