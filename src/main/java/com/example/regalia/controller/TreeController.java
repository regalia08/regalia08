package com.example.regalia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.regalia.dto.TreeNodeDto;
import com.example.regalia.service.TreeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TreeController {

    private final TreeService treeService;

    @GetMapping("/tree")
    public ResponseEntity<List<TreeNodeDto>> getTree() {
        return ResponseEntity.ok(treeService.getTree());
    }
}