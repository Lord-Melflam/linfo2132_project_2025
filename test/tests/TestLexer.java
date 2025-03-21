package tests;

import static compiler.Parser.Utils.CodeSamples.basicCodeConstant;
import static compiler.Parser.Utils.CodeSamples.constantError;
import static compiler.Parser.Utils.CodeSamples.controlStructure;
import static compiler.Parser.Utils.CodeSamples.expression;
import static compiler.Parser.Utils.CodeSamples.inputNotAscii;
import static compiler.Parser.Utils.CodeSamples.invalidArrayAccess;
import static compiler.Parser.Utils.CodeSamples.memoryManagement;
import static compiler.Parser.Utils.CodeSamples.mixedTypeOperations;
import static compiler.Parser.Utils.CodeSamples.nestedRecord;
import static compiler.Parser.Utils.CodeSamples.operatorPrecedence;
import static compiler.Parser.Utils.CodeSamples.recordArrayUsage;
import static compiler.Parser.Utils.CodeSamples.scopeViolation;
import static compiler.Parser.Utils.CodeSamples.typeMismatch;
import static compiler.Parser.Utils.CodeSamples.unrecognisedToken;
import static compiler.Parser.Utils.UtilsTest.getLexer;
import static compiler.Parser.Utils.UtilsTest.loadExpectedTokens;
import static compiler.Parser.Utils.UtilsTest.readLexer;
import static compiler.Parser.Utils.UtilsTest.tokenizeFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import compiler.Exceptions.Lexer.NotASCIIException;
import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Lexer.Symbols.Literal;
import compiler.Parser.Utils.Enum.Token;
import compiler.Parser.Utils.Enum.TokenType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

public class TestLexer {

  private final ArrayList<String> fileName = new ArrayList<>(
      List.of("code", "code_full_lexer_test"));

  @Test
  public void testEntireFile() {
    for (String testFile : fileName) {
      String sourceFile = "test/TestFile/Code/" + testFile + ".txt";
      String expectedFile = "test/TestFile/AnswersLexer/" + testFile + ".txt";

      try {
        Lexer lexer = tokenizeFile(sourceFile);
        List<Symbol> actualTokens = readLexer(lexer);
        List<Symbol> expectedTokens = loadExpectedTokens(expectedFile);

        assertEquals("Nombre de tokens incorrect dans " + testFile, expectedTokens.size(),
            actualTokens.size());

        for (int i = 0; i < expectedTokens.size(); i++) {
          assertEquals(
              "Type incorrect à l'index " + i + " dans " + testFile + " " + expectedTokens.get(i)
                  .getToken()
                  + " " + actualTokens.get(i).getToken(),
              expectedTokens.get(i).getName(),
              actualTokens.get(i).getName());
          assertEquals("Valeur incorrecte à l'index " + i + " dans " + testFile,
              expectedTokens.get(i).getToken(), actualTokens.get(i).getToken());
        }

      } catch (IOException | NotASCIIException | UnrecognisedTokenException e) {
        fail("Erreur dans le fichier " + testFile + ": " + e.getMessage());
      }
    }
  }

  @Test
  public void testWithNotASCII()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    assertThrows(NotASCIIException.class, () -> {
      readLexer(getLexer(inputNotAscii));
    });

  }

  @Test
  public void testBasicCodeConstant()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(basicCodeConstant));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testRecordArrayUsage()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(recordArrayUsage));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testExpression()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(expression));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testControlStructure()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(controlStructure));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testConstantError()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(constantError));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testTypeMismatch()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(typeMismatch));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testInvalidArrayAccess()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(invalidArrayAccess));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testScopeViolation()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(scopeViolation));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testOperatorPrecedence()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(operatorPrecedence));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testMixedTypeOperations()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(mixedTypeOperations));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testNestedRecord()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(nestedRecord));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testMemoryManagement()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      readLexer(getLexer(memoryManagement));
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testWithUnrecognisedToken()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    assertThrows(UnrecognisedTokenException.class, () -> {
      readLexer(getLexer(unrecognisedToken));
    });
  }

  @Test
  public void testWithRecord()
      throws NotASCIIException, IOException, UnrecognisedTokenException {

    String input = "Person rec { name string; \n age int; }";
    Lexer lexer = getLexer(input);

    List<Token> expectedTokens = List.of(
        new Token(TokenType.RECORD, "Person"),
        new Token(TokenType.REC, "rec"),
        new Token(TokenType.LBRACE, "{"),
        new Token(TokenType.IDENTIFIER, "name"),
        new Token(TokenType.TYPESPECIFIER, "string"),
        new Token(TokenType.SEMICOLON, ";"),
        new Token(TokenType.IDENTIFIER, "age"),
        new Token(TokenType.TYPESPECIFIER, "int"),
        new Token(TokenType.SEMICOLON, ";"),
        new Token(TokenType.RBRACE, "}")
    );

    LinkedList<Symbol> actualTokens = readLexer(lexer);
    assertEquals("Nombre de tokens incorrect", Integer.toString(expectedTokens.size()),
        Integer.toString(actualTokens.size()));

    for (int i = 0; i < expectedTokens.size(); i++) {
      assertEquals("Type incorrect à l'index " + i,
          expectedTokens.get(i).getTokenType().getCategory(),
          actualTokens.get(i).getName());
      assertEquals("Valeur incorrecte à l'index " + i, expectedTokens.get(i).getValue(),
          actualTokens.get(i).getToken()
      );
    }
  }

  @Test
  public void TestFloat() {
    Literal literal = new Literal("0.9", 2);
    assertTrue(literal.isFloat(literal.getToken()));
    assertTrue(literal.isFloat("1.00001"));
    assertTrue(literal.isFloat("0.00001"));
    assertTrue(literal.isFloat("11.00001"));
    assertTrue(literal.isFloat("11.9867"));
    assertTrue(literal.isFloat("897667.786675"));
    assertTrue(literal.isFloat(".00001"));
    assertTrue(literal.isFloat(".1100001"));
    assertTrue(literal.isFloat(".263461"));
    assertTrue(literal.isFloat("-0.1100001"));
    assertTrue(literal.isFloat("-0.263461"));
    assertTrue(literal.isFloat("-0.01100001"));
    assertTrue(literal.isFloat("-0.000000263461"));
  }

  @Test
  public void TestInt() {
    Literal literal = new Literal("9", 2);
    assertTrue(literal.isInt(literal.getToken()));
    assertTrue(literal.isInt("1"));
    assertTrue(literal.isInt("00001"));
    assertTrue(literal.isInt("111"));
    assertTrue(literal.isInt("119867"));
    assertTrue(literal.isInt("897667786675"));
    assertTrue(literal.isInt("1100001"));
    assertTrue(literal.isInt("263461"));
    assertTrue(literal.isInt("-1100001"));
    assertTrue(literal.isInt("-263461"));
    assertTrue(literal.isInt("0"));
    assertTrue(literal.isInt("-0"));
  }

}
