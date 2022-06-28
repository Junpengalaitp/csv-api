package com.junpeng.csvapi.controller;

import com.junpeng.csvapi.service.CsvEnrichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class CsvController {

    @Autowired
    private CsvEnrichService csvEnrichService;

    @PostMapping(value = "/enrich", produces = "text/csv", consumes = "text/csv")
    public ResponseEntity<String> enrichCsv(@RequestBody String csv) {
        String s = csvEnrichService.enrichCsv(csv);
        return ResponseEntity.ok(s);
    }
}
