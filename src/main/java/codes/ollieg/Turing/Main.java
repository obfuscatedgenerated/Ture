package codes.ollieg.Turing;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) {
        // Create a CharStream from input data
        String input = "% Store the first letter in the word as a state\n" +
                "(qInit, a) -> (qA, a , right)\n" +
                "(qInit, b) -> (qB, b, right)\n" +
                "\n" +
                "% Move to the end of the tape\n" +
                "(qA, a) -> (qA, a, right)\n" +
                "(qA, b) -> (qA, b, right)\n" +
                "(qB, a) -> (qB, a, right)\n" +
                "(qB, b) -> (qB, b, right)\n" +
                "\n" +
                "% Backtrack once when we reach the end of the tape\n" +
                "(qA, ⬚) -> (qEndA, ⬚, left)\n" +
                "(qB, ⬚) -> (qEndB, ⬚, left)\n" +
                "\n" +
                "% Replace the last letter with the first letter and enter the trap state\n" +
                "(qEndA, a) -> (qHalt, a, right)\n" +
                "(qEndA, b) -> (qHalt, a, right)\n" +
                "(qEndB, a) -> (qHalt, b, right)\n" +
                "(qEndB, b) -> (qHalt, b, right)";
        CharStream charStream = CharStreams.fromString(input);

        // Create the lexer
        TuringLexer lexer = new TuringLexer(charStream);

        // Create a token stream from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Create the parser
        TuringParser parser = new TuringParser(tokens);

        // Begin parsing at the start rule
        ParseTree tree = parser.program();

        // Print the parse tree
        System.out.println(tree.toStringTree(parser));
    }
}