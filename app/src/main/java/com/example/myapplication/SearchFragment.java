package com.example.myapplication;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.Filter;
import android.widget.TextView;

import com.google.android.material.slider.RangeSlider;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.ranges.URangesKt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, RangeSlider.OnChangeListener{

    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<School> items;

    private List<School> schoolList = new ArrayList<>();

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
    private AppCompatButton resetButton,northButton,southButton,eastButton,westButton,filterButton;
    private AppCompatButton expressButton,normalaButton,normaltButton;
    private RangeSlider psleSlider;
    private TextView region,streams,pslecutoff;
    private int black,white,red;

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
        hideFilter();
        initColors();

        schoolList = readSchoolData();
        schoolList = readCCAData(schoolList);
        schoolList = readSubjectData(schoolList);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter(getActivity(), schoolList);
        recyclerView.setAdapter(adapter);

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sort_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView back = (TextView) dialog.findViewById(R.id.backbutton);
        back.setOnClickListener(this);

        Button sort = (Button) dialog.findViewById(R.id.sort);
        sort.setOnClickListener(this);

        sortRG = (RadioGroup) dialog.findViewById(R.id.sortRG);
        ascending = (Switch) dialog.findViewById(R.id.ascending);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.searchbar, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R .id.action_search);
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

        MenuItem item2 = menu.findItem(R.id.action_sort);
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dialog.show();
                return false;
            }
        });
    }

    private List<School> readSchoolData() {
        try {
            InputStream is = getResources().openRawResource(R.raw.general_information_of_schools);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            );
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {

                String[] tokens = line.split("\t");

                if (tokens[27].equalsIgnoreCase(getString(R.string.sch_level))){
                    ArrayList<String> contact = new ArrayList<String>();
                    ArrayList<String> transport = new ArrayList<String>();

                    School school = new School();
                    school.setImageUrl(tokens[0]);
                    school.setSchoolName(tokens[1]);
                    school.setAddress(tokens[3]);
                    school.setMission(tokens[20]);
                    school.setVision(tokens[19]);
                    school.setLocation(tokens[22]);
                    school.setRegion(tokens[23]);
                    school.setType(tokens[24]);
                    school.setGender(tokens[25]);

                    if (!tokens[36].equalsIgnoreCase("-")){
                        school.setCutOffPoint(Integer.parseInt(tokens[36]));
                    }

                    contact.add("Tel no: " + tokens[5]);
                    contact.add("Email address: "  + tokens[9]);
                    school.setContactInfo(contact);
                    if (tokens[10].contains("\"")){
                        transport.add("By MRT: " + tokens[10].substring(1,tokens[10].length() -1).toLowerCase());
                    } else{
                        transport.add("By MRT: " + tokens[10].toLowerCase());
                    }
                    if (tokens[11].contains("\"")){
                        transport.add("By bus: " + tokens[11].substring(1,tokens[11].length() -1));
                    } else{
                        transport.add("By bus: " + tokens[11]);
                    }
                    school.setTransport(transport);

                    schoolList.add(school);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schoolList;
    }

    private List<School> readCCAData(List<School> schoolList) {
        HashMap<String, ArrayList<String>> schCCA = new HashMap<String, ArrayList<String>>();

        try {
            InputStream is = getResources().openRawResource(R.raw.co_curricular_activities_ccas);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            );
            String line;
            int i;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String cca, ccaType;
                String[] tokens = line.split("\t");
                if (tokens[1].equalsIgnoreCase(getString(R.string.sch_level))){
                    if (!schCCA.containsKey(tokens[0])){
                        ArrayList<String> ccas = new ArrayList<String>();
                        ccas.add("");
                        ccas.add("");
                        ccas.add("");
                        ccas.add("");
                        ccas.add("");
                        schCCA.put(tokens[0], ccas);
                    }
                    switch(tokens[2]) {
                        case "PHYSICAL SPORTS":
                            i = 0;
                            ccaType = "Sports: ";
                            break;
                        case "VISUAL AND PERFORMING ARTS":
                            ccaType = "Performing Arts: ";
                            i = 1;
                            break;
                        case "CLUBS AND SOCIETIES":
                            ccaType = "Clubs & Societies: ";
                            i = 2;
                            break;
                        case "UNIFORMED GROUPS":
                            ccaType = "Uniformed Groups: ";
                            i = 3;
                            break;
                        case "OTHERS":
                            ccaType = "Others: ";
                            i = 4;
                            break;
                        default:
                            continue;
                    }
                        if (schCCA.get(tokens[0]).get(i).equals("")){
                            cca = new String(ccaType + schCCA.get(tokens[0]).get(i) + tokens[3].toLowerCase());
                        } else{
                            cca = new String( schCCA.get(tokens[0]).get(i) + ", "+ tokens[3].toLowerCase());
                        }
                        schCCA.get(tokens[0]).set(i, cca);
                }
            }
            for (School school: schoolList){
                if (schCCA.containsKey(school.getSchoolName())){
                    school.setCca(schCCA.get(school.getSchoolName()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return schoolList;
    }

    private List<School> readSubjectData(List<School> schoolList) {
        HashMap<String, String> schSubject = new HashMap<String, String>();

        try {
            InputStream is = getResources().openRawResource(R.raw.subjects_offered);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            );
            String line;
            int i;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String subject;
                String[] tokens = line.split("\t");
                if (!schSubject.containsKey(tokens[0])){
                    String subjects = new String("");
                    schSubject.put(tokens[0], subjects);
                }
                if (schSubject.get(tokens[0]).equals("")){
                    subject = new String("Subjects: " + schSubject.get(tokens[0]) + tokens[1].toLowerCase());
                } else{
                    subject = new String(schSubject.get(tokens[0]) + ", " + tokens[1].toLowerCase());
                }
                schSubject.put(tokens[0], subject);
            }
            for (School school: schoolList){
                if (schSubject.containsKey(school.getSchoolName())){
                    school.setSubjects(schSubject.get(school.getSchoolName()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return schoolList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.filter:
                showFilterTapped();
                break;
            case R.id.reset:
                adapter.resetFilter();
                unselectAllRegion();
                resetSlider();
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
        }
    }

    private void northFilter(){
        if (!adapter.getSelectedFilter().contains("north"))
        {
            adapter.filterRegion("north");
            unselectAllRegion();
            lookSelected(northButton);

        }

        else {
            lookUnSelected(northButton);
            unselectAllRegion();
            adapter.resetFilter();
            searchView.setQuery("",false);
            searchView.clearFocus();
        }

    }

    private void southFilter(){
        if (!adapter.getSelectedFilter().contains("south"))
        {
            adapter.filterRegion("south");
            unselectAllRegion();
            lookSelected(southButton);

        }

        else {
            lookUnSelected(southButton);
            unselectAllRegion();
            adapter.resetFilter();
            searchView.setQuery("",false);
            searchView.clearFocus();
        }
    }

    private void eastFilter(){
        if (!adapter.getSelectedFilter().contains("east"))
        {
            adapter.filterRegion("east");
            unselectAllRegion();
            lookSelected(eastButton);

        }

        else {
            lookUnSelected(eastButton);
            unselectAllRegion();
            adapter.resetFilter();
            searchView.setQuery("",false);
            searchView.clearFocus();
        }
    }

    private void westFilter(){
        if (!adapter.getSelectedFilter().contains("west"))
        {
            adapter.filterRegion("west");
            unselectAllRegion();
            lookSelected(westButton);

        }

        else {
            lookUnSelected(westButton);
            unselectAllRegion();
            adapter.resetFilter();
            searchView.setQuery("",false);
            searchView.clearFocus();
        }
    }

    private void expressFilter(){}

    private void normalaFilter(){}

    private void normaltFilter(){}

    private void resetSlider(){
        psleSlider.setValues((float)(0),(float)(300));
    }

    private void showFilterTapped(){
        if(filterHidden) {
            filterHidden = false;
            showFilter();
        }
        else
        {
            filterHidden = true;
            hideFilter();
        }
    }

    private void hideFilter(){
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
        filterButton.setText("FILTER");
    }

    private void showFilter(){
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
        filterButton.setText("HIDE");
    }

    private void initColors()
    {
        black = ContextCompat.getColor(getContext(),R.color.black);
        white = ContextCompat.getColor(getContext(),R.color.white);
        red = ContextCompat.getColor(getContext(),android.R.color.holo_red_light);
    }
    private void lookSelected(AppCompatButton button){
        button.setTextColor(white);
        button.setBackgroundColor(red);
    }

    private void lookUnSelected(AppCompatButton button){
        button.setTextColor(black);
        button.setBackgroundColor(white);
    }
    private void unselectAllRegion(){
        lookUnSelected(northButton);
        lookUnSelected(southButton);
        lookUnSelected(eastButton);
        lookUnSelected(westButton);
    }
    private void unselectAllStreams(){
        lookUnSelected(expressButton);
        lookUnSelected(normalaButton);
        lookUnSelected(normaltButton);
    }

    @Override
    public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
        int low = (int)(float)psleSlider.getValues().get(0);
        int high = (int)(float)psleSlider.getValues().get(1);
        adapter.filterPSLE(low,high);
    }
    public void sort() {

        int index;

        if(sortRG.getCheckedRadioButtonId()==-1){
            Toast.makeText(getActivity(), "Please select a category to sort!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            selectedID = sortRG.getCheckedRadioButtonId();
            View radioButton = sortRG.findViewById(selectedID);
            index = sortRG.indexOfChild(radioButton);
        }

        if(!ascending.isChecked()){
            switch(index){
                case 0:
                    adapter.sort(0);
                    break;
                case 1:
                    adapter.sort(1);
                    break;
                case 2:
                    adapter.sort(2);
                    break;
            }
        }
    }
}