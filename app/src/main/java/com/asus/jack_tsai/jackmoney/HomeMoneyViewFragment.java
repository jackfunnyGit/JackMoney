package com.asus.jack_tsai.jackmoney;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class HomeMoneyViewFragment extends Fragment {
    //static final field
    private static final String ACTION_ADD = "com.asus.jack_tsai.jackmoney.ACTION_ADD";
    private static final String ACTION_EDIT = "com.asus.jack_tsai.jackmoney.ACTION_EDIT";
    //member field
    private ListView mListView;
    private FloatingActionButton mFab;
    private ItemMoneyCursorAdapter mItemAdapter;
    private OnFragmentInteractionListener mListener;

    public HomeMoneyViewFragment() {
        Log.e("jackfunny ", "HomeMoneyViewFragment constructor getid()= " + getId());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public String getDateCallBack() {
        if (mListener != null) {
            return mListener.onGetDate();
        }
        return null;
    }

    public void setAdapterCallBack(ItemMoneyCursorAdapter ItemAdapter) {
        if (mListener != null) {
            mListener.onSetAdapter(ItemAdapter);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("jackfunny", "HomeMoneyViewFragment  : onCreate" + getId());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("jackfunny", "HomeMoneyViewFragment  : onCreateView" + getId());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.homemoney_view_fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.daylist);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("jackfunny", "HomeMoneyViewFragment onActivityCreated : " + getId());
        final Context context = getContext();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoneyAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(MoneyProvider.DATE, getDateCallBack());
                intent.setAction(ACTION_ADD);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mItemAdapter = new ItemMoneyCursorAdapter(getContext(), null, 0);
        setAdapterCallBack(mItemAdapter);
        mListView.setAdapter(mItemAdapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, final View view, final int
                    position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(context.getString(R.string.Dialog_Title))
                        .setMessage(context.getString(R.string.Dialog_Message))
                        .setPositiveButton(context.getString(R.string.Dialog_YES), new
                                DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        removeItem(view);

                                    }
                                })
                        .setNegativeButton(context.getString(R.string.Dialog_NO), new
                                DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                        .show();

                return true;
            }

        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                updateItem(view);

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("jackfunny", "HomeMoneyViewFragment  : onAttach" + getId());
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("jackfunny", "HomeMoneyViewFragment onResume");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void removeItem(View view) {
        Log.e("jackfunny", "removeItem by  id= " + view.getTag());
        Uri Uri_itempos = Uri.parse(String.format("%s/%s", MoneyProvider.URL, view.getTag()));
        int count = getActivity().getContentResolver().delete(Uri_itempos, null, null);
        Log.e("jackfunny", "removeItem count= " + count);
    }

    private void updateItem(View view) {
        Intent intent = new Intent(getActivity(), MoneyAddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MoneyProvider._ID, (int) view.getTag());
        bundle.putString(MoneyProvider.NAME, ((TextView) view.findViewById(R.id.item_Name))
                .getText().toString());
        bundle.putString(MoneyProvider.PRICE, ((TextView) view.findViewById(R.id.price)).getText
                ().toString());
        bundle.putString(MoneyProvider.CATEGORY, ((TextView) view.findViewById(R.id.category))
                .getText().toString());
        bundle.putString(MoneyProvider.MEMO, ((TextView) view.findViewById(R.id.memo)).getText()
                .toString());
        bundle.putString(MoneyProvider.DATE, ((TextView) view.findViewById(R.id.date)).getText()
                .toString());
        intent.setAction(ACTION_EDIT);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);

    }

    public interface OnFragmentInteractionListener {

        String onGetDate();

        void onSetAdapter(ItemMoneyCursorAdapter ItemAdapter);
    }

}
