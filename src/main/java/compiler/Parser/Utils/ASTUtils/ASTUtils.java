package compiler.Parser.Utils.ASTUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import compiler.Parser.ASTNode.ASTNodeProcessor;
import java.io.File;
import java.io.IOException;

public class ASTUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static void saveAST(ASTNodeProcessor ast, String filePath, boolean print)
      throws IOException {

    if (print) {
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), ast);
    }
  }

  public static String saveAST(ASTNodeProcessor ast) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ast);
  }

  public static ASTNodeProcessor loadAST(String filePath) throws IOException {
    return objectMapper.readValue(new File(filePath), ASTNodeProcessor.class);
  }
}
