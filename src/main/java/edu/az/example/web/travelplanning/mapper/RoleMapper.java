package edu.az.example.web.travelplanning.mapper;

import edu.az.example.web.travelplanning.dto.RoleDto;
import edu.az.example.web.travelplanning.model.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface RoleMapper {

    RoleDto toRoleDto(Role role);

    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Role toRole(RoleDto roleDto);


    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateRoleEntity(RoleDto roleDto, @MappingTarget Role role);
}
