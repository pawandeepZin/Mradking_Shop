package com.mradking.mradkingshop.kirana_store.custmer.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mradking.mradkingshop.R;
import com.mradking.mradkingshop.kirana_store.custmer.Model.CommonModal;
import com.mradking.mradkingshop.kirana_store.custmer.Model.cart_list_modal;
import com.mradking.mradkingshop.kirana_store.custmer.intface.RecyclerView_home_list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart_page_adapter extends RecyclerView.Adapter<Cart_page_adapter.ViewHolder> {

    public Context context;

    private FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    private List<CommonModal> pd;
    private List<cart_list_modal> productListFull;
    private RecyclerView_home_list recyclerView_home_list;

    //getting the context and product list with constructor
    public Cart_page_adapter(Context mCtx, List<CommonModal> pd, RecyclerView_home_list recyclerView_home_list) {
        this.context = mCtx;
        this.pd = pd;

        this.recyclerView_home_list = recyclerView_home_list;


    }

    @NonNull
    @Override
    public Cart_page_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kirana_add_cart_row, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(context);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Cart_page_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.glide.with(context).load(pd.get(position).getProduct_image_url()).into(holder.product_image);
        holder.product_name.setText(pd.get(position).getName_product());
        holder.product_price.setText("Rs " + pd.get(position).getSalling_price());
        holder.quantity_txt.setText(pd.get(position).getQuantity());

        recyclerView_home_list.total_saving(holder.total_saving());


        recyclerView_home_list.visblity_cart_bar(pd.size());


        holder.count = Integer.parseInt(pd.get(position).getQuantity());





        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                holder.progressDialog.setMessage("Please Wait......");
                holder.progressDialog.setCancelable(false);
                holder.progressDialog.setCanceledOnTouchOutside(false);
                holder.progressDialog.show();


                recyclerView_home_list.visblity_cart_bar(0);
                holder.quantity_txt.setText(String.valueOf( ++holder.count));

                Map<String, Object> map = new HashMap<>();


                map.put("quantity",String.valueOf( holder.count));
                map.put("total_amount",String.valueOf(Integer.parseInt(pd.get(position).getSalling_price())* holder.count));


                firebaseFirestore.collection("kirna_Add_cart").document(holder.firebaseAuth.getUid().toString()).collection("cart").document(pd.get(position).getItemId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        recyclerView_home_list.total_amount(0);

                        holder.progressDialog.dismiss();



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {

                        Toast.makeText(context, "Product not Added to Cart", Toast.LENGTH_SHORT).show();

                        holder.progressDialog.dismiss();

                    }
                });




            }
        });


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                holder.progressDialog.setMessage("Please Wait......");
                holder.progressDialog.setCancelable(false);
                holder.progressDialog.setCanceledOnTouchOutside(false);
                holder.progressDialog.show();

                recyclerView_home_list.visblity_cart_bar(1);


                if(holder.count<=0){

                    holder.quantity_txt.setText(String.valueOf( holder.count));

                    holder.count=1;



                }if(holder.count==1){

                    firebaseFirestore.collection("kirna_Add_cart").document(holder.firebaseAuth.getUid().toString()).collection("cart")
                            .document(pd.get(position).getItemId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                    recyclerView_home_list.total_amount(0);
                                    pd.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, pd.size());
                                    holder.progressDialog.dismiss();

                                    recyclerView_home_list.restart_act();

                                    holder.progressDialog.dismiss();



                                }
                            });



                }else {

                    Map<String, Object> map = new HashMap<>();

                    holder.quantity_txt.setText(String.valueOf( --holder.count));


                    map.put("quantity",String.valueOf( holder.count));
                    map.put("total_amount",String.valueOf(Integer.parseInt(pd.get(position).getSalling_price())* holder.count));


                    firebaseFirestore.collection("kirna_Add_cart").document(holder.firebaseAuth.getUid().toString()).collection("cart")
                            .document(pd.get(position).getItemId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    recyclerView_home_list.total_amount(0);
                                    holder.progressDialog.dismiss();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {

                                    Toast.makeText(context, "Product not Added to Cart", Toast.LENGTH_SHORT).show();
                                    holder.progressDialog.dismiss();


                                }
                            });
                }


            }
        });




    }

    @Override
    public int getItemCount() {
        return pd.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Glide glide;
        ImageView product_image;
        TextView product_name, product_price, add, minus, quantity_txt;
        View mView;
        int count = 0;
        FirebaseFirestore firebaseFirestore;
        FirebaseAuth firebaseAuth;

        ProgressDialog progressDialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            product_image = mView.findViewById(R.id.product_image);
            product_name = mView.findViewById(R.id.product_name);
            product_price = mView.findViewById(R.id.price);
            add = mView.findViewById(R.id.add);
            quantity_txt = mView.findViewById(R.id.quantity_txt);
            minus = mView.findViewById(R.id.minus);
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();
            progressDialog = new ProgressDialog(context);

        }




        public int total_saving() {

            int total_saving=0;
            for (int i = 0; i < pd.size(); i++) {

                int saing_calulation =Integer.parseInt(pd.get(i).getMarket_price())*Integer.parseInt(pd.get(i).getQuantity())-Integer.parseInt(pd.get(i).getSalling_price())*Integer.parseInt(pd.get(i).getQuantity());
                total_saving+=saing_calulation ;

            }

            return total_saving;

        }


    }


}
