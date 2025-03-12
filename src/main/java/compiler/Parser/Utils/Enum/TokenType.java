package compiler.Parser.Utils.Enum;

public enum TokenType {
  FINAL("Keyword", "final"), REC("Keyword", "rec"), FUN("Keyword", "fun"), IF("Keyword",
      "if"), ELSE("Keyword", "else"), WHILE("Keyword", "while"), ARRAY("Keyword", "array"), OF(
      "Keyword", "of"),
  FOR("Keyword", "for"), RETURN("Keyword", "return"), FREE("Keyword", "free"),

  ASSIGNMENT("Assignment", "="),

  LPAREN("SpecialSymbol", "("), RPAREN("SpecialSymbol", ")"), LBRACE(
      "SpecialSymbol", "{"), RBRACE("SpecialSymbol", "}"), LBRACKET("SpecialSymbol", "["), RBRACKET(
      "SpecialSymbol", "]"),
  SEMICOLON("Punctuation", ";"), COMMA("Punctuation", ","), DOT("Punctuation", "."),
  IDENTIFIER("Identifier", ""),
  LITERAL("Literal", ""),
  OPERATOR("Operator", ""),
  KEYWORD("Keyword", ""),
  RECORD("Record", ""),
  SPECIALSYMBOL("SpecialSymbol", ""),
  TYPESPECIFIER("TypeSpecifier", ""),
  BUILTINFUNCTION("BuiltInFunction", ""),
  PUNTUATION("Punctuation", "");
  private final String category;
  private final String value;

  TokenType(String category, String value) {
    this.category = category;
    this.value = value;
  }

  public String getCategory() {
    return category;
  }

  public String getValue() {
    return value;
  }
}