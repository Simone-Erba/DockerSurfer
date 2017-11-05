package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author Simone-Erba
 *
 *A class for get unique numbers from Strings, used for memory purposes
 */
public class IdManager {	
/**
 * contain the mapping between Strings and numbers
 */
	Map<String, Integer> m;
	int num;
	/**
	 * create a new map. First id is 1
	 */
	public IdManager() {
		super();
		m =new HashMap<String, Integer>();
		num=1;
	}
	/**
	 * If the string is already associated with a number, return it
	 * otherwise, generate a new number
	 * @param s The string to map
	 * @return The String unique number
	 */
	public Integer getNumero(String s)
	{
		if(m.get(s) != null)
		{
			return m.get(s);
		}
		else
		{
			num++;
			m.put(s, num);
			return m.get(s);
		}
	}
}