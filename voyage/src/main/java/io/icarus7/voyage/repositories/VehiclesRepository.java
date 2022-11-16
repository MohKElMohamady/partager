package io.icarus7.voyage.repositories;

import io.icarus7.voyage.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiclesRepository extends JpaRepository<Vehicle, String> {
}
