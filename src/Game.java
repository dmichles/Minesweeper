import java.util.Scanner;

public class Game {
    private Board board;
    private int size = 9;
    private int mines;

    public Game() {
        Scanner input = new Scanner(System.in);
        System.out.print("How many mines do you want on the field?");
        mines = input.nextInt();
        board = new Board(9, mines);
        gameloop();
    }

    public void gameloop() {

        boolean init = true;

        board.initialize();
        board.printBoard();
        board.setmark(init);
        init = false;
        board.printBoard();

        while (true) {
            board.setmark(init);
            board.printBoard();
            if (board.checkDisarmed()) {
                System.out.println("Congratulations! You found all mines!");
                break;
            }
            if (board.checkIfStepped()) {
                System.out.println("You stepped on a mine and failed!");
                break;
            }
        }
    }
}

