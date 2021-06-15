package br.com.andrebuarque.financeapi.validation;

import javax.validation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BeanValidator {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static void validate(Object obj) {
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);

        if (!violations.isEmpty()) {
            List<String> messages = new ArrayList<>();
            violations.stream()
                .map(violation -> String.format("'%s' %s", violation.getPropertyPath(), violation.getMessage()))
                .forEach(messages::add);
            throw new ConstraintViolationException("Error occurred: " + messages, violations);
        }
    }
}
