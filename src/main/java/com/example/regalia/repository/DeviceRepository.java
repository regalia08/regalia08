package com.example.regalia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.regalia.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByGroupId(Long groupId);
    List<Device> findByGroupIsNull();
}

