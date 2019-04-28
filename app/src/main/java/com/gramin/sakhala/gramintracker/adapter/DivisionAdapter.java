package com.gramin.sakhala.gramintracker.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gramin.sakhala.gramintracker.R;
import com.gramin.sakhala.gramintracker.callback.DivisionCallback;
import com.gramin.sakhala.gramintracker.dto.Division;
import com.gramin.sakhala.gramintracker.dto.DivisionData;
import com.gramin.sakhala.gramintracker.dto.DivisionHeader;

import java.util.List;

public class DivisionAdapter extends RecyclerView.Adapter {

    private static final int HEADER_VIEW = 0;
    private static final int DETAIL_VIEW = 1;

    private Context context;

    List<DivisionData> data;

    DivisionCallback divisionCallback;

    public DivisionAdapter(Context context, List<DivisionData> data, DivisionCallback divisionCallback) {
        this.context = context;
        this.data = data;
        this.divisionCallback = divisionCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER_VIEW:
                return new HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.division_header,
                        parent, false));
            default:
                return new DivisionHolder(LayoutInflater.from(context).inflate(R.layout.division_item,
                        parent, false));

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) ==  HEADER_VIEW){
            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
            if(data.get(position) instanceof DivisionHeader){
                viewHolder.onBind((DivisionHeader) data.get(position));
            }
        }else if(getItemViewType(position) == DETAIL_VIEW){
            DivisionHolder viewHolder = (DivisionHolder) holder;
            if(data.get(position) instanceof Division){
                viewHolder.onBind((Division) data.get(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0 ;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof DivisionHeader) {
            return HEADER_VIEW;
        } else {
            return DETAIL_VIEW;
        }
    }

    public class DivisionHolder extends RecyclerView.ViewHolder {
        TextView divisionName;
        ConstraintLayout divisionContainer;

        public DivisionHolder(View itemView) {
            super(itemView);
            divisionName = itemView.findViewById(R.id.division_name);
            divisionContainer = itemView.findViewById(R.id.main_layout);
        }

        public void onBind(Division data) {
            divisionName.setText(data.getName());
            divisionContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    divisionCallback.onItemClick(data);
                }
            });
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView divisionHeader;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            divisionHeader = itemView.findViewById(R.id.division_header);
        }

        public void onBind(DivisionHeader division) {
            divisionHeader.setText(division.getDivisionHeader());
        }
    }
}
