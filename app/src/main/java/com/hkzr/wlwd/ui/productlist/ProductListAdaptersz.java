package com.hkzr.wlwd.ui.productlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hkzr.wlwd.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ProductListAdaptersz extends RecyclerView.Adapter<ProductListHolder> {
    private List<Product> list;
    private Context context;

    public ProductListAdaptersz(Context context, List<Product> list) {
        this.list = list;
        this.context = context;
    }

    public List<Product> getList() {
        return list;
    }

    @Override
    public ProductListHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_recycle_check, parent, false);
        ProductListHolder myViewHolder = new ProductListHolder(v);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(ProductListHolder recycleHolder, int position) {
        if (list != null && list.size() > 0) {
            Product itemBean = list.get(position);
            recycleHolder.tv_checkProgram.setText(itemBean.FramePart);
            recycleHolder.tv_checkItemName.setText(itemBean.Assembly);

            recycleHolder.tc_checkMethod.setText("1".equals(itemBean.producttype) ? "半成品" : "成品");

        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void addList(Product ll) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (ll != null) {
            list.add(ll);
        }
        remove(list);
        notifyDataSetChanged();
    }

    //去Bean类重
    public void remove(List<Product> list) {
        Log.e("before del depulicate", list.size() + "");
        Set<Product> set = new HashSet<>();
        List<Product> listaaa = new ArrayList<>();
        for (Iterator<Product> ite = list.iterator(); ite.hasNext(); ) {
            Product aaa = ite.next();
            if (set.add(aaa)) {
                listaaa.add(aaa);
            }
        }
        list.clear();
        list.addAll(listaaa);
    }
}
