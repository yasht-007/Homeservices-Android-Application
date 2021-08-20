package com.yash.homeservice.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yash.homeservice.R;
import com.yash.homeservice.Usersidebooking.Finalorderuseractivity;
import com.yash.homeservice.activities.DashboardActivity;
import com.yash.homeservice.activities.Mapsactivity;
import com.yash.homeservice.fragments.InprogressFragment;
import com.yash.homeservice.modal.Usercart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsercartAdapter extends RecyclerView.Adapter<UsercartAdapter.Userviewholder>{

    private Context context;
    public String t;
    public String reschedulestatus="no";
    private LayoutInflater inflater;
    public ArrayList<Usercart> cartlist;
    private RadioGroup timing;
    public DatePickerDialog.OnDateSetListener listener;
    private RadioButton time;

    public UsercartAdapter(Context context,ArrayList<Usercart> cartlist)
    {
        this.context=context;
        this.cartlist=cartlist;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public Userviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.userordercart,parent,false);
        return new Userviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Userviewholder holder, int position) {

        final Usercart usercart=cartlist.get(position);
        holder.name.setText(usercart.getName());
        holder.name.setVisibility(View.VISIBLE);
        holder.service.setText(usercart.getService());
        holder.service.setVisibility(View.VISIBLE);
        holder.number.setText(usercart.getNumber());
        holder.orderstatus.setText(usercart.getOrderstatus());
        holder.orderstatus.setVisibility(View.VISIBLE);
        holder.id.setText(usercart.getId());
        holder.timing.setText(usercart.getTiming());
        holder.date.setText(usercart.getDate());
        holder.usercode.setText(usercart.getUniquecode());
        holder.servicecharge.setText(usercart.getServicecharge());


        Glide.with(context)
                .load(usercart.getImage())
                .placeholder(R.drawable.userprofile)
                .error(R.drawable.userprofile)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        boolean isExpanded = cartlist.get(position).isExpanded();

        holder.expandablelayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.reschedulebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("orders").document(holder.id.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        reschedulestatus=documentSnapshot.get("reschedulestatus").toString();
                    }
                });

                if (reschedulestatus.equals("no")) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.reschedule);
                    final TextView tvdate;
                    tvdate = dialog.findViewById(R.id.tvdate);
                    Button cancel, reschedule, btndate;
                    ImageView back;

                    cancel = dialog.findViewById(R.id.btncan);
                    back = dialog.findViewById(R.id.bkarrow);
                    reschedule = dialog.findViewById(R.id.btnbk);
                    btndate = dialog.findViewById(R.id.btnsel);
                    timing = dialog.findViewById(R.id.rgtime);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btndate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar calendar = Calendar.getInstance();
                            listener = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                    String date = dayOfMonth + "-" + month + "-" + year;
                                    tvdate.setText(date);
                                }
                            };

                            DatePickerDialog datePickerDialog = new DatePickerDialog(context, listener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));


                            calendar.add(Calendar.DATE, 0);
                            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                            calendar.add(Calendar.DATE, 8);
                            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                            datePickerDialog.show();
                        }
                    });
                    reschedule.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (timing.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(context, "Please select timing for service delivery", Toast.LENGTH_SHORT).show();
                            } else {
                                int timeid = timing.getCheckedRadioButtonId();
                                time = dialog.findViewById(timeid);
                                t = time.getText().toString();
                            }

                            if (tvdate.getText().equals(null) || tvdate.getText().equals("Date")) {
                                Toast.makeText(context, "Please select delivery date", Toast.LENGTH_SHORT).show();
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Service can be rescheduled only once are you sure to reschedule this service?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialo, int which) {
                                                Map<String, Object> map = new HashMap<>();
                                                map.put("ordertime", t);
                                                map.put("orderdate", tvdate.getText().toString());
                                                map.put("reschedulestatus","yes");
                                                holder.timing.setText(t);
                                                holder.date.setText(tvdate.getText().toString());
                                                FirebaseFirestore.getInstance().collection("orders").document(holder.id.getText().toString()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialog.dismiss();
                                                        holder.reschedulebtn.setClickable(false);
                                                        Toast.makeText(context, "Success! now refresh this page to see updated status", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialo, int which) {
                                        dialo.dismiss();
                                    }
                                }).setCancelable(false).show();

                            }
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                }else {
                    holder.reschedulebtn.setClickable(true);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("You can't reschedule the service as Service can be Reschedule only once")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public class Userviewholder extends RecyclerView.ViewHolder{
        private CircleImageView imageView;
        public Button cancelbtn,reschedulebtn;
        public LinearLayout expandablelayout;
        public RelativeLayout relativeLayout;
        public TextView name,service,number,orderstatus,id,timing,date,usercode,servicecharge;
        public Userviewholder(@NonNull View itemView) {
            super(itemView);

            expandablelayout=(LinearLayout) itemView.findViewById(R.id.expandableview);
            imageView=(CircleImageView) itemView.findViewById(R.id.uoimage);
            name=itemView.findViewById(R.id.uoname);
            service=itemView.findViewById(R.id.uoservice);
            orderstatus=itemView.findViewById(R.id.orderstatus);
            id=itemView.findViewById(R.id.uobkid);
            timing=itemView.findViewById(R.id.uotiming);
            date=itemView.findViewById(R.id.uodate);
            usercode=itemView.findViewById(R.id.uocode);
            servicecharge=itemView.findViewById(R.id.uocharge);
            cancelbtn=itemView.findViewById(R.id.btnordercancel);
            relativeLayout=itemView.findViewById(R.id.rlcart);
            reschedulebtn=itemView.findViewById(R.id.btnreschedule);
            number=itemView.findViewById(R.id.uonumber);



            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Usercart usercart= cartlist.get(getAdapterPosition());
                    usercart.setExpanded(!usercart.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }
}
