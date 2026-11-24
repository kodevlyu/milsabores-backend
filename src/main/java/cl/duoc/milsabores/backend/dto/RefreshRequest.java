package cl.duoc.milsabores.backend.dto;
import jakarta.validation.constraints.*;
public record RefreshRequest(@NotBlank @Size(min=43,max=43) String refreshToken) {}
