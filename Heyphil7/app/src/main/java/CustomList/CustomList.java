package CustomList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.heyphil.Data;
import com.app.heyphil.ProductBuy;
import com.app.heyphil.ProductList;
import com.app.heyphil.R;

public class CustomList extends ArrayAdapter<String>{
 
private final Activity context;
private final String[] proContent;
    private final String[] proID;
private final String[] product;
    private ProductList productList;
public CustomList(Activity context,
                  String[] proContent,String[] proID, String[] product) {
super(context, R.layout.list_single, proContent);
this.context = context;
this.proID = proID;
    this.proContent=proContent;
this.product = product;
 
}
   @Override
public View getView(int position, View view, ViewGroup parent) {
LayoutInflater inflater = context.getLayoutInflater();
View rowView= inflater.inflate(R.layout.list_single, null, true);
    Typeface tf=Typeface.createFromAsset(context.getAssets(),"fonts/Boogaloo-Regular.ttf");
final TextView imageView = (TextView) rowView.findViewById(R.id.img);
    final TextView prices=(TextView)rowView.findViewById(R.id.prices);
    final TextView products=(TextView)rowView.findViewById(R.id.products);
Button btn_details=(Button)rowView.findViewById(R.id.btn_details);
final Button btn_buy=(Button)rowView.findViewById(R.id.btn_buy);
    btn_details.setTypeface(tf);
    btn_buy.setTypeface(tf);
       imageView.setText(product[position]);
       products.setText(proContent[position]);
       prices.setText(proID[position]);

    btn_buy.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String value = (String) Data.PriceList.get(products.getText().toString());
            Intent i=new Intent(context,ProductBuy.class);
            i.putExtra("Product",imageView.getText().toString());
            i.putExtra("Price",value);
            context.startActivity(i);
        }
    });
    btn_details.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/Boogaloo-Regular.ttf");
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.product_detail);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final boolean[] status = new boolean[1];
            final TextView tv_product=(TextView)dialog.findViewById(R.id.tv_product);
            final TextView tv_price=(TextView)dialog.findViewById(R.id.tv_price);
            TextView tv_description=(TextView)dialog.findViewById(R.id.tv_description);
            final TextView tv_productdes=(TextView)dialog.findViewById(R.id.tv_prodescription);
            Button close=(Button)dialog.findViewById(R.id.button);
            Button btn_buy=(Button)dialog.findViewById(R.id.button2);
            tv_product.setTypeface(tf);tv_price.setTypeface(tf);
            tv_description.setTypeface(tf);tv_productdes.setTypeface(tf);
            btn_buy.setTypeface(tf);close.setTypeface(tf);
            tv_product.setText(imageView.getText().toString());
            final String value = (String) Data.PriceList.get(products.getText().toString());
            //System.out.println("========value"+Data.PriceList);
            tv_price.setText("Php "+value+".00");
            System.out.println("========value"+tv_price.getText().toString());

            Spanned htmlAsSpanned = Html.fromHtml(prices.getText().toString());
            tv_productdes.setText(htmlAsSpanned);
            tv_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_productdes.setVisibility(View.VISIBLE);
                }
            });
            tv_productdes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_productdes.setVisibility(View.GONE);
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String value = (String) Data.PriceList.get(products.getText().toString());
                    Intent i=new Intent(context,ProductBuy.class);
                    i.putExtra("Product",tv_product.getText().toString());
                    i.putExtra("Price",value);
                    context.startActivity(i);
                }
            });
            dialog.show();

        }
    });



return rowView;
}
}
