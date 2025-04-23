package com.example.lastproject.repo;

import com.example.lastproject.model.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotRepo extends JpaRepository<Snapshot,Long> {
}
