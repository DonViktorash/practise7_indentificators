import java.util.ArrayList;

public class HashTable
{
    private final static int TABLE_SIZE = 128;

    Token[] table = new Token[TABLE_SIZE];

    HashTable()
    {
        for (int i = 0; i < TABLE_SIZE; i++)
            table[i] = null;
    }

    public int LookUp(String name)
    {
        int hash = h(name);
        int initialHash = -1;

        while (hash != initialHash && (table[hash] != null && !table[hash].equals(name) || table[hash].symbolText == null))
        {
            if (initialHash == -1)
                initialHash = hash;

            hash = (hash + 1) % TABLE_SIZE;
        }
        if (table[hash] == null || hash == initialHash)
            return -1;
        else
            return hash;
    }

    public void Insert(Token token)
    {
        int hash = h(token.symbolText);
        int initialHash = -1;
        int indexOfDeletedEntry = -1;

        while (hash != initialHash && (table[hash] != null && table[hash].symbolText == null || table[hash] == token))
        {
            if (initialHash == -1)
                initialHash = hash;

            if (table[hash].symbolText == null)
                indexOfDeletedEntry = hash;

            hash = (hash + 1) % TABLE_SIZE;
        }

        if ((table[hash] == null || hash == initialHash) && indexOfDeletedEntry != -1)
            table[indexOfDeletedEntry] = token;

        else if (initialHash != hash)
            if (table[hash] != null && table[hash].symbolText != null && table[hash] == token)
                table[hash] = token;
            else
                table[hash] = token;
    }

    public boolean Remove(String name)
    {
        int hash = h(name);
        int initialHash = -1;

        while (hash != initialHash && (table[hash] != null && table[hash].symbolText == null || !table[hash].symbolText.equals(name)))
        {
            if (initialHash == -1)
                initialHash = hash;

            hash = (hash + 1) % TABLE_SIZE;
        }

        if (hash != initialHash && table[hash] != null)
        {
            table[hash] = new Token(null, null);
            return true;
        }
        return false;
    }

    private int h(String name)
    {
        return (name.hashCode() % TABLE_SIZE);
    }

    public ArrayList<Token> entrySet()
    {
        ArrayList tokenList = new ArrayList();
        for (int i = 0; i < TABLE_SIZE; i++)
            if (table[i] != null && table[i].symbolText != null)
                tokenList.add(table[i]);
        return tokenList;
    }
}
