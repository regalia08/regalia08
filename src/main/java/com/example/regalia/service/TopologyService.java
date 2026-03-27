package com.example.regalia.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.regalia.dto.TopologyDto;
import com.example.regalia.dto.TopologyDto.EdgeDto;
import com.example.regalia.dto.TopologyDto.NodeDto;
import com.example.regalia.entity.Device;
import com.example.regalia.entity.DeviceGroup;
import com.example.regalia.entity.DeviceLink;
import com.example.regalia.entity.DeviceStatus;
import com.example.regalia.repository.DeviceGroupRepository;
import com.example.regalia.repository.DeviceLinkRepository;
import com.example.regalia.repository.DeviceRepository;
import com.example.regalia.repository.DeviceStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopologyService {

    private final DeviceGroupRepository deviceGroupRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceStatusRepository deviceStatusRepository;
    private final DeviceLinkRepository deviceLinkRepository;

    public TopologyDto getTopology() {
        List<NodeDto> nodes = new ArrayList<>();
        List<EdgeDto> edges = new ArrayList<>();

        // 1. 그룹 노드
        List<DeviceGroup> groups = deviceGroupRepository.findAll();
        Random random = new Random();

        for (DeviceGroup group : groups) {
            NodeDto node = new NodeDto();
            node.setId("group_" + group.getId());
            node.setLabel(group.getName());
            node.setType("group");
            node.setX(group.getPosX() != null ? group.getPosX() : random.nextDouble() * 800 - 400);
            node.setY(group.getPosY() != null ? group.getPosY() : random.nextDouble() * 600 - 300);
            nodes.add(node);
        }

        // 2. 장비 노드
        List<Device> devices = deviceRepository.findAll();
        for (Device device : devices) {
            NodeDto node = new NodeDto();
            node.setId("device_" + device.getId());
            node.setLabel(device.getName());
            node.setType("device");
            node.setX(device.getPosX() != null ? device.getPosX() : random.nextDouble() * 800 - 400);
            node.setY(device.getPosY() != null ? device.getPosY() : random.nextDouble() * 600 - 300);

            DeviceStatus status = deviceStatusRepository.findById(device.getId()).orElse(null);
            node.setStatus(status != null ? status.getCurrentStatus().name() : "GREEN");
            nodes.add(node);

            // 3. 그룹-장비 연결선
            if (device.getGroup() != null) {
                EdgeDto edge = new EdgeDto();
                edge.setId("edge_g_" + device.getId());
                edge.setFrom("group_" + device.getGroup().getId());
                edge.setTo("device_" + device.getId());
                edges.add(edge);
            }
        }

        // 4. 장비-장비 연결선
        List<DeviceLink> links = deviceLinkRepository.findAll();
        for (DeviceLink link : links) {
            EdgeDto edge = new EdgeDto();
            edge.setId("edge_l_" + link.getId());
            edge.setFrom("device_" + link.getSource().getId());
            edge.setTo("device_" + link.getTarget().getId());
            edge.setLabel(link.getLabel());
            edges.add(edge);
        }

        TopologyDto topology = new TopologyDto();
        topology.setNodes(nodes);
        topology.setEdges(edges);
        return topology;
    }
}