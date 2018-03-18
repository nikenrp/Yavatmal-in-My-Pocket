package in.protechlabz.www.yavatmalindicatorserver.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.protechlabz.www.yavatmalindicatorserver.R;
import in.protechlabz.www.yavatmalindicatorserver.model.ListData;

/**
 * Created by Nikesh on 22/12/2016.
 */
public class ListDataAdapter extends ArrayAdapter<ListData> implements Filterable{

    LayoutInflater inflater;
    List<ListData> tempDataList;
    CustomFilter mFilter;
    List<ListData> filterList;

    public ListDataAdapter(Context context, int resource, List<ListData> dataList) {
        super(context, resource, dataList);
        inflater = LayoutInflater.from(context);
        tempDataList = dataList;
        this.filterList = dataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listitem_row, parent, false); // Lots of changes here getLayoutInflater cant be used here
        }

        ListData myCurrentData = tempDataList.get(position);

        ImageView myImage = (ImageView) convertView.findViewById(R.id.itemImageIcon);
        TextView myTextView = (TextView) convertView.findViewById(R.id.itemFieldName);

        myImage.setImageResource(myCurrentData.getItemImageID());
        myTextView.setText(myCurrentData.getNameofItem());

        return convertView;
    }



    @NonNull
    @Override
    public Filter getFilter() {
        if(mFilter==null) {
            mFilter = new CustomFilter();
        }
        return mFilter;
    }

    class CustomFilter extends Filter {

        public CustomFilter() {
            super();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if(constraint!=null && constraint.length()>0) {
                //Constraint to upper
                constraint = constraint.toString().toUpperCase();

                List<ListData> filters = new ArrayList<ListData>();
                //get specific items
                for(int i=0;i<filterList.size();i++) {
                    if(filterList.get(i).getNameofItem().toUpperCase().contains(constraint)) {
                        ListData d = new ListData(filterList.get(i).getNameofItem(),filterList.get(i).getItemImageID());
                        filters.add(d);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // This line important remove it if code not working
            //tempDataList = (ArrayList<ListData>) results.values;
            //notifyDataSetChanged();

        }
    }
}
