package io.icarus7.voyage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@Builder
@Entity
@RequiredArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private String userId;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @OneToMany(targetEntity = Voyage.class, mappedBy = "creator")
    private List<Voyage> createdVoyages;
    @OneToMany(targetEntity = Reservation.class, mappedBy = "reservedBy")
    private List<Reservation> reservations;
}
