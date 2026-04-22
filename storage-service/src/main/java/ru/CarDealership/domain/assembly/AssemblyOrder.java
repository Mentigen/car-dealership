package ru.CarDealership.domain.assembly;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssemblyOrder {
    private UUID id;
    private UUID sourceOrderId;
    private UUID traceId;
    private String orderType;
    private UUID carId;
    private UUID carConfigurationId;
    private AssemblyOrderStatus status;
    private String failReason;
    private Instant createdAt;
    private Instant updatedAt;
}
