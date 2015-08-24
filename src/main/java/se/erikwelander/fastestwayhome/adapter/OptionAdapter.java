package se.erikwelander.fastestwayhome.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.erikwelander.fastestwayhome.R;
import se.erikwelander.fastestwayhome.model.OptionRow;

public class OptionAdapter extends ArrayAdapter<OptionRow>
{
    private Context context;

    public OptionAdapter(Context context, int textViewResourceId, List<OptionRow> objects)
    {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    private class ViewHolder
    {
        ImageView thumbnail;
        TextView title;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        OptionRow rowItem = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_options_row, null);
            viewHolder = new ViewHolder();
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_option_row_thumbnail);
            viewHolder.title = (TextView) convertView.findViewById(R.id.list_option_row_title);

            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.thumbnail.setImageDrawable(rowItem.thumbnail);
        viewHolder.title.setText(rowItem.title);

        return convertView;
    }
}
