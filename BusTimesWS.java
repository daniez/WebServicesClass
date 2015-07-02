//Danielle Zoe Aloicius        BusTimesWS Web Service

package BusServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Calendar;
import java.util.Date;

@WebService(serviceName = "BusTimesWS")
public class BusTimesWS {

    static final String database ="jdbc:mysql://localhost:3306/bus";
    static final String username = "root"; //would not normally be hardcoded
    static final String password = "";     //testing purposes
    
    @WebMethod(operationName = "BusTimesOp")
    public String BusTimesOp() {
     try
        {
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dateFormat3 = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dateFormat4 = new SimpleDateFormat("mm");

        //timeCurrent
        Date dateCurrent = cal.getTime();
        String timeCurrent= dateFormat.format(dateCurrent);
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);
        
        //minutes
        String minutes = dateFormat4.format(dateCurrent);
        int minutesAM = 60-Integer.parseInt(minutes);
        String minutesAMString = "00:" + minutesAM + ":00"; 
        
        //timeAs 11:45pm 
        String yearMonthDay = dateFormat3.format(dateCurrent);
        Date elevenfortyfive = dateFormat2.parse(yearMonthDay +" 23:45:00");
        cal.setTime(elevenfortyfive);
        
        //timeAhead
        cal2.add(Calendar.MINUTE, 15);       
        String timeAhead = dateFormat.format(cal2.getTime());
        

        String day = "";
        switch(currentDay){
        case 1:
            day="Sunday";
            break;
        case 2: case 3: case 4: case 5: case 6:
            day="Weekday";
            break;
        case 7:
            day="Saturday";
            break;
        }
 
       
        //loading MySQL driver
        Class.forName("com.mysql.jdbc.Driver");
        //creating a variable type Connection to the database bus
        Connection connectiondB = DriverManager.getConnection(database,username,password);

        String SQLselect ="";
        
        //if after 11:45 pm Sunday
        if (currentDay==1 && dateCurrent.compareTo(elevenfortyfive)>0)
        {
            
            
            SQLselect="select * from buses_table where (day='" + day+"' AND '"+timeCurrent+
                    "' <= bus_time AND bus_time <='23:59:00') OR "+ 
                    "(day='Weekday' AND '00:00:00' <=bus_time AND bus_time <='"+minutesAMString+"');";
            
        }

        //if after 11:45 pm Friday
        else if (currentDay==6 && dateCurrent.compareTo(elevenfortyfive)>0)
        {
            SQLselect="select * from buses_table where (day='" + day+"' AND '"+timeCurrent+
                    "' <= bus_time AND bus_time <='23:59:00') OR "+ 
                    "(day='Saturday' AND '00:00:00' <=bus_time AND bus_time <='"+minutesAMString+"');";
        }

        //if after 11:45 pm Saturday
        else if (currentDay==7 && dateCurrent.compareTo(elevenfortyfive)>0)
        {
            SQLselect="select * from buses_table where (day='" + day+"' AND '"+timeCurrent+
                    "' <= bus_time AND bus_time <='23:59:00') OR "+ 
                    "(day='Sunday' AND '00:00:00' <=bus_time AND bus_time <='"+minutesAMString+"');";
        }

        else 
        {
        
            SQLselect = "select * from buses_table where day = '" + day + 
                "' AND ('" + timeCurrent +  
                "' <=bus_time  AND bus_time <='" + timeAhead+"');";
        }    
        //creating a variable type Statement
        Statement SQLstatement = connectiondB.createStatement();
        //executing the SQLselect and storing the outcome in a variable type ResultSet
        ResultSet result = SQLstatement.executeQuery(SQLselect);
        //displaying the record in case there is one

        boolean found = false;
        String foundbuses= "";
        while (result.next())
        {
            found =true;
            String busID_found = result.getString(1);
            String day_found = result.getString(2);
            String time_found = result.getString(3);

             foundbuses +=busID_found + " " + day_found + " " + time_found + " <br />";
        }
        if(found==false)
            return "Bus not found";

        return foundbuses;
        
        
                   
    }
    catch (Exception error)
    {
        return "Error: " + error;
    }

        
  
    }
}
