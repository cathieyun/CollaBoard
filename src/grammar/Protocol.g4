/**
 * This file is the grammar file used by ANTLR.
 *
 * In order to compile this file, navigate to this directory
 * (<src/grammar>) and run the following command:
 *
 * java -jar ../../antlr.jar Protocol.g4
 */

grammar Protocol;

/*
 * This puts "package grammar;" at the top of the output Java files.
 * Do not change these lines unless you know what you're doing.
 */
@header {
package grammar;
}

/*
 * This adds code to the generated lexer and parser. Do not change these lines. 
 */
@members {
    // This method makes the lexer or parser stop running if it encounters
    // invalid input and throw a RuntimeException.
    public void reportErrorsAsExceptions() {
        removeErrorListeners();
        addErrorListener(new ExceptionThrowingErrorListener());
    }
    
    private static class ExceptionThrowingErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                Object offendingSymbol, int line, int charPositionInLine,
                String msg, RecognitionException e) {
            throw new RuntimeException(msg);
        }
    }
}

/*
 * These are the lexical rules. They define the tokens used by the lexer.
 */
DRAW: 'D:';
MAKE: 'M:';
UNDO: 'U:';
EXIT: 'X:';
ENTER: 'E:';
ERROR: 'E*';
INT:  '0' | [1-9] [0-9]*;
COLOR: 'BLACK' | 'BLUE' | 'CYAN' | 'GREEN' | 'MAGENTA' | 'ORANGE' | 'RED' | 'WHITE' | 'YELLOW';
THICKNESS: 'SMALL' | 'MED' | 'LARGE';
USERNAME: [a-zA-Z0-9]+;
OPENPAREN: '(';
CLOSEPAREN: ')';
COMMA: ',';
WHITESPACE : [ \t\r\n]+ -> skip ;

/*
 * These are the parser rules. They define the structures used by the parser.
 *
 * You should make sure you have one rule that describes the entire input.
 * This is the "start rule". The start rule should end with the special
 * predefined token EOF so that it describes the entire input. Below, we've made
 * "line" the start rule.
 *
 * For more information, see
 * http://www.antlr.org/wiki/display/ANTLR4/Parser+Rules#ParserRules-StartRulesandEOF
 */
line: action EOF;
action: draw|enter|exit|make|undo|error;
error: ERROR;
undo: UNDO stroke INT; //int = whiteboardID
draw: DRAW stroke INT; //int = whiteboardID
stroke: coordinate coordinate COLOR THICKNESS;
coordinate: OPENPAREN INT COMMA INT CLOSEPAREN;
make: makeboard|makeusername;
makeboard: MAKE INT; //int = whiteboardID
makeusername: MAKE USERNAME INT; //int = userID
enter: ENTER USERNAME INT; //int = whiteboardID
exit: EXIT USERNAME INT; //int = whiteboardID



