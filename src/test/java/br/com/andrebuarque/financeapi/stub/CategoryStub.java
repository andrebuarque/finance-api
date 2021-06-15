package br.com.andrebuarque.financeapi.stub;

import br.com.andrebuarque.financeapi.entity.Category;
import br.com.andrebuarque.financeapi.entity.TransactionType;

public class CategoryStub {
    public static Category getCategory() {
        final Category category = new Category();
        category.setId("asdjhg12r");
        category.setName("Category name");
        category.setPattern("/market/");
        category.setType(TransactionType.OUTCOME);
        category.setUser(UserStub.getUser());
        return category;
    }
}
