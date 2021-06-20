package br.com.andrebuarque.financeapi.stub;

import br.com.andrebuarque.financeapi.dto.TransactionDto;
import br.com.andrebuarque.financeapi.entity.TransactionStatus;
import br.com.andrebuarque.financeapi.entity.TransactionType;

import java.time.LocalDate;

public class TransactionStubDto {
    public static TransactionDto getTransactionDto() {
        return TransactionDto.builder()
            .type(TransactionType.OUTCOME)
            .status(TransactionStatus.DONE)
            .value(100.0)
            .date(LocalDate.now())
            .description("description")
            .categoryId(CategoryStub.getCategory().getId())
            .build();
    }
}
