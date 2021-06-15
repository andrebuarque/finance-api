package br.com.andrebuarque.financeapi.stub;

import br.com.andrebuarque.financeapi.dto.CategoryDto;
import br.com.andrebuarque.financeapi.entity.TransactionType;

public class CategoryDtoStub {
    public static CategoryDto getCategory() {
        final CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Category name");
        categoryDto.setType(TransactionType.INCOME);
        categoryDto.setPattern("/pattern/");
        return categoryDto;
    }
}
