
/**
 * Fabian Flores
 * Date created: 03/27/2020
 * Last modified: 03/27/2020
 * Table output aided by code found here ->
 * https://www.logicbig.com/how-to/code-snippets/jcode-java-cmd-command-line-table.html
 */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class SJF {
    public SJF() {
    }

    public static int menu() {
        Scanner in = new Scanner(System.in);
        int option;
        System.out.println("Select from the following options:");
        System.out.println("1. Simulate 20 trials with 5 jobs");
        System.out.println("2. Simulate 20 trials with 10 jobs");
        System.out.println("3. Simulate 20 trials with 15 jobs");
        System.out.println("4. Exit\n");
        System.out.print("> ");
        option = in.nextInt();
        in.close();
        return option;
    }

    // bubble sort for our purposes
    public static void sort(ArrayList<String> names, ArrayList<Integer> times) {
        String tmpName;
        int tmpTime;
        for (int i = 0; i < times.size() - 1; i++) {
            for (int j = 0; j < times.size() - i - 1; j++) {
                if (times.get(j) > times.get(j + 1)) {
                    tmpName = names.get(j);
                    tmpTime = times.get(j);
                    names.set(j, names.get(j + 1));
                    times.set(j, times.get(j + 1));
                    names.set(j + 1, tmpName);
                    times.set(j + 1, tmpTime);
                }
            }
        }
    }

    public static void sjfFile(File file) {
        Scanner in;
        CommandLineTable table = new CommandLineTable();
        ArrayList<String> processNames = new ArrayList<>();
        ArrayList<Integer> processTimes = new ArrayList<>();
        ArrayList<Integer> completedTimes = new ArrayList<>();
        int totalTime = 0, startTime, sum;
        try {
            in = new Scanner(file);
            table.setShowVerticalLines(true);
            table.setHeaders("Job #", "Start time", "End time", "Job completion");

            // save process names and their respective process times
            while (in.hasNextLine()) {
                processNames.add(in.nextLine());
                processTimes.add(Integer.parseInt(in.nextLine()));
            }
            for (int i = 0; i < processTimes.size(); i++) {
                completedTimes.add(0);
            }
            // sorting based on shortest job
            sort(processNames, processTimes); // objects are pass-by-reference

            for (int i = 0; i < processNames.size(); i++) {
                startTime = totalTime;
                totalTime += processTimes.get(i);
                completedTimes.set(i, totalTime);
                table.addRow(processNames.get(i), Integer.toString(startTime), Integer.toString(totalTime),
                        processNames.get(i) + " completed @ " + totalTime);
            }
            table.print();
            System.out.println("Average Turnaround Time");
            System.out.print("= (");
            sum = 0;
            for (int i = 0; i < completedTimes.size(); i++) {
                sum += completedTimes.get(i);
                System.out.print(completedTimes.get(i));
                if (i < completedTimes.size() - 1)
                    System.out.print(" + ");
                else
                    System.out.println(") / " + completedTimes.size());
            }
            double averageTurnaroundTime = (sum * 1.0) / processTimes.size();
            System.out.printf("= %.2f ms\n", averageTurnaroundTime);
            in.close();
        } catch (

        Exception e) {
            System.out.println(e);
        }
    }

    public static void simulate(int n) {
        int runs = 20, maxJobLength = 20;
        Random rand = new Random();
        System.out.println(rand.nextInt(maxJobLength) + 1); // range[1, 20]
    }

    public static void main(String[] args) {
        System.out.println("SJF Scheduling Algorithm\n");

        if (args.length > 0) {
            sjfFile(new File(args[0]));
        } else {
            int option = menu();

            switch (option) {
                case 1:
                    simulate(5);
                    break;
                case 2:
                    simulate(10);
                    break;
                case 3:
                    simulate(15);
                    break;
                default:

                    break;
            }
        }
    }
}