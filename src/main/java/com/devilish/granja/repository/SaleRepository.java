package com.devilish.granja.repository;

import com.devilish.granja.entities.Sale;
import com.devilish.granja.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findBySeller(Seller seller);
}
