package War.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import War.Entities.*;

public class WarDaoImpl implements WarDao {
    private WeaponDao<Launcher> launcherDao;
    private WeaponDao<Missile> missileDao;
    private static String LAUNCHERS_TBL="tbl_launchers";
    private static String DISPATCHING_TBL="tbl_dispatchings";
    private static String DESTRUCTIONS_TBL="tbl_destructions";
    private static String DB_NAME="War";
    private static boolean s_isDBCreated=false;
    private static String dbUrl = "jdbc:mysql://localhost/";
    @Override
    public void saveLauncher(Launcher launcher) {
        //launcherDao.save(launcher);
       
        
    		Connection connection = null;
    		try {
    			Class.forName("com.mysql.jdbc.Driver").newInstance();
    			createDB();
    			establishDBConncection();
    			
    		} catch (InstantiationException e) {
    			e.printStackTrace();
    		} catch (IllegalAccessException e) {
    			e.printStackTrace();
    		} catch (ClassNotFoundException e) {
    			e.printStackTrace();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    		finally {
    			try {
    				if(connection!=null)
    					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
    	
    
    }

    @Override
    public void saveLauncherDestructor(LauncherDestructor launcherDestructor) {

    }

    @Override
    public void saveMissileDestructor(MissileDestructor missileDestructor) {

    }

    @Override
    public void saveMissile(Missile missile) {
        //missileDao.save(missile);
    }
    
    private void createDB() throws SQLException {
    	if(s_isDBCreated) {
	    	Connection connection = null;
			connection = DriverManager.getConnection(dbUrl, "root", "");
			System.out.println("DB Server connection established");
			Statement statement = connection.createStatement();
			int result=statement.executeUpdate("CREATE DATABASE IF NOT EXISTS "+DB_NAME);
			s_isDBCreated=result>-1; 
			connection.close();
    	}
    }
    private Statement establishDBConncection() throws SQLException {
    	Connection connection = null;
		connection = DriverManager.getConnection(dbUrl+DB_NAME, "root", "");
		System.out.println("Database connection established");
		Statement statement = connection.createStatement();
		return statement;
    }
    
    private void createLaunchersTable(Statement statement) throws SQLException { 
    	StringBuilder stringBuilder=new StringBuilder();
    	stringBuilder.append("CREATE TABLE IF NOT EXISTS "+LAUNCHERS_TBL);
    	stringBuilder.append(" (id INT(64) NOT NULL AUTO_INCREMENT,");
    	stringBuilder.append(" launcherId  VARCHAR(50) NOT NULL,");
    	stringBuilder.append("timeStamp DATETIME NOT NULL,");
    	stringBuilder.append("PRIMARY KEY(id))");     
  
    	statement.executeUpdate(stringBuilder.toString());
    }
    
    private void createDispatchingssTable(Statement statement) throws SQLException { 
    	StringBuilder stringBuilder=new StringBuilder();
    	stringBuilder.append("CREATE TABLE IF NOT EXISTS "+DISPATCHING_TBL);
    	stringBuilder.append(" (id INT(64) NOT NULL AUTO_INCREMENT,");
    	stringBuilder.append(" launcherId  VARCHAR(50) NOT NULL,");
    	stringBuilder.append("launchedAt INT(64) NOT NULL,");
    	stringBuilder.append(" destination  VARCHAR(50) NOT NULL,");
    	stringBuilder.append("timeStamp DATETIME NOT NULL,");
    	stringBuilder.append("PRIMARY KEY(id))");     
  
    	statement.executeUpdate(stringBuilder.toString());
    }
    
    private void createDestructionsTable(Statement statement) throws SQLException { 
    	StringBuilder stringBuilder=new StringBuilder();
    	stringBuilder.append("CREATE TABLE IF NOT EXISTS "+DESTRUCTIONS_TBL);
    	stringBuilder.append(" (id INT(64) NOT NULL AUTO_INCREMENT,");
    	stringBuilder.append(" missileId  VARCHAR(50) NOT NULL,");
    	stringBuilder.append(" isHit BIT NOT NULL,");
    	stringBuilder.append("timeStamp DATETIME NOT NULL,");
    	stringBuilder.append("PRIMARY KEY(id))");     
  
    	statement.executeUpdate(stringBuilder.toString());
    }
    
}
