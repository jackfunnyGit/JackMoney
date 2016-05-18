package com.asus.jack_tsai.jackmoney;

import android.content.Context;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment1 extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String text = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //final static

    //member field
    private CalendarView mCalendarView;
    private TextView mDaytextview, mMonthtextview;
    private int mDayBudget, mMonthBudget;
    private String mDate;
    private SharedPreferences mSp;
    private MonthCursorLoaderListiner mMonthCursorLoaderListiner;
    private DayCursorLoaderListiner mDayCursorLoaderListiner;
    private OnFragmentInteractionListener mListener;

    public BlankFragment1() {
        // Required empty public constructor
        mMonthCursorLoaderListiner = new MonthCursorLoaderListiner();
        mDayCursorLoaderListiner = new DayCursorLoaderListiner();
        Log.e("jackfunny ", "BlankFragment1() " + getId());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment1 newInstance(String param1, String param2, int IndicatorColor, int DividerColor, int IconId) {
        Log.e("jackfunny ", "BlankFragment1 newInstance() ");
        BlankFragment1 fragment = new BlankFragment1();

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
        Log.e("jackfunny", "blank1  : onCreate id= " + getId());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        Date date = new Date(System.currentTimeMillis());
        mDate = df.format(date);
        Log.e("jackfunny", "blank1  : onCreate mdate=" + mDate);
        ((SecondActivity) getActivity()).setDate(mDate);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("jackfunny", "blank1  : onCreateView id= " + getId());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_fragment1, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e("jackfunny", "blank1  :onSaveInstanceState id= " + getId());
        super.onSaveInstanceState(outState);
        outState.putLong("mDate", mCalendarView.getDate());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e("jackfunny", "blank1 onActivityCreated : id= " + getId());

        super.onActivityCreated(savedInstanceState);
        mSp = PreferenceManager.getDefaultSharedPreferences(getContext());
        mDayBudget = Integer.parseInt(mSp.getString(PrefFragment.PREFERENCE_EDITTEXT_DAYBUDGET, "0"));
        mMonthBudget = Integer.parseInt(mSp.getString(PrefFragment.PREFERENCE_EDITTEXT_MONTHBUDGET, "0"));
        Log.e("jackfunny", "blank1  : onActivityCreated preference mDayBudget = " + mDayBudget + " MonthBudget= " + mMonthBudget);
        mDaytextview = (TextView) getView().findViewById(R.id.daytextView);
        mDaytextview.setText(getString(R.string.Day_Budget));
        mDaytextview.append("" + mDayBudget);
        mMonthtextview = (TextView) getView().findViewById(R.id.monthtextView);
        mMonthtextview.setText(getString(R.string.Month_Budget));
        mMonthtextview.append("" + mMonthBudget);
        mCalendarView = (CalendarView) getView().findViewById(R.id.calendarView);
        getActivity().getSupportLoaderManager().initLoader(0, null, mMonthCursorLoaderListiner);
        getActivity().getSupportLoaderManager().initLoader(1, null, mDayCursorLoaderListiner);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.e("jackfunny", "Blank1 OnDateChange ");

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
                Date date = new Date(mCalendarView.getDate());
                mDate = df.format(date);

                //月份記得加一，因為一月是從0開始算
                //mDate = year + "-" + (month + 1) + "-" + dayOfMonth ;
                //Bundle bundle = new Bundle();
                //bundle.putString("date", mDate);
                ((SecondActivity) getActivity()).setDate(mDate);
                getActivity().getSupportLoaderManager().restartLoader(0, null, mMonthCursorLoaderListiner);
                getActivity().getSupportLoaderManager().restartLoader(1, null,mDayCursorLoaderListiner );

                //((SecondActivity) getActivity()).getActivityAdapter().notifyDataSetChanged();
                Log.e("jackfunny", "date: [" + mDate + "] today is = " + new Date(mCalendarView.getDate()));

            }
        });
        if (savedInstanceState!=null){
            Log.e("jackfunny", "blank1 onActivityCreated savedInstanceState!=null " + mDate);
            mCalendarView.setDate(savedInstanceState.getLong("mDate"));
        }
        Log.e("jackfunny", "blank1 onActivityCreated today is " + mDate);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("jackfunny", "blank1  : onPause " + getId());

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("jackfunny", "blank1  : onStop " + getId());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("jackfunny", "blank1  : onDestroyView " + getId());

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e("jackfunny", "blank1  : onAttach " + getId());
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.e("jackfunny", "blank1  : onDetach " + getId());
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

    private class DayCursorLoaderListiner implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            //String[] projection = {MoneyProvider._ID, MoneyProvider.NAME, MoneyProvider.DATE};
            Log.e("jackfunny", "Loader(blank1) onCreateLoader id= " + id);

            String selection = MoneyProvider.DATE + " = ?";
            String[] whereArgs = new String[]{
                    "" + ((SecondActivity) getActivity()).getDate()
            };
            /* String selection = MoneyProvider.DATE + " LIKE ?";
            String[] whereArgs = new String[]{
            "2016-05"+"%"
             };
               */
            return new CursorLoader(getContext(), MoneyProvider.CONTENT_URI, null, selection, whereArgs, null);

        }


        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            Log.e("jackfunny", "Loader(blank1)  onLoadFinished id=0");
            //mItemAdapter.changeCursor(cursor);
            //mItemAdapter.notifyDataSetChanged();//not update view without this
            int DayBudget = Integer.parseInt(mSp.getString(PrefFragment.PREFERENCE_EDITTEXT_DAYBUDGET, "0"));
            if (cursor.moveToFirst()) {
                Log.e("jackfunny", "Loader(blank1) onLoadFinished date = " + cursor.getString(cursor.getColumnIndex(MoneyProvider.PRICE)));
                do {
                    Log.e("jackfunny", "Loader(blank1) onLoadFinished id =0 price = " + cursor.getInt(cursor.getColumnIndex(MoneyProvider.PRICE)) + " Daybudget= " + DayBudget);
                    DayBudget = DayBudget - cursor.getInt(cursor.getColumnIndex(MoneyProvider.PRICE));


                } while (cursor.moveToNext());
            }
            mDaytextview.setText(getString(R.string.Day_Budget));
            mDaytextview.append("" + DayBudget);
            ((SecondActivity) getActivity()).getCursorAdapter().changeCursor(cursor);
            ((SecondActivity) getActivity()).getCursorAdapter().notifyDataSetChanged();//not update view without this
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {Log.e("jackfunny", "Loader(blank1) DayCursorLoader onLoaderReset ");}

    }

    private class MonthCursorLoaderListiner implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Log.e("jackfunny", "Loader(blank1) onCreateLoader id= " + id);
                SimpleDateFormat mf = new SimpleDateFormat("yyyy-MM", Locale.CHINESE);
                Date date = new Date(mCalendarView.getDate());
                String mdate = mf.format(date);
                String[] projection = {MoneyProvider.PRICE};
                String selection = MoneyProvider.DATE + " LIKE ?";
                String[] whereArgs = new String[]{
                        "" + mdate + "%"
                };
                return new CursorLoader(getContext(), MoneyProvider.CONTENT_URI, projection, selection, whereArgs, null);

        }


        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            Log.e("jackfunny", "Loader(blank1)  onLoadFinished id=1");
            int MonthBudget = Integer.parseInt(mSp.getString(PrefFragment.PREFERENCE_EDITTEXT_MONTHBUDGET, "0"));
            if (cursor.moveToFirst()) {
                do {
                    //Log.e("jackfunny", "Loader(blank1) onLoadFinished id =1 price = " + cursor.getInt(cursor.getColumnIndex(MoneyProvider.PRICE))+"monthbudget= "+mMonthBudget);
                    MonthBudget = MonthBudget - cursor.getInt(cursor.getColumnIndex(MoneyProvider.PRICE));


                } while (cursor.moveToNext());
            }
            mMonthtextview.setText(getString(R.string.Month_Budget));
            mMonthtextview.append("" + MonthBudget);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {Log.e("jackfunny", "Loader(blank1) MonthCursorLoader onLoaderReset ");}

    }




}



