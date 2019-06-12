package Lexer;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Collections;
import java.util.regex.Matcher;

public class Lexer {
    private static final int COMMON_TYPE_NUM = 12; //Количество НЕ служебных типов
    private static Scanner in;
    private static LinkedList<Token> tokens = new LinkedList<>();
    private static LinkedList<Token> candidates = new LinkedList<>();
    private static Matcher m;

    public static LinkedList<Token> read(String path) {
        try {
            /* Читаем содержимое файла в буфер
             * Добавляем к строке знаки из буфера до тех пор, пока она не перестанет подходить
             * под какое-либо из регулярных выражений. Если строка подходит - добавляем токен
             * в список кандидатов. Если совпадений нет, то в список возвращаемых функцией токенов
             * берём кандидата максимальной длины с максимальным приоритетом.
             */

            in = new Scanner(new File(path));
            int line = 1;

            while (in.hasNextLine()) {
                //TODO: переписать обработку последнего токена в строке
                char[] buffer = (in.nextLine() + "\0").toCharArray();
                StringBuilder sb = new StringBuilder();
                int pos = 1;

                for (int i = 0; i < buffer.length; i++) {
                    char peek = buffer[i];
                    if ((peek == ' ') || (peek == '\t')) {
                        continue;
                    }

                    sb.append(peek);
                    boolean found = false;

                    for (int j = 0; j < COMMON_TYPE_NUM; j++){
                        /* Возникла необходимость добавить служебные типы для стек-машины.
                         * Про итерацию вида for (Lexeme type : Lexeme.values()) { можно забыть
                         */
                        Lexeme type = Lexeme.values()[j];
                        m = type.getPattern().matcher(sb);
                        boolean match = m.matches();
                        found = found | match;
                        if (match) {
                            candidates.add(new Token(m.group(), type, line, pos + m.start()));
                        }
                    }

                    if (!candidates.isEmpty() && !found) {
                        Token max = Collections.max(candidates);
                        tokens.add(max);
                        sb.setLength(0);
                        candidates.clear();
                        pos = i + 1;
                        i--;
                    }
                }
                line++;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokens;
    }
}
