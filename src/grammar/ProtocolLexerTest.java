package grammar;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;


public class ProtocolLexerTest {
    
    @Test
    public void drawTest(){
        verifyLexer("D: (1, 1) (2, 2) BLUE BIG", new String[] {"D:","(","1",",", "1",")","(", "2",",","2",")","BLUE", "BIG"});     		
    }
    
    @Test
    public void enterExitTest(){
        verifyLexer("E: bob", new String[]{"E:", "bob"});
        verifyLexer("X: bob", new String[]{"X:", "bob"});
    }
    
    @Test
    public void undoTest(){
        verifyLexer("U: (1, 1) (2, 2) RED SMALL", new String[] {"U:", "(","1",",", "1",")","(", "2",",","2",")","RED", "SMALL"}); 
    }
    
    @Test
    public void createTest(){
        verifyLexer("M: 123", new String[]{"M:", "123"});
    }
    
    public void verifyLexer(String input, String[] expectedTokens) {
        CharStream stream = new ANTLRInputStream(input);
        ProtocolLexer lexer = new ProtocolLexer(stream);
        lexer.reportErrorsAsExceptions();
        List<? extends Token> actualTokens = lexer.getAllTokens();
        assertEquals(expectedTokens.length, actualTokens.size());
        
        
        for(int i = 0; i < actualTokens.size(); i++) {
             String actualToken = actualTokens.get(i).getText();
             String expectedToken = expectedTokens[i];
             assertEquals(actualToken, expectedToken);
        }
    }
}
