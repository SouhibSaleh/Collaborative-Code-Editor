package com.example.lastproject.controller.adminArea;

import com.example.lastproject.model.Languages;
import com.example.lastproject.service.LanguagesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adminArea/languages")
public class LanguagesControllerAdmin {

    private LanguagesService languagesService;

    public LanguagesControllerAdmin(LanguagesService languagesService) {
        this.languagesService = languagesService;

    }

    @PostMapping
    public ResponseEntity<?> addLanguage(@RequestBody String language){
        Languages languages = new Languages();
        languages.setLanguage(language);
        return ResponseEntity.ok(languagesService.saveLanguage(languages));
    }

}