
/**
 * Fabian Flores
 * Date created: 03/26/2020
 * Last modified: 03/27/2020
 * Table output aided by code found here ->
 * https://www.logicbig.com/how-to/code-snippets/jcode-java-cmd-command-line-table.html
 */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class FCFS {
    public FCFS() {
    }

    public static void main(String[] args) {
        File file;
        Scanner in;
        CommandLineTable table = new CommandLineTable();
        ArrayList<String> processNames = new ArrayList<>();
        ArrayList<Integer> processTimes = new ArrayList<>();
        ArrayList<Integer> completedTimes = new ArrayList<>();
        int totalTime = 0, startTime, sum;

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
            }

            for (int i = 0; i < processNames.size(); i++) {
                startTime = totalTime;
                totalTime += processTimes.get(i);
                completedTimes.set(i, totalTime);
                table.addRow(processNames.get(i), Integer.toString(startTime), Integer.toString(totalTime),
                        processNames.get(i) + " completed @ " + totalTime);
            }
            System.out.println("FCFS Scheduling Algorithm");
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
}