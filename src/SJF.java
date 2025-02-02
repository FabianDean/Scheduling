
/**
 * Fabian Flores
 * Date created: 03/27/2020
 * Last modified: 04/07/2020
 * Table output aided by code found here ->
 * https://www.logicbig.com/how-to/code-snippets/jcode-java-cmd-command-line-table.html
 */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

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

    /**
     * Use bubble sort to sort the lists in ascending order based on times
     * 
     * @param names
     * @param times
     */
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

    /**
     * Perform Shortest-Job-First scheduling algorithm
     * 
     * @param file method needs a file that contains the job names and lengths
     * @param n    number of jobs in the file. Used for running the simulations
     */
    public static double sjf(File file, int n) {
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
            // based on the input data size selected by user
            if (n == 0) {
                // save process names and their respective process times
                while (in.hasNextLine()) {
                    processNames.add(in.nextLine());
                    processTimes.add(Integer.parseInt(in.nextLine()));
                }
            } else {
                for (int i = 0; i < n; i++) {
                    processNames.add(in.nextLine());
                    processTimes.add(Integer.parseInt(in.nextLine()));
                }
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
            if (n == 0) {
                table.print();
                System.out.println("Average Turnaround Time");
                System.out.print("= (");
            }
            sum = 0;
            for (int i = 0; i < completedTimes.size(); i++) {
                sum += completedTimes.get(i);
                if (n == 0) {
                    System.out.print(completedTimes.get(i));
                    if (i < completedTimes.size() - 1)
                        System.out.print(" + ");
                    else
                        System.out.println(") / " + completedTimes.size());
                }
            }
            double averageTurnaroundTime = (sum * 1.0) / processTimes.size();
            // do not print for simulation
            if (n == 0)
                System.out.printf("= %.2f ms\n", averageTurnaroundTime);
            in.close();
            return averageTurnaroundTime;
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    /**
     * simulate the scheduling algorithm over 20 trials over the first n jobs each
     * file
     * 
     * @param n the number of jobs to use in each input file (first 5, first 10, all
     *          15)
     */
    public static void simulate(int n) {
        File file;
        int runs = 20;
        double sum = 0, averageTurnaroundTime;
        for (int i = 0; i < runs; i++) {
            // loop through each input data file and run algorithm
            file = new File("../input/jobs" + (i + 1) + ".txt");
            sum += sjf(file, n);
        }
        averageTurnaroundTime = sum / runs;
        System.out.println("Average Turnaround Time for " + runs + " trials with " + n + " jobs");
        System.err.printf("= %.2f ms\n", averageTurnaroundTime);
    }

    /**
     * To run over a specific file, simply given file path as argument when running
     * program; otherwise, a menu will prompt with the number of jobs to run for the
     * simulation over data sets in input folder
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("SJF Scheduling Algorithm\n");
        if (args.length > 0) {
            sjf(new File(args[0]), 0);
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