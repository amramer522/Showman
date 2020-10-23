package showman0.amoor.showman.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.amoor.showman.R;
import showman0.amoor.showman.data.model.Product;
import showman0.amoor.showman.data.model.Sales;
import showman0.amoor.showman.ui.activity.ProductDetailsActivity;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import showman0.amoor.showman.helper.HelperMethod;

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.Hold> {

    private Context context;
    private List<Product> productArrayList = new ArrayList<>();
    private DatabaseReference reference;
    private Dialog dialog;
    private SharedPreferences preferences;
    private DatabaseReference firebaseDatabase;
    private String user_name;
    private String user_place;
    private String user_privilege;

    public SwipeRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(ViewGroup parent, int viewType) {
        someInfo();
        return new Hold(LayoutInflater.from(context).inflate(R.layout.swipe_layout, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final Hold hold, @SuppressLint("RecyclerView") final int position) {
        getSharedPrefrencesValues();
        setTextValues(hold, position);
        setUpSwipe(hold);
        onClickView(hold);

        if (user_privilege != null && user_privilege.equals("مندوب مبيعات")) {
            hold.AddQuantity.setVisibility(View.GONE);
            hold.Delete.setVisibility(View.GONE);
        } else {
            addQuantityAction(hold, position);
            deleteProduct(hold, position);
        }
        editProductQuantity(hold, position);

        mItemManger.bindView(hold.itemView, position);
    }


    private void onClickView(final Hold hold) {
        hold.swipe.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetailsActivity.class);
                i.putExtra("product_code", hold.ItemProductTvProductId.getText());
                context.startActivity(i);
                ((Activity) context).finish();
            }
        });
    }

    private void addQuantityAction(Hold hold, final int position) {
        hold.AddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(R.layout.dialog_add_quantity);
                Button addQuantityBtn = dialog.findViewById(R.id.Dialog_Add_Quantity_Btn_done);
                final TextInputLayout product_quantity = dialog.findViewById(R.id.Dialog_Add_Quantity_Til_Product_Quantity);
                final TextView available_quantity = dialog.findViewById(R.id.Dialog_Add_Quantity_Tv_Available_Quantity);

                // set available quantity
                final long available_pro_quantity = productArrayList.get(position).getProductQuantity();
                available_quantity.setText("المتاح : " + available_pro_quantity + " متر ");


                addQuantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String til_quantity = HelperMethod.getTextFromTil(product_quantity);
                        if (!TextUtils.isEmpty(til_quantity)) {
                            Long q = Long.valueOf(til_quantity);
                            Long new_product_quantity = productArrayList.get(position).getProductQuantity() + q;
                            String productCode = productArrayList.get(position).getProductCode();
                            String productType = productArrayList.get(position).getProductType();
                            String companyName = productArrayList.get(position).getCompanyName();
                            firebaseDatabase.child("products").child(productType).child(companyName).child(productCode).child("productQuantity").setValue(new_product_quantity).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    Toast.makeText(context, "تم اضافة كمية المنتج", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(context, "يجب ادخال الكمية", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    private void editProductQuantity(Hold hold, final int position) {
        hold.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long pro_quantity = productArrayList.get(position).getProductQuantity();
                if (pro_quantity > 0) {
                    showDialog(R.layout.dialog_sale_quantity);

                    Button decreaseBtn = dialog.findViewById(R.id.Dialog_Sale_Quantity_Btn_Decrease);
                    final TextInputLayout product_quantity = dialog.findViewById(R.id.Dialog_Sale_Quantity_Til_product_quantity);
                    final TextInputLayout out_number = dialog.findViewById(R.id.Dialog_Sale_Quantity_Til_Out_Number);
                    final TextInputLayout customer_name = dialog.findViewById(R.id.Dialog_Sale_Quantity_Til_Customer_Name);
                    final TextView available_quantity = dialog.findViewById(R.id.Dialog_Sale_Quantity_Tv_Available_Quantity);
                    available_quantity.setText("المتاح : " + pro_quantity + " متر ");

                    decreaseBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String quantityFromTil = HelperMethod.getTextFromTil(product_quantity);
                            Long long_quantity_til = Long.valueOf(quantityFromTil);
                            String out_numberTil = HelperMethod.getTextFromTil(out_number);
                            String customer_name_Til = HelperMethod.getTextFromTil(customer_name);
                            if (!TextUtils.isEmpty(quantityFromTil) && !TextUtils.isEmpty(out_numberTil) && !TextUtils.isEmpty(customer_name_Til)) {
                                if (long_quantity_til <= pro_quantity) {
                                    // update the quantity
                                    updateQuantityAfterSale(position, long_quantity_til, pro_quantity);
                                    //add to cart
                                    addSaleToCart(position, long_quantity_til, out_numberTil, customer_name_Til);
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(context, "مفيش الكمية دى", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "يجب ادخال كل الخانات", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(context, "مفيش كمية من المنتج ده", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updateQuantityAfterSale(int position, Long quantityFromTil, long pro_quantity) {
        long newQuantity = pro_quantity - quantityFromTil;
        String productCode = productArrayList.get(position).getProductCode();
        String productType = productArrayList.get(position).getProductType();
        String companyName = productArrayList.get(position).getCompanyName();

        firebaseDatabase.child("products").child(productType).child(companyName)
                .child(productCode).child("productQuantity").setValue(newQuantity);
    }

    private void addSaleToCart(int position, Long quantityFromTil, String out_numberTil, String customer_name_til)
    {
        String saleId = firebaseDatabase.child("Sales").child(HelperMethod.getCurrentDate()).push().getKey();
        String productName = productArrayList.get(position).getProductName();
        String productCode = productArrayList.get(position).getProductCode();
        String productType = productArrayList.get(position).getProductType();
        String companyName = productArrayList.get(position).getCompanyName();

        Sales sales = new Sales(
                saleId,
                user_name,
                HelperMethod.getCurrentTime(),
                productCode,
                productName,
                HelperMethod.getCurrentDate(),
                quantityFromTil,
                user_place,
                customer_name_til,
                out_numberTil,
                productType,
                companyName
        );
        if (saleId != null) {
            firebaseDatabase.child("Sales").child(HelperMethod.getCurrentDate()).child(saleId).setValue(sales);
            Toast.makeText(context, "تم اضافة الكمية المباعه الى المبيعات", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProduct(Hold hold, final int position) {
        hold.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("انتبه")
                        .setMessage("انت متأكد انك عايز تمسح المنتج ؟")
                        .setIcon(R.drawable.ic_delete_forever)
                        .setPositiveButton("امسح", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String productCode = productArrayList.get(position).getProductCode();
                                String productType = productArrayList.get(position).getProductType();
                                String companyName = productArrayList.get(position).getCompanyName();
                                reference.child("products").child(productType).child(companyName).child(productCode).removeValue();
                            }
                        }).setNegativeButton("تراجع", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show().setCanceledOnTouchOutside(false);
            }
        });
    }


    private void showDialog(int dialog_layout) {
        dialog = new Dialog(context);
        dialog.setContentView(dialog_layout);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void getSharedPrefrencesValues() {
        user_privilege = preferences.getString("user_privilege", "");
        user_name = preferences.getString("user_name", "");

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private void setUpSwipe(Hold hold) {
        hold.swipe.setShowMode(SwipeLayout.ShowMode.PullOut);
        hold.swipe.addDrag(SwipeLayout.DragEdge.Right, hold.swipe.findViewById(R.id.bottom_wraper));
        hold.swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
    }

    private void setTextValues(Hold hold, int position) {
        hold.ItemProductTvProductId.setText(productArrayList.get(position).getProductCode() + "");
        hold.ItemProductTvProductName.setText(productArrayList.get(position).getProductName() + " فرز " + productArrayList.get(position).getProductQuality());
        hold.ItemProductTvProductQuantity.setText(String.valueOf(productArrayList.get(position).getProductQuantity()));
    }

    public void setData(List<Product> productArrayList) {
        this.productArrayList = productArrayList;
    }

    static class Hold extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.Edit)
        TextView Edit;
        @BindView(R.id.AddQuantity)
        TextView AddQuantity;
        @BindView(R.id.Delete)
        TextView Delete;
        @BindView(R.id.Item_Product_Tv_Product_id)
        TextView ItemProductTvProductId;
        @BindView(R.id.Item_Product_Tv_Product_Name)
        TextView ItemProductTvProductName;
        @BindView(R.id.Item_Product_Tv_Product_Quantity)
        TextView ItemProductTvProductQuantity;
        @BindView(R.id.swipe)
        SwipeLayout swipe;

        Hold(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }

    private void someInfo() {
        reference = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        preferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String user_privilege = preferences.getString("user_privilege", "");

        if (user_privilege.equals("مندوب مبيعات")) {
            user_place = preferences.getString("user_place", "");
        } else {
            user_place = "ادمن";
        }
    }
}
