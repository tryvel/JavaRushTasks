package com.javarush.task.task20.task2027;

import java.util.ArrayList;
import java.util.List;

/* 
Кроссворд
*/
public class Solution {
    public static void main(String[] args) {
        int[][] crossword = new int[][]{
                {'f', 'd', 'e', 'r', 'l', 'k'},
                {'u', 's', 'a', 'm', 'e', 'o'},
                {'l', 'n', 'g', 'r', 'o', 'v'},
                {'m', 'l', 'p', 'r', 'r', 'h'},
                {'p', 'o', 'e', 'e', 'j', 'j'}
        };
        detectAllWords(crossword, "home", "same");
        /*
Ожидаемый результат
home - (5, 3) - (2, 0)
same - (1, 1) - (4, 1)
         */
    }

    public static List<Word> detectAllWords(int[][] crossword, String... words) {
        List<Word> wordList = new ArrayList<>();

        int[] dx = new int[] { 0,  1, 1, 1, 0, -1, -1, -1};
        int[] dy = new int[] {-1, -1, 0, 1, 1,  1,  0, -1};

        for (String s : words) {
            char[] c = s.toCharArray();
            for (int y = 0; y < crossword.length; y++) {
                for (int x = 0; x < crossword[y].length; x++) {
                    if (crossword[y][x] == c[0]) {
                        for (int i = 0; i < dx.length; i++) {
                            boolean wordFound = true;
                            for (int j = 1; j < c.length; j++) {
                                if (y + dy[i] * j < 0 || y + dy[i] * j >= crossword.length ||
                                    x + dx[i] * j < 0 || x + dx[i] * j >= crossword[y + dy[i] * j].length ||
                                    crossword[y + dy[i] * j][x + dx[i] * j] != c[j]) {
                                    wordFound = false;
                                    break;
                                }
                            }
                            if (wordFound) {
                                Word w = new Word(s);
                                w.setStartPoint(x, y);
                                w.setEndPoint(x + dx[i] * (c.length - 1), y + dy[i] * (c.length - 1));
                                wordList.add(w);
                            }

                        }
                    }
                }
            }
        }
        return wordList;
    }

    public static class Word {
        private String text;
        private int startX;
        private int startY;
        private int endX;
        private int endY;

        public Word(String text) {
            this.text = text;
        }

        public void setStartPoint(int i, int j) {
            startX = i;
            startY = j;
        }

        public void setEndPoint(int i, int j) {
            endX = i;
            endY = j;
        }

        @Override
        public String toString() {
            return String.format("%s - (%d, %d) - (%d, %d)", text, startX, startY, endX, endY);
        }
    }
}
