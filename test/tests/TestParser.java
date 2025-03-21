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
import static compiler.Parser.Utils.UtilsTest.getLexerFilePath;
import static compiler.Parser.Utils.UtilsTest.getLexerInput;
import static compiler.Parser.Utils.UtilsTest.readLexer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import compiler.Exceptions.Lexer.NotASCIIException;
import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Parser.ASTNode.ASTNodeProcessor;
import compiler.Parser.Utils.ASTUtils.ASTComparator;
import compiler.Parser.Utils.ASTUtils.ASTUtils;
import compiler.Parser.Utils.Enum.Token;
import compiler.Parser.Utils.Enum.TokenType;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

public class TestParser {

  private final ArrayList<String> fileName = new ArrayList<>(
      List.of("code", "code_full_lexer_test", "code3", "parserTestCode_1", "parserTestCode_2",
          "parserTestCode_3", "parserTestCodeEdgeCase_1"));


  @Test
  public void testEntireFile() {
    for (String testFile : fileName) {
      String sourceFile = "./test/TestFile/Code/" + testFile + ".txt";
      String expectedFile = "./test/TestFile/AnswersParser/" + testFile + ".json";

      try {
        ASTNodeProcessor astNodeProcessorActual = getLexerFilePath(
            sourceFile).getAstNodeProcessor();
        ASTNodeProcessor astNodeProcessorActualExpected = ASTUtils.loadAST(expectedFile);
        boolean areEqual = ASTComparator.compareAST(astNodeProcessorActual,
            astNodeProcessorActualExpected);

        assertTrue(areEqual);
      } catch (IOException | NotASCIIException | UnrecognisedTokenException e) {
        fail("Erreur dans le fichier " + testFile + ": " + e.getMessage());
      } catch (ParserException e) {
        System.out.println(testFile);
        throw new RuntimeException(e);
      }
    }
  }

  @Test
  public void testCode2() {
    String sourceFile = "test/TestFile/Code/" + "code2" + ".txt";
    assertThrows(ParserException.class, () -> {
      ASTNodeProcessor astNodeProcessorActual = getLexerFilePath(sourceFile).getAST();
    });

  }

  @Test
  public void testWithNotASCII()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    assertThrows(NotASCIIException.class, () -> {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(inputNotAscii).getAST();

    });

  }

  @Test
  public void testBasicCodeConstant()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(basicCodeConstant).getAST();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testRecordArrayUsage()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(recordArrayUsage).getAST();
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testExpression()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(expression).getAST();
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testControlStructure()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(controlStructure).getAST();

    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testConstantError()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    assertThrows(ParserException.class, () -> {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(constantError).getAST();

    });
  }

  @Test
  public void testTypeMismatch()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(typeMismatch).getAST();
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testInvalidArrayAccess()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(invalidArrayAccess).getAST();
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testScopeViolation()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(scopeViolation).getAST();

    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testOperatorPrecedence()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(operatorPrecedence).getAST();
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testMixedTypeOperations()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(mixedTypeOperations).getAST();

    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testNestedRecord()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(nestedRecord).getAST();
    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testMemoryManagement()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      ASTNodeProcessor astNodeProcessorActual = getLexerInput(memoryManagement).getAST();

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
    StringReader reader = new StringReader(input);
    Lexer lexer = new Lexer(reader);

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


}
