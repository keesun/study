package me.whiteship.demodiff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DemoDiffApplication {

    public static void main(String[] args) throws IOException {
        Path exchangePath = Paths.get("C:\\Users\\kebaik\\workspace\\study\\demo-diff\\src\\main\\resources\\ExchangeServer.reg");
        File exchange = exchangePath.toFile();
        Path exchangeBEPath = Paths.get("C:\\Users\\kebaik\\workspace\\study\\demo-diff\\src\\main\\resources\\ExchangeServer_BE.reg");
        File exchangeBE = exchangeBEPath.toFile();

        System.out.println(exchange.exists());
        System.out.println(exchangeBE.exists());

        if (!exchange.exists() || !exchangeBE.exists()) {
            return;
        }

        List<String> keys = new ArrayList<>();
        try(Scanner scanner = new Scanner(exchange)) {
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("[")) {
                    keys.add(line);
                }
            }
        }

        System.out.println("found " + keys.size() + " keys");

        try(Scanner scanner = new Scanner(exchangeBE)) {
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("[")) {
                    keys.remove(line);
                }
            }
        }

        System.out.println("Remaining... " + keys.size() + " keys");
        keys.forEach(System.out::println);
    }

}
