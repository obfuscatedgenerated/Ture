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

program: (turing_rule NEWLINE* SPACE*)*;

// note: blank will not be parsed properly by LEGAL_CHAR when using ASCII based programs (like grun, but it's fine with proper handling like in the intellij plugin)

/*
(qInit, a) -> (qA, a, right)
(qInit, b) -> (qB, b, right)

(qA, a) -> (qA, a, right)
(qA, b) -> (qA, b, right)
(qB, a) -> (qB, a, right)
(qB, b) -> (qB, b, right)

(qA, ⬚) -> (qEndA, ⬚, left)
(qB, ⬚) -> (qEndB, ⬚, left)

(qEndA, a) -> (qHalt, a, right)
(qEndA, b) -> (qHalt, a, right)
(qEndB, a) -> (qHalt, b, right)
(qEndB, b) -> (qHalt, b, right)
*/
