package com.alpacaflow.meditrackplatform.relatives.domain.model.entities;
import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "RelativeSeniorCitizen")
@Table(name = "relatives_senior_citizens")
@Getter
public class SeniorCitizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private float height;

    @Column(name = "birth_date", nullable = false)
    private String birthDate;

    @Column(nullable = false)
    private float weight;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private String deviceId;

    protected SeniorCitizen() {}

    public SeniorCitizen(
        String firstName,
        String lastName,
        String dni,
        String gender,
        float height,
        String birthDate,
        float weight,
        String profileImage,
        String deviceId
    ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.gender = gender;
        this.height = height;
        this.deviceId = deviceId;
        this.weight = weight;
        this.profileImage = profileImage;
        this.birthDate = birthDate;
    }
}
