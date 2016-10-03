package com.app.heyphil;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

	private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1;
	Context context;

	public ChatArrayAdapter(Context context, List<ChatMessage> data) {
		super(context, R.layout.item_mine_message, data);
	}

	@Override
	public int getViewTypeCount() {
		// my message, other message, my image, other image
		return 4;
	}

	@Override
	public int getItemViewType(int position) {
		ChatMessage item = getItem(position);

		if (item.isMine()) return MY_MESSAGE;
		else return OTHER_MESSAGE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int viewType = getItemViewType(position);
		long date = System.currentTimeMillis();

		SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
		String dateString = sdf.format(date);
		if (viewType == MY_MESSAGE) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_message, parent, false);

			TextView textView = (TextView) convertView.findViewById(R.id.singleMessage);
			TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
			CircularImageView imageView = (CircularImageView) convertView.findViewById(R.id.circularImageView);
			textView.setText(getItem(position).getContent());
			timestamp.setText(dateString);
			if(!Data.Bitmap&& Data.sex.equals("FEMALE"))
			{
				imageView.setImageResource(R.drawable.femele_icon);
			}
			else if(!Data.Bitmap&& Data.sex.equals("MALE"))
			{
				imageView.setImageResource(R.drawable.male_icon);
			}
			else {
				imageView.setImageBitmap(Data.bitmap);
			}


		} else{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_message, parent, false);

			TextView textView = (TextView) convertView.findViewById(R.id.singleMessage);
			TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.usericon);
			textView.setText(getItem(position).getContent());
			textView.setText(Html.fromHtml(getItem(position).getContent().toString()));
			timestamp.setText(dateString);
		/*	if(Data.avatar.equals("Salesman")){
				imageView.setImageResource(R.drawable.salesman);
			}
			else if(Data.avatar.equals("Doctor")){
				imageView.setImageResource(R.drawable.doctor);
			}
			else if(Data.avatar.equals("Agent")){
				imageView.setImageResource(R.drawable.agent);
			}
			else{
				imageView.setImageResource(R.drawable.male_icon);
			}*/
		}
		return convertView;
	}
}