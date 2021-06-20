package br.com.andrebuarque.financeapi.dto;

import br.com.andrebuarque.financeapi.entity.TransactionStatus;
import br.com.andrebuarque.financeapi.entity.TransactionType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

public class TransactionDto {
    private String description;
    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;
    @Positive
    @NotNull
    private Double value;
    @NotNull
    private TransactionType type;
    @NotNull
    private TransactionStatus status;
    private String categoryId;

    public static class Builder {
        private static TransactionDto transaction;

        Builder() {
            transaction = new TransactionDto();
        }

        public Builder description(String description) {
            transaction.setDescription(description);
            return this;
        }

        public Builder date(LocalDate date) {
            transaction.setDate(date);
            return this;
        }

        public Builder value(Double value) {
            transaction.setValue(value);
            return this;
        }

        public Builder type(TransactionType type) {
            transaction.setType(type);
            return this;
        }

        public Builder status(TransactionStatus status) {
            transaction.setStatus(status);
            return this;
        }

        public Builder categoryId(String categoryId) {
            transaction.setCategoryId(categoryId);
            return this;
        }

        public TransactionDto build() {
            return transaction;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(final Double value) {
        this.value = value;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(final TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(final TransactionStatus status) {
        this.status = status;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final String categoryId) {
        this.categoryId = categoryId;
    }
}
