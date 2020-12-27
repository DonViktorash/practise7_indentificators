public class Token
{
    public TokenType type;
    public String symbolText;
    public int to;

    public Token(TokenType value, String symbolText, int to)
    {
        type = value;
        this.symbolText = symbolText;
        this.to = to;
    }

    public Token(TokenType value, String symbolText)
    {
        type = value;
        this.symbolText = symbolText;
        to = 0;
    }
}
