import static org.junit.Assert.assertNotNull;

import compiler.Exceptions.Lexer.NotASCIIException;
import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Lexer.Lexer;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

public class TestLexer {

  @Test
  public void test() throws IOException, NotASCIIException, UnrecognisedTokenException {
    String input = "var x int = 2;";
    StringReader reader = new StringReader(input);
    Lexer lexer = new Lexer(reader);
    assertNotNull(lexer.getNextSymbol());
  }

}
