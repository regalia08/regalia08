package com.example.regalia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.regalia.dto.TopologyDto;
import com.example.regalia.service.TopologyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/topology")
@RequiredArgsConstructor
public class TopologyController {

    private final TopologyService topologyService;

    @GetMapping
    public ResponseEntity<TopologyDto> getTopology() {
        return ResponseEntity.ok(topologyService.getTopology());
    }
}