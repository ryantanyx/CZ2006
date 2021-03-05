package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<School> items;

    private List<School> schoolList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

        schoolList = readSchoolData();
        schoolList = readCCAData(schoolList);
        schoolList = readSubjectData(schoolList);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter(getActivity(), schoolList);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.searchbar, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R .id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
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
}