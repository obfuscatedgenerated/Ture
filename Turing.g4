grammar Turing;

WS: [ \t]+;
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
lhs: OPENER WS* state WS* COMMA WS* letter WS* CLOSER;
rhs: OPENER WS* state WS* COMMA WS* letter WS* COMMA WS* DIRECTION WS* CLOSER;
turing_rule: WS* lhs WS* ARROW WS* rhs WS*;

// each rule ends with newline except the last one (optional newline). this last clause is also optional as the program could be empty
// antlr should resolve whichever is the case for the program correctly as these combinations don't conflict
program: (turing_rule NEWLINE)* WS* (turing_rule NEWLINE?)? WS*;

// antlr4 -no-listener .\Turing.g4;javac *.java;grun Turing program -gui 
// (qinit, ⬚) -> (q1, 1, right)
// ^Z enter
// note: blank will not be parsed properly by LEGAL_CHAR when using ASCII based programs (like grun, but it's fine with proper handling like in the intellij plugin)
