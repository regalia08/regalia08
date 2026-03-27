package com.example.regalia.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "device_status")
@Getter
@Setter
public class DeviceStatus {

    @Id
    private Long deviceId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "device_id")
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    private StatusType currentStatus = StatusType.GREEN;

    @Column(name = "last_response_time")
    private Integer lastResponseTime;

    @Column(name = "consecutive_fails")
    private Integer consecutiveFails = 0;

    @Column(name = "last_checked_at")
    private LocalDateTime lastCheckedAt;

    public enum StatusType {
        GREEN, YELLOW, RED
    }
}