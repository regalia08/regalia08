package com.example.regalia.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.regalia.entity.Device;
import com.example.regalia.entity.DeviceStatus;
import com.example.regalia.repository.DeviceRepository;
import com.example.regalia.repository.DeviceStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceStatusRepository deviceStatusRepository;

    // 전체 조회
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    // 단건 조회
    public Device findById(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장비입니다. id=" + id));
    }

    // 장비 등록
    @Transactional
    public Device save(Device device) {
        device.setCreatedAt(LocalDateTime.now());
        device.setUpdatedAt(LocalDateTime.now());
        Device saved = deviceRepository.save(device);

        // device_status 초기 레코드 생성
        DeviceStatus status = new DeviceStatus();
        status.setDevice(saved);
        status.setCurrentStatus(DeviceStatus.StatusType.GREEN);
        status.setConsecutiveFails(0);
        deviceStatusRepository.save(status);

        return saved;
    }

    // 장비 수정
    @Transactional
    public Device update(Long id, Device updated) {
        Device device = findById(id);
        device.setName(updated.getName());
        device.setUrl(updated.getUrl());
        device.setDescription(updated.getDescription());
        device.setGroup(updated.getGroup());
        device.setCheckIntervalSec(updated.getCheckIntervalSec());
        device.setUpdatedAt(LocalDateTime.now());
        return deviceRepository.save(device);
    }

    // 장비 삭제
    @Transactional
    public void delete(Long id) {
        deviceRepository.deleteById(id);
    }

    // 토폴로지 위치 저장
    @Transactional
    public void updatePosition(Long id, Double posX, Double posY) {
        Device device = findById(id);
        device.setPosX(posX);
        device.setPosY(posY);
        device.setUpdatedAt(LocalDateTime.now());
        deviceRepository.save(device);
    }
}