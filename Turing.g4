grammar Turing;


WS: [ \t\r]+ -> skip;
NEWLINE: '\n';

OPENER: '(';
CLOSER: ')';
COMMA: ',';

ARROW: '->';
EMPTY: '⬚';

DIRECTION: ('left' | 'right');

// equivalent to ~(OPENER | CLOSER | COMMA | EMPTY | WS) except this syntax is unsupported.
LEGAL_CHAR: ~('('|')'|','|'⬚'|' '|'\t'|'\r'|'\n');
STATE: LEGAL_CHAR+;

letter: LEGAL_CHAR | EMPTY;
lhs: OPENER STATE COMMA letter CLOSER;
rhs: OPENER STATE COMMA letter COMMA DIRECTION CLOSER;
turing_rule: lhs ARROW rhs;

// each rule ends with newline except the last one (optional newline). this last clause is also optional as the program could be empty
// antlr should resolve whichever is the case for the program correctly as these combinations don't conflict
program: (turing_rule NEWLINE)* (turing_rule NEWLINE?)?;

// antlr4 -no-listener .\Turing.g4;javac *.java;grun Turing program -gui 
// (qinit, ⬚) -> (q1, 1, right)
// ^Z enter
// note: blank will not be parsed properly by LEGAL_CHAR when using ASCII based programs (like grun, but it's fine with proper handling like in the intellij plugin)
