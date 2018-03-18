package in.protechlabz.www.yavatmalindicatorserver.model;

/**
 * Created by Nikesh on 22/12/2016.
 */
public class ListData {

    private String nameofItem;
    private int itemImageID;

    public ListData(String nameofItem, int itemImageID) {
        this.nameofItem = nameofItem;
        this.itemImageID = itemImageID;
    }

    public String getNameofItem() {
        return nameofItem;
    }

    public int getItemImageID() {
        return itemImageID;
    }
}
