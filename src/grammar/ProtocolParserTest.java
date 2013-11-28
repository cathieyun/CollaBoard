package grammar;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;


public class ProtocolParserTest {

    @Test
    public void basicDrawTest(){
        
    }
    /**
     * parserTest checks to see if the input .abc is lexed, parsed, and listener walks through
     *      tree correctly, asserts that expectedOutput and input are equal
     * @param input String of the .abc file being tested
     * @param expectedOutput String of what we expect after input is parsed
     */
    public void parserTest(String input, String expectedOutput){

        CharStream stream = new ANTLRInputStream(input);
        ProtocolLexer lexer = new ProtocolLexer(stream);
        lexer.reportErrorsAsExceptions();

        TokenStream tokens = new CommonTokenStream(lexer);
        ProtocolParser parser = new ProtocolParser(tokens);
        parser.reportErrorsAsExceptions();

        ParseTree tree;
        tree = parser.line(); // "line" is the starter rule.
        ParseTreeWalker walker = new ParseTreeWalker();
        //ParseTreeListener listener = new Listener();
    }
}
