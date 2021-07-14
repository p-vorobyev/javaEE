package ru.voroby.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.voroby.validation.UserAgeValidation;

import javax.validation.Payload;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Payload {

    private Integer id;

    @NotBlank
    private String name;

    @UserAgeValidation(message = "Age must be > 5", payload = User.class)
    private Integer age;

}
