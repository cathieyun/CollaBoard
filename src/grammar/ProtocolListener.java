// Generated from src/grammar/Protocol.g4 by ANTLR 4.0

package grammar;

import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface ProtocolListener extends ParseTreeListener {
	void enterMakeboard(ProtocolParser.MakeboardContext ctx);
	void exitMakeboard(ProtocolParser.MakeboardContext ctx);

	void enterDraw(ProtocolParser.DrawContext ctx);
	void exitDraw(ProtocolParser.DrawContext ctx);

	void enterStroke(ProtocolParser.StrokeContext ctx);
	void exitStroke(ProtocolParser.StrokeContext ctx);

	void enterError(ProtocolParser.ErrorContext ctx);
	void exitError(ProtocolParser.ErrorContext ctx);

	void enterCoordinate(ProtocolParser.CoordinateContext ctx);
	void exitCoordinate(ProtocolParser.CoordinateContext ctx);

	void enterUndo(ProtocolParser.UndoContext ctx);
	void exitUndo(ProtocolParser.UndoContext ctx);

	void enterAction(ProtocolParser.ActionContext ctx);
	void exitAction(ProtocolParser.ActionContext ctx);

	void enterMakeusername(ProtocolParser.MakeusernameContext ctx);
	void exitMakeusername(ProtocolParser.MakeusernameContext ctx);

	void enterLine(ProtocolParser.LineContext ctx);
	void exitLine(ProtocolParser.LineContext ctx);

	void enterEnter(ProtocolParser.EnterContext ctx);
	void exitEnter(ProtocolParser.EnterContext ctx);

	void enterMake(ProtocolParser.MakeContext ctx);
	void exitMake(ProtocolParser.MakeContext ctx);

	void enterExit(ProtocolParser.ExitContext ctx);
	void exitExit(ProtocolParser.ExitContext ctx);
}