package org.example.entities;

import java.time.LocalDate;

public class Product {
    private Long id;
    private String nombre;
    private Double precio;
    private Category category;
    private LocalDate fechaRegistro;
    private String sku;

    public Product(Long id, String nombre, Double precio, Category category, LocalDate fechaRegistro, String sku) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.category = category;
        this.fechaRegistro = fechaRegistro;
        this.sku = sku;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
