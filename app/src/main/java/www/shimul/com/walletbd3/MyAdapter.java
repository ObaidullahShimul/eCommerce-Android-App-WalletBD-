package www.shimul.com.walletbd3;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    //private static ClickListenerView clickListenerView;

    private Context context;
    private List<Upload> uploadList;

    //String codeT=


    public MyAdapter(Context context, List<Upload> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.single_item_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final Upload uploadDis=uploadList.get(i);
        myViewHolder.codeTextView.setText("Code: "+uploadDis.getCode());
        //myViewHolder.titleTextView.setText(uploadDis.getTitle());
        //myViewHolder.desTextView.setText(uploadDis.getDescription());
        //myViewHolder.codeTextView.setText(uploadDis.getCode());
        myViewHolder.priceTextView.setText("Price: "+uploadDis.getPrice());
        Picasso.with(context)
                .load(uploadDis.getImage_ONE())
                .placeholder(R.drawable.italyflag)
                .fit()
                .centerCrop()
                .into(myViewHolder.imageView);


        //myViewHolder.codeTextView.geti

        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,WalletItemsClickView.class);
                intent.putExtra("des",uploadDis.getDescription());
                intent.putExtra("title",uploadDis.getTitle());
                intent.putExtra("price",uploadDis.getPrice());
                intent.putExtra("code",uploadDis.getCode());
                intent.putExtra("image",uploadDis.getImage_ONE());
                intent.putExtra("imagetwo",uploadDis.getImage_TWO());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }


    //-----------MyViewHolder--------------
    public class MyViewHolder extends RecyclerView.ViewHolder { //implements View.OnClickListener,View.OnLongClickListener {

        TextView codeTextView;
        //TextView titleTextView;
        //TextView desTextView;
        TextView priceTextView;
        ImageView imageView;
        ImageView imageViewTwo;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            codeTextView=itemView.findViewById(R.id.codeTextViewId);
            //titleTextView=itemView.findViewById(R.id.codeTextViewId);
            //desTextView=itemView.findViewById(R.id.codeTextViewId);
            priceTextView=itemView.findViewById(R.id.priceTextViewId);
            imageView=itemView.findViewById(R.id.cardImageViewId);
            linearLayout=itemView.findViewById(R.id.linearId);

            //--------item setonlistener-----
            //itemView.setOnClickListener(this);
            //itemView.setOnLongClickListener(this);
        }


        /*/-------implementing onClick method------------
        @Override
        public void onClick(View v) {
            clickListenerView.onItemClick(getAdapterPosition(),v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListenerView.onItemLongClick(getAdapterPosition(),v);
            return false;
        }*/
    }

    /*public interface ClickListenerView{

        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    public static void setOnItemClickListenerView(ClickListenerView clickListenerView) {
        MyAdapter.clickListenerView = clickListenerView;
    }*/
}
