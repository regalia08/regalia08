package com.example.regalia.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.regalia.entity.CheckLog;
import com.example.regalia.entity.Device;
import com.example.regalia.entity.DeviceStatus;
import com.example.regalia.repository.CheckLogRepository;
import com.example.regalia.repository.DeviceStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckLogService {

    private final CheckLogRepository checkLogRepository;
    private final DeviceStatusRepository deviceStatusRepository;
    private final DeviceService deviceService;

    // 장비별 로그 조회 (최신 100건)
    public List<CheckLog> findByDevice(Long deviceId) {
        Device device = deviceService.findById(deviceId);
        return checkLogRepository.findByDeviceOrderByCheckedAtDesc(device, PageRequest.of(0, 100));
    }

    // 장비별 로그 조회 (그래프용, 최신 50건)
    public List<CheckLog> findByDeviceForGraph(Long deviceId) {
        Device device = deviceService.findById(deviceId);
        return checkLogRepository.findByDeviceOrderByCheckedAtAsc(device, PageRequest.of(0, 50));
    }

    // 로그 저장 + device_status 업데이트
    @Transactional
    public void saveLog(Long deviceId, Integer responseTime, Integer statusCode, boolean isSuccess) {
        Device device = deviceService.findById(deviceId);
        DeviceStatus deviceStatus = deviceStatusRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장비 상태입니다."));

        // 연속 실패 횟수 계산
        int consecutiveFails = isSuccess ? 0 : deviceStatus.getConsecutiveFails() + 1;

        // 상태 판정
        DeviceStatus.StatusType status;
        if (!isSuccess || consecutiveFails >= 3) {
            status = DeviceStatus.StatusType.RED;
        } else if (responseTime != null && responseTime > 500) {
            status = DeviceStatus.StatusType.YELLOW;
        } else {
            status = DeviceStatus.StatusType.GREEN;
        }

        // 로그 저장
        CheckLog log = new CheckLog();
        log.setDevice(device);
        log.setStatus(status);
        log.setResponseTime(responseTime);
        log.setStatusCode(statusCode);
        log.setIsSuccess(isSuccess);
        log.setCheckedAt(LocalDateTime.now());
        checkLogRepository.save(log);

        // device_status 업데이트
        deviceStatus.setCurrentStatus(status);
        deviceStatus.setLastResponseTime(responseTime);
        deviceStatus.setConsecutiveFails(consecutiveFails);
        deviceStatus.setLastCheckedAt(LocalDateTime.now());
        deviceStatusRepository.save(deviceStatus);
    }

    // 30일 지난 로그 삭제 (배치용)
    @Transactional
    public void deleteOldLogs() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        checkLogRepository.deleteByCheckedAtBefore(threshold);
    }
}