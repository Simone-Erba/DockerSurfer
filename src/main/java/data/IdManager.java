package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class IdManager {	
	Map<String, Integer> m;
	int num;
	public IdManager() {
		super();
		m =new HashMap<String, Integer>();
		num=1;
	}
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