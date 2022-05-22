package src.Utilities;
public class ConsoleColor {
    public final String reset = "\u001B[0m";
    public final String black = "\u001B[30m";
    public final String red = "\u001B[31m";
    public final String green = "\u001B[32m";
    public final String yellow = "\u001B[33m";
    public final String blue = "\u001B[34m";
    public final String purple = "\u001B[35m";
    public final String cyan = "\u001B[36m";
    public final String white = "\u001B[37m";

    public void printRedText(String text) {
        System.out.println(red + text + reset);
    }

    public void printGreenText(String text) {
        System.out.println(green + text + reset);
    }

    public void printYellowText(String text) {
        System.out.println(yellow + text + reset);
    }

    public void printCyanText(String text) {
        System.out.println(cyan + text + reset);
    }
}
