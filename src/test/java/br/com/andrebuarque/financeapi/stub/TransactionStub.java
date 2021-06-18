package br.com.andrebuarque.financeapi.stub;

import br.com.andrebuarque.financeapi.entity.Transaction;
import br.com.andrebuarque.financeapi.entity.TransactionStatus;
import br.com.andrebuarque.financeapi.entity.TransactionType;

import java.time.LocalDate;

public class TransactionStub {
    public static Transaction getTransaction() {
        return Transaction.builder()
            .id("id")
            .type(TransactionType.OUTCOME)
            .status(TransactionStatus.DONE)
            .value(100.0)
            .date(LocalDate.now())
            .description("description")
            .category(CategoryStub.getCategory())
            .user(UserStub.getUser())
            .build();
    }
}
