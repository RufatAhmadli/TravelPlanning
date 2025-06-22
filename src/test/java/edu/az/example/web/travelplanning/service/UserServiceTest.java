package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.mapper.UserMapper;
import edu.az.example.web.travelplanning.model.dto.AddressDto;
import edu.az.example.web.travelplanning.model.dto.UserDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;


import static edu.az.example.web.travelplanning.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;
    private UserDto updatedDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .age(13)
                .gender(MALE)
                .addresses(List.of(Address.builder().street("123 Main St").build()))
                .build();
        userDto = UserDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .age(13)
                .gender(MALE)
                .addresses(List.of(AddressDto.builder().street("123 Main St").build()))
                .build();
        updatedDto = UserDto.builder()
                .id(1L)
                .firstName("Jack")
                .lastName("Doe")
                .age(14)
                .gender(MALE)
                .build();
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        List<UserDto> all = userService.findAll();
        assertNotNull(all);
        assertEquals(1, all.size());
        assertEquals(userDto.getId(), all.get(0).getId());
        assertEquals(userDto.getFirstName(), all.get(0).getFirstName());
        assertEquals(userDto.getLastName(), all.get(0).getLastName());

    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto foundUserDto = userService.findById(1L);
        assertEquals(user.getId(), foundUserDto.getId());
        assertEquals(user.getName(), foundUserDto.getFirstName());
    }

    @Test
    void testFindByIdShouldThrowEntityNotFoundException() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(java.util.Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.findById(id)); //!!
    }

    @Test
    void testFindByFirstname() {
        when(userRepository.findByName(user.getName())).thenReturn(List.of(user));
        when(userMapper.toUserDto(user)).thenReturn((userDto));

        List<UserDto> result = userService.findByFirstname("John");
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());

        assertEquals(user.getName(), userDto.getFirstName());
        verify(userRepository).findByName(user.getName());
        verify(userMapper).toUserDto(user);
    }

    @Test
    void testFindByAge() {
        when(userRepository.findAllByAge(13)).thenReturn(List.of(user));
        when(userMapper.toUserDto(user)).thenReturn((userDto));

        List<UserDto> result = userService.findByAge(user.getAge());
        assertEquals(1, result.size());
        assertEquals(13, result.get(0).getAge());

        verify(userRepository).findAllByAge(13);
    }

    @Test
    void testFindByGender() {
        when(userRepository.findAllByGender(MALE)).thenReturn(List.of(user));
        when(userMapper.toUserDto(user)).thenReturn((userDto));

        List<UserDto> result = userService.findByGender(MALE);
        assertEquals(1, result.size());
        assertEquals(MALE, result.get(0).getGender());

        verify(userRepository).findAllByGender(MALE);
    }

    @Test
    void testCreate() {
        when(userMapper.toUser(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.create(userDto);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User expected = captor.getValue();
        assertNotNull(result);
        assertEquals(expected.getName(), result.getFirstName());
        assertEquals(expected.getAge(), result.getAge());

        verify(userMapper, times(1)).toUserDto(user);
        verify(userMapper, times(1)).toUser(userDto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when creating user with null DTO")
    void testNullUserDto() {
        assertThrows(IllegalArgumentException.class, () -> userService.create(null));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdate() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        doNothing().when(userMapper).toUserEntity(user, updatedDto);
        when(userMapper.toUserDto(user)).thenReturn((updatedDto));

        UserDto res = userService.update(1L, updatedDto);

        assertEquals("Jack", res.getFirstName());
        assertEquals(1L, res.getId());
        assertEquals(14, res.getAge());
    }

    @Test
    @DisplayName("Should throw exception when dto is not found")
    void testUpdateNonexistentUser() {
        Long nonExistedId = 10L;
        when(userRepository.findById(nonExistedId)).thenReturn(java.util.Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.update(nonExistedId, updatedDto));
    }

    @Test
    @DisplayName("Should throw exception when id is null")
    void testUpdateNullId() {
        assertThrows(IllegalArgumentException.class, () -> userService.update(null, updatedDto));
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Should throw exception when dto is null")
    void testUpdateNullDto() {
        assertThrows(IllegalArgumentException.class, () -> userService.update(1L, null));
        verifyNoInteractions(userRepository);
    }


    @Test
    @DisplayName("Should delete existing user")
    void testDelete() {
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when entity is not found")
    void testDeleteEntityNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> userService.delete(1L));
    }

    @Test
    @DisplayName("Should throw exception for null ID")
    void shouldThrowExceptionForNullId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.delete(null)
        );

        verifyNoInteractions(userRepository);
    }
}