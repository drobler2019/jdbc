package org.example.entities;

import java.sql.Date;

public record Product(Long id, String nombre, Double precio, Date fechaRegistro) {}
