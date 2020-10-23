package showman0.amoor.showman.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amoor.showman.R;
import showman0.amoor.showman.data.model.Sales;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Hold> {

    private Context context;
    private List<Sales> salesList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String user_name;
    private DatabaseReference firebaseDatabase;

    public CartAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        someInfo();
        return new Hold(LayoutInflater.from(context).inflate(R.layout.item_cart, viewGroup, false));
    }



    @Override
    public void onBindViewHolder(@NonNull Hold hold, int i)

    {
        setItemsData(hold, i);
        setAction(hold, i);
    }

    private void setItemsData(Hold hold, int i) {
        hold.ItemDelegateSalesTvProductName.setText("اسم الصنف : "+salesList.get(i).getProduct_name());
        if (salesList.get(i).getUser_place().equals("ادمن"))
        {
            hold.ItemDelegateSalesTvDelegateName.setText("اسم الأدمن : "+salesList.get(i).getSalesManName());

        }else
        {
            hold.ItemDelegateSalesTvDelegateName.setText("اسم المندوب : "+salesList.get(i).getSalesManName());
        }
        hold.ItemDelegateSalesTvDelegateSaleTime.setText("وقت الحجز : "+salesList.get(i).getTime());
        hold.ItemDelegateSalesTvProductCode.setText("كود الصنف : "+salesList.get(i).getProduct_code());
        hold.ItemDelegateSalesTvProductQuantity.setText("الكمية المحجوزه : "+salesList.get(i).getQuantity());
        hold.ItemDelegateSalesTvOutNumber.setText("رقم اذن الصرف : "+salesList.get(i).getOut_number());
        hold.ItemDelegateSalesTvCustomerName.setText("اسم العميل : "+salesList.get(i).getCustomer_name());
    }

    private void setAction(final Hold hold, final int i)
    {
        hold.ItemDelegateSalesIvDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deleteProductQuantity(i);
            }
        });
    }



    private void deleteProductQuantity(final int i)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("انتبه")
                .setMessage("انت متأكد انك عايز تلغى حجز الكمية دى ؟")
                .setIcon(R.drawable.ic_delete_forever)
                .setPositiveButton("الغى", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which)
                            {
                                progressDialog.setMessage("انتظر قليلا يتم حذف الكمية المحجوزه ... ");
                                progressDialog.show();
                                progressDialog.setCanceledOnTouchOutside(false);
                                // لما يلغى الحجز
                                cancelOrder(i);
                                dialog.dismiss();
                                progressDialog.dismiss();

                            }
                        }
                ).setNegativeButton("تراجع", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show().setCanceledOnTouchOutside(false);
    }

    private void cancelOrder(int i)
    {
        returnTheQuantity(i);
    }

    private void returnTheQuantity(final int i)
    {
        final long sale_quantity = salesList.get(i).getQuantity();
        String companyName = salesList.get(i).getCompanyName();
        String productType = salesList.get(i).getProductType();
        String productCode = salesList.get(i).getProduct_code();
        final DatabaseReference quantityRef = firebaseDatabase.child("products").child(productType).child(companyName).child(productCode).child("productQuantity");
        quantityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Long total_quantity = dataSnapshot.getValue(Long.class);
                if (total_quantity!=null)
                {
                    Long new_total_quantity = total_quantity +sale_quantity;
                    quantityRef.setValue(new_total_quantity);
                    removeFromCart(i);
                }
                else
                {
                    Toast.makeText(context, "total_quantity null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void removeFromCart(int i)
    {
        String sale_date = salesList.get(i).getDate();
        String sale_id = salesList.get(i).getSaleId();
        firebaseDatabase.child("Sales").child(sale_date).child(sale_id).removeValue();
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    public void setData(List<Sales> salesList) {
        this.salesList = salesList;
    }


    class Hold extends RecyclerView.ViewHolder {
        @BindView(R.id.Item_Delegate_Sales_Tv_delegate_name)
        TextView ItemDelegateSalesTvDelegateName;
        @BindView(R.id.Item_Delegate_Sales_Tv_product_name)
        TextView ItemDelegateSalesTvProductName;
        @BindView(R.id.Item_Delegate_Sales_Tv_product_code)
        TextView ItemDelegateSalesTvProductCode;
        @BindView(R.id.Item_Delegate_Sales_Tv_product_quantity)
        TextView ItemDelegateSalesTvProductQuantity;
        @BindView(R.id.Item_Delegate_Sales_Tv_delegate_sale_time)
        TextView ItemDelegateSalesTvDelegateSaleTime;
        @BindView(R.id.Item_Delegate_Sales_Iv_delete)
        ImageView ItemDelegateSalesIvDelete;
        @BindView(R.id.Item_Delegate_Sales_Tv_out_number)
        TextView ItemDelegateSalesTvOutNumber;
        @BindView(R.id.Item_Delegate_Sales_Tv_customer_name)
        TextView ItemDelegateSalesTvCustomerName;
        View view;

        Hold(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }

    private void someInfo()
    {
        progressDialog = new ProgressDialog(context);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        SharedPreferences preferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        user_name = preferences.getString("user_name", "");
    }
}
