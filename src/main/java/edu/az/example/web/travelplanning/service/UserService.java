package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.enums.Gender;
import edu.az.example.web.travelplanning.model.dto.UserDto;
import edu.az.example.web.travelplanning.model.dto.UserPatchDto;
import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.mapper.UserMapper;
import edu.az.example.web.travelplanning.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(EntityNotFoundException::new);
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
        if (userDto == null) {
            throw new IllegalArgumentException();
        }
        User user = userMapper.toUser(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    public UserDto update(Long id, UserDto userDto) {
        if (id == null || userDto == null) {
            throw new IllegalArgumentException();
        }
        User user = userRepository.findById(id).
                orElseThrow(EntityNotFoundException::new);
        userMapper.toUserEntity(user, userDto);
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    public UserDto partialUpdate(Long id, UserPatchDto userDto) {
        if (id == null || userDto == null) {
            throw new IllegalArgumentException();
        }
        User user = userRepository.findById(id).
                orElseThrow(EntityNotFoundException::new);

        userMapper.applyPatch(user, userDto);
        return userMapper.toUserDto(userRepository.save(user));
    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        userRepository.deleteById(id);
    }

}
