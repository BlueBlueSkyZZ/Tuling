package com.hhuc.sillyboys.tuling.navi_fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.dinuscxj.itemdecoration.ShaderItemDecoration;
import com.hhuc.sillyboys.tuling.R;
import com.hhuc.sillyboys.tuling.adapter.RoundImgAdapter;
import com.hhuc.sillyboys.tuling.broadcast.BroadcastActivity;
import com.hhuc.sillyboys.tuling.broadcast.BroadcastActivity2;
import com.hhuc.sillyboys.tuling.chat.ChatAcitivity;
import com.hhuc.sillyboys.tuling.search.SearchActivity;
import com.hhuc.sillyboys.tuling.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ThirdFragment extends Fragment {
    private static final String TAG = "thirdFragment";
    private RecyclerView mRecyclerView;
    private RoundImgAdapter mAdapter;
    private List<String> subject,description,pictures;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int selfId = 0;
    private StringBuffer sb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navi_fragment_third, container, false);
        Log.d(TAG, "onCreatView");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        TextView toolbarText = (TextView)getActivity().findViewById(R.id.toolbar_text);
        toolbarText.setText("外卖专区");
        initDatas();
        init();

    }

    private void init(){
        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.third_recyclerView);
        // 设置布局
        mAdapter = new RoundImgAdapter(getActivity(),subject,description,pictures);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // 设置Item之间的分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        ShaderItemDecoration shaderItemDecoration = new ShaderItemDecoration(getActivity(),
                ShaderItemDecoration.SHADER_BOTTOM | ShaderItemDecoration.SHADER_TOP);
        shaderItemDecoration.setShaderTopDistance(1);
        shaderItemDecoration.setShaderBottomDistance(1);
        mRecyclerView.addItemDecoration(shaderItemDecoration);

        // 设置增加删除的动画效果
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // 接口回调，设置点击事件
        mAdapter.setOnItemClickListener(new RoundImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 跳转对应的广播
//                String compactId = description.get(position);
//                Intent chatIntent = new Intent(getActivity(), ChatAcitivity.class);
//                chatIntent.putExtra("compactId", compactId).putExtra("cname", subject.get(position));
//                Log.d(TAG, "用户选择了频道: " + compactId);
//                startActivity(chatIntent);
                String compactId = description.get(position);
                Intent broadcastIntent = new Intent(getActivity(), BroadcastActivity.class);
                broadcastIntent.putExtra("compactId", compactId)
                            .putExtra("cname", subject.get(position))
                            .putExtra("type" , "chat");
                Log.d(TAG, "用户选择了频道: " + compactId);
                startActivity(broadcastIntent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });

        // 顶部搜索
        RecyclerViewHeader header = RecyclerViewHeader.fromXml(getActivity(),R.layout.navi_header);
        header.attachTo(mRecyclerView);
        TextView searchInfo = (TextView) getActivity().findViewById(R.id.search_info);
        searchInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchBroadcastIntent = new Intent(getActivity(), SearchActivity.class);
                searchBroadcastIntent.putExtra("search_type", "search_shop");
                startActivity(searchBroadcastIntent);
            }
        });
    }

    // 样例频道
    private void initDatas() {
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = pref.edit();
        selfId = pref.getInt("selfid", 0);    // 用户id

        subject = new ArrayList<String>(Arrays.asList("黄焖鸡米饭","宝食林","老潼关肉夹馍","平价饭店"));
        sb = new StringBuffer();
        Iterator<String> i = subject.iterator();
        while(i.hasNext()){
            sb.append(i.next().toString() + ";");
        }
        editor.putString("shopChannels", sb.toString());
        editor.commit();
        description = new ArrayList<String>(Arrays.asList("","","",""));
        pictures = new ArrayList<String>(Arrays.asList(
                "" + resourceIdToUri(getActivity(), R.drawable.shop_huangmenji),
                "" + resourceIdToUri(getActivity(), R.drawable.shop_baoshilin),
                "" + resourceIdToUri(getActivity(), R.drawable.shop_roujiamo),
                "" + resourceIdToUri(getActivity(), R.drawable.shop_pingjiafandian)
                ));
    }

    private static final String ANDROID_RESOURCE = "android.resource://";
    private static final String FOREWARD_SLASH = "/";
    private static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

}
