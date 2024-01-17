package com.creeperevents.oggehej.obsidianbreaker.obsidianbreaker;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVReader {

    public static String readAndRemoveDataFromCSV() {
        String csvFileName = "C:\\Users\\Chapy\\Desktop\\WW2 1.12.2 TEST\\csv\\test.csv";
        String data = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFileName))) {
            String line = reader.readLine();
            if (line != null) {
                data = line;
                StringBuilder tempBuffer = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    tempBuffer.append(line).append(System.lineSeparator());
                }
                try (FileWriter writer = new FileWriter(csvFileName)) {
                    writer.write(tempBuffer.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}