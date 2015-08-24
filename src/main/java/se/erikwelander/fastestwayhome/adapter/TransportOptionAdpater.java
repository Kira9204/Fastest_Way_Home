package se.erikwelander.fastestwayhome.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.erikwelander.fastestwayhome.R;
import se.erikwelander.fastestwayhome.model.TransportOptionRow;

public class TransportOptionAdpater extends ArrayAdapter<TransportOptionRow>
{
    private Context context;

    public TransportOptionAdpater(Context context, int textViewResourceId, List<TransportOptionRow> objects)
    {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    private class ViewHolder
    {
        ImageView thumbnail;
        TextView title,
                 description,
                 note;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        TransportOptionRow rowItem = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_transport_option_row, null);
            viewHolder = new ViewHolder();
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_transport_option_thumbnail);
            viewHolder.title = (TextView) convertView.findViewById(R.id.list_transport_option_title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.list_transport_option_description);
            viewHolder.note = (TextView) convertView.findViewById(R.id.list_transport_option_note);

            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.thumbnail.setImageDrawable(rowItem.thumbnail);
        viewHolder.title.setText(rowItem.title);
        viewHolder.description.setText(rowItem.description);
        viewHolder.note.setText(rowItem.note);

        return convertView;
    }
}
