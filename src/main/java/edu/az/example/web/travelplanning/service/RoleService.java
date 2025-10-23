package edu.az.example.web.travelplanning.service;

import edu.az.example.web.travelplanning.dto.RoleDto;
import edu.az.example.web.travelplanning.exception.custom.RoleNotFoundException;
import edu.az.example.web.travelplanning.mapper.RoleMapper;
import edu.az.example.web.travelplanning.model.entity.Role;
import edu.az.example.web.travelplanning.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional(readOnly = true)
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id).
                orElseThrow(() -> new RoleNotFoundException(id));
        return roleMapper.toRoleDto(role);
    }

    @Transactional(readOnly = true)
    public List<RoleDto> getRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleDto)
                .toList();
    }

    @Transactional
    public RoleDto createRole(RoleDto roleDto) {
        Role role = roleMapper.toRole(roleDto);
        return roleMapper.toRoleDto(roleRepository.save(role));
    }

    @Transactional
    public RoleDto updateRole(Long id,RoleDto roleDto) {
        Role role = roleRepository.findById(id).
                orElseThrow(() -> new RoleNotFoundException(id));
        roleMapper.updateRoleEntity(roleDto, role);
        return roleMapper.toRoleDto(roleRepository.save(role));
    }

    @Transactional
    public void deleteRole(Long id) {
        roleRepository.findById(id).
                orElseThrow(() -> new RoleNotFoundException(id));

        roleRepository.deleteById(id);
    }
}
