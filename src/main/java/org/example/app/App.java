package org.example.app;

public class App {
    public static void main(String[] args) {
        CLI cli = new CLI();

        if (args.length > 0) {
            cli.start(args[0]);
        } else {
            cli.start(null);
        }
    }
}
