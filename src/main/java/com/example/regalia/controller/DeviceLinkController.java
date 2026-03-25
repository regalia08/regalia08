package com.example.regalia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.regalia.entity.DeviceLink;
import com.example.regalia.service.DeviceLinkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class DeviceLinkController {

    private final DeviceLinkService deviceLinkService;

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<DeviceLink>> findAll() {
        return ResponseEntity.ok(deviceLinkService.findAll());
    }

    // 링크 등록
    @PostMapping
    public ResponseEntity<DeviceLink> save(@RequestBody Map<String, Object> body) {
        Long sourceId = Long.valueOf(body.get("sourceId").toString());
        Long targetId = Long.valueOf(body.get("targetId").toString());
        String label = body.get("label") != null ? body.get("label").toString() : null;
        return ResponseEntity.ok(deviceLinkService.save(sourceId, targetId, label));
    }

    // 링크 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deviceLinkService.delete(id);
        return ResponseEntity.noContent().build();
    }
}