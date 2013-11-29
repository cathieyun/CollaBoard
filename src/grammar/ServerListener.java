package grammar;

public class ServerListener extends ProtocolBaseListener{
    public void enterLine(ProtocolParser.LineContext ctx){
        
    }
    public void exitLine(ProtocolParser.LineContext ctx){
    }
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

    @Override public void enterEnter(ProtocolParser.EnterContext ctx) { }
    @Override public void exitEnter(ProtocolParser.EnterContext ctx) { }

    @Override public void enterMake(ProtocolParser.MakeContext ctx) { }
    @Override public void exitMake(ProtocolParser.MakeContext ctx) { }

    @Override public void enterExit(ProtocolParser.ExitContext ctx) { }
    @Override public void exitExit(ProtocolParser.ExitContext ctx) { }

}
