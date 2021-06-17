package br.com.andrebuarque.financeapi.dto;

import br.com.andrebuarque.financeapi.entity.TransactionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CategoryDto {
    @NotBlank
    private String name;
    @NotNull
    private TransactionType type;
    private String pattern;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(final TransactionType type) {
        this.type = type;
    }
}
