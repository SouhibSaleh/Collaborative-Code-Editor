package com.example.lastproject.controller.sharedArea;

import com.example.lastproject.service.LanguagesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sharedArea/languages")
public class LanguagesControllerShared {

    private LanguagesService languagesService;

    public LanguagesControllerShared(LanguagesService languagesService) {
        this.languagesService = languagesService;

    }

    @GetMapping
    public ResponseEntity<?> getAllLanguages(){
        System.out.println(languagesService.getAllLanguages());
        return ResponseEntity.ok(languagesService.getAllLanguages());
    }

}