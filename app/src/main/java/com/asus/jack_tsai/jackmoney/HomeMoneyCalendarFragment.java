package com.asus.jack_tsai.jackmoney;

import android.content.Context;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
 * {@link HomeMoneyCalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeMoneyCalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeMoneyCalendarFragment extends HomeMoneyBaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // the fragment initialization parameters
    private static final int DAYLOADER_ID =0;
    private static final int MONTHLOADER_ID =1;
    public static final SimpleDateFormat Dayformat;
    public static final SimpleDateFormat Monthformat;
   static {
       Monthformat =new SimpleDateFormat("yyyy-MM-", Locale.CHINESE);
       Dayformat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
   }

    //member field
    private CalendarView mCalendarView;
    private TextView mDaytextview, mMonthtextview;
    private int mDayBudget, mMonthBudget;
    private String mDate;
    private SharedPreferences mSp;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {

        void onSetDate(String date);
        ItemMoneyCursorAdapter ongetCursorAdapter();
    }
    public HomeMoneyCalendarFragment() {
        // Required empty public constructor
        Log.e("jackfunny ", "HomeMoneyCalendarFragment() " + getId());
    }

    public static HomeMoneyCalendarFragment newInstance(String title, int IndicatorColor, int DividerColor, int IconId) {
        Log.e("jackfunny ", "HomeMoneyCalendarFragment newInstance() ");
        HomeMoneyCalendarFragment fragment = new HomeMoneyCalendarFragment();
        fragment.setTitle(title);
        fragment.setIndicatorColor(IndicatorColor);
        fragment.setDividerColor(DividerColor);
        fragment.setIconResId(IconId);
        return fragment;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void SetDateCallBack(String date) {
        if (mListener != null) {
            mListener.onSetDate(date);
        }
    }
    public ItemMoneyCursorAdapter getCursorAdapterCallBack(){
        if (mListener != null) {
           return  mListener.ongetCursorAdapter();
        }
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("jackfunny", "HomeMoneyCalendarFragment  : onCreate id= " + getId());
        // set today's date to mDate for initialization
        mDate = Dayformat.format(new Date(System.currentTimeMillis()));
        Log.e("jackfunny", "HomeMoneyCalendarFragment  : onCreate mdate=" + mDate);
        SetDateCallBack(mDate);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("jackfunny", "HomeMoneyCalendarFragment  : onCreateView id= " + getId());
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.homemoney_calendar_fragment, container, false);
        mDaytextview = (TextView)view.findViewById(R.id.daytext_view);
        mMonthtextview = (TextView)view.findViewById(R.id.monthtext_view);
        mCalendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e("jackfunny", "HomeMoneyCalendarFragment  :onSaveInstanceState id= " + getId());
        super.onSaveInstanceState(outState);
        outState.putLong("mDate", mCalendarView.getDate());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("jackfunny", "HomeMoneyCalendarFragment onActivityCreated : id= " + getId());
        // read preference for day budget and month budget
        mSp = PreferenceManager.getDefaultSharedPreferences(getContext());
        mDayBudget = mSp.getInt(SettingPrefFragment.PREFERENCE_EDITTEXT_DAYBUDGET_KEY, 0);
        mMonthBudget = mSp.getInt(SettingPrefFragment.PREFERENCE_EDITTEXT_MONTHBUDGET_KEY, 0);
        Log.e("jackfunny", "HomeMoneyCalendarFragment  : onActivityCreated preference mDayBudget = " + mDayBudget + " MonthBudget= " + mMonthBudget);
        //initialize the views
        initView();
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.e("jackfunny", "HomeMoneyCalendarFragment OnDateChange ");
                mDate = Dayformat.format(new Date(mCalendarView.getDate()));
                SetDateCallBack(mDate);
                getActivity().getSupportLoaderManager().restartLoader(DAYLOADER_ID, null, HomeMoneyCalendarFragment.this);
                getActivity().getSupportLoaderManager().restartLoader(MONTHLOADER_ID, null,HomeMoneyCalendarFragment.this);

                //((HomeMoneyActivity) getActivity()).getActivityAdapter().notifyDataSetChanged();
                Log.e("jackfunny", "date: [" + mDate + "] today is = " + new Date(mCalendarView.getDate()));

            }
        });
        if (savedInstanceState!=null){
            Log.e("jackfunny", "HomeMoneyCalendarFragment onActivityCreated savedInstanceState !=null " + mDate);
            mCalendarView.setDate(savedInstanceState.getLong("mDate"));
        }
        Log.e("jackfunny", "HomeMoneyCalendarFragment onActivityCreated today is " + mDate);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("jackfunny", "HomeMoneyCalendarFragment  : onResume " + getId());
        //initialize cursorLoader one for dayloader the other for monthloader
        getActivity().getSupportLoaderManager().initLoader(DAYLOADER_ID, null, this);
        getActivity().getSupportLoaderManager().initLoader(MONTHLOADER_ID, null, this);
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.e("jackfunny", "HomeMoneyCalendarFragment  : onPause " + getId());

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("jackfunny", "HomeMoneyCalendarFragment  : onStop " + getId());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("jackfunny", "HomeMoneyCalendarFragment  : onDestroyView " + getId());

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e("jackfunny", "HomeMoneyCalendarFragment  : onAttach " + getId());
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.e("jackfunny", "HomeMoneyCalendarFragment  : onDetach " + getId());
        super.onDetach();
        mListener = null;
    }

    private void initView(){
        mDaytextview.setText(getString(R.string.Day_Budget));
        mDaytextview.append(String.format("%d", mDayBudget));
        mMonthtextview.setText(getString(R.string.Month_Budget));
        mMonthtextview.append(String.format("%d", mMonthBudget));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //String[] projection = {MoneyProvider._ID, MoneyProvider.NAME, MoneyProvider.DATE};
        Log.e("jackfunny", "Loader(HomeMoneyCalendarFragment) onCreateLoader id= " + id);
        switch (id) {
            case DAYLOADER_ID: {
                //to query the day item where the calendar date is picked
                String selection = String.format("%s = ?", MoneyProvider.DATE);
                String[] whereArgs = new String[]{
                        String.format("%s", mDate)
                };

                return new CursorLoader(getContext(), MoneyProvider.CONTENT_URI, null, selection, whereArgs, null);
            }
            case MONTHLOADER_ID: {
                //to query the whole month item where the calendar date is picked
                String mdate = Monthformat.format(new Date(mCalendarView.getDate()));
                String[] projection = {MoneyProvider.PRICE};
                String selection = MoneyProvider.DATE + " LIKE ?";
                String[] whereArgs = new String[]{
                        String.format("%s%%", mdate)
                };
                return new CursorLoader(getContext(), MoneyProvider.CONTENT_URI, projection, selection, whereArgs, null);

            }
            default:
                Log.e("jackfunny","CursorLoader has an unknown id");
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.e("jackfunny", "Loader(HomeMoneyCalendarFragment)  onLoadFinished id= "+loader.getId());
        //mItemAdapter.changeCursor(cursor);
        //mItemAdapter.notifyDataSetChanged();//not update view without this
        switch (loader.getId()) {
            case  DAYLOADER_ID: {
                int DayBudget =mSp.getInt(SettingPrefFragment.PREFERENCE_EDITTEXT_DAYBUDGET_KEY, 0);

                if (cursor.moveToFirst()) {

                    do {
                        Log.e("jackfunny", "Loader(HomeMoneyCalendarFragment) onLoadFinished id =0 price = " + cursor.getInt(cursor.getColumnIndex(MoneyProvider.PRICE)) + " Daybudget= " + DayBudget);
                        DayBudget = DayBudget - cursor.getInt(cursor.getColumnIndex(MoneyProvider.PRICE));
                    } while (cursor.moveToNext());
                }
                mDaytextview.setText(getString(R.string.Day_Budget));
                mDaytextview.append(String.format("%d", DayBudget));
                getCursorAdapterCallBack().changeCursor(cursor);
                getCursorAdapterCallBack().notifyDataSetChanged();//not update view without this
            }
            case  MONTHLOADER_ID: {
                int MonthBudget =mSp.getInt(SettingPrefFragment.PREFERENCE_EDITTEXT_DAYBUDGET_KEY, 0);
                if (cursor.moveToFirst()) {
                    do {

                        MonthBudget = MonthBudget - cursor.getInt(cursor.getColumnIndex(MoneyProvider.PRICE));

                    } while (cursor.moveToNext());
                }
                mMonthtextview.setText(getString(R.string.Month_Budget));
                mMonthtextview.append(String.format("%d", MonthBudget));


            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case DAYLOADER_ID:
                Log.e("jackfunny", "Loader(HomeMoneyCalendarFragment) DayCursorLoader onLoaderReset ");
                break;
            case MONTHLOADER_ID:
                Log.e("jackfunny", "Loader(HomeMoneyCalendarFragment) MonthCursorLoader onLoaderReset ");
                break;

        }

    }
}



