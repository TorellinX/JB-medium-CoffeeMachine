package machine;

public class Model {
  private int waterAvailable;
  private int milkAvailable;
  private int coffeeAvailable;
  private int disposableCupsAvailable;
  private int money;
  private State state;
  private Fullness fullnessState;

  Model() {
    this.waterAvailable = 400;
    this.milkAvailable = 540;
    this.coffeeAvailable = 120;
    this.disposableCupsAvailable = 9;
    this.money = 550;
    setStateToMenu();
    this.fullnessState = Fullness.FILLED;
  }

  public void interact(String input) {
    switch (state) {
      case MENU -> processMenu(input);
      case BUY -> buyCoffee(input);
      case FILL -> fillMachine(input);
    }

  }

  private void processMenu(String input) {
    switch (input) {
      case "buy" -> {
        setStateToBuy();
      }
      case "fill" -> {
        setStateToFill();
        setFillWater();
      }
      case "take" -> {
        takeMoney();
        printMenuMsg();
      }
      case "remaining" -> {
        printRemaining();
        printMenuMsg();
      }
      case "exit" -> {
        setStateToStop();
      }
      default -> System.out.println("Wrong input");
    }
  }



  private void printRemaining(){
    String remainingMsg = String.format("""

            The coffee machine has:
            %d ml of water
            %d ml of milk
            %d g of coffee beans
            %d disposable cups
            $%d of money
            """,
        waterAvailable, milkAvailable, coffeeAvailable, disposableCupsAvailable, money);
    System.out.println(remainingMsg);
  }

  private void buyCoffee(String input){
    int waterPerCup;
    int milkPerCup;
    int coffeePerCup;
    int moneyPerCup;

    switch (input) {
      case "1" -> {
        waterPerCup = 250;
        milkPerCup = 0;
        coffeePerCup = 16;
        moneyPerCup = 4;
      }
      case "2" -> {
        waterPerCup = 350;
        milkPerCup = 75;
        coffeePerCup = 20;
        moneyPerCup = 7;
      }
      case "3" -> {
        waterPerCup = 200;
        milkPerCup = 100;
        coffeePerCup = 12;
        moneyPerCup = 6;
      }
      case "back" -> {
        setStateToMenu();
        return;
      }
      default -> {
        System.out.println("Wrong input!\n");
        return;
      }
    }
    if (waterPerCup > waterAvailable) {
      System.out.println("Sorry, not enough water!\n");
      setStateToMenu();
      return;
    }
    if (milkPerCup > milkAvailable) {
      System.out.println("Sorry, not enough milk!\n");
      setStateToMenu();
      return;
    }
    if (coffeePerCup > coffeeAvailable) {
      System.out.println("Sorry, not enough coffee!\n");
      setStateToMenu();
      return;
    }
    if (disposableCupsAvailable == 0) {
      System.out.println("Sorry, not enough disposable cups!\n");
      setStateToMenu();
      return;
    }
    System.out.println("I have enough resources, making you a coffee!\n");
    waterAvailable -= waterPerCup;
    milkAvailable -= milkPerCup;
    coffeeAvailable -= coffeePerCup;
    disposableCupsAvailable--;
    money += moneyPerCup;
    setStateToMenu();
  }

  private void fillMachine(String input){
    int added = 0;
    try {
      added = Integer.parseInt(input);
    } catch (NumberFormatException e) {
      System.out.println("Wrong input!");
      return;
    }
    if (added < 0) {
      System.out.println("Wrong input!");
      return;
    }
    switch (fullnessState) {
      case FILLING_WATER -> {
        waterAvailable += added;
        setFillMilk();
      }
      case FILLING_MILK -> {
        milkAvailable += added;
        setFillCoffee();
      }
      case FILLING_COFFEE -> {
        coffeeAvailable += added;
        setFillCups();
      }
      case FILLING_CUPS -> {
        disposableCupsAvailable += added;
        fullnessState = Fullness.FILLED;
        System.out.println();
        setStateToMenu();
      }
      default -> throw new RuntimeException("Illegal fullness state.");
    }
  }

  private void takeMoney(){
    System.out.printf("\nI gave you $%d%n\n", money);
    money = 0;
  }

  public State getState() {
    return state;
  }

  private void setStateToMenu() {
    state = State.MENU;
    printMenuMsg();
  }

  private void setStateToBuy(){
    state = State.BUY;
    System.out.println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, "
        + "back - to main menu:");
  }

  private void setStateToStop() {
    state = State.STOP;
  }

  private void setStateToFill() {
    state = State.FILL;
  }
  private void printMenuMsg() {
    System.out.println("Write action (buy, fill, take, remaining, exit): ");
  }

  private void setFillWater() {
    fullnessState = Fullness.FILLING_WATER;
    printFillingWater();
  }

  private void setFillMilk() {
    fullnessState = Fullness.FILLING_MILK;
    printFillingMilk();
  }

  private void setFillCoffee() {
    fullnessState = Fullness.FILLING_COFFEE;
    printFillingCoffee();
  }

  private void setFillCups() {
    fullnessState = Fullness.FILLING_CUPS;
    printFillingCups();
  }

  private void printFillingWater() {
    System.out.println("\nWrite how many ml of water you want to add: ");
  }

  private void printFillingMilk() {
    System.out.println("Write how many ml of milk you want to add: ");
  }

  private void printFillingCoffee() {
    System.out.println("Write how many grams of coffee beans you want to add: ");
  }

  private void printFillingCups() {
    System.out.println("Write how many disposable cups you want to add: ");
  }

  private int calculateCups(int waterPerCup, int milkPerCup, int coffeePerCup){
    return Math.min(Math.min(waterAvailable / waterPerCup, milkAvailable / milkPerCup),
        coffeeAvailable / coffeePerCup);
  }
}

enum Fullness {
  FILLING_WATER, FILLING_MILK, FILLING_COFFEE, FILLING_CUPS, FILLED
}
