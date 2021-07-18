package ru.voroby.entity;

import lombok.Data;
import ru.voroby.validation.UserAgeValidation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;

@Data
@NamedQueries({
        @NamedQuery(name = User.BY_NAME, query = "select u from User u where u.name = :name"),
        @NamedQuery(name = User.ALL, query = "select u from User u")
})
@Entity
@Table(name = "users")
public class User implements Payload {

    public static final String BY_NAME = "User.getByName";
    public static final String ALL = "User.getAll";

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    private Integer id;

    @Column(name = "name", length = 60)
    @NotBlank
    private String name;

    @Column(name = "age")
    @UserAgeValidation(message = "Age must be > 5", payload = User.class)
    private Integer age;

    public User() {
    }

    public User(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User(String name, Integer age) {
        this(null, name, age);
    }
}
