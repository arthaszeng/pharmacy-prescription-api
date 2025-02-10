package com.arthas.pharmacyprescriptionapi.domain.model;

import com.arthas.pharmacyprescriptionapi.infrastructure.schema.PatientSchema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDomain {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PatientDomain fromSchema(PatientSchema schema) {
        return PatientDomain.builder()
                .id(schema.getId())
                .firstName(schema.getFirstName())
                .lastName(schema.getLastName())
                .dateOfBirth(schema.getDateOfBirth())
                .gender(schema.getGender())
                .phone(schema.getPhone())
                .email(schema.getEmail())
                .createdAt(schema.getCreatedAt())
                .updatedAt(schema.getUpdatedAt())
                .build();
    }

    public PatientSchema toSchema() {
        return PatientSchema.builder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .dateOfBirth(this.dateOfBirth)
                .gender(this.gender)
                .phone(this.phone)
                .email(this.email)
                .createdAt(this.createdAt != null ? this.createdAt : LocalDateTime.now())
                .updatedAt(this.updatedAt != null ? this.updatedAt : LocalDateTime.now())
                .build();
    }
}