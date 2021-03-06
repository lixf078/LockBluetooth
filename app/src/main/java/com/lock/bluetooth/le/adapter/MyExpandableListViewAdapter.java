//package com.lock.bluetooth.le.adapter;
//
//import android.widget.BaseExpandableListAdapter;
//
///**
// * Created by admin on 2017/6/26.
// */
//
//public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
//
//    //  获得某个父项的某个子项
//    @Override
//    public Object getChild(int parentPos, int childPos) {
//        return dataset.get(parentList[parentPos]).get(childPos);
//    }
//
//    //  获得父项的数量
//    @Override
//    public int getGroupCount() {
//        return dataset.size();
//    }
//
//    //  获得某个父项的子项数目
//    @Override
//    public int getChildrenCount(int parentPos) {
//        return dataset.get(parentList[parentPos]).size();
//    }
//
//    //  获得某个父项
//    @Override
//    public Object getGroup(int parentPos) {
//        return dataset.get(parentList[parentPos]);
//    }
//
//    //  获得某个父项的id
//    @Override
//    public long getGroupId(int parentPos) {
//        return parentPos;
//    }
//
//    //  获得某个父项的某个子项的id
//    @Override
//    public long getChildId(int parentPos, int childPos) {
//        return childPos;
//    }
//
//    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    //  获得父项显示的view
//    @Override
//    public View getGroupView(int parentPos, boolean b, View view, ViewGroup viewGroup) {
//        return view;
//    }
//
//    //  获得子项显示的view
//    @Override
//    public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
//        return view;
//    }
//
//    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
//    @Override
//    public boolean isChildSelectable(int i, int i1) {
//        return false;
//    }
//}
