package com.example.regalia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.regalia.entity.CheckLog;
import com.example.regalia.service.CheckLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class CheckLogController {

    private final CheckLogService checkLogService;

    // 장비별 최신 로그 조회
    @GetMapping("/{deviceId}")
    public ResponseEntity<List<CheckLog>> findByDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(checkLogService.findByDevice(deviceId));
    }

    // 장비별 그래프용 로그 조회
    @GetMapping("/{deviceId}/graph")
    public ResponseEntity<List<CheckLog>> findByDeviceForGraph(@PathVariable Long deviceId) {
        return ResponseEntity.ok(checkLogService.findByDeviceForGraph(deviceId));
    }
}