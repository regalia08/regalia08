package com.example.regalia.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeNodeDto {

    private String id;       // "group_1" 또는 "device_1"
    private String text;     // 화면에 표시될 이름
    private String type;     // "group" 또는 "device"
    private String status;   // GREEN / YELLOW / RED (장비만)
    private List<TreeNodeDto> children;  // 하위 노드 (그룹만)
}