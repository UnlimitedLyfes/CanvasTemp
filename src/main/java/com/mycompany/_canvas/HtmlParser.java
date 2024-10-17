package com.mycompany._canvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class HtmlParser {
    private File htmlFile;
    private DataHandler dataHandler;            // COULD MAKE HTMLREADER A MEMBER
    
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
                    if (isCorrectAnswer(foundString, htmlReader)) 
                    {
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
    
    private boolean isCorrectAnswer(String score, Scanner htmlReader)
    {
        if(score != "0")
        {
            String line = htmlReader.nextLine();
            int index1 = line.indexOf("/ ") + 2;
            int index2 = line.indexOf('<', index1);
            String highestScore = line.substring(index1, index2);
            System.out.println(highestScore);
            System.out.println(score.equals(highestScore));
            return score.equals(highestScore);
        }
        return false;
    }

    private void processQuestion(Scanner htmlReader, int points) {
        extractAndWriteQuestionId(htmlReader);
        extractAndWriteQuestion(htmlReader);
        extractAndWriteAnswer(htmlReader);
        dataHandler.writeLine("");
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
                    dataHandler.writeLine(Integer.toString(questionId));
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
    
    private void extractAndWriteQuestion(Scanner htmlReader)
    { 
        String line = htmlReader.nextLine();
        line = line.trim();
        dataHandler.writeLine(line);      
    }
    
    private void extractAndWriteAnswer(Scanner htmlReader)
    {
        //<div class="answer_text" style="display:none;">B salary is: 4000.0 Bonus of B is: 10000</div>
        //<div class="answer_text">System.out.println(#);</div>
        while (htmlReader.hasNextLine()) {
            String line = htmlReader.nextLine();

            if (line.contains("<div class=\"answer_text\"")) {
                line = extractAnswer(line);
                dataHandler.writeLine(line);
                break;
            }
        }
    }
    
    private String extractAnswer(String line)
    {
        int index1 = line.indexOf('>') + 1;
        int index2 = line.indexOf("</div>", index1);
        return line.substring(index1, index2);
    }
}