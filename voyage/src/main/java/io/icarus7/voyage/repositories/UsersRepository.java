package io.icarus7.voyage.repositories;

import io.icarus7.voyage.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersRepository extends JpaRepository<User, String> {

}
