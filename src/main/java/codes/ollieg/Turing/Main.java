package codes.ollieg.Turing;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            throw new IllegalArgumentException("Please provide a file to execute.");
        }

        CharStream charStream = CharStreams.fromFileName(args[0]);

        // Create the lexer
        TuringLexer lexer = new TuringLexer(charStream);

        // Create a token stream from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Create the parser
        TuringParser parser = new TuringParser(tokens);

        // Begin parsing at the start rule
        ParseTree tree = parser.program_with_init();

        // visit and parse
        TuringExecutor executor = new TuringExecutor();
        executor.visit(tree);

        // get tape input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tape input: ");

        // execute
        char[] in_tape = scanner.nextLine().toCharArray();
        char[] out_tape = executor.execute(in_tape);

        // print tape output until the last non empty character
        System.out.print("Tape output: ");
        System.out.flush();

        // move pointer back from end of string until the first instance of a non empty
        int end_ptr = out_tape.length - 1;
        while (end_ptr >= 0 && out_tape[end_ptr] == executor.EMPTY) {
            end_ptr--;
        }

        // print characters until the end pointer is reached
        for (int i = 0; i <= end_ptr; i++) {
            System.out.print(out_tape[i]);
        }

        // newline and flush
        System.out.println();
    }
}

// TODO: gui
