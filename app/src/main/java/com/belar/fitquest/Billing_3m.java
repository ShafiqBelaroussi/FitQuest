package com.belar.fitquest;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.billingclient.api.SkuDetails;

import static android.content.ContentValues.TAG;

public class Billing_3m extends Fragment
{

    Button btn_Subscribe;
    LinearLayout cv_tNn, cv_t;
    TextView tv_subsName1, tv_subsName2, tv_pricePerWeek1, tv_pricePerWeek2, tv_pricePerSub1, tv_pricePerSub2;
    SkuDetails sku_TnN, sku_T, sku_Selected;
    double price_TnN, price_T;


    public Billing_3m(SkuDetails _TnN, SkuDetails _T)
    {
        this.sku_TnN = _TnN;
        this.sku_T = _T;
        this.price_TnN = this.CalculatePrice(sku_TnN);
        this.price_T = this.CalculatePrice(sku_T);
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_billing, container, false);
        btn_Subscribe = v.findViewById(R.id.btn_Subscribe);
        cv_tNn = v.findViewById(R.id.cv_tNn);
        cv_t = v.findViewById(R.id.cv_t);
        tv_subsName1 = v.findViewById(R.id.tv_subsName1);
        tv_subsName2 = v.findViewById(R.id.tv_subsName2);
        tv_pricePerSub1 = v.findViewById(R.id.tv_pricePerSub1);
        tv_pricePerSub2 = v.findViewById(R.id.tv_pricePerSub2);
        tv_pricePerWeek1 = v.findViewById(R.id.tv_pricePerWeek1);
        tv_pricePerWeek2 = v.findViewById(R.id.tv_pricePerWeek2);

        tv_pricePerSub1.setText("$"+price_TnN+"/ Month");
        tv_pricePerWeek1.setText("$"+Math.round((price_TnN/3)*100.0)/100.0+"/ Month");
        tv_pricePerSub2.setText("$"+price_T+"/ Month");
        tv_pricePerWeek2.setText("$"+Math.round((price_T/3)*100.0)/100.0+"/ Month");



        cv_tNn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                manageCardView(v);
                sku_Selected = sku_TnN;
            }
        });

        cv_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                manageCardView(v);
                sku_Selected = sku_T;
            }
        });


        btn_subscribeClicked();



        return v;
    }

    private void manageCardView(View card)
    {
        if (cv_tNn.equals(card))
        {
            cv_tNn.setBackgroundColor(getResources().getColor(R.color.White)); cv_t.setBackgroundColor(getResources().getColor(R.color.ItemGray));
            tv_subsName1.setTextColor(getResources().getColor(R.color.SecondaryBlue)); tv_subsName2.setTextColor(getResources().getColor(R.color.White));
            tv_pricePerWeek1.setTextColor(getResources().getColor(R.color.SecondaryBlue)); tv_pricePerSub1.setTextColor(getResources().getColor(R.color.SecondaryBlue));
            tv_pricePerWeek2.setTextColor(getResources().getColor(R.color.White)); tv_pricePerSub2.setTextColor(getResources().getColor(R.color.White));

            tv_pricePerSub1.setText("$"+price_TnN+"/ Month");
            tv_pricePerWeek1.setText("$"+Math.round((price_TnN/3)*100.0)/100.0+"/ Month");
        }
        else
        {
            cv_t.setBackgroundColor(getResources().getColor(R.color.White)); cv_tNn.setBackgroundColor(getResources().getColor(R.color.ItemGray));
            tv_subsName2.setTextColor(getResources().getColor(R.color.SecondaryBlue)); tv_subsName1.setTextColor(getResources().getColor(R.color.White));
            tv_pricePerWeek2.setTextColor(getResources().getColor(R.color.SecondaryBlue)); tv_pricePerSub2.setTextColor(getResources().getColor(R.color.SecondaryBlue));
            tv_pricePerWeek1.setTextColor(getResources().getColor(R.color.White)); tv_pricePerSub1.setTextColor(getResources().getColor(R.color.White));

            tv_pricePerSub2.setText("$"+price_T+"/ Month");
            tv_pricePerWeek2.setText("$"+Math.round((price_T/3)*100.0)/100.0+"/ Month");
        }
    }

    private double CalculatePrice(SkuDetails sku)
    {
        try
        {
            double price = (sku.getOriginalPriceAmountMicros()/1000000f) /3.64f;
            return Math.round(price*100.0)/100.0;
        }
        catch (Exception e)
        {
            return 0.0;
        }

    }


    private void btn_subscribeClicked()
    {
        btn_Subscribe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                int resposeCode = ((MainApp)getActivity()).startBillingFlow(sku_Selected);
                Log.d(TAG, "onClick: "+ resposeCode);
            }
        });
    }


}
