package com.example.JogoDaVida.JogoDaVida;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameOfLifeController {

    private boolean[][] board;
    private boolean playing;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("board", board);
        return "index";
    }

    @PostMapping("/initialize")
    public String initialize() {
        board = new boolean[10][10];
        return "redirect:/";
    }

    @PostMapping("/toggleCell")
    public String toggleCell(int row, int col) {
        board[row][col] = !board[row][col];
        return "redirect:/";
    }

    @PostMapping("/play")
    public String play() {
        if (playing) {
            playing = false;
            return "redirect:/";
        }

        playing = true;
        while (playing) {
            boolean[][] newBoard = new boolean[10][10];
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    int liveNeighbors = countLiveNeighbors(row, col);
                    if (board[row][col]) {
                        // Cell is alive
                        if (liveNeighbors == 2 || liveNeighbors == 3) {
                            newBoard[row][col] = true; // Cell remains alive
                        }
                    } else {
                        // Cell is dead
                        if (liveNeighbors == 3) {
                            newBoard[row][col] = true; // Cell becomes alive
                        }
                    }
                }
            }
            board = newBoard;
            try {
                Thread.sleep(1000); // Delay between generations (1 second)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/";
    }

    private int countLiveNeighbors(int row, int col) {
        int count = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < board.length && j >= 0 && j < board[i].length) {
                    if (board[i][j] && !(i == row && j == col)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    @RequestMapping("/stop")
    public String stop() {
        playing = false;
        return "redirect:/";
    }
}
