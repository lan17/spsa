package edu.ohiou.lev_neiman.misc;


public class Pair<T, U>
{
    private final T first;
    private final U second;
    private final int hash;

    public Pair( T f, U s )
    {
        this.first = f;
        this.second = s;
        hash = ( first == null ? 0 : first.hashCode() )
               + ( second == null ? 0 : second.hashCode() );
    }

    public T getFirst()
    {
        return first;
    }

    public U getSecond()
    {
        return second;
    }


    public int hashCode()
    {
        return hash;
    }

    public String toString()
    {
        return "{ " + first.toString() + ", " + second.toString() + " }";
    }


    public boolean equals( Object oth )
    {
        if( this == oth )
        {
            return true;
        }
        if( oth == null || ! ( getClass().isInstance( oth ) ) )
        {
            return false;
        }
        Pair<T, U> other = getClass().cast( oth );
        return( first == null ? other.first == null : first.equals( other.first ) )
                && ( second == null ? other.second == null : second.equals( other.second ) );
    }


}
