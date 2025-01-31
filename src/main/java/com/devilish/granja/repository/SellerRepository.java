package com.devilish.granja.repository;

import com.devilish.granja.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByCpf(String cpf);
    Optional<Seller> findByRegistration(String registration);
}
