package org.example.entities;

import java.time.LocalDate;

public record Product(Long id, String nombre, Double precio, Category category, LocalDate fechaRegistro) {}
