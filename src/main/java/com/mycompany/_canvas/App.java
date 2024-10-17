package com.mycompany._canvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        HtmlParser parser = new HtmlParser("html.txt");
        parser.processFile();
    }
}