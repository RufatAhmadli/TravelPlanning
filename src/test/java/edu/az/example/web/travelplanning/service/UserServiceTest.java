package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.TestAuditable;
import edu.az.example.web.travelplanning.dto.UserPatchDto;
import edu.az.example.web.travelplanning.exception.custom.RoleNotFoundException;
import edu.az.example.web.travelplanning.exception.custom.UserNotFoundException;
import edu.az.example.web.travelplanning.mapper.UserMapper;
import edu.az.example.web.travelplanning.dto.AddressDto;
import edu.az.example.web.travelplanning.dto.UserDto;
import edu.az.example.web.travelplanning.model.entity.Address;
import edu.az.example.web.travelplanning.model.entity.Role;
import edu.az.example.web.travelplanning.model.entity.User;
import edu.az.example.web.travelplanning.repository.RoleRepository;
import edu.az.example.web.travelplanning.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;
import java.util.Optional;


import static edu.az.example.web.travelplanning.enums.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest implements TestAuditable {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;
    private UserDto updatedDto;
    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(1L)
                .name("USER")
                .build();

        user = User.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .age(13)
                .gender(MALE)
                .role(role)
                .password("123")
                .addresses(List.of(Address.builder().street("123 Main St").build()))
                .build();
        assignAuditFields(user);

        userDto = UserDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .age(13)
                .gender(MALE)
                .password("123")
                .addresses(List.of(AddressDto.builder().street("123 Main St").build()))
                .build();

        updatedDto = UserDto.builder()
                .id(1L)
                .firstName("Jack")
                .lastName("Doe")
                .age(14)
                .gender(MALE)
                .password("124")
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
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto foundUserDto = userService.findById(1L);
        assertEquals(user.getId(), foundUserDto.getId());
        assertEquals(user.getName(), foundUserDto.getFirstName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdShouldThrowUserNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findById(99L));
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
        when(userMapper.toUserEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(encoder.encode(anyString())).thenReturn("encoded");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.create(userDto);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User expected = captor.getValue();
        assertNotNull(result);
        assertEquals("encoded", expected.getPassword());
        assertEquals(expected.getName(), result.getFirstName());
        assertEquals(expected.getAge(), result.getAge());

        verify(userMapper, times(1)).toUserDto(user);
        verify(userMapper, times(1)).toUserEntity(userDto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreate_RoleNotFound() {
        when(userMapper.toUserEntity(userDto)).thenReturn(user);
        when(encoder.encode(anyString())).thenReturn("encoded");
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> userService.create(userDto));
        verifyNoInteractions(userRepository);
    }

    @Test
    void testUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateUserEntity(user, updatedDto);
        when(userMapper.toUserDto(user)).thenReturn(updatedDto);

        UserDto result = userService.update(1L, updatedDto);

        assertEquals("Jack", result.getFirstName());
        assertEquals(14, result.getAge());
        verify(userRepository).save(user);
    }

    @Test
    void testUpdate_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.update(999L, updatedDto));
    }

    @Test
    void testPartialUpdate() {
        UserPatchDto patchDto = new UserPatchDto();
        patchDto.setFirstName("Updated");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).applyPatch(user, patchDto);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.partialUpdate(1L, patchDto);
        assertNotNull(result);
        verify(userMapper).applyPatch(user, patchDto);
    }

    @Test
    void testDelete() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDelete_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.delete(1L));
    }

    @Test
    void testDelete_NullId() {
        assertThrows(IllegalArgumentException.class, () -> userService.delete(null));
        verifyNoInteractions(userRepository);
    }

    @Test
    void testAssignRole() {
        Role newRole = Role.builder().id(2L).name("ADMIN").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(newRole));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.assignRole(1L, 2L);

        assertNotNull(result);
        verify(roleRepository).findById(2L);
        verify(userRepository).save(user);
    }

    @Test
    void testAssignRole_RoleNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundException.class, () -> userService.assignRole(1L, 99L));
    }
}
