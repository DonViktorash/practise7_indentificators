public enum TokenType
{
    /**
     * Пробелы
     */
    SPACES,
    /**
     * Римские цифры 'X', 'V' и 'I'
     */
    ROMAN_NUMBER,
    /**
     * Символ '+'
     */
    ADD,
    /**
     * Символ '-'
     */
    SUB,
    /**
     * Символ '*'
     */
    MUL,
    /**
     * Символ '/'
     */
    DIV,
    /**
     * Символ '('
     */
    LPAR,
    /**
     * Символ ')'
     */
    RPAR,
    /**
     * Переменная
     */
    VAR,
    /**
     * Приравнивание
     */
    ASSIGN,
    /**
     * Символ ';'
     */
    SM
}