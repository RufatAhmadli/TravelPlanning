package edu.az.example.web.travelplanning.dto;

import edu.az.example.web.travelplanning.enums.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;

    @NotNull
    private NotificationType type;

    @NotNull
    private String message;

    private boolean read;

    private LocalDateTime scheduledTime;
    private UserDto user;
}
