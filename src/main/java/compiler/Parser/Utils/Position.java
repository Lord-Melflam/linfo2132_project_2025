package compiler.Parser.Utils;

import compiler.Parser.Utils.Interfaces.Observer;
import compiler.Parser.Utils.Interfaces.Subject;
import java.util.ArrayList;
import java.util.List;

public class Position implements Subject {

  private final List<Observer> observers = new ArrayList<>();

  private int savedPosition;

  public Position() {
    this.savedPosition = -1;
  }

  public Position(int savedPosition) {
    this.savedPosition = savedPosition;
  }

  public void setSavedPosition(int savedPosition) {
    this.savedPosition = savedPosition;
    notifyObservers();
  }

  public int getSavedPosition() {
    return savedPosition;
  }

  public int getNextPosition() {
    return savedPosition + 1;
  }

  public int getPreviousPosition() {
    return savedPosition - 1;
  }

  public int add() {
    savedPosition++;
    notifyObservers();
    return getSavedPosition();
  }

  public int add(List<?> list) {
    savedPosition += list.size();
    notifyObservers();
    return getSavedPosition();
  }

  @Override
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.updatePosition(this);
    }
  }
}
