package ru.CarDealership.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.CarDealership.domain.assembly.AssemblyOrderStatus;

@Data
public class UpdateAssemblyOrderStatusRequest {
    @NotNull
    private AssemblyOrderStatus status;
    private String failReason;
}
