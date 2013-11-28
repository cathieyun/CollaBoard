// Generated from src/grammar/Protocol.g4 by ANTLR 4.0

package grammar;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ProtocolParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DRAW=1, MAKE=2, UNDO=3, EXIT=4, ENTER=5, ERROR=6, INT=7, COLOR=8, THICKNESS=9, 
		USERNAME=10, OPENPAREN=11, CLOSEPAREN=12, COMMA=13, WHITESPACE=14;
	public static final String[] tokenNames = {
		"<INVALID>", "'D:'", "'M:'", "'U:'", "'X:'", "'E:'", "'E*'", "INT", "COLOR", 
		"THICKNESS", "USERNAME", "'('", "')'", "','", "WHITESPACE"
	};
	public static final int
		RULE_line = 0, RULE_action = 1, RULE_error = 2, RULE_undo = 3, RULE_draw = 4, 
		RULE_stroke = 5, RULE_coordinate = 6, RULE_make = 7, RULE_makeboard = 8, 
		RULE_makeusername = 9, RULE_enter = 10, RULE_exit = 11;
	public static final String[] ruleNames = {
		"line", "action", "error", "undo", "draw", "stroke", "coordinate", "make", 
		"makeboard", "makeusername", "enter", "exit"
	};

	@Override
	public String getGrammarFileName() { return "Protocol.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }


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

	public ProtocolParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class LineContext extends ParserRuleContext {
		public ActionContext action() {
			return getRuleContext(ActionContext.class,0);
		}
		public TerminalNode EOF() { return getToken(ProtocolParser.EOF, 0); }
		public LineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_line; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitLine(this);
		}
	}

	public final LineContext line() throws RecognitionException {
		LineContext _localctx = new LineContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_line);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24); action();
			setState(25); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActionContext extends ParserRuleContext {
		public DrawContext draw() {
			return getRuleContext(DrawContext.class,0);
		}
		public ErrorContext error() {
			return getRuleContext(ErrorContext.class,0);
		}
		public UndoContext undo() {
			return getRuleContext(UndoContext.class,0);
		}
		public EnterContext enter() {
			return getRuleContext(EnterContext.class,0);
		}
		public MakeContext make() {
			return getRuleContext(MakeContext.class,0);
		}
		public ExitContext exit() {
			return getRuleContext(ExitContext.class,0);
		}
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitAction(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_action);
		try {
			setState(33);
			switch (_input.LA(1)) {
			case DRAW:
				enterOuterAlt(_localctx, 1);
				{
				setState(27); draw();
				}
				break;
			case ENTER:
				enterOuterAlt(_localctx, 2);
				{
				setState(28); enter();
				}
				break;
			case EXIT:
				enterOuterAlt(_localctx, 3);
				{
				setState(29); exit();
				}
				break;
			case MAKE:
				enterOuterAlt(_localctx, 4);
				{
				setState(30); make();
				}
				break;
			case UNDO:
				enterOuterAlt(_localctx, 5);
				{
				setState(31); undo();
				}
				break;
			case ERROR:
				enterOuterAlt(_localctx, 6);
				{
				setState(32); error();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ErrorContext extends ParserRuleContext {
		public TerminalNode ERROR() { return getToken(ProtocolParser.ERROR, 0); }
		public ErrorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_error; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterError(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitError(this);
		}
	}

	public final ErrorContext error() throws RecognitionException {
		ErrorContext _localctx = new ErrorContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_error);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(35); match(ERROR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UndoContext extends ParserRuleContext {
		public StrokeContext stroke() {
			return getRuleContext(StrokeContext.class,0);
		}
		public TerminalNode INT() { return getToken(ProtocolParser.INT, 0); }
		public TerminalNode UNDO() { return getToken(ProtocolParser.UNDO, 0); }
		public UndoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_undo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterUndo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitUndo(this);
		}
	}

	public final UndoContext undo() throws RecognitionException {
		UndoContext _localctx = new UndoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_undo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37); match(UNDO);
			setState(38); stroke();
			setState(39); match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DrawContext extends ParserRuleContext {
		public StrokeContext stroke() {
			return getRuleContext(StrokeContext.class,0);
		}
		public TerminalNode INT() { return getToken(ProtocolParser.INT, 0); }
		public TerminalNode DRAW() { return getToken(ProtocolParser.DRAW, 0); }
		public DrawContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_draw; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterDraw(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitDraw(this);
		}
	}

	public final DrawContext draw() throws RecognitionException {
		DrawContext _localctx = new DrawContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_draw);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41); match(DRAW);
			setState(42); stroke();
			setState(43); match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StrokeContext extends ParserRuleContext {
		public TerminalNode THICKNESS() { return getToken(ProtocolParser.THICKNESS, 0); }
		public List<CoordinateContext> coordinate() {
			return getRuleContexts(CoordinateContext.class);
		}
		public CoordinateContext coordinate(int i) {
			return getRuleContext(CoordinateContext.class,i);
		}
		public TerminalNode COLOR() { return getToken(ProtocolParser.COLOR, 0); }
		public StrokeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stroke; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterStroke(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitStroke(this);
		}
	}

	public final StrokeContext stroke() throws RecognitionException {
		StrokeContext _localctx = new StrokeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_stroke);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45); coordinate();
			setState(46); coordinate();
			setState(47); match(COLOR);
			setState(48); match(THICKNESS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CoordinateContext extends ParserRuleContext {
		public TerminalNode CLOSEPAREN() { return getToken(ProtocolParser.CLOSEPAREN, 0); }
		public List<TerminalNode> INT() { return getTokens(ProtocolParser.INT); }
		public TerminalNode OPENPAREN() { return getToken(ProtocolParser.OPENPAREN, 0); }
		public TerminalNode COMMA() { return getToken(ProtocolParser.COMMA, 0); }
		public TerminalNode INT(int i) {
			return getToken(ProtocolParser.INT, i);
		}
		public CoordinateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_coordinate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterCoordinate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitCoordinate(this);
		}
	}

	public final CoordinateContext coordinate() throws RecognitionException {
		CoordinateContext _localctx = new CoordinateContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_coordinate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50); match(OPENPAREN);
			setState(51); match(INT);
			setState(52); match(COMMA);
			setState(53); match(INT);
			setState(54); match(CLOSEPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeContext extends ParserRuleContext {
		public MakeboardContext makeboard() {
			return getRuleContext(MakeboardContext.class,0);
		}
		public MakeusernameContext makeusername() {
			return getRuleContext(MakeusernameContext.class,0);
		}
		public MakeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_make; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterMake(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitMake(this);
		}
	}

	public final MakeContext make() throws RecognitionException {
		MakeContext _localctx = new MakeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_make);
		try {
			setState(58);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(56); makeboard();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(57); makeusername();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeboardContext extends ParserRuleContext {
		public TerminalNode MAKE() { return getToken(ProtocolParser.MAKE, 0); }
		public TerminalNode INT() { return getToken(ProtocolParser.INT, 0); }
		public MakeboardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeboard; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterMakeboard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitMakeboard(this);
		}
	}

	public final MakeboardContext makeboard() throws RecognitionException {
		MakeboardContext _localctx = new MakeboardContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_makeboard);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60); match(MAKE);
			setState(61); match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MakeusernameContext extends ParserRuleContext {
		public TerminalNode MAKE() { return getToken(ProtocolParser.MAKE, 0); }
		public TerminalNode INT() { return getToken(ProtocolParser.INT, 0); }
		public TerminalNode USERNAME() { return getToken(ProtocolParser.USERNAME, 0); }
		public MakeusernameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeusername; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterMakeusername(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitMakeusername(this);
		}
	}

	public final MakeusernameContext makeusername() throws RecognitionException {
		MakeusernameContext _localctx = new MakeusernameContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_makeusername);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63); match(MAKE);
			setState(64); match(USERNAME);
			setState(65); match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnterContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(ProtocolParser.INT, 0); }
		public TerminalNode USERNAME() { return getToken(ProtocolParser.USERNAME, 0); }
		public TerminalNode ENTER() { return getToken(ProtocolParser.ENTER, 0); }
		public EnterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterEnter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitEnter(this);
		}
	}

	public final EnterContext enter() throws RecognitionException {
		EnterContext _localctx = new EnterContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_enter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67); match(ENTER);
			setState(68); match(USERNAME);
			setState(69); match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExitContext extends ParserRuleContext {
		public TerminalNode EXIT() { return getToken(ProtocolParser.EXIT, 0); }
		public TerminalNode INT() { return getToken(ProtocolParser.INT, 0); }
		public TerminalNode USERNAME() { return getToken(ProtocolParser.USERNAME, 0); }
		public ExitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).enterExit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ProtocolListener ) ((ProtocolListener)listener).exitExit(this);
		}
	}

	public final ExitContext exit() throws RecognitionException {
		ExitContext _localctx = new ExitContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_exit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71); match(EXIT);
			setState(72); match(USERNAME);
			setState(73); match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\2\3\20N\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t"+
		"\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\5\3$\n\3\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\5\t=\n\t\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\2\16\2\4\6\b\n\f\16\20\22\24"+
		"\26\30\2\2G\2\32\3\2\2\2\4#\3\2\2\2\6%\3\2\2\2\b\'\3\2\2\2\n+\3\2\2\2"+
		"\f/\3\2\2\2\16\64\3\2\2\2\20<\3\2\2\2\22>\3\2\2\2\24A\3\2\2\2\26E\3\2"+
		"\2\2\30I\3\2\2\2\32\33\5\4\3\2\33\34\7\1\2\2\34\3\3\2\2\2\35$\5\n\6\2"+
		"\36$\5\26\f\2\37$\5\30\r\2 $\5\20\t\2!$\5\b\5\2\"$\5\6\4\2#\35\3\2\2\2"+
		"#\36\3\2\2\2#\37\3\2\2\2# \3\2\2\2#!\3\2\2\2#\"\3\2\2\2$\5\3\2\2\2%&\7"+
		"\b\2\2&\7\3\2\2\2\'(\7\5\2\2()\5\f\7\2)*\7\t\2\2*\t\3\2\2\2+,\7\3\2\2"+
		",-\5\f\7\2-.\7\t\2\2.\13\3\2\2\2/\60\5\16\b\2\60\61\5\16\b\2\61\62\7\n"+
		"\2\2\62\63\7\13\2\2\63\r\3\2\2\2\64\65\7\r\2\2\65\66\7\t\2\2\66\67\7\17"+
		"\2\2\678\7\t\2\289\7\16\2\29\17\3\2\2\2:=\5\22\n\2;=\5\24\13\2<:\3\2\2"+
		"\2<;\3\2\2\2=\21\3\2\2\2>?\7\4\2\2?@\7\t\2\2@\23\3\2\2\2AB\7\4\2\2BC\7"+
		"\f\2\2CD\7\t\2\2D\25\3\2\2\2EF\7\7\2\2FG\7\f\2\2GH\7\t\2\2H\27\3\2\2\2"+
		"IJ\7\6\2\2JK\7\f\2\2KL\7\t\2\2L\31\3\2\2\2\4#<";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}