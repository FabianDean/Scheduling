
/**
 * Fabian Flores
 * Date created: 03/26/2020
 * Last modified: 04/04/2020
 * Table output aided by code found here ->
 * https://www.logicbig.com/how-to/code-snippets/jcode-java-cmd-command-line-table.html
 */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class FCFS {
    public FCFS() {
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

    public static double fcfs(File file, int n) {
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
            if (n == 0)
                System.out.printf("= %.2f ms\n", averageTurnaroundTime);
            in.close();
            return averageTurnaroundTime;
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    public static void simulate(int n) {
        File file;
        int runs = 20;
        double sum = 0, averageTurnaroundTime;
        for (int i = 0; i < runs; i++) {
            file = new File("../input/jobs" + (i + 1) + ".txt");
            sum += fcfs(file, n);
        }
        averageTurnaroundTime = sum / runs;
        System.out.println("Average Turnaround Time for " + runs + " trials with " + n + " jobs");
        System.err.printf("= %.2f ms\n", averageTurnaroundTime);
    }

    public static void main(String[] args) {
        System.out.println("FCFS Scheduling Algorithm\n");
        if (args.length > 0) {
            fcfs(new File(args[0]), 0);
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