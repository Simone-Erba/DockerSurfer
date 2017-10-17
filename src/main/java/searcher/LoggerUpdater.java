package searcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class LoggerUpdater {
File f;
FileWriter fw;
LoggerUpdater instance;
private LoggerUpdater(String path)
{
	f=new File(path);
	try {
		fw=new FileWriter(f);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public LoggerUpdater getInstance()
{
	if(instance==null)
	{
		instance=new LoggerUpdater("/data/log.txt");
	}
	return instance;
}
public synchronized void write(String str)
{
	Date d=new Date();
	try {
		fw.write(d+"     "+str);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
