package com.devilish.granja.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_duck")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Duck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "mae_id")
    private Duck mae;

    private boolean sold;

    private double price;

}
