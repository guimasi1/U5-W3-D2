package com.example.U5W3D2.device;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DevicesDAO extends JpaRepository<Device, UUID> {
    Page<Device> findByStatus(String status, Pageable pageable);
    Page<Device> findByType(String type, Pageable pageable);
    Page<Device> findByTypeAndStatus(String type, String status, Pageable pageable);

}
