package database;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

public class DataBase {
	private Connection connection = null;
	private Statement statement = null;
	private final String[] DEFAULT_MAZES_NAME = {"Map01","Map02"};
	private final String[] DEFAULT_MAZES_LOCATION = {"mazes/inlaadmaze.maze","mazes/traptest.maze"};
	
	public DataBase(){
		try {
			setup();
		} catch (ClassNotFoundException e) {
			System.err.println("DataBase: Somthing went wrong, couldn't initialize DataBase correctly: " + e);
		}
	}
	
	
	protected void setup() throws ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
		
		try{
			// Create DataBase connection
			connection = DriverManager.getConnection("jdbc:sqlite:mazerunner.db");
			statement = connection.createStatement();
			statement.setQueryTimeout(30); // timeout to 30 sec
			
			if(!doesTableExists("Map",connection)){
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS Map(ID AutoNumber,Name TINYTEXT, Data LONGBLOB);"); // create table with an ID, Name (max 255 byte) and Data (max 4GB)
				statement.executeUpdate("CREATE INDEX IF NOT EXISTS ID ON Map(ID);"); // create index for table Map for faster search
				
				
				// adds the default lvl's to the database if the database was empty
				for(int i = 0; i < DEFAULT_MAZES_LOCATION.length;i++)	
					addMap(DEFAULT_MAZES_NAME[i],DEFAULT_MAZES_LOCATION[i]);
				
			}
		}
		catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
		}
	}
    
	protected void cleanUp(){
		try
		{
			if(connection != null)
				connection.close();
		}
			catch(SQLException e)
			{
				System.err.println("DataBase: Closing the connection failed! " + e);
			}
	}
	
	@Override
	protected void finalize() throws Throwable{
		try{
			this.cleanUp();
		}finally{
			super.finalize();
		}
	}
	
	public void addMap(String name,String fileLocation){
		try{
			FileInputStream in = new FileInputStream(fileLocation);
			String data = "";
			int bufferSize = in.available(); // estimation of the available bytes to read
			byte[] buffer = new byte[bufferSize];
			int readed = 0,totalReaded = 0;
			while(readed != -1 && in.available() > 0){
				readed = in.read(buffer, totalReaded, bufferSize);
				
				if(readed != -1){
					data += new String(buffer);
					totalReaded = readed;
				}
			}
			in.close();
		
			statement.executeUpdate("INSERT INTO Map(Name,Data)" +
								"VALUES('" + name + "','" + data + "');");
		}catch(SQLException | IOException e){
			System.err.println("DataBase: " + e.getMessage());
		}
	}
	
	
	public ByteArrayInputStream getMap(String name){
		try{
			ResultSet rs = statement.executeQuery("SELECT Data " +
												"FROM Map " +
												"WHERE Map.Name = '" + name + "';");
			if(rs.next()){
				byte[] res = rs.getBytes("Data");
				return new ByteArrayInputStream(res);
			}
			else{
				System.err.println("DataBase: rs is not open!\n\tSomething wrong with SQL statement?");
				return null;
			}
		}catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
			return null;
		}
	}
	
	/* Doesn't work
	 * - The Map.ID is always 0 don't know why
	 * - Something goes wrong in the Query 
	 */
	public ByteArrayInputStream getMap(int ID){
		
		try{
			ResultSet rs = statement.executeQuery("SELECT Data " +
												"FROM Map " +
												"WHERE Map.ID = '" + ID + "';"); // my guess is that the integer is the problem
			if(rs.next()){
				byte[] res = rs.getBytes("Data");
				return new ByteArrayInputStream(res);
			}
			else{
				System.err.println("DataBase: rs is not open!\n\tSomething wrong with SQL statement?");
				return null;
			}
		}catch(SQLException e){
			System.err.println("DataBase: " + e.getMessage());
			return null;
		}
	}
	
	private boolean doesTableExists(String tableName,Connection conn) throws SQLException{
		DatabaseMetaData dbmd = conn.getMetaData(); 
		ResultSet rs = dbmd.getTables(null, null, tableName, null);
		
		if(rs.next())
			return rs.getRow() == 1;
		
		return false;
	}
}
