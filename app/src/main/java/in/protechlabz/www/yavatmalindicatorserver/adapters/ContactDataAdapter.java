package in.protechlabz.www.yavatmalindicatorserver.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import in.protechlabz.www.yavatmalindicatorserver.model.ContactData;

/**
 * Created by Nikesh on 23/12/2016.
 */
public class ContactDataAdapter extends ArrayAdapter<ContactData> implements Filterable{

    LayoutInflater inflater;
    List<ContactData> tempDataList;
    List<ContactData> clonetempDataList;
    Intent intent;
    private Context myContext;
    private ContactData myCurrentData;

    public ContactDataAdapter(Context context, int resource, List<ContactData> dataList) {
        super(context, resource, dataList);
        inflater = LayoutInflater.from(context);
        tempDataList = dataList;
        clonetempDataList = dataList;
        myContext=context;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clonetempDataList = (List<ContactData>) results.values;
                ContactDataAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ContactData> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = tempDataList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<ContactData> getFilteredResults(String constraint) {
        List<ContactData> results = new ArrayList<>();
        for (ContactData item : tempDataList) {
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    @Override
    public int getCount() {
        return clonetempDataList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.eachcontact_row, parent, false); // Lots of changes here getLayoutInflater cant be used here
        }

        myCurrentData = clonetempDataList.get(position);

        TextView myTextName = (TextView) convertView.findViewById(R.id.itemName);
        TextView myTextAddress = (TextView) convertView.findViewById(R.id.itemAddress);
        TextView myTextNumber = (TextView) convertView.findViewById(R.id.itemPhoneNumber);
        //ImageView myMap = (ImageView) convertView.findViewById(R.id.itemMapIcon);
        ImageView myCall = (ImageView) convertView.findViewById(R.id.itemCallIcon);

        //myMap.setTag(position);
        myCall.setTag(position);

        myTextName.setText(myCurrentData.getName());
        myTextAddress.setText(myCurrentData.getAddress());
        myTextNumber.setText("+91" + String.valueOf(myCurrentData.getTelephone()));

        myCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionMap=(Integer)v.getTag();
                ContactData myMapData = clonetempDataList.get(positionMap);
                String number = "+91" + String.valueOf(myMapData.getTelephone());
                // Parse only one number if multiple numbers present
                /*int indexOfComma = number.indexOf(",");

                if (indexOfComma != -1) {
                    number = number.substring(0, indexOfComma);
                }*/
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                myContext.startActivity(intent);
            }
        });
        /*myMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionMap=(Integer)v.getTag();
                ContactData myMapData = tempDataList.get(positionMap);
                String name = myMapData.getName();
                double templat = myMapData.getLatitude();
                double tempLon = myMapData.getLongitude();

                Intent i = new Intent(getContext(), MapActivity.class);
                i.putExtra("ContactDataMapKey",myMapData);
                myContext.startActivity(i);
            }
        });*/
        return convertView;
    }
}
