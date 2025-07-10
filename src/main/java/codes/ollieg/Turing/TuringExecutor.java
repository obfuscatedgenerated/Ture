package codes.ollieg.Turing;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TuringExecutor extends TuringBaseVisitor<String> {
    public final char EMPTY = '⬚';

    private boolean parsed = false;

    private String init_state = null;
    private final Map<String, TuringParser.RhsContext> lookup = new HashMap<>();

    @Override
    public String visitLetter(TuringParser.LetterContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitState(TuringParser.StateContext ctx) {
        return ctx.getText();
    }

    private String constructLookupKey(String state, char letter) {
        return state + "#" + letter;
    }

    @Override
    public String visitLhs(TuringParser.LhsContext ctx) {
        return constructLookupKey(visit(ctx.from_state), visit(ctx.from_letter).charAt(0));
    }

    @Override
    public String visitInit_declaration(TuringParser.Init_declarationContext ctx) {
        if (init_state != null) {
            throw new RuntimeException("multiple init declarations");
        }

        init_state = visitState(ctx.init_state);

        return super.visitInit_declaration(ctx);
    }

    @Override
    public String visitTuring_rule(TuringParser.Turing_ruleContext ctx) {
        String lookup_key = visit(ctx.left);

        if (lookup.containsKey(lookup_key)) {
            throw new RuntimeException("duplicate lhs for " + lookup_key + ". non-deterministic turing machines not yet supported");
        }

        lookup.put(lookup_key, ctx.right);

        return super.visitTuring_rule(ctx);
    }

    @Override
    public String visit(ParseTree tree) {
        String result = super.visit(tree);
        parsed = true;
        return result;
    }

    private enum ExecResultStatus {
        Halt,
        Left,
        Right
    }

    private class ExecResult {
        public ExecResultStatus status;
        public char[] new_tape;

        ExecResult(ExecResultStatus status, char[] new_tape) {
            this.status = status;
            this.new_tape = new_tape;
        }
    }

    private String current_state = null;

    private char[] writeTapeLetter(char[] in_tape, int pos, char letter) {
        if (pos < in_tape.length) {
            char[] new_tape = in_tape.clone();
            new_tape[pos] = letter;
            return new_tape;
        }

        // need to expand tape
        char[] new_tape = new char[pos + 1];
        System.arraycopy(in_tape, 0, new_tape, 0, in_tape.length);

        // fill space with empties
        // TODO: is it easier to just make the rule execution treat a null char as the empty char
        int start = in_tape.length;
        for (int i = start; i <= pos; i++) {
            new_tape[i] = EMPTY;
        }

        // safely perform replacement as usual
        new_tape[pos] = letter;
        return new_tape;
    }

    private ExecResult executeStep(char[] tape, int pos) {
        char current_letter;
        if (pos >= tape.length) {
            current_letter = EMPTY;
        } else {
            current_letter = tape[pos];
        }

        String key = constructLookupKey(current_state, current_letter);
        TuringParser.RhsContext rhs = lookup.get(key);

        if (rhs == null) {
            // no rules apply, halt
            return new ExecResult(ExecResultStatus.Halt, tape);
        }

        // write the letter to the tape
        char letter = visit(rhs.to_letter).charAt(0);
        char[] new_tape = writeTapeLetter(tape, pos, letter);

        // update the state
        current_state = visit(rhs.to_state);

        // return the direction
        String dir_str = rhs.direction.getText();
        if (dir_str == null) {
            throw new RuntimeException("direction token could not be read");
        }

        if (dir_str.equals("left")) {
            return new ExecResult(ExecResultStatus.Left, new_tape);
        } else if (dir_str.equals("right")) {
            return new ExecResult(ExecResultStatus.Right, new_tape);
        } else {
            throw new RuntimeException("invalid direction token");
        }
    }

    public char[] execute(char[] in_tape, int init_pos) {
        if (!parsed) {
            throw new IllegalStateException("rules not yet parsed. call visitor first");
        }

        if (init_pos < 0) {
            throw new IllegalArgumentException("init_pos cannot be less than 0");
        }

        if (init_pos > in_tape.length - 1) {
            throw new IllegalArgumentException("init_pos is outside tape range");
        }

        current_state = init_state;
        int pos = init_pos;
        char[] tape = in_tape;

        while (true) {
            ExecResult res = executeStep(tape, pos);

            // update tape
            tape = res.new_tape;

            // update position or halt
            if (res.status == ExecResultStatus.Right) {
                // unbounded to right
                // TODO: is this correct? should it be configurable?
                pos += 1;
            } else if (res.status == ExecResultStatus.Left) {
                // bounded on left
                if (pos != 0) {
                    pos -= 1;
                }
            } else {
                break;
            }
        }

        // TODO: trim trailing empties

        return tape;
    }

    public char[] execute(char[] in_tape) {
        return execute(in_tape, 0);
    }
}
