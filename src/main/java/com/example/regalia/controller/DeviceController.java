package com.example.regalia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.regalia.entity.Device;
import com.example.regalia.service.DeviceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<Device>> findAll() {
        return ResponseEntity.ok(deviceService.findAll());
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Device> findById(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.findById(id));
    }

    // 장비 등록
    @PostMapping
    public ResponseEntity<Device> save(@RequestBody Device device) {
        return ResponseEntity.ok(deviceService.save(device));
    }

    // 장비 수정
    @PutMapping("/{id}")
    public ResponseEntity<Device> update(@PathVariable Long id, @RequestBody Device device) {
        return ResponseEntity.ok(deviceService.update(id, device));
    }

    // 장비 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}