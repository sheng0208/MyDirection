package com.billylu.mydirection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.billylu.mydirection.model.BaseActivity;
import com.billylu.mydirection.bean.DirectionBean;
import com.billylu.mydirection.model.MyDialog;
import com.billylu.mydirection.model.Utils;


public class DirectionActivity extends BaseActivity {
    private final String TAG = DirectionActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecycleAdapter adapter;
    private String imei;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DirectionActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar(R.id.toolbar, true, R.menu.toolbar_menu);

        DirectionBean bean = (DirectionBean) getIntent().getExtras().getSerializable("bean");

        imei = new Utils(this).getIMEI();
        recyclerView = (RecyclerView) findViewById(R.id.main_recycle_listview);


        setView(bean);

    }

    @Override
    protected void setMenuItemAction(int itemID) {
        super.setMenuItemAction(itemID);
        switch (itemID){
            case R.id.menu_add:
                Toast.makeText(DirectionActivity.this, "新增", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_setting:
                Toast.makeText(DirectionActivity.this, "設定", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_exit:
                Toast.makeText(DirectionActivity.this, "離開", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void btn_add_direction(View view){
        MyDialog dialog = new MyDialog(this);
        dialog.showAddDirectionDialog(imei);
    }

    private void setView(DirectionBean bean) {
        setRecyclerView(bean);
    }

    private void setRecyclerView(DirectionBean bean) {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecycleAdapter(bean, R.layout.direction_item);
        recyclerView.setAdapter(adapter);
    }

    class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
        private int layout;
        private DirectionBean bean;

        public RecycleAdapter(DirectionBean bean, int layout) {
            this.layout = layout;
            this.bean = bean;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            public Button send;
            public ConstraintLayout viewitem;
            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text_direction);
                send = (Button) itemView.findViewById(R.id.btn_send);
                viewitem = (ConstraintLayout) itemView.findViewById(R.id.viewitem);
            }
        }

        @Override
        public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            RecycleAdapter.ViewHolder holder = new RecycleAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecycleAdapter.ViewHolder holder, final int position) {
            holder.textView.setText(bean.getDirection());
            holder.send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dir = bean.getDirection();
                    Log.i("DIR", dir);
                    startSearchDirection(dir);
                }
            });

            holder.viewitem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String key = bean.getId();
                    new MyDialog(DirectionActivity.this).deleteDialog(imei, key);
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public void startSearchDirection(String dir) {
            Uri intentUri = Uri.parse("geo:0,0?q="+dir);
            Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        }
    }


}
