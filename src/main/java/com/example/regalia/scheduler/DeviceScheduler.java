package com.example.regalia.scheduler;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.regalia.entity.Device;
import com.example.regalia.service.CheckLogService;
import com.example.regalia.service.DeviceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceScheduler {

    private final DeviceService deviceService;
    private final CheckLogService checkLogService;

    // 5분마다 실행
    @Scheduled(fixedDelay = 300000)
    public void checkDevices() {
        List<Device> devices = deviceService.findAll();
        log.info("장비 상태 체크 시작 - 총 {}대", devices.size());

        for (Device device : devices) {
            check(device);
        }

        // 30일 지난 로그 정리
        checkLogService.deleteOldLogs();
    }

    private void check(Device device) {
        long startTime = System.currentTimeMillis();
        Integer responseTime = null;
        Integer statusCode = null;
        boolean isSuccess = false;

        try {
            URL url = new URL(device.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);  // 5초 timeout
            conn.setReadTimeout(5000);

            statusCode = conn.getResponseCode();
            responseTime = (int) (System.currentTimeMillis() - startTime);
            isSuccess = (statusCode >= 200 && statusCode < 400);

            conn.disconnect();
            log.info("체크 완료 - [{}] {}ms, HTTP {}", device.getName(), responseTime, statusCode);

        } catch (Exception e) {
            responseTime = null;
            statusCode = null;
            isSuccess = false;
            log.warn("체크 실패 - [{}] {}", device.getName(), e.getMessage());
        }

        checkLogService.saveLog(device.getId(), responseTime, statusCode, isSuccess);
    }
}