package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.enums.Gender;
import edu.az.example.web.travelplanning.dto.UserDto;
import edu.az.example.web.travelplanning.dto.UserPatchDto;
import edu.az.example.web.travelplanning.exception.custom.RoleNotFoundException;
import edu.az.example.web.travelplanning.exception.custom.UserNotFoundException;
import edu.az.example.web.travelplanning.model.entity.Role;
import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.mapper.UserMapper;
import edu.az.example.web.travelplanning.repository.RoleRepository;
import edu.az.example.web.travelplanning.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(Transactional.TxType.REQUIRED)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    private static final String DEFAULT_ROLE = "USER";

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<UserDto> findByFirstname(String username) {
        return userRepository.findByName(username)
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    public List<UserDto> findByAge(Integer age) {
        return userRepository.findAllByAge(age)
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    public List<UserDto> findByGender(Gender gender) {
        return userRepository.findAllByGender(gender)
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }


    public UserDto create(UserDto userDto) {
        User user = userMapper.toUserEntity(userDto);
        Role role = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new RoleNotFoundException("No such a role"));

        user.setRole(role);
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    public UserDto update(Long id, UserDto userDto) {
        User user = findUserEntityById(id);
        userMapper.updateUserEntity(user, userDto);
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    public UserDto partialUpdate(Long id, UserPatchDto userDto) {
        User user = findUserEntityById(id);

        userMapper.applyPatch(user, userDto);
        return userMapper.toUserDto(userRepository.save(user));
    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public UserDto assignRole(Long userId, Long roleId) {
        User user = findUserEntityById(userId);
        Role role = findRoleEntityById(roleId);

        user.setRole(role);
        return userMapper.toUserDto(userRepository.save(user));
    }

    private Role findRoleEntityById(Long roleId) {
        return roleRepository.findById(roleId).
                orElseThrow(() -> new RoleNotFoundException(roleId));
    }

    private User findUserEntityById(Long userId) {
        return userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException(userId));
    }
}
