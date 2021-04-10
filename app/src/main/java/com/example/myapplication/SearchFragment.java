package com.example.myapplication;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.slider.RangeSlider;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the Search Page Boundary where users can search for the schools they are interested in
 */
public class SearchFragment extends Fragment implements View.OnClickListener, RangeSlider.OnChangeListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    /**
     * RecyclerView to contain the list of schools
     */
    RecyclerView recyclerView;
    /**
     * Instance of the SearchAdapter to control the logic
     */
    SearchAdapter adapter;
    /**
     * The list of schools available
     */
    List<School> schoolList;
    /**
     * Dialog for the sort function
     */
    private Dialog dialog;
    /**
     * RadioGroup to display sort options
     */
    private RadioGroup sortRG;
    /**
     * Switch button to select ascending or descending for sort
     */
    private Switch ascending;
    /**
     * Integer to store the user's sort choice
     */
    private int selectedID;
    private String mParam1;
    private String mParam2;
    /**
     * SearchView to display search bar
     */
    private SearchView searchView;
    /**
     * Boolean to indicate whether filter page is hidden
     */
    private boolean filterHidden = true;
    /**
     * Buttons for the region filter and reset button
     */
    private AppCompatButton resetButton, northButton, southButton, eastButton, westButton, filterButton;
    /**
     * Buttons for the stream filter
     */
    private AppCompatButton expressButton, normalaButton, normaltButton;
    /**
     * Buttons for sorting by stream
     */
    private AppCompatButton expressSort, normalAcadSort, normalTechSort;
    /**
     * RadioButtons for sorting by school name, region, PSLE cut-off and distance
     */
    private RadioButton sortSchoolName, sortRegion, sortPSLECutOff,sortDistance;
    /**
     * RangeSlider for users to select range of PSLE scores and distance from home
     */
    private RangeSlider psleSlider,distSlider;
    /**
     * TextView to display relevant text in Filter page
     */
    private TextView region,streams,pslecutoff,cca,ccatype,ccaspecific,distText;
    /**
     * Spinner for users to select CCA to filter
     */
    private AppCompatSpinner cca1,cca2;
    /**
     * Integers to store values of colours
     */
    private int black,white,red;
    /**
     * ArrayList for drop down list when selecting CCA to filter
     */
    private ArrayList<String> arrayList_parent, arrayList_all, arrayList_sports, arrayList_vpa, arrayList_cs,arrayList_ug;
    /**
     * ArrayAdapter to control text displayed in drop down list
     */
    private ArrayAdapter<String> arrayAdapter_parent, arrayAdapter_child;
    /**
     * Set of each CCA type to ensure duplicates are removed
     */
    private Set<String> set_sports = new HashSet<String>(), set_vpa = new HashSet<String>(), set_cs = new HashSet<String>(), set_ug = new HashSet<String>();

    /**
     * Required empty public constructor
     */
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * Initial creation of fragment from savedInstanceState
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    /**
     * Graphical Initialisation of the fragment and inflates the layout of the fragment onto a container
     * @param inflater Inflate the layout of the fragment
     * @param container Container for the layout
     * @param savedInstanceState
     * @return The search page
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        getActivity().setTitle("Search");
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SearchAdapter(getActivity());
        schoolList = adapter.getSchoolList();
        recyclerView.setAdapter(adapter);

        filterButton = view.findViewById(R.id.filter);
        filterButton.setOnClickListener(this);
        resetButton = view.findViewById(R.id.reset);
        resetButton.setOnClickListener(this);
        northButton = view.findViewById(R.id.north);
        northButton.setOnClickListener(this);
        southButton = view.findViewById(R.id.south);
        southButton.setOnClickListener(this);
        eastButton = view.findViewById(R.id.east);
        eastButton.setOnClickListener(this);
        westButton = view.findViewById(R.id.west);
        westButton.setOnClickListener(this);
        region =  view.findViewById(R.id.region);
        expressButton = view.findViewById(R.id.express);
        expressButton.setOnClickListener(this);
        normalaButton = view.findViewById(R.id.normala);
        normalaButton.setOnClickListener(this);
        normaltButton = view.findViewById(R.id.normalt);
        normaltButton.setOnClickListener(this);
        streams =  view.findViewById(R.id.streams);
        psleSlider = view.findViewById(R.id.psleslider);
        psleSlider.addOnChangeListener(this::onValueChange);
        pslecutoff = view.findViewById(R.id.pslecutoff);
        distSlider = view.findViewById(R.id.distslider);
        distSlider.addOnChangeListener(this::onValueChange);
        distText = view.findViewById(R.id.distance);
        cca = view.findViewById(R.id.cca);
        ccatype = view.findViewById(R.id.ccatype);
        ccaspecific = view.findViewById(R.id.ccaspecific);
        cca1 = view.findViewById(R.id.cca1);
        cca2 = view.findViewById(R.id.cca2);
        ccaSpinner();

        AdapterView.OnItemSelectedListener spinner1 = new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0) {
                    arrayAdapter_child = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_all);
                }
                if (position ==1) {
                    arrayAdapter_child = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_sports);
                }
                if (position ==2) {
                    arrayAdapter_child = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_vpa);
                }
                if (position ==3) {
                    arrayAdapter_child = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_cs);
                }
                if (position ==4) {
                    arrayAdapter_child = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_ug);
                }
                cca2.setAdapter(arrayAdapter_child);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        cca1.setOnItemSelectedListener(spinner1);
        AdapterView.OnItemSelectedListener spinner2 = new AdapterView.OnItemSelectedListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arrayAdapter_child.getItem(position).equals("'No CCA Selected'")){
                    adapter.filterCCA("all");
                }
                else{
                    adapter.filterCCA(arrayAdapter_child.getItem(position));
                }
                if (sortRG.getCheckedRadioButtonId() != -1) {
                    sort(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        cca2.setOnItemSelectedListener(spinner2);

        hideFilter();
        initColors();

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sort_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.background));
        }
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView back = (TextView) dialog.findViewById(R.id.backbutton);
        back.setOnClickListener(this);

        Button sort = (Button) dialog.findViewById(R.id.sort);
        sort.setOnClickListener(this);

        sortRG = (RadioGroup) dialog.findViewById(R.id.sortRG);
        ascending = (Switch) dialog.findViewById(R.id.switchAsc);
        expressSort = dialog.findViewById(R.id.expressSort);
        expressSort.setOnClickListener(this);
        normalAcadSort = dialog.findViewById(R.id.normalAcadSort);
        normalAcadSort.setOnClickListener(this);
        normalTechSort = dialog.findViewById(R.id.normalTechSort);
        normalTechSort.setOnClickListener(this);
        sortSchoolName = dialog.findViewById(R.id.sortSchoolName);
        sortSchoolName.setOnClickListener(this);
        sortRegion = dialog.findViewById(R.id.sortRegion);
        sortRegion.setOnClickListener(this);
        sortPSLECutOff = dialog.findViewById(R.id.sortPSLECutOff);
        sortPSLECutOff.setOnClickListener(this);
        sortDistance = dialog.findViewById(R.id.sortDistance);
        sortDistance.setOnClickListener(this);
        return view;
    }

    /**
     * Create the options menu at the top of the page
     * @param menu The menu to be created
     * @param inflater The inflater to inflate the menu onto the layout
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.searchbar, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.searchViewFilter(newText);
                return false;
            }
        });

        MenuItem sort = menu.findItem(R.id.action_sort);
        sort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dialog.show();
                return false;
            }
        });
    }

    /**
     * Populate the drop down list in the CCA filter
     */
    private void ccaSpinner(){
        arrayList_parent = new ArrayList<>();
        arrayList_all = new ArrayList<>();
        arrayList_sports = new ArrayList<>();
        arrayList_vpa = new ArrayList<>();
        arrayList_cs = new ArrayList<>();
        arrayList_ug = new ArrayList<>();
        arrayList_parent.add("All");
        arrayList_parent.add("Physical Sports");
        arrayList_parent.add("Visual & Performing Arts");
        arrayList_parent.add("Clubs & Societies");
        arrayList_parent.add("Uniformed Groups");
        for (School school:schoolList) {
            if (school.getCca() != null) {
                for (Map.Entry<String, ArrayList<String>> temp : school.getCca().entrySet()) {
                    if (temp.getKey().equals("Sports")) {
                        ArrayList<String> strings = temp.getValue();
                        set_sports.addAll(strings);
                    }
                    if (temp.getKey().equals("Performing Arts")) {
                        ArrayList<String> strings = temp.getValue();
                        set_vpa.addAll(strings);
                    }
                    if (temp.getKey().equals("Clubs & Societies")) {
                        ArrayList<String> strings = temp.getValue();
                        set_cs.addAll(strings);
                    }
                    if (temp.getKey().equals("Uniformed Groups")) {
                        ArrayList<String> strings = temp.getValue();
                        set_ug.addAll(strings);
                    }
                }
            }
        }
        arrayAdapter_parent = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_parent);
        cca1.setAdapter(arrayAdapter_parent);
        arrayList_sports.addAll(set_sports);
        arrayList_vpa.addAll(set_vpa);
        arrayList_cs.addAll(set_cs);
        arrayList_ug.addAll(set_ug);
        arrayList_all.addAll(set_sports);
        arrayList_all.addAll(set_vpa);
        arrayList_all.addAll(set_cs);
        arrayList_all.addAll(set_ug);
        arrayList_all.add("'No CCA Selected'");
        Collections.sort(arrayList_all);
        Collections.sort(arrayList_sports);
        Collections.sort(arrayList_vpa);
        Collections.sort(arrayList_cs);
        Collections.sort(arrayList_ug);
    }
    /**
     * Switch case to execute different commands for the respective buttons
     * @param v View
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter:
                showFilterTapped();
                break;
            case R.id.reset:
                adapter.resetFilter();
                unselectAllRegion();
                unselectAllStreams();
                resetSlider();
                resetSpinner();
                if (sortRG.getCheckedRadioButtonId() != -1) {
                    sort(true);
                }
                break;
            case R.id.north:
                northFilter();
                if (sortRG.getCheckedRadioButtonId() != -1) {
                    sort(true);
                }
                break;
            case R.id.south:
                southFilter();
                if (sortRG.getCheckedRadioButtonId() != -1) {
                    sort(true);
                }
                break;
            case R.id.east:
                eastFilter();
                if (sortRG.getCheckedRadioButtonId() != -1) {
                    sort(true);
                }
                break;
            case R.id.west:
                westFilter();
                if (sortRG.getCheckedRadioButtonId() != -1) {
                    sort(true);
                }
                break;
            case R.id.express:
                expressFilter();
                if (sortRG.getCheckedRadioButtonId() != -1) {
                    sort(true);
                }
                break;
            case R.id.normala:
                normalaFilter();
                if (sortRG.getCheckedRadioButtonId() != -1) {
                    sort(true);
                }
                break;
            case R.id.normalt:
                normaltFilter();
                if (sortRG.getCheckedRadioButtonId() != -1) {
                    sort(true);
                }
                break;
            case R.id.backbutton:
                dialog.dismiss();
                break;
            case R.id.sort:
                sort(false);
                dialog.dismiss();
                break;
            case R.id.sortSchoolName:
            case R.id.sortRegion:
            case R.id.sortDistance:
                nonScoreSelect();
                break;
            case R.id.sortPSLECutOff:
                scoreSelect();
                break;
            case R.id.expressSort:
                expressSortSelect();
                break;
            case R.id.normalAcadSort:
                normalAcadSelect();
                break;
            case R.id.normalTechSort:
                normalTechSelect();
                break;
            default:
                break;
        }
    }

    /**
     * Filter by north region
     */
    private void northFilter() {
        if (!adapter.getSelectedRegion().contains("north"))  {
            adapter.filterRegion("north");
            unselectAllRegion();
            lookSelected(northButton);

        } else {
            lookUnSelected(northButton);
            unselectAllRegion();
            adapter.filterRegion("all");
            searchView.setQuery("",false);
            searchView.clearFocus();
        }
    }
    /**
     * Filter by south region
     */
    private void southFilter() {
        if (!adapter.getSelectedRegion().contains("south")) {
            adapter.filterRegion("south");
            unselectAllRegion();
            lookSelected(southButton);

        } else {
            lookUnSelected(southButton);
            unselectAllRegion();
            adapter.filterRegion("all");
            searchView.setQuery("",false);
            searchView.clearFocus();
        }
    }
    /**
     * Filter by east region
     */
    private void eastFilter() {
        if (!adapter.getSelectedRegion().contains("east"))
        {
            adapter.filterRegion("east");
            unselectAllRegion();
            lookSelected(eastButton);

        } else {
            lookUnSelected(eastButton);
            unselectAllRegion();
            adapter.filterRegion("all");
            searchView.setQuery("",false);
            searchView.clearFocus();
        }
    }
    /**
     * Filter by west region
     */
    private void westFilter() {
        if (!adapter.getSelectedRegion().contains("west"))
        {
            adapter.filterRegion("west");
            unselectAllRegion();
            lookSelected(westButton);
        }

        else {
            lookUnSelected(westButton);
            unselectAllRegion();
            adapter.filterRegion("all");
            searchView.setQuery("",false);
            searchView.clearFocus();
        }
    }
    /**
     * Filter by express stream
     */
    private void expressFilter() {
        if (!adapter.getSelectedStream().contains("express"))
        {
            adapter.filterStream("express");
            unselectAllStreams();
            lookSelected(expressButton);
        }

        else {
            lookUnSelected(expressButton);
            unselectAllStreams();
            adapter.filterStream("all");
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
    }
    /**
     * Filter by normal academic stream
     */
    private void normalaFilter() {
        if (!adapter.getSelectedStream().contains("na"))
        {
            adapter.filterStream("na");
            unselectAllStreams();
            lookSelected(normalaButton);
        }

        else {
            lookUnSelected(normalaButton);
            unselectAllStreams();
            adapter.filterStream("all");
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
    }
    /**
     * Filter by normal technical stream
     */
    private void normaltFilter() {
        if (!adapter.getSelectedStream().contains("nt"))
        {
            adapter.filterStream("nt");
            unselectAllStreams();
            lookSelected(normaltButton);
        }

        else {
            lookUnSelected(normaltButton);
            unselectAllStreams();
            adapter.filterStream("all");
            searchView.setQuery("", false);
            searchView.clearFocus();
        }
    }
    /**
     * Reset the CCA filter selection
     */
    private void resetSpinner() {
        cca1.setSelection(0);
        cca2.setSelection(0);
    }
    /**
     * Reset the PSLE cutoff and distance sliders
     */
    private void resetSlider() {
        psleSlider.setValues((float)(0),(float)(300));
        distSlider.setValues((float)(0),(float)(50));
    }

    /**
     * Show/hide the filter page when filter button is clicked on
     */
    private void showFilterTapped() {
        if (filterHidden) {
            filterHidden = false;
            showFilter();
        } else {
            filterHidden = true;
            hideFilter();
        }
    }

    /**
     * Hide the filter page
     */
    private void hideFilter() {
        resetButton.setVisibility(View.GONE);
        northButton.setVisibility(View.GONE);
        southButton.setVisibility(View.GONE);
        eastButton.setVisibility(View.GONE);
        westButton.setVisibility(View.GONE);
        region.setVisibility(View.GONE);
        expressButton.setVisibility(View.GONE);
        normalaButton.setVisibility(View.GONE);
        normaltButton.setVisibility(View.GONE);
        streams.setVisibility(View.GONE);
        psleSlider.setVisibility(View.GONE);
        pslecutoff.setVisibility(View.GONE);
        distSlider.setVisibility(View.GONE);
        distText.setVisibility(View.GONE);
        cca.setVisibility(View.GONE);
        ccatype.setVisibility(View.GONE);
        ccaspecific.setVisibility(View.GONE);
        cca1.setVisibility(View.GONE);
        cca2.setVisibility(View.GONE);
        filterButton.setText("FILTER");
    }

    /**
     * Show the filter page
     */
    private void showFilter() {
        resetButton.setVisibility(View.VISIBLE);
        northButton.setVisibility(View.VISIBLE);
        southButton.setVisibility(View.VISIBLE);
        eastButton.setVisibility(View.VISIBLE);
        westButton.setVisibility(View.VISIBLE);
        region.setVisibility(View.VISIBLE);
        expressButton.setVisibility(View.VISIBLE);
        normalaButton.setVisibility(View.VISIBLE);
        normaltButton.setVisibility(View.VISIBLE);
        streams.setVisibility(View.VISIBLE);
        psleSlider.setVisibility(View.VISIBLE);
        pslecutoff.setVisibility(View.VISIBLE);
        distSlider.setVisibility(View.VISIBLE);
        distText.setVisibility(View.VISIBLE);
        cca.setVisibility(View.VISIBLE);
        ccatype.setVisibility(View.VISIBLE);
        ccaspecific.setVisibility(View.VISIBLE);
        cca1.setVisibility(View.VISIBLE);
        cca2.setVisibility(View.VISIBLE);
        filterButton.setText("HIDE");
    }

    /**
     * Populate the list of colours used in the fragment
     */
    private void initColors() {
        black = ContextCompat.getColor(getContext(), R.color.black);
        white = ContextCompat.getColor(getContext(), R.color.white);
        red = ContextCompat.getColor(getContext(), android.R.color.holo_red_light);
    }

    /**
     * Show users the button is selected by changing colour
     * @param button
     */
    private void lookSelected(AppCompatButton button) {
        button.setTextColor(white);
        button.setBackgroundColor(red);
    }
    /**
     * Show users the button is not selected by changing colour
     * @param button
     */
    private void lookUnSelected(AppCompatButton button) {
        button.setTextColor(black);
        button.setBackgroundColor(white);
    }

    /**
     * Unselect all the region filter buttons
     */
    private void unselectAllRegion() {
        lookUnSelected(northButton);
        lookUnSelected(southButton);
        lookUnSelected(eastButton);
        lookUnSelected(westButton);
    }

    /**
     * Unselect all the stream filter buttons
     */
    private void unselectAllStreams() {
        lookUnSelected(expressButton);
        lookUnSelected(normalaButton);
        lookUnSelected(normaltButton);
    }

    /**
     * Switch case for the sliders to execute their functions when value is changed
     * @param slider The slider with the change in value
     * @param value The value of the slider
     * @param fromUser Whether the change was initiated from user
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
        switch (slider.getId()) {
            case R.id.psleslider:
                int low = (int) (float) psleSlider.getValues().get(0);
                int high = (int) (float) psleSlider.getValues().get(1);
                adapter.filterPSLE(low, high);
                if (sortRG.getCheckedRadioButtonId() != -1) {
                    sort(true);
                }
                break;
            case R.id.distslider:
                low = (int) (float) distSlider.getValues().get(0);
                high = (int) (float) distSlider.getValues().get(1);
                adapter.filterDist(low, high);
                if (sortRG.getCheckedRadioButtonId() != -1) {
                    sort(true);
                }
        }
    }

    /**
     * Switch case to sort the list of schools depending on user's selection
     * @param filter Flag to indicate whether the sort is performed from the filter function
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort(boolean filter) {
        int index;

        if (sortRG.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), "Please select a category to sort!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            selectedID = sortRG.getCheckedRadioButtonId();
            View radioButton = sortRG.findViewById(selectedID);
            index = sortRG.indexOfChild(radioButton);
        }

            switch (index) {
                case 0:
                    adapter.sort(0);
                    if (!filter) {
                        Toast.makeText(getActivity(), "Sorted by School Name!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    adapter.sort(1);
                    if (!filter) {
                        Toast.makeText(getActivity(), "Sorted by Region(ENSW)!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    adapter.sort(5);
                    if (!filter) {
                        Toast.makeText(getActivity(), "Sorted by Distance from Address!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    if (expressSort.isSelected()) {
                        adapter.sort(2);
                        if (!filter) {
                            Toast.makeText(getActivity(), "Sorted by Cut-Off Point (Express)!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    else if(normalAcadSort.isSelected()) {
                        adapter.sort(3);
                        if (!filter) {
                            Toast.makeText(getActivity(), "Sorted by Cut-Off Point (NA)!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    else if (normalTechSort.isSelected()) {
                        adapter.sort(4);
                        if (!filter) {
                            Toast.makeText(getActivity(), "Sorted by Cut-Off Point (NA)!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }

            }
        if (!ascending.isChecked()) {
            adapter.reverse();
        }
    }

    /**
     * Changes the colour and set whether sort by express button is selected
     */
    public void expressSortSelect() {
        if (expressSort.isSelected()) {
            lookUnSelected(expressSort);
            expressSort.setSelected(false);
        } else {
            unselectAllSort();
            lookSelected(expressSort);
            expressSort.setSelected(true);
        }
    }
    /**
     * Changes the colour and set whether sort by normal academic button is selected
     */
    public void normalAcadSelect() {
        if (normalAcadSort.isSelected()) {
            lookUnSelected(normalAcadSort);
            normalAcadSort.setSelected(false);
        } else {
            unselectAllSort();
            lookSelected(normalAcadSort);
            normalAcadSort.setSelected(true);
        }
    }
    /**
     * Changes the colour and set whether sort by normal technical button is selected
     */
    public void normalTechSelect() {
        if (normalTechSort.isSelected()) {
            lookUnSelected(normalTechSort);
            normalTechSort.setSelected(false);
        } else {
            unselectAllSort();
            lookSelected(normalTechSort);
            normalTechSort.setSelected(true);
        }
    }
    /**
     * Unselect all sort buttons
     */
    private void unselectAllSort() {
        expressSort.setSelected(false);
        normalAcadSort.setSelected(false);
        normalTechSort.setSelected(false);
        lookUnSelected(expressSort);
        lookUnSelected(normalAcadSort);
        lookUnSelected(normalTechSort);
    }

    /**
     * Hide buttons if PSLE cut-off sort was not selected
     */
    private void nonScoreSelect() {
        expressSort.setVisibility(View.GONE);
        normalAcadSort.setVisibility(View.GONE);
        normalTechSort.setVisibility(View.GONE);
    }

    /**
     * Show buttons if PSLE cut-off sort was selected
     */
    private void scoreSelect() {
        expressSort.setVisibility(View.VISIBLE);
        normalAcadSort.setVisibility(View.VISIBLE);
        normalTechSort.setVisibility(View.VISIBLE);
    }
}