// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Bonus Task
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class BonusTask extends Task {
    int nodes;
    int edges;
    int clauses;
    int variables;
    int sumWeights;
    ArrayList<Integer> ans = new ArrayList<>();

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
        scanner.close();
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        variables = nodes;
        clauses = nodes + edges;
        sumWeights = 2 * nodes;
        FileWriter writer = new FileWriter(oracleInFilename);
        writer.write("p wcnf " + variables + " " + clauses + " " + (sumWeights + 1) +  "\n");
        // hard clauses
        Scanner scanner = new Scanner(new File(inFilename));
        scanner.nextLine();
        for (int i = 0; i < edges; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            writer.write((sumWeights + 1) + " " + x + " " + y + " 0\n");
        }
        scanner.close();
        // soft clauses
        for (int i = 1; i <= nodes; i++) {
            writer.write("2 " + -i + " 0\n");
        }

        writer.flush();
        writer.close();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        Scanner scanner = new Scanner(new File(oracleOutFilename));
        int copyNodes = scanner.nextInt();
        int copyEdges = scanner.nextInt();
        // verificare corectitudine algoritm
        assert copyNodes != nodes;
        assert copyEdges != edges;
        for (int i = 1; i <= nodes; i++) {
            ans.add(scanner.nextInt());
        }
        scanner.close();
    }

    @Override
    public void writeAnswer() throws IOException {
        String out = "";
        for (Integer integer : ans) {
            if (integer > 0) {
                out += integer;
                out += " ";
            }
        }
        out += "\n";
        FileWriter writer = new FileWriter(outFilename);
        writer.write(out);
        writer.flush();
        writer.close();
    }
}
