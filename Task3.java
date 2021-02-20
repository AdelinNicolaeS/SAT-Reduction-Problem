// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Task3
 * This being an optimization problem, the solve method's logic has to work differently.
 * You have to search for the minimum number of arrests by successively querying the oracle.
 * Hint: it might be easier to reduce the current task to a previously solved task
 */
public class Task3 extends Task {
    String task2InFilename;
    String task2OutFilename;
    int nodes;
    int edges;
    int k;
    int[][] graph;
    ArrayList<Integer> ans;

    @Override
    public void solve() throws IOException, InterruptedException {
        task2InFilename = inFilename + "_t2";
        task2OutFilename = outFilename + "_t2";
        Task2 task2Solver = new Task2();
        task2Solver.addFiles(task2InFilename, oracleInFilename, oracleOutFilename, task2OutFilename);
        readProblemData();

        reduceToTask2(nodes);
        task2Solver.readProblemData();
        boolean ok = false;
        while(!ok) {
            task2Solver.solve();
            if (task2Solver.success) {
                ok = true;
                k = task2Solver.getK();
            } else {
                reduceToTask2(task2Solver.getK() - 1);
            }
        }
        extractAnswerFromTask2();
        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
        Scanner scanner = new Scanner(new File(inFilename));
        nodes = scanner.nextInt();
        edges = scanner.nextInt();
        graph = new int[nodes + 1][nodes + 1];
        ans = new ArrayList<>();
        for (int i = 0; i <= nodes; i++) {
            for (int j = 0; j <= nodes; j++) {
                graph[i][j] = 0;
            }
        }
        for (int i = 0; i < edges; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            graph[x][y] = 1;
            graph[y][x] = 1;
        }
        scanner.close();
    }

    public void reduceToTask2(int k) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(task2InFilename);
        writer.println(nodes + " " + ((nodes - 1) * nodes / 2 - edges) + " " + k);
        for (int i = 1; i <= nodes; i++) {
            for (int j = i + 1; j <= nodes; j++) {
                if (graph[i][j] == 0) {
                    writer.println(i + " " + j);
                }
            }
        }
        writer.close();
    }

    public void extractAnswerFromTask2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(oracleOutFilename));
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("True")) {
            int count = scanner.nextInt();
            for (int i = 0; i < count; i++) {
                ans.add(scanner.nextInt());
            }
        }
        scanner.close();
    }

    @Override
    public void writeAnswer() throws IOException {
        PrintWriter writer = new PrintWriter(new File(outFilename));
        ArrayList<Integer> cliqueList = new ArrayList<>();
        String out = "";
        for (int i = 1; i <= nodes; i++) {
            for (int j = 1; j <= k; j++) {
                if (ans.get((i - 1) * k + j - 1) > 0) {
                    cliqueList.add(i);
                }
            }
        }
        for (int i = 1; i <= nodes; i++) {
            if (!cliqueList.contains(i)) {
                out += i;
                out += " ";
            }
        }
        System.out.println(out);
        writer.println(out);
        writer.close();
    }
}
