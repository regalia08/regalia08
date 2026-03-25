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

import com.example.regalia.entity.DeviceGroup;
import com.example.regalia.service.DeviceGroupService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class DeviceGroupController {

    private final DeviceGroupService deviceGroupService;

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<DeviceGroup>> findAll() {
        return ResponseEntity.ok(deviceGroupService.findAll());
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<DeviceGroup> findById(@PathVariable Long id) {
        return ResponseEntity.ok(deviceGroupService.findById(id));
    }

    // 그룹 등록
    @PostMapping
    public ResponseEntity<DeviceGroup> save(@RequestBody DeviceGroup group) {
        return ResponseEntity.ok(deviceGroupService.save(group));
    }

    // 그룹 수정
    @PutMapping("/{id}")
    public ResponseEntity<DeviceGroup> update(@PathVariable Long id, @RequestBody DeviceGroup group) {
        return ResponseEntity.ok(deviceGroupService.update(id, group));
    }

    // 그룹 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deviceGroupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}