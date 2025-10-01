package org.example;

import java.util.Scanner;


public class App 
{
    String[] ticket = new String[100];
    public static void main( String[] args )
    {
        App app = new App();
        CLI cli = new CLI();
        cli.start();
    }
}
