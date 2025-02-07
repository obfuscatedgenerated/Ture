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

COMMENT: '%' ~[\n\r]* -> skip;

turing_line: turing_rule NEWLINE+;

// must be 1 to n newlines between rules. the last line can have 0 to n newlines. there may be no lines.
program: (turing_line | NEWLINE)* turing_rule?;
