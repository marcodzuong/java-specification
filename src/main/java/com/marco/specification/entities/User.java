package com.marco.specification.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_codes", columnDefinition = "varchar(500)")
    private String roleCodes;

    @Column(name = "user_name", columnDefinition = "varchar(100)")
    private String userName;

    @Column(name = "password", columnDefinition = "varchar(100)")
    @JsonIgnore
    private String password;

    @Column(name = "salt", columnDefinition = "varchar(100)")
    @JsonIgnore
    private String salt;

}
