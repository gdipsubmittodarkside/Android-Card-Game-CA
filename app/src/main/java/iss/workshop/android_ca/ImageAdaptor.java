package iss.workshop.android_ca;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

    // defines the columns and positions for gridview
    public class ImageAdaptor extends BaseAdapter {

        private Context context;

        public ImageAdaptor(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return 12;
        }
        // since in layout we defined numCol=3, this will return a 4x3 layout

        @Override
        public Object getItem(int position) {
            return null;
        }
        // not used

        @Override
        public long getItemId(int position) {
            return 0;
        }
        // not used

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView;

            if(convertView == null){
                imageView = new ImageView(this.context);
                imageView.setLayoutParams(new GridView.LayoutParams(350,300));
                // defining the width and height of each grid
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            else imageView = (ImageView) convertView;

            imageView.setImageResource(R.drawable.question_mark);

            return imageView;
        }
    }



