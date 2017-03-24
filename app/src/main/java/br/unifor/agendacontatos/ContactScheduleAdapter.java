package br.unifor.agendacontatos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by rafaelpinheiro on 21/02/17.
 */

public class ContactScheduleAdapter extends RecyclerView.Adapter<ContactScheduleAdapter.ViewHolder>{

    private ArrayList<Contact> contactScheduleInAdapter;
    private OnDataSelectedInterface onDataSelectedInterface;
    private Context context;

    public interface OnDataSelectedInterface{
        void onDataSelectedMethod(View view, int position);
    }


    public ContactScheduleAdapter(ArrayList<Contact> contactSchedule, OnDataSelectedInterface onDataSelectedInterface, Context context){
        this.contactScheduleInAdapter = contactSchedule;
        this.onDataSelectedInterface = onDataSelectedInterface;
        this.context = context;
    }


    public void setList(ArrayList<Contact> contactSchedule){
        this.contactScheduleInAdapter = contactSchedule;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewLetter;

        public ViewHolder(View v){
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    treatOnDataSelectedIfNecessary(view, getAdapterPosition());
                }
            });
            textViewName = (TextView) v.findViewById(R.id.textViewContactNameId);
            textViewLetter = (TextView) v.findViewById(R.id.textViewLetterId);
        }

    }

    private void treatOnDataSelectedIfNecessary(View view, int position) {
        if(onDataSelectedInterface != null) {
            onDataSelectedInterface.onDataSelectedMethod(view, position);
        }
    }


    @Override
    public ContactScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ContactScheduleAdapter.ViewHolder holder, int position) {
        holder.textViewName.setText(contactScheduleInAdapter.get(position).getName());
        holder.textViewLetter.setText(Character.toString(contactScheduleInAdapter.get(position).getName().charAt(0)));
        GradientDrawable shapeLetter = (GradientDrawable) holder.textViewLetter.getBackground();
        shapeLetter.setColor(randomColor());
    }

    @Override
    public int getItemCount() {
        return contactScheduleInAdapter.size();
    }

    private int randomColor(){
        int sortedColor;
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(5);
        switch(randomNumber){
            case 0:
                sortedColor = ContextCompat.getColor(context, R.color.lightCyan);
                break;
            case 1:
                sortedColor = ContextCompat.getColor(context, R.color.lightOrange);
                break;
            case 2:
                sortedColor = ContextCompat.getColor(context, R.color.lightGreen);
                break;
            case 3:
                sortedColor = ContextCompat.getColor(context, R.color.lightPink);
                break;
            case 4:
                sortedColor = ContextCompat.getColor(context, R.color.lightRed);
                break;
            case 5:
                sortedColor = ContextCompat.getColor(context, R.color.lightYellow);
                break;
            case 6:
                sortedColor = ContextCompat.getColor(context, R.color.lightBlue);
                break;
            default:
                sortedColor = Color.BLACK;
        }
        return sortedColor;
    }
}
