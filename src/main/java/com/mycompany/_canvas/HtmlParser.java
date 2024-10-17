package com.mycompany._canvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class HtmlParser {
    private File htmlFile;

    public HtmlParser(String filePath) {
        this.htmlFile = new File(filePath);
    }

    public void processFile() {
        try (Scanner htmlReader = new Scanner(htmlFile)) {
            while (htmlReader.hasNextLine()) {
                String line = htmlReader.nextLine();

                if (isPointsLine(line)) {
                    String foundString = line.trim();

                    // Skip if the points are "0"
                    if (!"0".equals(foundString)) {
                        processPointsLine(htmlReader, Integer.parseInt(foundString));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + htmlFile.getName());
        }
        System.out.println("Processing finished");
    }

    private boolean isPointsLine(String line) {
        return line.matches(".*\\s\\s\\d");
    }

    private void processPointsLine(Scanner htmlReader, int points) {
        while (htmlReader.hasNextLine()) {
            String line = htmlReader.nextLine();

            if (line.contains(" / ")) {
                int outOf = extractOutOfValue(line);

                if (outOf == points) {
                    extractAndWriteQuestionId(htmlReader);
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
                if(!isExistingQuestionId(questionId))
                {
                    writeQuestionId();
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
    
    private boolean isExistingQuestionId(int questionId)
    {
        try
        {
            File dataFile = new File("data.txt");
            Scanner dataReader = new Scanner(dataFile);
            boolean isFound = false;
            while(dataReader.hasNextLine())
            {
                String line = dataReader.nextLine();
                if(line.contains(Integer.toString(questionId)))
                {
                    isFound = true;
                    break;
                }
            }
            return isFound;
        }
        catch(Exception e)
        {
            System.out.println("isExistingQuestionId Failed");
            System.exit(1);
            return false;
        }
    }
    
//    private void writeQuestionId(int questionId)
//    {
//        String questionIdString = Integer.toString(questionId);
//        
//        try
//        {
//            File dataFile = new File("data.txt");
//            Scanner dataReader = new Scanner(dataFile);
//        }
//        catch(Exception e)
//        {
//            System.out.println("writeQuestionId Failed");
//            System.exit(1);
//        }
        
    }
}