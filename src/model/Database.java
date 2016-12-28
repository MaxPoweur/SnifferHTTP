package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Database
{
    public static void initBackup() throws IOException
    {
    	if(Database.getBackup()!=null)
    		return;
    	File backup;
    	Date d = new Date();
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        backup = new File("data/capture_" + format.format(d).replace('/','_') + ".json");
	    try
		{
	    	backup.getParentFile().mkdirs();
	    	backup.createNewFile();
			   System.out.println("Successfuly saved datas in the file \"" + backup.getAbsolutePath() + "\".");
	    }
		catch (IOException e)
		{
		    throw new IOException("Error while saving datas in the backup file : \"" + backup.getAbsolutePath() + "\".");
	    }
    }
    
    public static File getBackup()
    {
    	File backup;
    	Date d = new Date();
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        backup = new File("data/capture_" + format.format(d).replace('/','_') + ".json");
	    if(!backup.exists())
	    	return null;
	    else
	    	return backup;
    }
    
	/**
	 * Enregistre les deux headers
	 * On ne verfie pas non plus quel est le site qui a été visitées
	 * !! il faut le rajouter en premier dans le csv sous la forme site;nomdusite;....values
     *
	 *
	 * @param header the header of http's request
	 *
	 *	 Nous devons récupérer le host (la page qui a été demandé) et
	 *      y ajouter l'heure actuel pour l'ajouter
	 */
    public static synchronized void save(String method, String request, String answer) throws FileNotFoundException, IOException
	{
    	Database.initBackup();
    	String saving = "";
	    try
		{
		    Scanner sc = new Scanner(Database.getBackup());
		    while (sc.hasNextLine())
		    	saving += sc.nextLine()+"\r\n";
			sc.close();
	    }
		catch (FileNotFoundException e)
		{
			throw new FileNotFoundException("Error while trying to open the backup file : \"" + Database.getBackup().getAbsolutePath() + "\".");
	    }

		// Je pense qu'il y a mieux pour ajouter � la fin d'un fichier ;)
		
	    int indexFirstLine = request.indexOf("\r\n");
    	String introductionLine = request.substring(0, indexFirstLine);
    	request = request.substring(indexFirstLine+2, request.length());
	    saving += "{\r\n";
	    saving += "\t\"introductionLine\": \""+ introductionLine +"\",\r\n";
	    saving += "\t\"method\": \""+ method +"\",\r\n";
	    
	    String variable, value;
	    String[] lines = request.split("\r\n"), content;
	    
	    for(int cpt = 0; cpt<lines.length; cpt++)
	    {
	    	content = lines[cpt].split(": ", 2);
	    	variable = content[0];
	    	value = content[1];
	    	saving += "\t\""+variable+"\": \""+value+"\"";
	    	
	    	if(cpt<(lines.length-1))
	    		saving+= ",";
	    	
	    	saving+="\r\n";
	    }
	    saving+="}";
	    
	    try
		{
		    FileWriter fw = new FileWriter(Database.getBackup());
		    fw.write(saving);
		    fw.close();
	    }
		catch (IOException e)
		{
			throw new IOException("Error while trying to write datas in the backup file : \"" + Database.getBackup().getAbsolutePath() + "\".");
	    }
    }

	/**
	 * hashmap qui fait correspondre : site vers données
	 * les données sont représenté par une hashMap se string vers string
	 * le 1er string représente les noms des headers et le 2nd les valeurs
	 * @return une hashmap qui fait correspondre : site vers données
	 */
	public HashMap<String, HashMap<String, String>> read() throws FileNotFoundException
	{
	    HashMap<String, HashMap<String, String>> retour = new HashMap<String, HashMap<String, String>>();
	    HashMap<String, String> values = new HashMap<>();
	    String sitename="";
	    Scanner sc = null;
	    try
		{
		    sc = new Scanner(Database.getBackup());
		    
		    while(sc.hasNextLine()) // Each line of the backup file matches to a website
		    {
		    	String[] couples = sc.nextLine().split(";");
		    	
			    if(!couples[0].equals("site") || couples.length%2!=0) // Need to have even values, and the site's name
			    	return null; // Reconsider to create new customized Exception

			    sitename = couples[1];
			    
			    for(int i=2; i < couples.length; i+=2)
			    {
					values.put(couples[i], couples[i+1]);
			    }
			    retour.put(sitename, values);
			    
			    values = new HashMap<>(); // Reset
		    }
		    
	    }
		catch (FileNotFoundException e)
		{
			throw new FileNotFoundException("Error while trying to open the backup file : \"" + Database.getBackup().getAbsolutePath() + "\".");
	    }
	    finally
	    {
	    	if(sc!=null)
			    sc.close();
	    }
	    return retour;
    }
}
