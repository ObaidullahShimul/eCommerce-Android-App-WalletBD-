package www.shimul.com.walletbd3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Shimul on 2/11/2018.
 */

class CustomAdapter extends BaseAdapter{

    Context context;
    //Wallet_Fragment wallet_fragment;
    String[] studentN;
    int []spic;
    LayoutInflater inflater;

    CustomAdapter(Context context, String[] studentN, int[] spic) {
        //this.wallet_fragment = wallet_fragment;
        this.context=context;
        this.studentN = studentN;
        this.spic = spic;
    }


    @Override
    public int getCount() {
        return studentN.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.sample_grid_view,parent,false);
        }

        ImageView imageView= (ImageView) convertView.findViewById(R.id.imageviewId);
        TextView textView= (TextView) convertView.findViewById(R.id.textviewId);

        imageView.setImageResource(spic[position]);
        textView.setText(studentN[position]);
        return convertView;
    }
}
