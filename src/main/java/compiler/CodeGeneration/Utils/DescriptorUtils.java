package compiler.CodeGeneration.Utils;

import java.util.List;
import org.objectweb.asm.Type;

public class DescriptorUtils {

  public static String getMethodDescriptor(String returnTypeStr, List<String> argTypesStr) {
    Type returnType = getTypeFromString(returnTypeStr);
    Type[] argTypes = argTypesStr.stream()
        .map(DescriptorUtils::getTypeFromString)
        .toArray(Type[]::new);
    return Type.getMethodDescriptor(returnType, argTypes);
  }


  public static Type getTypeFromString(String typeStr) {
    // Gestion des tableaux
    if (typeStr.endsWith("[]")) {
      String elementType = typeStr.substring(0, typeStr.length() - 2);
      switch (elementType) {
        case "int":
          return Type.getType("[I");
        case "float":
          return Type.getType("[F");
        case "byte":
          return Type.getType("[B");
        case "boolean":
        case "bool":
          return Type.getType("[Z");
        case "string":
          return Type.getType("[Ljava/lang/String;");
        default:
          // Type record personnalisé (ex: MyRecord -> [LMyRecord;)
          return Type.getType("[L" + elementType.replace('.', '/') + ";");
      }
    }

    // Type simple (non tableau)
    switch (typeStr) {
      case "int":
        return Type.INT_TYPE;
      case "float":
        return Type.FLOAT_TYPE;
      case "byte":
        return Type.BYTE_TYPE;
      case "boolean":
      case "bool":
        return Type.BOOLEAN_TYPE;
      case "void":
        return Type.VOID_TYPE;
      case "string":
        return Type.getType("Ljava/lang/String;");
      default:
        // Type record personnalisé (ex: MyRecord -> LMyRecord;)
        return Type.getType("L" + typeStr.replace('.', '/') + ";");
    }
  }


  public static String descriptorToString(String descriptor) {
    // Si le descripteur est vide ou nul, on retourne une chaîne vide
    if (descriptor == null || descriptor.isEmpty()) {
      return "";
    }

    // Vérifie si c'est un tableau (commence par un '[')
    if (descriptor.charAt(0) == '[') {
      // Appel récursif pour gérer les tableaux multidimensionnels
      return descriptorToString(descriptor.substring(1)) + "[]";
    }

    // Si c'est un type primitif, on le mappe directement
    switch (descriptor) {
      case "B":
        return "byte";
      case "C":
        return "char";
      case "D":
        return "double";
      case "F":
        return "float";
      case "I":
        return "int";
      case "J":
        return "long";
      case "L":
        return "Object"; // Ce cas est traité plus bas pour les objets
      case "S":
        return "short";
      case "Z":
        return "boolean";
      default:
        // Cas pour les objets, on extrait le nom de la classe
        if (descriptor.charAt(0) == 'L') {
          return descriptor.substring(1, descriptor.length() - 1).replace('/', '.');
        }
        return "Unknown";
    }
  }

  public static Type getTypeFraomString(String typeStr) {
    switch (typeStr) {
      case "int":
        return Type.INT_TYPE;
      case "float":
        return Type.FLOAT_TYPE;
      case "byte":
        return Type.BYTE_TYPE;
      case "boolean", "bool":
        return Type.BOOLEAN_TYPE;
      case "void":
        return Type.VOID_TYPE;
      case "string":
        return Type.getType("Ljava/lang/String;");
      default:
        if (typeStr.endsWith("[]")) {
          String elementType = typeStr.substring(0, typeStr.length() - 2);
          return Type.getType("[L" + elementType.replace('.', '/') + ";");
        }
        return Type.getType("L" + typeStr.replace('.', '/') + ";");
    }
  }


}
