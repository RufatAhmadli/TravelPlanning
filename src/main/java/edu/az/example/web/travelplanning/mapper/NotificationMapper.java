package edu.az.example.web.travelplanning.mapper;

import edu.az.example.web.travelplanning.dto.NotificationDto;
import edu.az.example.web.travelplanning.model.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface NotificationMapper {
    NotificationDto toNotificationDto(Notification notification);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Notification toNotificationEntity(NotificationDto notificationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void toUpdateNotification(@MappingTarget Notification entity, NotificationDto dto);
}
