package org.example.entitie;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@ToString
@EqualsAndHashCode


public class Customer {
    private Long id;
    private String login;
    private String password;
}
