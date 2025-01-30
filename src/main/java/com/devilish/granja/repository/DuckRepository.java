package com.devilish.granja.repository;

import com.devilish.granja.entities.Duck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuckRepository extends JpaRepository<Duck, Long> {
}
