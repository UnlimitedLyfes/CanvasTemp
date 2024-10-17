package com.mycompany._canvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class DataHandler {
    private File dataFile;
    private FileWriter dataWriter;
    int questionsWrote = 0;
    
    public DataHandler(String filePath)
    {
        this.dataFile = new File(filePath);
        try
        {
            this.dataWriter = new FileWriter(filePath, true);
        }
        catch(Exception e)
        {
            System.out.println("Data Handler Constructor Failed");
            System.exit(1);
        }
    }
    
    public Scanner createScanner(File file)
    {
        try
        {
            Scanner scn = new Scanner(file);
            return scn;
        }
        catch(Exception e)
        {
            System.out.println("Create Data Scanner Failed");
            System.exit(1);
        }
        return null;
    }
    
    public boolean isExistingQuestionId(int questionId)
    {
        Scanner dataReader = createScanner(dataFile);
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
    
    
    public void writeLine(String str)
    { 
        try
        {
            dataWriter.write(str + '\n');
            questionsWrote++;
            System.out.println("Wrote " + str);
        }
        catch(Exception e)
        {
            System.out.println("Question ID Failed to Write");
            System.exit(1);
        }
    }
    
    public void closeFiles()
    {
        try
        {
           dataWriter.close(); 
        }
        catch(Exception e)
        {
            System.out.println("Failed to Close File");
        }
        
        
    }
}
