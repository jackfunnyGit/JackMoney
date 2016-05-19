package com.asus.jack_tsai.jackmoney;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeMoneyViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeMoneyViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeMoneyViewFragment extends HomeMoneyBaseFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String Dialog_title="delete item";
    private static final String Dialog_message="Are you sure to delete?";
    private static final String Dialog_yes="Yes";
    private static final String Dialog_no="No";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView mListView;
    private FloatingActionButton mFab;
    private ItemMoneyCursorAdapter mItemAdapter;


    private OnFragmentInteractionListener mListener;

    public HomeMoneyViewFragment() {
        // Required empty public constructor
        Log.e("jackfunny ", "HomeMoneyViewFragment() " + getId());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeMoneyViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeMoneyViewFragment newInstance(String param1, String param2,int IndicatorColor,int DividerColor,int IconId) {
        Log.e("jackfunny ", "HomeMoneyViewFragment newInstance() ");
        HomeMoneyViewFragment fragment = new HomeMoneyViewFragment();
        Bundle args = new Bundle();
        fragment.setTitle(param1);
        fragment.setIndicatorColor(IndicatorColor);
        fragment.setDividerColor(DividerColor);
        fragment.setIconResId(IconId);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("jackfunny", "blank2  : onCreate" + getId());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("jackfunny", "blank2  : onCreateView" + getId());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.homemoney_view_fragment, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.e("jackfunny", "blank2 onActivityCreated : " + getId());
        mListView = (ListView)getView().findViewById(R.id.daylist);
        mFab= (FloatingActionButton) getView().findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoneyAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(MoneyAddActivity.DATE, ((HomeMoneyActivity) getActivity()).getDate());
                intent.putExtra(MoneyAddActivity.ACTION, MoneyAddActivity.ACTION_ADD);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

        mItemAdapter = new ItemMoneyCursorAdapter(getContext(), null,0);
        ((HomeMoneyActivity)getActivity()).setCursorAdapter(mItemAdapter);
        mListView .setAdapter(mItemAdapter);
        mListView .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, final View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(Dialog_title)
                        .setMessage(Dialog_message)
                        .setPositiveButton(Dialog_yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                removeItem(position, view);
                                String selection = MoneyProvider.DATE + " = ?";
                                String[] whereArgs = new String[]{
                                        "" + ((HomeMoneyActivity) getActivity()).getDate()
                                };

                               /* Cursor c = getActivity().getContentResolver().query(Uri.parse(MoneyProvider.URL), null, selection, whereArgs, null);
                                mItemAdapter.changeCursor(c);
                                mItemAdapter.notifyDataSetChanged();
                                */
                            }
                        })
                        .setNegativeButton(Dialog_no, new DialogInterface.OnClickListener() {

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
                updateItem(position, view);

            }
        });
        LoaderManager.enableDebugLogging(true);


    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("jackfunny", "blank2  : onAttach" + getId());
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("jackfunny", "blank2 onResume" );
        Uri Uri_Table1 = Uri.parse(MoneyProvider.URL);
        String[] projection = new String[]{MoneyProvider.NAME,MoneyProvider.PRICE,MoneyProvider._ID};
        //String selection=MoneyProvider.DATE+" = "+((HomeMoneyActivity)getActivity()).getDate();
        /*String selection=MoneyProvider.DATE+" = ?";
        String[] whereArgs = new String[] {
                ""+((HomeMoneyActivity) getActivity()).getDate()
        };


        Cursor c =  getActivity().getContentResolver().query(Uri_Table1, null, selection, whereArgs, null);
        Log.e("jackfunny", "" + c.getColumnIndex(MoneyProvider._ID));
        Log.e("jackfunny",""+c.getColumnIndex(MoneyProvider.NAME));
        Log.e("jackfunny",""+c.getColumnIndex(MoneyProvider.PRICE));
        if (c.moveToFirst()) {
            do{

                Log.e("jackfunny",c.getString(c.getColumnIndex(MoneyProvider._ID)) +
                        ", " +  c.getString(c.getColumnIndex( MoneyProvider.NAME)) +
                        ", " + c.getString(c.getColumnIndex( MoneyProvider.PRICE))+
                        ", " + c.getString(c.getColumnIndex( MoneyProvider.DATE))
                );
            } while (c.moveToNext());
        }
        */





    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void removeItem(int position,View view) {
        Log.e("jackfunny", "removeItem by  id= " + view.getTag());
        Uri Uri_itempos = Uri.parse(MoneyProvider.URL + "/" + view.getTag());
        int count = getActivity().getContentResolver().delete(Uri_itempos, null, null);
        Log.e("jackfunny", "removeItem count= " + count);
    }

    public void updateItem(int position,View view){
        Intent intent = new Intent(getActivity(),MoneyAddActivity.class);
        Bundle bundle =new Bundle();
        bundle.putInt(MoneyAddActivity._ID, (int) view.getTag());
        bundle.putString(MoneyAddActivity.NAME, ((TextView) view.findViewById(R.id.Item_Name)).getText().toString());
        bundle.putString(MoneyAddActivity.PRICE, ((TextView) view.findViewById(R.id.price)).getText().toString());
        bundle.putString(MoneyAddActivity.CATEGORY, ((TextView) view.findViewById(R.id.Category)).getText().toString());
        bundle.putString(MoneyAddActivity.MEMO, ((TextView) view.findViewById(R.id.Memo)).getText().toString());
        bundle.putString(MoneyAddActivity.DATE,((TextView)view.findViewById(R.id.Date)).getText().toString());
        intent.putExtra(MoneyAddActivity.ACTION, MoneyAddActivity.ACTION_EDIT);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);

    }

   public void updateListView(){
       /*String date=((HomeMoneyActivity) getActivity()).getDate();
       String selection=MoneyProvider.DATE+" = ?";
       String[] whereArgs = new String[] {
               ""+((HomeMoneyActivity) getActivity()).getDate()
       };
       Uri Uri_Table1 = Uri.parse(MoneyProvider.URL);
       Cursor c =  getActivity().getContentResolver().query(Uri_Table1, null, selection, whereArgs, null);


       mItemAdapter.changeCursor(c);
       mItemAdapter.notifyDataSetChanged();
       */
   }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //String[] projection = {MoneyProvider._ID, MoneyProvider.NAME, MoneyProvider.DATE};
        Log.e("jackfunny","Loader onCreateLoader id="+id);
        String selection=MoneyProvider.DATE+" = ?";
        String[] whereArgs = new String[] {
                ""+((HomeMoneyActivity) getActivity()).getDate()
        };
        return new CursorLoader(getContext(), MoneyProvider.CONTENT_URI, null, selection, whereArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.e("jackfunny","Loader onLoadFinished ");
        mItemAdapter.changeCursor(cursor);
        mItemAdapter.notifyDataSetChanged();//not update view without this
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
