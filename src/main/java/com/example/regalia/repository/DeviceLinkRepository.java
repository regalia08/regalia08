package com.example.regalia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.regalia.entity.DeviceLink;

public interface DeviceLinkRepository extends JpaRepository<DeviceLink, Long> {
}