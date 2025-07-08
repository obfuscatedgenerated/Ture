package codes.ollieg.Turing;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) {
        // Create a CharStream from input data
        String input = """
                % Store the first letter in the word as a state
                (qInit, a) -> (qA, a , right)
                (qInit, b) -> (qB, b, right)

                % Move to the end of the tape
                (qA, a) -> (qA, a, right)
                (qA, b) -> (qA, b, right)
                (qB, a) -> (qB, a, right)
                (qB, b) -> (qB, b, right)

                % Backtrack once when we reach the end of the tape
                (qA, ⬚) -> (qEndA, ⬚, left)
                (qB, ⬚) -> (qEndB, ⬚, left)

                % Replace the last letter with the first letter and enter the trap state
                (qEndA, a) -> (qHalt, a, right)
                (qEndA, b) -> (qHalt, a, right)
                (qEndB, a) -> (qHalt, b, right)
                (qEndB, b) -> (qHalt, b, right)""";
        CharStream charStream = CharStreams.fromString(input);

        // Create the lexer
        TuringLexer lexer = new TuringLexer(charStream);

        // Create a token stream from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Create the parser
        TuringParser parser = new TuringParser(tokens);

        // Begin parsing at the start rule
        ParseTree tree = parser.program();

        // execute
        TuringExecutor executor = new TuringExecutor();
        executor.visit(tree);
    }
}