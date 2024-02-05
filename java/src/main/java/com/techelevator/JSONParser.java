package com.techelevator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONParser {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Ladder2023> data = mapper.readValue(new File("C:\\Users\\HP\\Projects\\My-AFL-Ladder\\afltables_scraper\\afltables_scraper\\spiders\\ladder-output.json"), new TypeReference<List<Ladder2023>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
