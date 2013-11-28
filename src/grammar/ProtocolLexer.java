// Generated from src/grammar/Protocol.g4 by ANTLR 4.0

package grammar;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ProtocolLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DRAW=1, MAKE=2, UNDO=3, EXIT=4, ENTER=5, ERROR=6, INT=7, COLOR=8, THICKNESS=9, 
		USERNAME=10, OPENPAREN=11, CLOSEPAREN=12, COMMA=13, WHITESPACE=14;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'D:'", "'M:'", "'U:'", "'X:'", "'E:'", "'E*'", "INT", "COLOR", "THICKNESS", 
		"USERNAME", "'('", "')'", "','", "WHITESPACE"
	};
	public static final String[] ruleNames = {
		"DRAW", "MAKE", "UNDO", "EXIT", "ENTER", "ERROR", "INT", "COLOR", "THICKNESS", 
		"USERNAME", "OPENPAREN", "CLOSEPAREN", "COMMA", "WHITESPACE"
	};


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


	public ProtocolLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Protocol.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 13: WHITESPACE_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WHITESPACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\2\4\20\u008b\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b"+
		"\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2"+
		"\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3"+
		"\b\3\b\3\b\7\b\65\n\b\f\b\16\b8\13\b\5\b:\n\b\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\5\ti\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\5\nx\n\n\3\13\6\13{\n\13\r\13\16\13|\3\f\3\f\3\r\3\r\3\16\3\16"+
		"\3\17\6\17\u0086\n\17\r\17\16\17\u0087\3\17\3\17\2\20\3\3\1\5\4\1\7\5"+
		"\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17"+
		"\1\35\20\2\3\2\6\3\63;\3\62;\5\62;C\\c|\5\13\f\17\17\"\"\u0098\2\3\3\2"+
		"\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17"+
		"\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2"+
		"\2\2\2\33\3\2\2\2\2\35\3\2\2\2\3\37\3\2\2\2\5\"\3\2\2\2\7%\3\2\2\2\t("+
		"\3\2\2\2\13+\3\2\2\2\r.\3\2\2\2\179\3\2\2\2\21h\3\2\2\2\23w\3\2\2\2\25"+
		"z\3\2\2\2\27~\3\2\2\2\31\u0080\3\2\2\2\33\u0082\3\2\2\2\35\u0085\3\2\2"+
		"\2\37 \7F\2\2 !\7<\2\2!\4\3\2\2\2\"#\7O\2\2#$\7<\2\2$\6\3\2\2\2%&\7W\2"+
		"\2&\'\7<\2\2\'\b\3\2\2\2()\7Z\2\2)*\7<\2\2*\n\3\2\2\2+,\7G\2\2,-\7<\2"+
		"\2-\f\3\2\2\2./\7G\2\2/\60\7,\2\2\60\16\3\2\2\2\61:\7\62\2\2\62\66\t\2"+
		"\2\2\63\65\t\3\2\2\64\63\3\2\2\2\658\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2"+
		"\2\67:\3\2\2\28\66\3\2\2\29\61\3\2\2\29\62\3\2\2\2:\20\3\2\2\2;<\7D\2"+
		"\2<=\7N\2\2=>\7C\2\2>?\7E\2\2?i\7M\2\2@A\7D\2\2AB\7N\2\2BC\7W\2\2Ci\7"+
		"G\2\2DE\7E\2\2EF\7[\2\2FG\7C\2\2Gi\7P\2\2HI\7I\2\2IJ\7T\2\2JK\7G\2\2K"+
		"L\7G\2\2Li\7P\2\2MN\7O\2\2NO\7C\2\2OP\7I\2\2PQ\7G\2\2QR\7P\2\2RS\7V\2"+
		"\2Si\7C\2\2TU\7Q\2\2UV\7T\2\2VW\7C\2\2WX\7P\2\2XY\7I\2\2Yi\7G\2\2Z[\7"+
		"T\2\2[\\\7G\2\2\\i\7F\2\2]^\7Y\2\2^_\7J\2\2_`\7K\2\2`a\7V\2\2ai\7G\2\2"+
		"bc\7[\2\2cd\7G\2\2de\7N\2\2ef\7N\2\2fg\7Q\2\2gi\7Y\2\2h;\3\2\2\2h@\3\2"+
		"\2\2hD\3\2\2\2hH\3\2\2\2hM\3\2\2\2hT\3\2\2\2hZ\3\2\2\2h]\3\2\2\2hb\3\2"+
		"\2\2i\22\3\2\2\2jk\7U\2\2kl\7O\2\2lm\7C\2\2mn\7N\2\2nx\7N\2\2op\7O\2\2"+
		"pq\7G\2\2qx\7F\2\2rs\7N\2\2st\7C\2\2tu\7T\2\2uv\7I\2\2vx\7G\2\2wj\3\2"+
		"\2\2wo\3\2\2\2wr\3\2\2\2x\24\3\2\2\2y{\t\4\2\2zy\3\2\2\2{|\3\2\2\2|z\3"+
		"\2\2\2|}\3\2\2\2}\26\3\2\2\2~\177\7*\2\2\177\30\3\2\2\2\u0080\u0081\7"+
		"+\2\2\u0081\32\3\2\2\2\u0082\u0083\7.\2\2\u0083\34\3\2\2\2\u0084\u0086"+
		"\t\5\2\2\u0085\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0085\3\2\2\2\u0087"+
		"\u0088\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008a\b\17\2\2\u008a\36\3\2\2"+
		"\2\t\2\669hw|\u0087";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}