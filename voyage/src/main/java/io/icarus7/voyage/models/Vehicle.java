package io.icarus7.voyage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
@Entity
@RequiredArgsConstructor
@Table(name = "vehicles")
public class Vehicle {
    @Id
    private String vehicleId;
    private String name;
    @Enumerated(EnumType.STRING)
    private Icon icon;
}
