package com.devilish.granja.repository;

import com.devilish.granja.dto.response.SellerRankingResponseDTO;
import com.devilish.granja.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByCpf(String cpf);
    Optional<Seller> findByRegistration(String registration);
}
