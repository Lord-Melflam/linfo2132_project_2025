package compiler.Parser.Utils.ASTUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import compiler.Parser.ASTNode.ASTNodeProcessor;
import java.io.IOException;
import java.util.Objects;

public class ASTComparator {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static boolean compareAST(ASTNodeProcessor node1, ASTNodeProcessor node2) {
    try {
      String json1 = objectMapper.writeValueAsString(node1);
      String json2 = objectMapper.writeValueAsString(node2);
      return Objects.equals(json1, json2);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}
