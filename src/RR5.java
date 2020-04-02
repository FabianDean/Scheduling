
/**
 * Fabian Flores
 * Date created: 03/24/2020
 * Last modified: 03/26/2020
 * Table output aided by code found here ->
 * https://www.logicbig.com/how-to/code-snippets/jcode-java-cmd-command-line-table.html
 */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class RR5 {
    public RR5() {
    }

    public static void main(String[] args) {
        File file;
        Scanner in;
        CommandLineTable table = new CommandLineTable();
        ArrayList<String> processNames = new ArrayList<>();
        ArrayList<Integer> processTimes = new ArrayList<>(); // preserve original process times
        ArrayList<Integer> completedTimes = new ArrayList<>();
        ArrayList<Integer> timeLeft = new ArrayList<>(); // copy of processTimes: calculations performed on this
        int burstTime = 5, totalTime = 0, startTime, time, sum;
        boolean done = false;

        try {
            file = new File(args[0]);
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
            System.out.println("RR-5 Scheduling Algorithm");
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
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}