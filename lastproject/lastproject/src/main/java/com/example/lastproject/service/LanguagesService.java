package com.example.lastproject.service;

import com.example.lastproject.model.Languages;
import com.example.lastproject.repo.LanguagesRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguagesService {
    private LanguagesRepo languagesRepo;
    public LanguagesService(LanguagesRepo languagesRepo){
        this.languagesRepo = languagesRepo;
    }
    public Languages saveLanguage(Languages languages){
       return languagesRepo.save(languages);
    }
    public List<Languages>getAllLanguages(){
       return languagesRepo.findAll();
    }

}
