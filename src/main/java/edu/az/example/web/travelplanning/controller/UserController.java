package edu.az.example.web.travelplanning.controller;

import edu.az.example.web.travelplanning.enums.Gender;
import edu.az.example.web.travelplanning.dto.UserPatchDto;
import edu.az.example.web.travelplanning.validation.OnCreate;
import edu.az.example.web.travelplanning.dto.UserDto;
import edu.az.example.web.travelplanning.service.UserService;
import edu.az.example.web.travelplanning.validation.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    public UserDto getById(@Positive @PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/gender/{gender}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getByGender(@PathVariable Gender gender) {
        return userService.findByGender(gender);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto create(@RequestBody @Validated(OnCreate.class) UserDto userDto) {
        return userService.create(userDto);
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> assignRole(@Positive @PathVariable Long userId,
                                              @Positive @RequestParam Long roleId) {
        return ResponseEntity.ok(userService.assignRole(userId, roleId));
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    public UserDto update(@Positive @PathVariable Long id,
                          @RequestBody @Validated(OnUpdate.class) UserDto userDto) {
        return userService.update(id, userDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    public ResponseEntity<UserDto> partialUpdate(@Positive @PathVariable Long id,
                                                 @RequestBody @Valid UserPatchDto userPatchDto) {
        UserDto userDto = userService.partialUpdate(id, userPatchDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    public void delete(@Positive @PathVariable Long id) {
        userService.delete(id);
    }

}
