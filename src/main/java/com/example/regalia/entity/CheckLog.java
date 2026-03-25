package com.example.regalia.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "check_log")
@Getter
@Setter
public class CheckLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus.StatusType status;

    @Column(name = "response_time")
    private Integer responseTime;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "is_success", nullable = false)
    private Boolean isSuccess = false;

    @Column(name = "checked_at", updatable = false)
    private LocalDateTime checkedAt;
}