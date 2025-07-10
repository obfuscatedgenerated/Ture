grammar Turing;

SPACE: [ \t]+;
NEWLINE: '\n' | '\r\n' | '\r';

OPENER: '(';
CLOSER: ')';
COMMA: ',';

ARROW: '->';
EMPTY: '⬚';

DIRECTION: ('left' | 'right');

// equivalent to ~(OPENER | CLOSER | COMMA | EMPTY | SPACE | NEWLINE) except this syntax is unsupported.
LEGAL_CHAR: ESCAPED_PERCENT | ~('('|')'|','|'⬚'|' '|'\t'|'\n'|'\r');

ESCAPED_PERCENT: '\\%';
COMMENT: '%' ~[\n\r]* -> skip;

letter: LEGAL_CHAR | EMPTY;
state: LEGAL_CHAR+;

// whitespace is allowed between the elements of each rule but not in state names etc (so we aren't skipping whitespace in the lexer)
lhs: OPENER SPACE* from_state=state SPACE* COMMA SPACE* from_letter=letter SPACE* CLOSER;
rhs: OPENER SPACE* to_state=state SPACE* COMMA SPACE* to_letter=letter SPACE* COMMA SPACE* direction=DIRECTION SPACE* CLOSER;
turing_rule: SPACE* left=lhs SPACE* ARROW SPACE* right=rhs SPACE*;

program: (turing_rule | NEWLINE)*;

init_declaration: 'INIT' SPACE init_state=state;
program_with_init: init_declaration NEWLINE program;
