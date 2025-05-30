package edu.az.example.web.travelplanning.controller;

import edu.az.example.web.travelplanning.validation.OnCreate;
import edu.az.example.web.travelplanning.model.dto.UserDto;
import edu.az.example.web.travelplanning.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll() {
        return userService.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getByName(@PathVariable String name) {
        return userService.findByFirstname(name);
    }

    @GetMapping("/age/{age}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getByAge(@PathVariable Integer age) {
        return userService.findByAge(age);
    }

    @GetMapping("/gender")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getByGender(@RequestParam String gender) {
        return userService.findByGender(gender);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Validated(OnCreate.class) UserDto userDto) {
        return userService.create(userDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        return userService.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

}
