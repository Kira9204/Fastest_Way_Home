package se.erikwelander.fastestwayhome.model;
import android.graphics.drawable.Drawable;

public final class TransportOptionRow
{
    public final Drawable thumbnail;
    public final String title,
                        description,
                        note;

    public TransportOptionRow(final Drawable thumbnail, final String title, final String description, final String note)
    {
        this.thumbnail = thumbnail;
        this.title = title;
        this.description = description;
        this.note = note;
    }
}
