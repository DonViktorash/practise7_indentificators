import java.util.List;
import java.util.Scanner;

/**
 * Грамматический разбор грамматики
 * выражение ::= ЧИСЛО ('+' ЧИСЛО)*
 */
public class Parser1
{
    /**
     * Список лексем
     */
    private final List<Token> tokens;
    /**
     * Индекс текущей лексемы
     */
    private int index = 0;

    public Parser1(List<Token> tokens)
    {
        this.tokens = tokens;
    }

    /**
     * Проверка типа текущей лексемы.
     *
     * @param type предполагаемый тип лексемы
     * @return не null, если текущая лексема предполагаемого типа (при этом
     * текущи индекс сдвигается на 1);
     * null, если текущая лексема другого типа
     */
    private Token match(TokenType type)
    {
        if (index >= tokens.size())
            return null;
        Token token = tokens.get(index);
        if (token.type != type)
            return null;
        index++;
        return token;
    }

    /**
     * Сообщение об ошибке с указанием текущей позиции в тексте.
     *
     * @param message текст сообщения
     */
    private void error(String message) throws ParseException
    {
        // Позиция ошибки в тексте
        int errorPosition;
        if (index >= tokens.size())
        {
            // Мы стоим в конце текста
            if (tokens.isEmpty())
                // Лексем не было вообще - текст пустой; указываем на начало текста
                errorPosition = 0;
            else
                // Берем координату после последней лексемы
                errorPosition = tokens.get(tokens.size() - 1).to;
        }
        else
        {
            // Берем координату текущей лексемы
            Token token = tokens.get(index);
            errorPosition = token.to;
        }
        throw new ParseException(message, errorPosition);
    }

    /**
     * Грамматический разбор выражения по грамматике
     * выражение ::= ЧИСЛО ('+' ЧИСЛО)*
     */
    public void matchExpression() throws ParseException
    {
        if(match(TokenType.SM) != null)
            matchExpression();

        int paramCounter = 0;
        Token lparam = match(TokenType.LPAR);
        if(lparam != null)
            paramCounter++;

        Token minus = match(TokenType.SUB);
        Token n1 = match(TokenType.ROMAN_NUMBER);
        Token n12 = match(TokenType.VAR);
        if (n1 == null && n12 == null)
            error("Missing number/variable!");

        if(match(TokenType.SM) != null)
            matchExpression();

        while ((match(TokenType.ASSIGN) != null) || (match(TokenType.ADD) != null) || (match(TokenType.SUB) != null) || (match(TokenType.MUL) != null) || (match(TokenType.DIV) != null))
        {
            lparam = match(TokenType.LPAR);
            if (lparam != null)
                paramCounter++;

            minus = match(TokenType.SUB);
            if (lparam == null && minus != null)
                error("You should insert negative numbers in parentheses");
            Token n2 = match(TokenType.ROMAN_NUMBER);
            if (n2 == null)
                error("Missing number!");
            else if ((match(TokenType.SM) == null) && (match(TokenType.ROMAN_NUMBER) != null))
                error("You should divide expressions by a semicolon");

            Token rparam = match(TokenType.RPAR);
            if (rparam != null)
                paramCounter--;
        }
        while (match(TokenType.RPAR) != null)
            paramCounter--;

        if (paramCounter != 0)
            error("Missing parentheses");

        if(match(TokenType.VAR) != null || match(TokenType.ROMAN_NUMBER) != null)
            error("Missing arithmetic sign");

        if(match(TokenType.SM) != null)
            matchExpression();
    }

    public static void main(String[] args) throws ParseException
    {
        Scanner in = new Scanner(System.in);
        String expression = in.nextLine();

        Lexer lexer = new Lexer(expression);
        List<Token> allTokens = lexer.getAllTokens();
        Parser1 parser = new Parser1(allTokens);
        parser.matchExpression();

        for (int i = 0; i < parser.tokens.size(); i++)
            System.out.println(parser.tokens.get(i).symbolText + " " + parser.tokens.get(i).type);
    }
}
