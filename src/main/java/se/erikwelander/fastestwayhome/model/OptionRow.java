package se.erikwelander.fastestwayhome.model;

import android.graphics.drawable.Drawable;

public final class OptionRow
{
    public final Drawable thumbnail;
    public final String title;

    public OptionRow(final Drawable thumbnail, final String title)
    {
        this.thumbnail = thumbnail;
        this.title = title;
    }
}
