package com.VTool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDataRepository extends JpaRepository<ProjectData, String> {
}

