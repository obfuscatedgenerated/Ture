grammar Turing;

SPACE: [ \t]+;
NEWLINE: '\n' | '\r\n' | '\r';

OPENER: '(';
CLOSER: ')';
COMMA: ',';

ARROW: '->';
EMPTY: '⬚';

DIRECTION: ('left' | 'right');

// equivalent to ~(OPENER | CLOSER | COMMA | EMPTY | WS | NEWLINE) except this syntax is unsupported.
LEGAL_CHAR: ~('('|')'|','|'⬚'|' '|'\t'|'\n'|'\r');

letter: LEGAL_CHAR | EMPTY;
state: LEGAL_CHAR+;

// whitespace is allowed between the elements of each rule but not in state names etc (so we aren't skipping whitespace in the lexer)
lhs: OPENER SPACE* state SPACE* COMMA SPACE* letter SPACE* CLOSER;
rhs: OPENER SPACE* state SPACE* COMMA SPACE* letter SPACE* COMMA SPACE* DIRECTION SPACE* CLOSER;
turing_rule: SPACE* lhs SPACE* ARROW SPACE* rhs SPACE*;

// lines not starting with the opener (ignoring whitespace before it) are treated as comments
// we exclude newlines from the comment body to avoid ambiguity with the newline separator which is required between rules
// there is no prefix character for comments, they are just lines that don't start with (
// the impact of this is that comments cant begin with ( but i think that's fine, it could fallback if the rule doesn't match but that would be a bad debugging experience
comment: ~(OPENER | NEWLINE) (~NEWLINE)*;

line: turing_rule | comment;

// must be 1 to n newlines between rules. the last line can have 0 to n newlines. there may be no lines.
program: (line NEWLINE+ SPACE*)* (line NEWLINE*)?;
