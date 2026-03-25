package com.example.regalia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.regalia.entity.DeviceGroup;

public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long> {
}