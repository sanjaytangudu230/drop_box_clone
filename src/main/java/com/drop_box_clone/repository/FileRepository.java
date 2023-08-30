package com.drop_box_clone.repository;

import com.drop_box_clone.entites.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {
    File findByIdAndIsActive (UUID id,Boolean isActive);
    List<File> findByUserIdAndIsActive (String id, Boolean isActive);
}
