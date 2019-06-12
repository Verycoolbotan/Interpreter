package Lexer;

import Lexer.Lexeme;

public class Token implements Comparable {
    private String value;
    private Lexeme type;
    private int line, pos;

    public Token(String value, Lexeme type, int line, int pos) {
        this.value = value;
        this.type = type;
        this.line = line;
        this.pos = pos;
    }

    public Token(String value, Lexeme type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public Lexeme getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value + ", " + this.type + ", " + this.line + ":" + this.pos;
    }

    @Override
    public int compareTo(Object o) {
        int result = 0;
        /* TODO: переписать сравнение. Текущий вариант между if и переменной,
         * начинающейся на if, выберет первое из-за большего приоритета
         */
        Token t = (Token) o;
        if (this.type.ordinal() != t.type.ordinal()) {
            result = this.type.ordinal() < t.type.ordinal() ? 1 : -1;
        } else {
            if (this.value.length() != t.value.length()) {
                result = this.value.length() > t.value.length() ? 1 : -1;
            }
        }
        return result;
    }
}
