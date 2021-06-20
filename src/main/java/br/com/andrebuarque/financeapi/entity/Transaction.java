package br.com.andrebuarque.financeapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Document
public class Transaction {
    @Id
    private String id;
    private String description;
    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate date;
    @Positive
    @NotNull
    private Double value;
    @NotNull
    private TransactionType type;
    @NotNull
    private TransactionStatus status;
    @NotNull
    private User user;
    private Category category;

    public static class Builder {
        private static Transaction transaction;

        Builder() {
            transaction = new Transaction();
        }

        public Builder id(String id) {
            transaction.setId(id);
            return this;
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

        public Builder user(User user) {
            transaction.setUser(user);
            return this;
        }

        public Builder category(Category category) {
            transaction.setCategory(category);
            return this;
        }

        public Transaction build() {
            return transaction;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(final TransactionStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
