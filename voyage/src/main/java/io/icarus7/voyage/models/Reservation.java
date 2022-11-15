package io.icarus7.voyage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;
@AllArgsConstructor
@Builder
@Data
@Entity
@RequiredArgsConstructor
@Table(name = "reservations")
public class Reservation {
    @Id
    private UUID reservationId;
    private int reservedSeats;
    @ManyToOne
    private User reservedBy;
    @ManyToOne
    private Voyage voyageReserved;
}
