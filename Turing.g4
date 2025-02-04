grammar Turing;


WS: [ \t\r\n]+ -> skip;

OPENER: '(';
CLOSER: ')';
COMMA: ',';

ARROW: '->';
EMPTY: '⬚';

DIRECTION: ('left' | 'right');

// TODO: this isnt exlcuding empty char???
LEGAL_CHAR: ~[(),\u2B1A \t\r\n]; // any single character that is not () , ⬚, or whitespace. equivalent to ~(OPENER | CLOSER | COMMA | EMPTY | WS) except this syntax is unsupported. square char is multiple characters so cant use simple exclude either. not sure why it needs to explicitly check WS but hey ho
STATE: LEGAL_CHAR+;

letter: LEGAL_CHAR | EMPTY;
lhs: OPENER STATE COMMA letter CLOSER;
rhs: OPENER STATE COMMA letter COMMA DIRECTION CLOSER;
turing_rule: lhs ARROW rhs;

// antlr4 -no-listener .\Turing.g4;javac *.java;grun Turing turing_rule -gui 
// (qinit, ⬚) -> (q1, 1, right)
// ^Z enter