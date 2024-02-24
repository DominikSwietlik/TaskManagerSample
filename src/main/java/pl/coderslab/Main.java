package pl.coderslab;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {
    static final String FILE_NAME = "tasks.csv";
    public static void main(String[] args) {
        exist();
        boolean status = true;
        change(listIt());
        while (status) {
            System.out.println(ConsoleColors.BLUE + "Please select an option");
            green("add\nremove\nlist\nexit");
            Scanner scanner = new Scanner(System.in);
            String tmp = scanner.nextLine().toLowerCase();
            switch (tmp) {
                case "add" -> add();
                case "remove" -> remove(listIt());
                case "list" -> list(listIt());
                case "exit" -> status = exit();
                default -> standard("something wrong, try again");
            }
        }
    }

    private static void green(String msg) {
        System.out.println(ConsoleColors.RESET + msg + ConsoleColors.GREEN);
    }

    private static void standard(String msg) {
        System.out.println(ConsoleColors.RESET + msg);
    }

    private static void list(ArrayList<String> list) {
        standard("list");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + " : " + list.get(i));
        }
    }
    private static void exist()
    {
        Path path = Paths.get(Main.FILE_NAME);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.out.println("Create file error");
            }

        }
    }
    private static void add() {
        standard("add");
        try (FileWriter writer = new FileWriter(Main.FILE_NAME, true)) {
            Scanner scanner = new Scanner(System.in);
            green("Please add task description");
            String line = scanner.nextLine();
            green("Please add task due date in format yyyy-mm-dd");
            String line2;
            while (true) {
                try {
                    String date = scanner.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    try {
                        LocalDate tmp = LocalDate.parse(date, formatter);
                        line2 = tmp.toString();
                        break;
                    } catch (Exception e) {
                        System.out.println("Try again, correct format of date it yyyy-MM-dd");

                    }
                } catch (InputMismatchException e) {

                }
            }
            green("Is your task is important true/false");
            while (true) {
                String line3 = scanner.nextLine().toLowerCase();
                if (line3.equals("true")) {
                    writer.write("\n" + line + ", " + line2 + ", " + line3);
                    break;
                } else if (line3.equals("false")) {
                    writer.write("\n" + line + ", " + line2 + ", " + line3);
                    break;
                } else {
                    System.out.println("You wrote bad value, try again ");
                }
            }
        } catch (IOException e) {
            System.out.println("Nie udało się zapisać danych do pliku!");
        }
        change(listIt());
    }
    private static ArrayList<String> listIt() {
        ArrayList<String> list = new ArrayList<>();
        try (FileReader fileReader = new FileReader(Main.FILE_NAME)) {
            Scanner scanner = new Scanner(fileReader);
            while (scanner.hasNextLine()) {
                {
                    String line = scanner.nextLine();
                    list.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Something wrong, try again");
        } catch (IOException e) {

        }
        change(list);
        return list;
    }

    private static void remove(ArrayList<String> list) {
        standard("remove");
        int no;
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                no = scanner.nextInt();
                if (no >= 0 && no < listIt().size()) {
                    list.remove(no);
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("You print bad value, try again");
            }
        }
        change(list);
    }

    private static void change(ArrayList<String> list) {
        String fileTmpName = "tasksTmp.csv";

        Path path = Paths.get(fileTmpName);
        Path path2 = Paths.get(Main.FILE_NAME);
        try {
            Files.createFile(path);
        } catch (IOException e) {
        }
            try (FileWriter writer = new FileWriter(fileTmpName, true)) {
                for (String element : list) {
                    if (element.length() > 4) {
                        writer.write(element + "\n");
                    }
                }
                Files.move(path, path2, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(path);
            } catch (IOException e) {
            }

    }
    private static boolean exit() {
        standard("exit");
        return false;
    }
}