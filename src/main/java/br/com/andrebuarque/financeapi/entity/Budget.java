package br.com.andrebuarque.financeapi.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Document
public class Budget {
    @Id
    private String id;
    @NotNull
    @Positive
    private Short month;
    @NotNull
    @Positive
    private Short year;
    @NotNull
    private Category category;
    @NotNull
    @Positive
    private Double value;
    @NotNull
    private User user;
}
