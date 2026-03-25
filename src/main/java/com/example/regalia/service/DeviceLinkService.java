package com.example.regalia.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.regalia.entity.Device;
import com.example.regalia.entity.DeviceLink;
import com.example.regalia.repository.DeviceLinkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceLinkService {

    private final DeviceLinkRepository deviceLinkRepository;
    private final DeviceService deviceService;

    // 전체 조회
    public List<DeviceLink> findAll() {
        return deviceLinkRepository.findAll();
    }

    // 링크 등록
    @Transactional
    public DeviceLink save(Long sourceId, Long targetId, String label) {

        // source_id < target_id 보장
        if (sourceId > targetId) {
            Long temp = sourceId;
            sourceId = targetId;
            targetId = temp;
        }

        Device source = deviceService.findById(sourceId);
        Device target = deviceService.findById(targetId);

        DeviceLink link = new DeviceLink();
        link.setSource(source);
        link.setTarget(target);
        link.setLabel(label);
        link.setCreatedAt(LocalDateTime.now());

        return deviceLinkRepository.save(link);
    }

    // 링크 삭제
    @Transactional
    public void delete(Long id) {
        deviceLinkRepository.deleteById(id);
    }
}