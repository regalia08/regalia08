package com.example.regalia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.regalia.entity.DeviceStatus;

public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, Long> {
}