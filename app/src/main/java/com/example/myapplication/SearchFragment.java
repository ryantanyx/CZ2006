package com.example.myapplication;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Filter;
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

import static com.example.myapplication.R.id.sortRegion;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, RangeSlider.OnChangeListener {

    RecyclerView recyclerView;
    SearchAdapter adapter;
    List<School> schoolList;

    private Dialog dialog;
    private RadioGroup sortRG;
    private Switch ascending;

    private int selectedID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SearchView searchView;
    private boolean filterHidden = true;
    private AppCompatButton resetButton, northButton, southButton, eastButton, westButton, filterButton;
    private AppCompatButton expressButton, normalaButton, normaltButton;
    private AppCompatButton expressSort, normalAcadSort, normalTechSort;
    private RadioButton sortSchoolName, sortRegion, sortPSLECutOff;
    private RangeSlider psleSlider;
    private TextView region,streams,pslecutoff,cca,ccatype,ccaspecific;
    private AppCompatSpinner cca1,cca2;
    private int black,white,red;
    private ArrayList<String> arrayList_parent, arrayList_all, arrayList_sports, arrayList_vpa, arrayList_cs,arrayList_ug;
    private ArrayAdapter<String> arrayAdapter_parent, arrayAdapter_child;
    private Set<String> set_sports = new HashSet<String>(), set_vpa = new HashSet<String>(), set_cs = new HashSet<String>(), set_ug = new HashSet<String>();


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
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arrayAdapter_child.getItem(position).equals("'No CCA Selected'"))
                    adapter.filterCCA("all");
                else
                    adapter.filterCCA(arrayAdapter_child.getItem(position));
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
        return view;
    }


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
                break;
            case R.id.north:
                northFilter();
                break;
            case R.id.south:
                southFilter();
                break;
            case R.id.east:
                eastFilter();
                break;
            case R.id.west:
                westFilter();
                break;
            case R.id.express:
                expressFilter();
                break;
            case R.id.normala:
                normalaFilter();
                break;
            case R.id.normalt:
                normaltFilter();
                break;
            case R.id.backbutton:
                dialog.dismiss();
                break;
            case R.id.sort:
                sort();
                dialog.dismiss();
                break;
            case R.id.sortSchoolName:
            case R.id.sortRegion:
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

    private void resetSpinner() {
        cca1.setSelection(0);
        cca2.setSelection(0);
    }

    private void resetSlider() {
        psleSlider.setValues((float)(0),(float)(300));
    }

    private void showFilterTapped() {
        if (filterHidden) {
            filterHidden = false;
            showFilter();
        } else {
            filterHidden = true;
            hideFilter();
        }
    }

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
        cca.setVisibility(View.GONE);
        ccatype.setVisibility(View.GONE);
        ccaspecific.setVisibility(View.GONE);
        cca1.setVisibility(View.GONE);
        cca2.setVisibility(View.GONE);
        filterButton.setText("FILTER");
    }

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
        cca.setVisibility(View.VISIBLE);
        ccatype.setVisibility(View.VISIBLE);
        ccaspecific.setVisibility(View.VISIBLE);
        cca1.setVisibility(View.VISIBLE);
        cca2.setVisibility(View.VISIBLE);
        filterButton.setText("HIDE");
    }

    private void initColors() {
        black = ContextCompat.getColor(getContext(), R.color.black);
        white = ContextCompat.getColor(getContext(), R.color.white);
        red = ContextCompat.getColor(getContext(), android.R.color.holo_red_light);
    }

    private void lookSelected(AppCompatButton button) {
        button.setTextColor(white);
        button.setBackgroundColor(red);
    }

    private void lookUnSelected(AppCompatButton button) {
        button.setTextColor(black);
        button.setBackgroundColor(white);
    }

    private void unselectAllRegion() {
        lookUnSelected(northButton);
        lookUnSelected(southButton);
        lookUnSelected(eastButton);
        lookUnSelected(westButton);
    }

    private void unselectAllStreams() {
        lookUnSelected(expressButton);
        lookUnSelected(normalaButton);
        lookUnSelected(normaltButton);
    }

    @Override
    public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
        int low = (int) (float) psleSlider.getValues().get(0);
        int high = (int) (float) psleSlider.getValues().get(1);
        adapter.filterPSLE(low, high);
    }

    public void sort() {

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
                    break;
                case 1:
                    adapter.sort(1);
                    break;
                case 2:
                    if (expressSort.isSelected())
                        adapter.sort(2);
                    else if(normalAcadSort.isSelected())
                        adapter.sort(3);
                    else if (normalTechSort.isSelected())
                        adapter.sort(4);
                    break;
            }
        if (!ascending.isChecked()) {
            adapter.reverse();
        }
        }


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

    private void unselectAllSort() {
        expressSort.setSelected(false);
        normalAcadSort.setSelected(false);
        normalTechSort.setSelected(false);
        lookUnSelected(expressSort);
        lookUnSelected(normalAcadSort);
        lookUnSelected(normalTechSort);
    }
    private void nonScoreSelect() {
        expressSort.setVisibility(View.GONE);
        normalAcadSort.setVisibility(View.GONE);
        normalTechSort.setVisibility(View.GONE);
    }
    private void scoreSelect() {
        expressSort.setVisibility(View.VISIBLE);
        normalAcadSort.setVisibility(View.VISIBLE);
        normalTechSort.setVisibility(View.VISIBLE);
    }
}