package io.icarus7.voyage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@Builder
@Data
@Entity
@RequiredArgsConstructor
@Table(name = "voyages")
public class Voyage {
    @Id
    private UUID voyageId;
    @ManyToOne
    private User creator;
    private String origin;
    private String destination;
    private Timestamp departure;
    private int maximumCapacity;
    private String description;
    @OneToMany(targetEntity = Reservation.class, mappedBy = "voyageReserved")
    private List<Reservation> reservations;
    private BigDecimal cost;
    @Enumerated(EnumType.STRING)
    private VoyageStatus status;
    @OneToOne
    private Vehicle vehicle;

}
