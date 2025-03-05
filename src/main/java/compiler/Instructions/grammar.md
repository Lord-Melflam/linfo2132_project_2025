grammar CustomLang;

// --- Tokens ---
INT: 'int';
FLOAT: 'float';
BOOL: 'bool';
STRING: 'string';

KEYWORDS: ;

// --- Literals ---
LITERAL: ;

// --- Identifiers ---
IDENTIFIER:;

// --- Operators ---
OPERATOR: ;
ASSIGNMENT: '=';

// --- Punctuation ---
SPECIAL_SYMBOL: ;

// --- Grammar Rules ---
program: constantDeclaration recordDeclaration globalVarDeclaration functionDeclaration ;

constantDeclaration: KEYWORDS IDENTIFIER type ASSIGNMENT expression SPECIAL_SYMBOL | epsilon |
program;
recordDeclaration: IDENTIFIER KEYWORDS SPECIAL_SYMBOL recordField SPECIAL_SYMBOL | epsilon |
program;
recordField: IDENTIFIER type SPECIAL_SYMBOL | epsilon | program;
globalVarDeclaration: IDENTIFIER type ASSIGNMENT expression SPECIAL_SYMBOL| epsilon | program;
functionDeclaration: KEYWORDS IDENTIFIER SPECIAL_SYMBOL parameterList SPECIAL_SYMBOL type
SPECIAL_SYMBOL statementList SPECIAL_SYMBOL | epsilon | program;

parameter: IDENTIFIER type;
parameterList: epsilon | parameter parameterListTail;
parameterListTail: epsilon | SPECIAL_SYMBOL parameter parameterListTail;

statement: variableDeclaration | assignment | controlStructure | functionCall SPECIAL_SYMBOL |
returnStatement;
statementList: epsilon | statement statementList;

variableDeclaration: IDENTIFIER type ASSIGNMENT expression SPECIAL_SYMBOL;
assignment: (IDENTIFIER | arrayAccess | recordAccess) ASSIGNMENT expression SPECIAL_SYMBOL;
arrayAccess: IDENTIFIER SPECIAL_SYMBOL expression SPECIAL_SYMBOL;
recordAccess: IDENTIFIER SPECIAL_SYMBOL IDENTIFIER;
functionCall: IDENTIFIER SPECIAL_SYMBOL parameterList SPECIAL_SYMBOL;
returnStatement: KEYWORDS optionalExpression SPECIAL_SYMBOL;

controlStructure: ifStatement | whileLoop | forLoop;
ifStatement: KEYWORDS SPECIAL_SYMBOL expression SPECIAL_SYMBOL SPECIAL_SYMBOL statementList
SPECIAL_SYMBOL (KEYWORDS SPECIAL_SYMBOL statementList SPECIAL_SYMBOL)?;
whileLoop: KEYWORDS SPECIAL_SYMBOL expression SPECIAL_SYMBOL SPECIAL_SYMBOL statementList
SPECIAL_SYMBOL;
forLoop: KEYWORDS SPECIAL_SYMBOL IDENTIFIER SPECIAL_SYMBOL expression SPECIAL_SYMBOL expression
SPECIAL_SYMBOL expression SPECIAL_SYMBOL SPECIAL_SYMBOL statementList SPECIAL_SYMBOL;

type: VOID | INT | FLOAT | BOOL | STRING | IDENTIFIER | IDENTIFIER SPECIAL_SYMBOL SPECIAL_SYMBOL;
typeSpecifier;

optionalExpression: epsilon | expression;

expression: primary expressionTail;
expressionTail: epsilon | binaryOp primary expressionTail;

primary: LITERAL
| IDENTIFIER
| SPECIAL_SYMBOL expression SPECIAL_SYMBOL
| functionCall
| arrayAccess
| recordAccess;

binaryOp: OPERATOR;





