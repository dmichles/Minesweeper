import java.util.*;

public class Board {
    private char[][] board;
    private int size;
    private int mines;
    private ArrayList<Integer> minelist;
    private ArrayList<Integer> marklist;
    private ArrayList<Integer> freecellslist;
    private char[][] digits;
    private Queue<Integer> explorecellqueue;
    private ArrayList<Integer> exploredcells;
    private int x;
    private int y;

    public Board(int size, int mines) {
        board = new char[size][size];
        this.size = size;
        this.mines = mines;
        minelist = new ArrayList<>();
        marklist = new ArrayList<>();
        freecellslist = new ArrayList<>();
        explorecellqueue = new ArrayDeque<>();
        exploredcells = new ArrayList<>();
        digits = new char[size*size][1];
    }

    public void initialize() {
        for (char[] row : board) {
            Arrays.fill(row, '.');
        }
    }

    public void placebombs() {
        int count = mines;
        int index = Integer.MAX_VALUE;
        int temp;

        int[] array = new int[size * size];
        for (int i = 0; i < size * size; i++) {
            array[i] = i;
        }

        temp = array[0];
        array[0] = array[x * size + y];
        array[x * size + y] = temp;

        for (int i = 1; i < mines + 1; i++) {
            index = (int) (Math.random() * (size * size - i)) + i;

            temp = array[i];
            array[i] = array[index];
            array[index] = temp;
            minelist.add(array[i]);
        }
        Collections.sort(minelist);
    }

    public void updateNeighbors() {
        int number = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!minelist.contains(i * size + j)) {
                    number = countBombs(i, j);
                    if (number > 0) {
                        digits[i * size + j][0] = (char) (number + '0');
                    } else {
                        freecellslist.add(i * size + j);
                    }
                }
            }
        }
    }

    public int countBombs(int i, int j) {
        int imin = i - 1 < 0 ? 0 : -1;
        int jmin = j - 1 < 0 ? 0 : -1;
        int imax = i + 1 > size - 1 ? 0 : 1;
        int jmax = j + 1 > size - 1 ? 0 : 1;
        int number = 0;
        for (int l = imin; l <= imax; l++) {
            for (int k = jmin; k <= jmax; k++) {
                if (minelist.contains((l + i) * size + (k + j))) {
                    number++;
                }
            }
        }
        return number;
    }

    public void setmark(boolean init) {

        Scanner input = new Scanner(System.in);
        String s;
        while (true) {
            System.out.print("Set/unset mines marks or claim a cell as free:");
            y = input.nextInt() - 1;
            x = input.nextInt() - 1;
            s = input.next();
            if (!Character.isDigit(board[x][y])) {
                break;
            }
            System.out.println("There is a number here!");
        }
        if (init) {
            placebombs();
            updateNeighbors();
        }

        if (s.equals("mine") && marklist.contains(x * size + y)) {
            marklist.remove(Integer.valueOf(x * size + y));
            board[x][y] = '.';
        } else if (s.equals("free") && freecellslist.contains(x * size + y)) {
            explorecellqueue.add(x * size + y);
            processFreeCells();
        } else if (s.equals("free") && Character.isDigit(digits[x * size + y][0])) {
            board[x][y] = digits[x * size + y][0];
        } else if (s.equals("free") && minelist.contains(x * size + y)) {
            for (Integer i : minelist) {
                board[i / size][i % size] = 'X';
            }
        } else {
            marklist.add(x * size + y);
            Collections.sort(marklist);
            board[x][y] = '*';
        }
    }

    public boolean checkDisarmed() {
        boolean disarmed = true;
        if (marklist.size() == minelist.size()) {
            for (int i = 0; i < marklist.size(); i++) {
                if (marklist.get(i) != minelist.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkIfStepped() {
        if (minelist.contains(x*size+y) && board[x][y] == 'X') {
            return true;
        }
        return false;
    }

    public void processFreeCells() {
        while (!explorecellqueue.isEmpty()) {
            int n = explorecellqueue.peek();
            board[n / size][n % size] = '/';
            processHelper(n / size, n % size);
            exploredcells.add(explorecellqueue.remove());
        }
    }

    public void processHelper(int i, int j) {
        int imin = i - 1 < 0 ? 0 : -1;
        int jmin = j - 1 < 0 ? 0 : -1;
        int imax = i + 1 > size - 1 ? 0 : 1;
        int jmax = j + 1 > size - 1 ? 0 : 1;

        for (int l = imin; l <= imax; l++) {
            for (int k = jmin; k <= jmax; k++) {
                int index = (l + i) * size + (k + j);
                if (freecellslist.contains(index)) {
                    if (!exploredcells.contains(index) && !explorecellqueue.contains(index)) {
                        explorecellqueue.add(index);
                    }
                } else if (Character.isDigit(digits[index][0])) {
                    board[index / size][index % size] = digits[index][0];
                }
            }
        }
    }
    public void printBoard() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < size; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("-|---------|");
    }
}