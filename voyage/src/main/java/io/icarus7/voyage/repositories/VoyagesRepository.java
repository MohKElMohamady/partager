package io.icarus7.voyage.repositories;

import io.icarus7.voyage.models.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoyagesRepository extends JpaRepository<Voyage, String> {

}
