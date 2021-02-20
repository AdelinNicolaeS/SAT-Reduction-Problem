// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Task2
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task2 extends Task {
    int nodes;
    int edges;
    int k;
    int[][] graph;
    int clauses;
    int variables;
    ArrayList<Integer> ans = new ArrayList<>();
    boolean success = false;
    
    public int getK() {
        return k;
    }

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
        ans.clear();
        Scanner scanner = new Scanner(new File(inFilename));
        nodes = scanner.nextInt();
        edges = scanner.nextInt();
        k = scanner.nextInt();
        graph = new int[nodes + 1][nodes + 1];
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

    @Override
    public void formulateOracleQuestion() throws IOException {
        variables = nodes * k;
        clauses = (nodes * (nodes - 1) / 2 - edges) * k * (k - 1) + k + ((k - 1) * k / 2) * nodes;
        FileWriter writer = new FileWriter(oracleInFilename);
        writer.write("p cnf " + variables + " " + clauses + "\n");
        // 2 noduri neadiacente nu se pot afla in clica
        for (int i = 1; i <= nodes; i++) {
            for (int j = 1; j <= nodes; j++) {
                if (i > j && graph[i][j] == 0) {
                    int l, m;
                    for (l = 1; l <= k; l++) {
                        int x = -((i - 1) * k + l);
                        for (m = 1; m <= k; m++) {
                            if (l != m) {
                                int y = -((j - 1) * k + m);
                                String out = x + " " + y + " 0\n";
                                writer.write(out);
                            }
                        }
                    }
                }
            }
        }
        // pe o pozitie din clica este cel putin un nod
        for (int i = 1; i <= k; i++) {
            String out = "";
            for (int j = 1; j <= nodes; j++) {
                int x = (j - 1) * k + i;
                out += x;
                out += " ";
            }
            out += "0\n";
            writer.write(out);
        }
        // un nod sa nu fie simultan pe 2 pozitii in clica
        for (int i = 1; i <= nodes; i++) {
            int number = (i - 1) * k;
            for (int j = 1; j < k; j++) {
                int x = -(number + j);
                for (int l = j + 1; l <= k; l++) {
                    int y = -(number + l);
                    writer.write(x + " " + y + " 0\n");
                }
            }
        }

        writer.flush();
        writer.close();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        Scanner scanner = new Scanner(new File(oracleOutFilename));
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("True")) {
            success = true;
            int count = scanner.nextInt();
            for (int i = 0; i < count; i++) {
                ans.add(scanner.nextInt());
            }
        } else {
            success = false;
        }
        
        scanner.close();
    }

    @Override
    public void writeAnswer() throws IOException {

        PrintWriter writer = new PrintWriter(outFilename);
        if (!success) {
            writer.println("False");
        } else {
            writer.println("True");
            String out = "";
            for (int i = 1; i <= nodes; i++) {
                for (int j = 1; j <= k; j++) {
                    if (ans.get((i - 1) * k + j - 1) > 0) {
                        out += i;
                        out += " ";
                    }
                }
            }
            writer.println(out);
        }
        writer.close();
    }

}
