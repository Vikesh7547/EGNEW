package com.example.patientskydashoard.controller;

import com.example.patientskydashoard.services.DPoPJWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DPoPController {

    @Autowired
    private DPoPJWTGenerator dPoPJWTGenerator;

    @GetMapping("/generate-dpop")
    public String noPayloadGenerateDPoP(@RequestParam String privateKey,
                                    @RequestParam String publicKey,
                                    @RequestParam String url,
                                    @RequestParam String httpMethod) {
        try {
            return dPoPJWTGenerator.noPayloadGenerateDPoP(privateKey, publicKey, url, httpMethod);
        } catch (Exception e) {
            return "Error generating DPoP token: " + e.getMessage();
        }
    }
}
