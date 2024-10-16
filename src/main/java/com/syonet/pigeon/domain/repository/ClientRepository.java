package com.syonet.pigeon.domain.repository;

import com.syonet.pigeon.domain.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    List<Client> findByDateBirth(LocalDate date);

    List<Client> findByDateBirthNot(LocalDate date);

}
