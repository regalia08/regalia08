package com.example.regalia.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.regalia.entity.DeviceGroup;
import com.example.regalia.repository.DeviceGroupRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceGroupService {

    private final DeviceGroupRepository deviceGroupRepository;

    // 전체 조회
    public List<DeviceGroup> findAll() {
        return deviceGroupRepository.findAll();
    }

    // 단건 조회
    public DeviceGroup findById(Long id) {
        return deviceGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다. id=" + id));
    }

    // 그룹 등록
    @Transactional
    public DeviceGroup save(DeviceGroup group) {
        group.setCreatedAt(LocalDateTime.now());
        return deviceGroupRepository.save(group);
    }

    // 그룹 수정
    @Transactional
    public DeviceGroup update(Long id, DeviceGroup updated) {
        DeviceGroup group = findById(id);
        group.setName(updated.getName());
        group.setDescription(updated.getDescription());
        return deviceGroupRepository.save(group);
    }

    // 그룹 삭제
    @Transactional
    public void delete(Long id) {
        deviceGroupRepository.deleteById(id);
    }
}