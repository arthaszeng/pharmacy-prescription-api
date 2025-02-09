package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.DrugSchema;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DrugDomainTest {

    @Test
    void testFromSchema() {
        DrugSchema schema = new DrugSchema(
                1L, "Paracetamol", "XYZ Pharma", "B123",
                new Date(), 100, LocalDateTime.now(), LocalDateTime.now(), false
        );

        DrugDomain domain = DrugDomain.fromSchema(schema);

        assertNotNull(domain);
        assertEquals(schema.getId(), domain.getId());
        assertEquals(schema.getName(), domain.getName());
        assertEquals(schema.getManufacturer(), domain.getManufacturer());
        assertEquals(schema.getBatchNumber(), domain.getBatchNumber());
        assertEquals(schema.getStock(), domain.getStock());
    }

    @Test
    void testToSchema() {
        DrugDomain domain = new DrugDomain(
                1L, "Paracetamol", "XYZ Pharma", "B123",
                new Date(), 100, LocalDateTime.now(), LocalDateTime.now(), false
        );

        DrugSchema schema = domain.toSchema();

        assertNotNull(schema);
        assertEquals(domain.getId(), schema.getId());
        assertEquals(domain.getName(), schema.getName());
        assertEquals(domain.getManufacturer(), schema.getManufacturer());
        assertEquals(domain.getBatchNumber(), schema.getBatchNumber());
        assertEquals(domain.getStock(), schema.getStock());
    }
}