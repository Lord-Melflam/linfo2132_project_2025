package tests;

import static compiler.Parser.Utils.Test.CodeSamples.basicCodeConstant;
import static compiler.Parser.Utils.Test.CodeSamples.mixedTypeOperations;
import static compiler.Parser.Utils.Test.CodeSamples.operatorPrecedence;
import static compiler.Parser.Utils.Test.CodeSamples.recordArrayUsage;
import static compiler.Parser.Utils.Test.CodeSamples.typeMismatch;
import static compiler.Parser.Utils.Test.UtilsTest.getLexerInput;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import compiler.Exceptions.Lexer.NotASCIIException;
import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Semantic.ArgumentError;
import compiler.Exceptions.Semantic.RecordError;
import compiler.Exceptions.Semantic.ScopeError;
import compiler.Exceptions.Semantic.TypeError;
import compiler.Semantic.Semantic_analysis;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class TestSemantic {

  @Test
  public void testConstant() throws NotASCIIException, IOException, UnrecognisedTokenException {
    String code0 = "final maxItems int = 10 * 2 + 5;";
    String code1 = "final average float = (5.0 + 3.0) / 2.0;";
    String code2 = "final isValid bool = (10 > 5) && (3 != 4);\n";
    String code3 = "final greeting string = \"Hello, \" + \"world!\";\n";
    String code4 = "final charAsString string = chr(65) + \"BC\";\n";
    String code5 = "final maxItems int = 2 * 2;\nfinal doubleValue int = maxItems * 2;\n";
    String code6 = "final isComplex bool = (len(\"test\") == 4) || (floor(3.7) < 4);\n";
    ArrayList<String> codes = new ArrayList(
        List.of(code0, code1, code2, code3, code4, code5, code6));
    for (String code : codes) {
      try {
        new Semantic_analysis(getLexerInput(code));
      } catch (Exception e) {
        System.out.println(code);
        System.out.println(e.getMessage());
        fail(e.getClass().getSimpleName() + " was thrown");
      }
    }
  }

  @Test
  public void testRecord() throws NotASCIIException, IOException, UnrecognisedTokenException {
    String code0 = "Point rec {\n" + "    x int;\n" + "    y int;\n" + "}\n";
    String code1 =
        "Person rec {\n" + "    name string;\n" + "    location string;\n" + "    history int[];\n"
            + "}\n";
    String code2 = "Rectangle rec {\n" + "    topLeft int;\n" + "    bottomRight int;\n" + "}\n";
    String code3 =
        "Student rec {\n" + "    id int;\n" + "    name string;\n" + "    grades float[];\n"
            + "}\n";

    String code4 = code0 + "Person rec {\n" + "    name string;\n" + "    location Point;\n"
        + "    history int[];\n" + "}\n";
    String code5 =
        code0 + "Rectangle rec {\n" + "    topLeft Point;\n" + "    bottomRight Point;\n" + "}\n";
    String code6 =
        code0 + "Student rec {\n" + "    id int;\n" + "    name string;\n" + "    grades float[];\n"
            + "}\n";
    ArrayList<String> codes = new ArrayList(
        List.of(code0, code1, code2, code3, code4, code5, code6));
    for (String code : codes) {
      try {
        new Semantic_analysis(getLexerInput(code));
      } catch (Exception e) {
        System.out.println(code);
        System.out.println(e.getMessage());
        fail(e.getClass().getSimpleName() + " was thrown");
      }
    }
  }

  @Test
  public void testBuiltInFunction()
      throws NotASCIIException, IOException, UnrecognisedTokenException {

    String code1 = "final charString string = chr(65);";
    String code2 = "final roundedValue int = floor(3.7);  $ Renvoie 3\n";
    String code3 = "final isFalse bool = !(true);  $ Renvoie false\n";
    String code4 = "final result int = len(chr(65) + \"BC\");  $ Renvoie 3\n";
    String code6 = "value int = 10 / (len(\"\") - 1);";

    ArrayList<String> codes = new ArrayList<>(
        List.of(code1, code2, code3, code4, code6));
    for (String code : codes) {
      try {
        new Semantic_analysis(getLexerInput(code));
      } catch (Exception e) {
        System.out.println(code);
        System.out.println(e.getMessage());
        fail(e.getClass().getSimpleName() + " was thrown");
      }
    }
  }

  @Test
  public void testScopeError() throws NotASCIIException, IOException, UnrecognisedTokenException {
    String code0 = "final b int = a + 3;\n";
    String code1 = "final p Point = Point(3,4);  \n";
    String code2 = "final x int = y + 2;  \n" + "final y int = x * 3;  \n";
    ArrayList<String> codes = new ArrayList<>(List.of(code0, code1, code2));
    for (String code : codes) {
      assertThrows(ScopeError.class, () -> {
        new Semantic_analysis(getLexerInput(code));
      });
    }
  }

  @Test
  public void testTypeError() throws NotASCIIException, IOException, UnrecognisedTokenException {
    String code0 = "  final n int = len(42);\n";
    ArrayList<String> codes = new ArrayList<>(List.of(code0));
    for (String code : codes) {
      assertThrows(ArgumentError.class, () -> {
        new Semantic_analysis(getLexerInput(code));
      });
    }
  }

  @Test
  public void testRecordError() throws NotASCIIException, IOException, UnrecognisedTokenException {
    String code0 =
        "Xxx rec{\n" + "x string;\n" + "}\n" + "Point rec {\n" + "    x Xxx;\n" + "    y Point[];\n"
            + "}";
    String code1 = "Point rec {\n" + "    x Xxx;\n" + "    y Point[];\n" + "}\n";
    String code2 = "\n" + "Point rec {\n" + "    x Poi;\n" + "    y int[];\n" + "}";
    String code3 = "Point rec {\n" + "    x Point;\n" + "    y int;\n" + "}";

    ArrayList<String> codes = new ArrayList<>(List.of(code0, code1, code2, code3));
    for (String code : codes) {
      assertThrows(RecordError.class, () -> {
        new Semantic_analysis(getLexerInput(code));
      });
    }
  }

  @Test
  public void testBasicCodeConstant()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      new Semantic_analysis(getLexerInput(basicCodeConstant));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testRecordArrayUsage()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      new Semantic_analysis(getLexerInput(recordArrayUsage));

    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testTypeMismatch() throws NotASCIIException, IOException, UnrecognisedTokenException {
    assertThrows(TypeError.class, () -> {
      new Semantic_analysis(getLexerInput(typeMismatch));
    });
  }

  @Test
  public void testOperatorPrecedence()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      new Semantic_analysis(getLexerInput(operatorPrecedence));

    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  @Test
  public void testMixedTypeOperations()
      throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      new Semantic_analysis(getLexerInput(mixedTypeOperations));

    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }

  /*@Test
  public void testNestedRecord() throws NotASCIIException, IOException, UnrecognisedTokenException {
    try {
      new Semantic_analysis(getLexerInput(nestedRecord));

    } catch (Exception e) {
      fail(e.getClass().getSimpleName() + " was thrown");
    }
  }*/

  /*
  Point rec {
    x int;
    y int;
}
Point rec {
    x int;
    y int;
}
final p Point = Point(3,4);
   */

}
