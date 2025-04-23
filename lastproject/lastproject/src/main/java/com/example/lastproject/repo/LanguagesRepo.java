package com.example.lastproject.repo;

import com.example.lastproject.model.Languages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguagesRepo extends JpaRepository<Languages, Long> {
}
