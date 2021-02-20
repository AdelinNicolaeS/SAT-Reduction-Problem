// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Task1
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task1 extends Task {
    int nodes;
    int edges;
    int colors;
    int clauses;
    int variables;
    ArrayList<Integer> ans;
    boolean success;

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
        Scanner scanner = new Scanner(new File(inFilename));
        nodes = scanner.nextInt();
        edges = scanner.nextInt();
        colors = scanner.nextInt();
        variables = nodes * colors;
        clauses = edges * colors + nodes + nodes * colors * (colors - 1) / 2;
        ans = new ArrayList<>();
        scanner.close();
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        FileWriter writer = new FileWriter(oracleInFilename);
        writer.write("p cnf " + variables + " " + clauses + "\n");
        // 2 noduri adiacente nu au aceeasi culoare
        Scanner scanner = new Scanner(new File(inFilename));
        scanner.nextLine();
        for (int i = 0; i < edges; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            for (int c = 1; c <= colors; c++) {
                int z = -((x - 1) * colors + c);
                int t = -((y - 1) * colors + c);
                writer.write(z + " " + t + " 0\n");
            }
        }
        scanner.close();
        // un nod are macar o culoare
        for (int i = 1; i <= nodes; i++) {  // second condition
            String out = "";
            int number = (i - 1) * colors;
            for (int c = 1; c <= colors; c++) {
                number++;
                out = out + number;
                out = out + " ";
            }
            out += "0\n";
            writer.write(out);
        }
        for (int i = 1; i <= nodes; i++) {  // third condition
            int number = (i - 1) * colors;
            for (int j = 1; j < colors; j++) {
                int x = -(number + j);
                for (int k = j + 1; k <= colors; k++) {
                    int y = -(number + k);
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
                for (int j = 1; j <= colors; j++) {
                    if (ans.get((i - 1) * colors + j - 1) > 0) {
                        out = out + j;
                        out = out + " ";
                    }
                }
            }
            writer.println(out);
        }
        writer.close();
    }
}
