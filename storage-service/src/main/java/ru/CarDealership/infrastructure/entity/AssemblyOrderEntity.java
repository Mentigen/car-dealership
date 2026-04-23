package ru.CarDealership.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.CarDealership.domain.assembly.AssemblyOrderStatus;

import java.util.UUID;

@Entity
@Table(name = "assembly_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyOrderEntity extends BaseEntity {
    @Column(nullable = false)
    private UUID sourceOrderId;

    @Column(nullable = false)
    private UUID traceId;

    @Column(nullable = false)
    private String orderType;

    @Column
    private UUID carId;

    @Column
    private UUID carConfigurationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssemblyOrderStatus status;

    @Column
    private String failReason;
}
