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

        System.out.print("Tape output: ");
        System.out.println(out_tape);
    }
}

// TODO: gui
