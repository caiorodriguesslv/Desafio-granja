package com.devilish.granja.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_seller")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Seller {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;

    private String name;

    private String registration;



}
