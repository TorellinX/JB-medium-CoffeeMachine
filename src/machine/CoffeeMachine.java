package machine;

import java.util.Scanner;

public class CoffeeMachine {
  private Model model;

  CoffeeMachine() {
    this.model = new Model();
  }

  public static void main(String[] args) {
    CoffeeMachine machine = new CoffeeMachine();
    Scanner scanner = new Scanner(System.in);

    while (machine.model.getState() != State.STOP) {
      String inputAction = scanner.next();
      machine.model.interact(inputAction);
    }
  }
}

