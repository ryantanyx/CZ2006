package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * Represents the class of a custom list view which is not scrollable
 */
public class NonScrollExpandableListView extends ExpandableListView {
    /**
     * Calls the superclass default constructor
     * @param context The current context of the application
     */
    public NonScrollExpandableListView(Context context) {
        super(context);
    }

    /**
     * Calls the superclass constructor with a set of attributes
     * @param context The current context of the application
     * @param attrs The set of attributes
     */
    public NonScrollExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * Calls the superclass constructor with a set of attributes and a base style
     * @param context The current context of the application
     * @param attrs The set of attributes
     * @param defStyle The base style used
     */
    public NonScrollExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Specifies the size of the custom view
     * @param widthMeasureSpec The width of the view
     * @param heightMeasureSpec The height of the view
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
