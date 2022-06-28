package com.junpeng.csvapi.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class CsvEnrichService {

    /**
     * key: product id
     * value: product name
     */
    public static final Map<String, String> PRODUCT_ID_TO_NAME = new HashMap<>();

    /**
     * @return csv string, line separator: '\n', column separator: ','
     */
    public String enrichCsv(String csv) {
        String[] lines = csv.split("\n");
        String head = lines[0];
        return head + "\n" + Stream.of(lines)
                .skip(1) // skip the header
                .parallel() // concurrency for large csv files
                .map(this::enrichLine) // deal with each line, validate and change product id to name
                .filter(s -> !s.isEmpty()) // discard the invalid row and log an error
                .collect(Collectors.joining("\n")); // join by '\n'
    }

    /**
     * validate the line, and replace the product id with product name
     * @param line: csv line
     * line[0]: date
     * line[1]: product_id
     * line[2]: currency
     * line[3]: price
     * @return
     */
    public String enrichLine(String line) {
        String[] data = line.split(",");
        String validateError = validateLine(data);
        if (!validateError.isEmpty()) {
            log.error("error line: {}", line);
            log.error("error message: {}", validateError);
            return "";
        }
        data[1] = PRODUCT_ID_TO_NAME.get(data[1]);
        return String.join(",", data);
    }

    private String validateLine(String[] line) {
        if (line.length != 4) {
            return "data length is incorrect, should be 4, but got " + line.length;
        }
        if (!isDateFormatValid(line[0], "yyyyMMdd")) {
            return "date format is incorrect, should be yyyyMMdd, but got " + line[0];
        }
        return "";
    }


    private boolean isDateFormatValid(String dateStr, String datePattern) {
        return GenericValidator.isDate(dateStr, datePattern , true);
    }

}
