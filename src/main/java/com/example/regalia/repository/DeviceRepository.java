package com.example.regalia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.regalia.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}