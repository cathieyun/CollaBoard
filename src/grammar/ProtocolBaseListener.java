// Generated from src/grammar/Protocol.g4 by ANTLR 4.0

package grammar;


import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.ErrorNode;

public class ProtocolBaseListener implements ProtocolListener {
	@Override public void enterMakeboard(ProtocolParser.MakeboardContext ctx) { }
	@Override public void exitMakeboard(ProtocolParser.MakeboardContext ctx) { }

	@Override public void enterDraw(ProtocolParser.DrawContext ctx) { }
	@Override public void exitDraw(ProtocolParser.DrawContext ctx) { }

	@Override public void enterStroke(ProtocolParser.StrokeContext ctx) { }
	@Override public void exitStroke(ProtocolParser.StrokeContext ctx) { }

	@Override public void enterError(ProtocolParser.ErrorContext ctx) { }
	@Override public void exitError(ProtocolParser.ErrorContext ctx) { }

	@Override public void enterCoordinate(ProtocolParser.CoordinateContext ctx) { }
	@Override public void exitCoordinate(ProtocolParser.CoordinateContext ctx) { }

	@Override public void enterUndo(ProtocolParser.UndoContext ctx) { }
	@Override public void exitUndo(ProtocolParser.UndoContext ctx) { }

	@Override public void enterAction(ProtocolParser.ActionContext ctx) { }
	@Override public void exitAction(ProtocolParser.ActionContext ctx) { }

	@Override public void enterMakeusername(ProtocolParser.MakeusernameContext ctx) { }
	@Override public void exitMakeusername(ProtocolParser.MakeusernameContext ctx) { }

	@Override public void enterLine(ProtocolParser.LineContext ctx) { }
	@Override public void exitLine(ProtocolParser.LineContext ctx) { }

	@Override public void enterEnter(ProtocolParser.EnterContext ctx) { }
	@Override public void exitEnter(ProtocolParser.EnterContext ctx) { }

	@Override public void enterMake(ProtocolParser.MakeContext ctx) { }
	@Override public void exitMake(ProtocolParser.MakeContext ctx) { }

	@Override public void enterExit(ProtocolParser.ExitContext ctx) { }
	@Override public void exitExit(ProtocolParser.ExitContext ctx) { }

	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	@Override public void visitTerminal(TerminalNode node) { }
	@Override public void visitErrorNode(ErrorNode node) { }
}