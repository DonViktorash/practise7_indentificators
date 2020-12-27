import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Лексический анализатор
 */
public class Lexer
{
    /**
     * Входная строка
     */
    private final String str;
    /**
     * Текущая позиция во входной строке
     */
    private int index = 0;

    private final HashTable hashTable = new HashTable();
    {
        hashTable.Insert(new Token(TokenType.ASSIGN, ":="));
        hashTable.Insert(new Token(TokenType.ADD,"+"));
        hashTable.Insert(new Token(TokenType.SUB, "-"));
        hashTable.Insert(new Token(TokenType.MUL, "*"));
        hashTable.Insert(new Token(TokenType.DIV, "/"));
        hashTable.Insert(new Token(TokenType.LPAR, "("));
        hashTable.Insert(new Token(TokenType.RPAR, ")"));
        hashTable.Insert(new Token(TokenType.SM, ";"));
    }

    public Lexer(String str)
    {
        this.str = str;
    }

    /**
     * Попытка сопоставить текст, начиная с текущей позиции index, с
     * регулярным выражением.
     *
     * @param pattern регулярное выражение
     * @return -1, если если регулярное выражение не удалось найти в
     * текущей позиции; значение >= 0 - индекс первого символа,
     * следующего после найденной лексемы, соответствующей
     * регулярному выражению
     */
    private int match(Pattern pattern)
    {
        Matcher matcher = pattern.matcher(str);
        // Устанавливаем регион поиска - начиная с текущей позиции:
        matcher.region(index, str.length());
        if (matcher.lookingAt())
            // Да, в текущей позиции найдено регулярное выражение - возвращаем индекс символа _после_ найденной лексемы
            return matcher.end();

        // Не найдено совпадения - возвращаем -1
        return -1;
    }

    private Token matchRomanNumber()
    {
        Pattern numberPattern = Pattern.compile("[IVX]+(\\.[IVX]*)?|\\.[IVX]+");
        int matched = match(numberPattern);
        if (matched < 0)
            return null;
        String numberText = str.substring(index, matched);

        Token temp = new Token(TokenType.ROMAN_NUMBER, numberText, matched);
        hashTable.Insert(temp);
        return temp;
    }

    private Token matchAnySymbol()
    {
        for (Token entry : hashTable.entrySet())
        {
            String key = entry.symbolText;
            TokenType value = entry.type;
            Pattern symbolPattern = Pattern.compile(Pattern.quote(key));
            int matched = match(symbolPattern);
            if (matched < 0)
                continue;
            String symbolText = str.substring(index, matched);
            return new Token(value, symbolText, matched);
        }
        return null;
    }

    private Token matchSpaces()
    {
        int i = index;
        while (i < str.length())
        {
            char ch = str.charAt(i);
            if (ch <= ' ')
                i++;
            else
                break;
        }
        if (i > index)
        {
            String spaces = str.substring(index, i);
            return new Token(TokenType.SPACES, spaces, i);
        }
        else
            return null;
    }

    private Token matchVariable()
    {
        Pattern varPattern = Pattern.compile("[A-Za-z]+[\\w]*");
        int matched = match(varPattern);
        if (matched < 0)
            return null;
        String varText = str.substring(index, matched);

        Token temp = new Token(TokenType.VAR, varText, matched);
        hashTable.Insert(temp);
        return temp;
    }

    /**
     * Получение лексемы, стоящей в текущей позиции.
     *
     * @return null, если в строке больше нет лексем
     */
    private Token matchAnyToken() throws ParseException
    {
        // Мы стоим в конце строки - больше нет лексем:
        if (index >= str.length())
            return null;
        // Перебираем все возможные типы лексем:
        Token spacesToken = matchSpaces();
        if (spacesToken != null)
            return spacesToken;
        Token romanNumberToken = matchRomanNumber();
        if (romanNumberToken != null)
            return romanNumberToken;
        Token symbolToken = matchAnySymbol();
        if (symbolToken != null)
            return symbolToken;
        Token varToken = matchVariable();
        if (varToken != null)
            return varToken;
        // Символ в текущей позиции не подходит ни к одной из возможных лексем - ошибка:
        throw new ParseException("Unexpected character '" + str.charAt(index) + "'", index);
    }

    /**
     * Получение лексемы, стоящей в текущей позиции и перемещение текущей
     * позиции дальше.
     *
     * @return null, если в строке больше нет лексем
     */
    public Token nextToken() throws ParseException
    {
        while (true)
        {
            Token token = matchAnyToken();
            if (token == null)
                // Строка закончилась, больше нет лексем:
                return null;

            // Перемещаем текущую позицию после найденной лексемы:
            index = token.to;
            if (token.type != TokenType.SPACES)
                // Непробельную лексему возвращаем:
                return token;
        }
    }

    public List<Token> getAllTokens() throws ParseException
    {
        List<Token> allTokens = new ArrayList<>();
        while (true)
        {
            Token token = nextToken();
            if (token == null)
                break;
            allTokens.add(token);
        }
        return allTokens;
    }
}
