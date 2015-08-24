package se.erikwelander.fastestwayhome.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StationDeparture
{
    public final long id;
    public final double x,
                         y;
    public final String name,
                        direction;
    public final Date leaving;

    public StationDeparture(long id, double x, double y, String name, String direction, String leaving) throws ParseException
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.direction = direction;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.leaving = dateFormat.parse(leaving);
    }
}
