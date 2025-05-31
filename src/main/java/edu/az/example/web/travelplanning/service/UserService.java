package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.model.dto.AddressDto;
import edu.az.example.web.travelplanning.model.dto.UserDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.model.mapper.AddressMapper;
import edu.az.example.web.travelplanning.model.mapper.UserMapper;
import edu.az.example.web.travelplanning.repository.AddressRepository;
import edu.az.example.web.travelplanning.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(Transactional.TxType.REQUIRED)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       AddressMapper addressMapper, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
    }

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

    public List<UserDto> findByGender(String gender) {
        return userRepository.findAllByGender(gender)
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    public List<AddressDto> findAddressesByUserId(Long userId) {
        List<Address> addresses = addressRepository.findAllByUserId(userId);
        return addresses.stream()
                .map(addressMapper::toDto)
                .toList();
    }

    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        // Manually assign 'user' to each Address to set the bidirectional relationship
        if (user.getAddresses() != null) {
            for (var address : user.getAddresses()) {
                address.setUser(user);
            }
        }

        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    public UserDto update(Long id,UserDto userDto) {
        User user = userRepository.findById(id).
                orElseThrow(EntityNotFoundException::new);
        userMapper.toUserEntity(user,userDto);
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
