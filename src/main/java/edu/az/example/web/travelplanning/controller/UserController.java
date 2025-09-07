package edu.az.example.web.travelplanning.controller;

import edu.az.example.web.travelplanning.enums.Gender;
import edu.az.example.web.travelplanning.model.dto.UserPatchDto;
import edu.az.example.web.travelplanning.validation.OnCreate;
import edu.az.example.web.travelplanning.model.dto.UserDto;
import edu.az.example.web.travelplanning.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/name/{name}")
    public List<UserDto> getByGender(@RequestParam Gender gender) {
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

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partialUpdate(@PathVariable Long id,
                                                 @RequestBody @Valid UserPatchDto userPatchDto) {
        UserDto userDto = userService.partialUpdate(id, userPatchDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

}
