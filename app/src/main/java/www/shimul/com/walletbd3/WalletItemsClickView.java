package www.shimul.com.walletbd3;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class WalletItemsClickView extends AppCompatActivity {

    TextView urlText,titleText;
    ImageView img;
    String Des,Title;
    String ImgDis,ImgDis3;
    //ScrollView scrollView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_items_click_view);

        String[] imageUrls=new String[]{

                ImgDis=getIntent().getStringExtra("image"),
                ImgDis3=getIntent().getStringExtra("imagetwo"),
                //ImgDis=getIntent().getStringExtra("image")

        };

        /*urlText=findViewById(R.id.urlId);
        titleText=findViewById(R.id.titleId);
        img=findViewById(R.id.imageD);

        Des=getIntent().getStringExtra("des");
        Title=getIntent().getStringExtra("title");
        ImgDis=getIntent().getStringExtra("image");
        urlText.setText(Des);
        titleText.setText(Title);
        */
        //img.setImageResource(Integer.parseInt(ImgDis));

        //scrollView=findViewById(R.id.wScrollId);
        //scrollView.setFillViewport(true);

        ViewPager viewPager=findViewById(R.id.walletPagerId);
        viewPagerAdapter adapter=new viewPagerAdapter(this,imageUrls);
        viewPager.setAdapter(adapter);

        getIncomingIntent();

    }


    public void getIncomingIntent(){

        if (getIntent().hasExtra("des") && getIntent().hasExtra("image")){

           String imgUrl=getIntent().getStringExtra("image");
           String des=getIntent().getStringExtra("des");
           String title=getIntent().getStringExtra("title");
           String price=getIntent().getStringExtra("price");
           String code=getIntent().getStringExtra("code");
           //urlText.setText(url);
            //setImage(url,des);
            setImage("Code: "+code,"Title: "+title,"Price: BDT-"+price,des);
        }
    }

    private void setImage(String pCode, String pTitle, String pPrice,  String pDescrtiption){

        TextView codeText=findViewById(R.id.codeTvId);
        codeText.setText(pCode);

        TextView titleText=findViewById(R.id.titleTvId);
        titleText.setText(pTitle);

        TextView priceText=findViewById(R.id.priceTvId);
        priceText.setText(pPrice);

        TextView desText=findViewById(R.id.descriptTvId);
        desText.setText(pDescrtiption);

        /*ImageView imageView=findViewById(R.id.imageD);
        Picasso.with(this)
                .load(pImage)
                .placeholder(R.drawable.italyflag)
                .fit()
                .into(imageView);*/
    }
}
