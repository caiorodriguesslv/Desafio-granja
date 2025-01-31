package com.devilish.granja.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateSale;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToMany
    @JoinTable(
            name = "sale_duck",
            joinColumns = @JoinColumn(name = "sale_id"),
            inverseJoinColumns = @JoinColumn(name = "duck_id")
    )
    private List<Duck> ducks;

    private double totalValue;
}
