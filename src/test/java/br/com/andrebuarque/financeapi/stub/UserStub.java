package br.com.andrebuarque.financeapi.stub;

import br.com.andrebuarque.financeapi.entity.User;

public class UserStub {
    public static User getUser() {
        return User.builder()
            .id("377492af-03c2-46e3-b3d7-f824ebf08721")
            .username("username")
            .name("name")
            .lastname("lastname")
            .email("name@email.com")
            .build();
    }
}
