package spsa;

import java.util.Vector;

public class Utils
{

	/**
	 * This method takes string and breaks it into a Vector of strings based on
	 * a given delimeter
	 * 
	 * @param string
	 *            String
	 * @param delimiter
	 *            char
	 * @return Vector
	 */
	public static Vector< String > splitString(final String string, char delimeter)
	{
		Vector< String > ret = new Vector< String >();
	
		int begin = 0;
		int i;
		for (i = 0; i < string.length(); i++)
		{
			if (i > 0 && string.charAt( i ) == delimeter && string.charAt( i - 1 ) != delimeter)
			{
				ret.add( string.substring( begin, i ) );
				begin = i + 1;
			}
			else
			{
				if (string.charAt( i ) == delimeter)
				{
					begin = i + 1;
				}
			}
		}
		if (begin < i)
		{
			ret.add( string.substring( begin ) );
		}
	
		return ret;
	}

}
