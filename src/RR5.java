
/**
 * Fabian Flores
 * Date created: 03/24/2020
 * Last modified: 04/07/2020
 * Table output aided by code found here ->
 * https://www.logicbig.com/how-to/code-snippets/jcode-java-cmd-command-line-table.html
 */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class RR5 {
    public RR5() {
    }

    /**
     * Perform Round Robin (w/ burst time 5) scheduling algorithm
     * 
     * @param file method needs a file that contains the job names and lengths
     * @param n    number of jobs in the file. Used for running the simulations. n
     *             == 0 when a file is given as an argument in the program
     */
    public static double rr5(File file, int n) {
        Scanner in;
        CommandLineTable table = new CommandLineTable();
        ArrayList<String> processNames = new ArrayList<>();
        ArrayList<Integer> processTimes = new ArrayList<>(); // preserve original process times
        ArrayList<Integer> completedTimes = new ArrayList<>(); // will be used to process average turnaround time
        ArrayList<Integer> timeLeft = new ArrayList<>(); // copy of processTimes: calculations performed on this
        int burstTime = 5, totalTime = 0, startTime, time, sum; // time is assigned the remaining processing time for a
                                                                // job each iteration (basically to stray from using
                                                                // timeLeft.get(i) and clean up code)
        boolean done = false;

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
                timeLeft.add(processTimes.get(i));
            }
            while (!done) {
                sum = 0;
                for (int i = 0; i < timeLeft.size(); i++) {
                    time = timeLeft.get(i);
                    startTime = totalTime; // record for Schedule Table
                    // skip iteration if process is already completed
                    if (time == 0) {
                        continue;
                    }
                    if (time <= burstTime) {
                        totalTime += time;
                        time -= time;
                    } else {
                        totalTime += burstTime;
                        time -= burstTime;
                    }
                    if (time == 0) {
                        completedTimes.set(i, totalTime);
                        timeLeft.set(i, 0); // set to 0 to skip in future iterations
                        table.addRow(processNames.get(i), Integer.toString(startTime), Integer.toString(totalTime),
                                "Job completed @ " + totalTime);
                    } else {
                        table.addRow(processNames.get(i), Integer.toString(startTime), Integer.toString(totalTime), "");
                        timeLeft.set(i, time);
                    }
                }
                // get sum to check if all processes completed
                for (Integer t : timeLeft)
                    sum += t;
                // if no processes have any time left, prep for loop exit
                if (sum == 0)
                    done = true;
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
        // loop through each input data file and run algorithm
        for (int i = 0; i < runs; i++) {
            file = new File("../input/jobs" + (i + 1) + ".txt");
            sum += rr5(file, n);
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
        System.out.println("RR-2 Scheduling Algorithm");
        if (args.length > 0) {
            rr5(new File(args[0]), 0);
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