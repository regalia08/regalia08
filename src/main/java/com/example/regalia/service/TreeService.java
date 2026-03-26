package com.example.regalia.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.regalia.dto.TreeNodeDto;
import com.example.regalia.entity.Device;
import com.example.regalia.entity.DeviceGroup;
import com.example.regalia.entity.DeviceStatus;
import com.example.regalia.repository.DeviceGroupRepository;
import com.example.regalia.repository.DeviceRepository;
import com.example.regalia.repository.DeviceStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TreeService {

    private final DeviceGroupRepository deviceGroupRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceStatusRepository deviceStatusRepository;

    public List<TreeNodeDto> getTree() {

        List<TreeNodeDto> children = new ArrayList<>();

        // 1. 그룹별 장비
        List<DeviceGroup> groups = deviceGroupRepository.findAll();
        for (DeviceGroup group : groups) {
            TreeNodeDto groupNode = new TreeNodeDto();
            groupNode.setId("group_" + group.getId());
            groupNode.setText(group.getName());
            groupNode.setType("group");

            List<Device> devices = deviceRepository.findByGroupId(group.getId());
            List<TreeNodeDto> deviceNodes = new ArrayList<>();
            for (Device device : devices) {
                deviceNodes.add(makeDeviceNode(device));
            }
            groupNode.setChildren(deviceNodes);
            children.add(groupNode);
        }

        // 2. 그룹 없는 장비는 REGALIA 바로 아래
        List<Device> ungrouped = deviceRepository.findByGroupIsNull();
        for (Device device : ungrouped) {
            children.add(makeDeviceNode(device));
        }

        // 3. 최상위 REGALIA 노드로 감싸기
        TreeNodeDto root = new TreeNodeDto();
        root.setId("root");
        root.setText("REGALIA");
        root.setType("root");
        root.setChildren(children);

        List<TreeNodeDto> tree = new ArrayList<>();
        tree.add(root);
        return tree;
    }

    private TreeNodeDto makeDeviceNode(Device device) {
        TreeNodeDto node = new TreeNodeDto();
        node.setId("device_" + device.getId());
        node.setText(device.getName());
        node.setType("device");

        DeviceStatus status = deviceStatusRepository.findById(device.getId()).orElse(null);
        node.setStatus(status != null ? status.getCurrentStatus().name() : "GREEN");
        return node;
    }
}