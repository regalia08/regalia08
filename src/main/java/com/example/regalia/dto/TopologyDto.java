package com.example.regalia.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopologyDto {

    private List<NodeDto> nodes;
    private List<EdgeDto> edges;

    @Getter
    @Setter
    public static class NodeDto {
        private String id;
        private String label;
        private String type;
        private String status;
        private Double x;
        private Double y;
        private String image;
    }

    @Getter
    @Setter
    public static class EdgeDto {
        private String id;
        private String from;
        private String to;
        private String label;
    }
}