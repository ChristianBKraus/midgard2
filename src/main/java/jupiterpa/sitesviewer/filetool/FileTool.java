package jupiterpa.sitesviewer.filetool;

import java.util.*;
import java.io.*;

public class FileTool
{
	private String fileName;
	private ArrayList<String> file = new ArrayList();
	
	public FileTool (String fileName)
	{
		this.fileName = fileName;
		
		try
		{
			BufferedReader Reader = new BufferedReader (new FileReader (fileName));
			boolean fileEnd = false;
			
			while (!fileEnd)
			{
				String line = Reader.readLine();
				if (line == null) fileEnd = true;
				else file.add (line);
			}
			Reader.close();
		}
		catch (IOException x) {
			System.err.println("Can not load file " + fileName + ". ");
		}
	}
	
	public ArrayList<String> getFile ()
	{
		return file;
	}

	public String getFileForOutput ()
	{
		String returning = "";
		for (int i = 0; i < file.size(); i++)
		{
			if (i != file.size() - 1)
			{
				returning += file.get(i) + "\n";
			}
			else
			{
				returning += file.get(i);
			}
		}
		return returning;
	}

	public void setFile (ArrayList newFile)
	{
		this.file = newFile;
	}
	
	public String getLine (int line)
	{
		return (String) file.get(line);
	}
	
	public void setLine (int line, String text)
	{
		file.set (line, text);
	}
	
	public void writeToLine (int line, String text)
	{
		this.setLine (line, this.getLine (line) + text);
	}
	
	public void saveFile () throws IOException {
		String input;
		BufferedWriter Writer = new BufferedWriter (new FileWriter (fileName));
		for (int i = 0; i < file.size(); i++)
		{
			Writer.write (file.get (i) + "\r\n");
		}
		Writer.close();
	}
}