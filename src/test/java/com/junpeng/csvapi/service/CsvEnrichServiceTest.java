package com.junpeng.csvapi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CsvEnrichServiceTest {

    @Autowired
    private CsvEnrichService csvEnrichService;


    @DisplayName("test a csv file can be enriched")
    @Test
    void enrichCsv() {
        String csv = """
                date,product_id,currency,price
                20160101,1,EUR, 10.0
                20160101,2,EUR, 20.1
                20160101,3,EUR, 30.342
                """;
        String s = csvEnrichService.enrichCsv(csv);
        assertEquals("""
                date,product_id,currency,price
                20160101,Treasury Bills Domestic,EUR, 10.0
                20160101,Corporate Bonds Domestic,EUR, 20.1
                20160101,REPO Domestic,EUR, 30.342""", s);
    }

    @DisplayName("test a large csv file can be enriched in 5 seconds")
    @Test
    void enrichLargeCsv() {
        String header = "date,product_id,currency,price\n";
        String csv = """
                20160101,1,EUR, 10.0
                20160101,2,EUR, 20.1
                20160101,3,EUR, 30.342
                """;
        StringBuilder stringBuilder = new StringBuilder(header);
        stringBuilder.append(csv.repeat(1000000));
        assertTimeout(Duration.ofSeconds(5), () -> csvEnrichService.enrichCsv(stringBuilder.toString()));
    }

    @DisplayName("test a product id can be replaced by name")
    @Test
    void enrichLineSuccess() {
        String line = "20160101,1,EUR, 10.0";
        String enrichedLine = csvEnrichService.enrichLine(line);
        assertEquals("20160101,Treasury Bills Domestic,EUR, 10.0", enrichedLine);
    }

    @DisplayName("test a line with invalid date format is discarded and logged error")
    @Test
    void enrichLineFail() {
        String line = "2016-01-01,1,EUR, 10.0";
        String enrichedLine = csvEnrichService.enrichLine(line);
        assertEquals("", enrichedLine);
    }
}