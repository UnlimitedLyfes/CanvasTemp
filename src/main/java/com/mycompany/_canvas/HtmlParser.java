package com.mycompany._canvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;   // For filterQuestion()

class HtmlParser {
    private File htmlFile;
    private DataHandler dataHandler;       
    private Scanner htmlReader;  // COULD MAKE HTMLREADER A MEMBER
    
    
    public HtmlParser(String htmlFile, String dataFile) {
        this.htmlFile = new File(htmlFile);
        this.dataHandler = new DataHandler(dataFile);
        try{htmlReader = new Scanner(this.htmlFile);}
        catch(Exception e){System.out.println("HTMLParser Constructer Failure");}
    }

    public void processFile() {
        while (htmlReader.hasNextLine()) {
            String line = htmlReader.nextLine();

            if (isPointsLine(line)) {
                String foundString = line.trim();

                // Skip if the points are "0"
                if (isCorrectAnswer(foundString)) 
                {
                    processQuestion(Integer.parseInt(foundString));
                }
            }
        }
        System.out.println("Processing finished");
        dataHandler.closeFiles();
    }

    private boolean isPointsLine(String line) {
        return line.matches(".*\\s\\s\\d");
    }
    
    private boolean isCorrectAnswer(String score)
    {
        if(score != "0")
        {
            String line = htmlReader.nextLine();
            int index1 = line.indexOf("/ ") + 2;
            int index2 = line.indexOf('<', index1);
            String highestScore = line.substring(index1, index2);
            return score.equals(highestScore);
        }
        return false;
    }

    private void processQuestion(int points) {
        if(extractAndWriteQuestionId())
        {
            extractAndWriteQuestion();
            extractAndWriteAnswer();
            dataHandler.writeLine("");
        }
    }

    private int extractOutOfValue(String line) {
        int index = line.indexOf('/') + 2;
        return Integer.parseInt(line.substring(index, index + 1));
    }

    private boolean extractAndWriteQuestionId() {
        while (htmlReader.hasNextLine()) {
            String line = htmlReader.nextLine();

            if (line.contains("id=\"question_")) {
                int questionId = extractQuestionId(line);
                boolean doesNotExist = !dataHandler.isExistingQuestionId(questionId);
                if(doesNotExist)
                {
                    dataHandler.writeLine(Integer.toString(questionId));
                }
                System.out.println(doesNotExist);
                return doesNotExist;
            }
        }
        return false;
    }

    private int extractQuestionId(String line) {
        int index1 = line.indexOf('_') + 1;
        int index2 = line.indexOf('_', index1);
        return Integer.parseInt(line.substring(index1, index2));
    }
    
    private void extractAndWriteQuestion()
    { 
        String line;
        String question = "";
        do{
            line = htmlReader.nextLine();
            if(line.contains("</div>"))
                break;
            // Get rid of whitespace and html tags
            line = filterQuestion(line);
            question = question.concat(line);
        }while(htmlReader.hasNextLine());
        dataHandler.writeLine(question);
    }
    
    private String filterQuestion(String line)
    {
        line = line.trim();
        String regexOfTags = "(?<!\\\\)<.+?>(?!\\\\)";
        line = line.replaceAll(regexOfTags, "");
        System.out.println(line);
        return line;
    }
    
    private void extractAndWriteAnswer()
    {
        //<div class="answer_text" style="display:none;">B salary is: 4000.0 Bonus of B is: 10000</div>
        //<div class="answer_text">System.out.println(#);</div>
        boolean isSelectedAnswer = false;
        while (htmlReader.hasNextLine()) {
            String line = htmlReader.nextLine();
            if(!isSelectedAnswer && line.contains("selected_answer"))
            {
                isSelectedAnswer = true;
            }
            if (isSelectedAnswer && line.contains("<div class=\"answer_text\"")) {
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