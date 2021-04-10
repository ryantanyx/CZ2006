package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Represents the Expandable Adaptor Controller which controls the layout of the school details displayed
 */
public class ExpandableAdaptor extends BaseExpandableListAdapter {
    /**
     * Context of the current state of the application
     */
    Context context;
    /**
     * List of groups of information to be displayed
     */
    List<String> listGroup;
    /**
     * HashMap of the list of items mapped to each group
     */
    HashMap<String, List<String>> listItem;
    /**
     * Size of the expandable list view
     */
    int size;

    /**
     * Constructor to create a new ExpandableAdaptor with default size 20
     * @param context the application's current context
     * @param listGroup list of groups to be placed in the view
     * @param listItem list of items under each header
     */
    public ExpandableAdaptor(Context context, List<String> listGroup, HashMap<String, List<String>> listItem){
        this.context = context;
        this.listGroup = listGroup;
        this.listItem = listItem;
        this.size = 20;
    }
    /**
     * Constructor to create a new ExpandableAdaptor with a specific size
     * @param context The application's current context
     * @param listGroup List of groups to be placed in the view
     * @param listItem List of items under each group
     * @param size Size of the expandable list view
     */
    public ExpandableAdaptor(Context context, List<String> listGroup, HashMap<String, List<String>> listItem, int size){
        this.context = context;
        this.listGroup = listGroup;
        this.listItem = listItem;
        this.size = size;
    }

    /**
     * Get the size of the list of groups
     * @return the size of the list of groups
     */
    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    /**
     * Get the size of the list of items from the position of the group
     * @param groupPosition Position of the group
     * @return The number of child in the group
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listItem.get(this.listGroup.get(groupPosition)).size();
    }

    /**
     * Get the group from the position
     * @param groupPosition Position of the group
     * @return returns the entire group as an Object
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroup.get(groupPosition);
    }

    /**
     * Get the item from the group position and item position
     * @param groupPosition Position of the group
     * @param childPosition Position of the child
     * @return Returns the entire child as an Object
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listItem.get(this.listGroup.get(groupPosition)).get(childPosition);
    }
    /**
     * Get the item from the group position and item position
     * @param groupPosition Position of the group
     * @return Returns the ID of the group
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Get the item from the group position and item position
     * @param groupPosition Position of the group
     * @param childPosition Position of the group
     * @return Returns the ID of the child
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Get the item from the group position and item position
     * @return Returns false since the same ID does always refers to the same object
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Get the item from the group position and item position
     * @param groupPosition Position of the group
     * @param isExpanded Whether the group is expanded or not
     * @param convertView the view to be converted to
     * @param parent the parent ViewGroup
     * @return Returns false since the same ID does always refers to the same object
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String group = (String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView textView = convertView.findViewById(R.id.list_parent);
        textView.setText(group);
        textView.setTextSize(size);
        return convertView;
    }

    /**
     * Get the item from the group position and item position
     * @param groupPosition Position of the group
     * @param childPosition Position of the child
     * @param isLastChild Whether the child is the last item in the ViewGroup
     * @param convertView the view to be converted to
     * @param parent the parent ViewGroup
     * @return Returns false since the same ID does always refers to the same object
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String child = (String) getChild(groupPosition,childPosition);
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        TextView textView = convertView.findViewById(R.id.list_child);
        textView.setText(child);
        textView.setTextSize(size);
        return convertView;
    }
    /**
     * Get the item from the group position and item position
     * @param groupPosition Position of the group
     * @param childPosition Position of the child
     * @return Returns false since the same ID does always refers to the same object
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
