package compiler.Semantic.Types;

import java.util.HashMap;
import java.util.LinkedList;

public class Type implements Cloneable {

  private String SymbolTypeName;
  private final String name;
  private final Type previous;
  private final String returnType;
  private final LinkedList<Type> parameters;
  private final HashMap<String, Type> current;
  private final HashMap<String, LinkedList<String>> reccordAtt;

  public Type(String symbolTypeName, String name, Type previous, String returnType,
      LinkedList<Type> parameters, HashMap<String, Type> current) {
    SymbolTypeName = symbolTypeName;
    this.name = name;
    this.previous = previous;
    this.returnType = returnType;
    this.parameters = parameters;
    this.current = current;
    reccordAtt = new HashMap<>();
  }

  public void addRecordAtt(String field, String recordName) {
    if (reccordAtt.containsKey(field)) {
      reccordAtt.get(field).add(recordName);
    } else {
      LinkedList<String> nameList = new LinkedList<>();
      nameList.add(recordName);
      reccordAtt.put(field, nameList);
    }
  }

  public LinkedList<String> findRecordName(String field) {
    return reccordAtt.getOrDefault(field, new LinkedList<>());
  }

  public HashMap<String, LinkedList<String>> getReccordAtt() {
    return reccordAtt;
  }

  public String getName() {
    return name;
  }

  public Type getPrevious() {
    return previous;
  }

  public String getReturnType() {
    return returnType;
  }

  public LinkedList<Type> getParameters() {
    return parameters;
  }

  public String getSymbolTypeName() {
    return SymbolTypeName;
  }

  public HashMap<String, Type> getCurrent() {
    return current;
  }

  @Override
  public Type clone() {
    try {
      Type clone = (Type) super.clone();

      // Deep copy of parameters
      LinkedList<Type> clonedParameters = new LinkedList<>();
      for (Type param : this.parameters) {
        if (!this.parameters.isEmpty()) {
          clonedParameters.add(param.clone());
        }
      }

      // Deep copy of current
      HashMap<String, Type> clonedCurrent = new HashMap<>();
      for (var entry : this.current.entrySet()) {
        clonedCurrent.put(entry.getKey(), entry.getValue().clone());
      }

      // Deep copy of reccordAtt
      HashMap<String, LinkedList<String>> clonedReccordAtt = new HashMap<>();
      for (var entry : this.reccordAtt.entrySet()) {
        clonedReccordAtt.put(entry.getKey(), new LinkedList<>(entry.getValue()));
      }

      // Use reflection or constructor to create new instance if needed
      // Here, we reassign the deep copies to the clone's fields using reflection since some are final
      // (Or change them to not-final if deep cloning is important and frequent)
      var parametersField = Type.class.getDeclaredField("parameters");
      parametersField.setAccessible(true);
      parametersField.set(clone, clonedParameters);

      var currentField = Type.class.getDeclaredField("current");
      currentField.setAccessible(true);
      currentField.set(clone, clonedCurrent);

      var reccordAttField = Type.class.getDeclaredField("reccordAtt");
      reccordAttField.setAccessible(true);
      reccordAttField.set(clone, clonedReccordAtt);

      return clone;
    } catch (Exception e) {
      throw new AssertionError(e); // covers both CloneNotSupported and ReflectiveOperation
    }
  }

}
