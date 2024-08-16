package com.fujitsu.delivery_fee_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Entity
@Table(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Integer wmoCode;
}