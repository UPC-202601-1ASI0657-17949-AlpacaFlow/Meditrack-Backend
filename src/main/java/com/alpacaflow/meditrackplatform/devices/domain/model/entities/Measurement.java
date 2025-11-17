package com.alpacaflow.meditrackplatform.devices.domain.model.entities;

import com.alpacaflow.meditrackplatform.devices.domain.model.valueobjects.EMeasurementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Abstract base class for measurements
 */
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "measurement_type")
public abstract class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EMeasurementType type;

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    protected Measurement() {
        this.measuredAt = LocalDateTime.now();
    }

    protected Measurement(EMeasurementType type) {
        this();
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public EMeasurementType getType() {
        return type;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }
}

