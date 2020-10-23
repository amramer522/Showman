package showman0.amoor.showman.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amoor.showman.R;
import showman0.amoor.showman.data.model.Company;
import showman0.amoor.showman.ui.activity.TypesActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.Hold> {

    private Context context;
    private ArrayList<Company> companyList = new ArrayList<>();
    private SharedPreferences preferences;

    public CompanyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        preferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return new Hold(LayoutInflater.from(context).inflate(R.layout.item_company, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Hold hold, int i) {
        hold.ItemCompanyTvCompanyName.setText(companyList.get(i).getCompany_name());
        setAction(hold, i);
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public void setData(ArrayList<Company> companyList) {
        this.companyList = companyList;
    }


    class Hold extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.Item_Company_Tv_Company_Name)
        TextView ItemCompanyTvCompanyName;

        Hold(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }

    private void setAction(final Hold hold, final int i)
    {
        hold.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, TypesActivity.class);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("company",hold.ItemCompanyTvCompanyName.getText().toString());
                editor.apply();
                context.startActivity(i);
                ((Activity) context).finish();

            }
        });

    }


}

