package codes.ollieg.Turing;

import java.util.HashMap;
import java.util.Map;

public class TuringExecutor extends TuringBaseVisitor<String> {
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

    @Override
    public String visitLhs(TuringParser.LhsContext ctx) {
        return visit(ctx.from_state) + "#" + visit(ctx.from_letter);
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
}
