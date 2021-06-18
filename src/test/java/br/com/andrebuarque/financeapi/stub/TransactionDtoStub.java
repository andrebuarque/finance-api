package br.com.andrebuarque.financeapi.stub;

import br.com.andrebuarque.financeapi.dto.TransactionDto;
import br.com.andrebuarque.financeapi.entity.TransactionStatus;
import br.com.andrebuarque.financeapi.entity.TransactionType;

import java.time.LocalDate;

public class TransactionDtoStub {
    public static TransactionDto getTransactionDto() {
        return TransactionDto.builder()
            .type(TransactionType.INCOME)
            .status(TransactionStatus.PENDING)
            .value(150.0)
            .date(LocalDate.now().plusDays(1))
            .description("new description")
            .categoryId(CategoryStub.getCategory().getId())
            .build();
    }
}
