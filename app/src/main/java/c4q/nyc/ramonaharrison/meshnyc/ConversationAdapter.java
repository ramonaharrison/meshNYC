package c4q.nyc.ramonaharrison.meshnyc;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alvin2 on 8/1/15.
 */
public class ConversationAdapter extends BaseAdapter{

        private Context mContext;
        private ArrayList<Message> mMessages;


        public ConversationAdapter(Context context, ArrayList<Message> messages) {
            super();
            this.mContext = context;
            this.mMessages = messages;
        }
        @Override
        public int getCount() {
            return mMessages.size();
        }
        @Override
        public Object getItem(int position) {
            return mMessages.get(position);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Message message = (Message) this.getItem(position);

            ViewHolder holder;
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, parent, false);
                holder.message = (TextView) convertView.findViewById(R.id.message_text);
                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder) convertView.getTag();

            holder.message.setText(message.getMessageContent());

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.message.getLayoutParams();
            //check if it is a status message then remove background, and change text color.
//                lp.gravity = Gravity.LEFT;
                holder.message.setTextColor(Color.BLUE);

            return convertView;
        }
        private static class ViewHolder
        {
            TextView message;
        }

        @Override
        public long getItemId(int position) {
            //Unimplemented, because we aren't using Sqlite.
            return position;
        }

}
