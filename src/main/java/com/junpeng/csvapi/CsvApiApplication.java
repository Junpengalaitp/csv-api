package com.junpeng.csvapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

import static com.junpeng.csvapi.service.CsvEnrichService.PRODUCT_ID_TO_NAME;

@Slf4j
@SpringBootApplication
public class CsvApiApplication {

    public static void main(String[] args) throws FileNotFoundException {
        SpringApplication.run(CsvApiApplication.class, args);
    }

    /**
     * load the static product csv to memory
     * @throws FileNotFoundException
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadProduct() throws FileNotFoundException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("product.csv");
        Scanner sc = new Scanner(new File(url.getFile()));
        //parsing a CSV file into the constructor of Scanner class
        sc.useDelimiter("\n");
        //setting comma as delimiter pattern
        while (sc.hasNext()) {
            String[] split = sc.next().split(",");
            PRODUCT_ID_TO_NAME.put(split[0], split[1]);
        }
        sc.close();
        log.info("the product id to name mapping load success, size: {}", PRODUCT_ID_TO_NAME.size());
    }

}
