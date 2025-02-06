package org.example.productservice.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    private Category parentCategory;
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
