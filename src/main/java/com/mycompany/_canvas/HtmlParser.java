package com.mycompany._canvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class HtmlParser {
    private File htmlFile;
    private DataHandler dataHandler;
    
    public HtmlParser(String htmlFile, String dataFile) {
        this.htmlFile = new File(htmlFile);
        this.dataHandler = new DataHandler(dataFile);
    }

    public void processFile() {
        try (Scanner htmlReader = new Scanner(htmlFile)) {
            while (htmlReader.hasNextLine()) {
                String line = htmlReader.nextLine();

                if (isPointsLine(line)) {
                    String foundString = line.trim();

                    // Skip if the points are "0"
                    if (!"0".equals(foundString)) {
                        processQuestion(htmlReader, Integer.parseInt(foundString));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + htmlFile.getName());
        }
        System.out.println("Processing finished");
        dataHandler.closeFiles();
    }

    private boolean isPointsLine(String line) {
        return line.matches(".*\\s\\s\\d");
    }

    private void processQuestion(Scanner htmlReader, int points) {
        while (htmlReader.hasNextLine()) {
            String line = htmlReader.nextLine();

            if (line.contains(" / ")) {
                int outOf = extractOutOfValue(line);

                if (outOf == points) {
                    extractAndWriteQuestionId(htmlReader);
                    //extractAndWriteQuestion(htmlReader);
                    break;
                } else {
                    break; // Exit when the points don't match
                }
            }
        }
    }

    private int extractOutOfValue(String line) {
        int index = line.indexOf('/') + 2;
        return Integer.parseInt(line.substring(index, index + 1));
    }

    private void extractAndWriteQuestionId(Scanner htmlReader) {
        while (htmlReader.hasNextLine()) {
            String line = htmlReader.nextLine();

            if (line.contains("id=\"question_")) {
                int questionId = extractQuestionId(line);
                if(!dataHandler.isExistingQuestionId(questionId))
                {
                    dataHandler.writeQuestionId(questionId);
                }
                break;
            }
        }
    }

    private int extractQuestionId(String line) {
        int index1 = line.indexOf('_') + 1;
        int index2 = line.indexOf('_', index1);
        return Integer.parseInt(line.substring(index1, index2));
    }
    
    
    

}