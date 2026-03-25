package com.example.regalia.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.regalia.entity.CheckLog;
import com.example.regalia.entity.Device;

public interface CheckLogRepository extends JpaRepository<CheckLog, Long> {
    List<CheckLog> findByDeviceOrderByCheckedAtDesc(Device device, Pageable pageable);		// 특정 장비의 로그를 최신순으로 조회
    // → WHERE device = ? ORDER BY checked_at DESC LIMIT 100
    List<CheckLog> findByDeviceOrderByCheckedAtAsc(Device device, Pageable pageable);		// 특정 장비의 로그를 오래된순으로 조회 (그래프용)
    // → WHERE device = ? ORDER BY checked_at ASC LIMIT 50
    void deleteByCheckedAtBefore(LocalDateTime threshold);			// 특정 날짜 이전 로그 삭제 (30일 정리)
    // → DELETE FROM check_log WHERE checked_at < ?
}