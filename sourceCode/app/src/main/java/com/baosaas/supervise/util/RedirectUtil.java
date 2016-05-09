package com.baosaas.supervise.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baosaas.supervise.activity.InventoryActivity;
import com.baosaas.supervise.activity.LoginActivity;


/**
 */
public class RedirectUtil {

    public static void go2Login(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
    //跳转到盘库状态
    public static void go2InventryDetail(Context activity, String billNo) {
        Intent intent = new Intent(activity, InventoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("billNo", billNo);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /*public static void go2SrearchPacknum(String title, Activity activity) {
        //搜索已解押捆包
        Intent intent = new Intent(activity, SearchUnboundpackNumActivity.class);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    public static void go2SrearchNumAudits(String title, Activity activity) {
        //搜索已审核捆包
        Intent intent = new Intent(activity, SearchAuditspackNumActivity.class);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    public static void go2PledgePacknum(String title, Activity activity) {
        Intent intent = new Intent(activity, SearchImpawnspackNumActivity.class);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    public static void go2Menu(Activity activity) {
        Intent intent = new Intent(activity, MenuActivity.class);
        activity.startActivity(intent);
    }

    public static void go2Select(Activity activity) {
        Intent intent = new Intent(activity, SelectActivity.class);
        activity.startActivity(intent);
    }


    public static void go2InventoryList(Activity activity) {
        //盘库
        Intent intent = new Intent(activity, NewInventoryListActivity.class);
        activity.startActivity(intent);
    }




    public static void go2Unbounddetail(Context activity, Unbound unbound) {
        Intent intent = new Intent(activity, UnboundDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("unbound", unbound);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void go2UnbounddetailVis(Context activity, Unbound unbound, String status) {
        Intent intent = new Intent(activity, UnboundDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Tag", status);
        bundle.putSerializable("unbound", unbound);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void go2AuditdetailVis(Context activity, Audits audits, String packNum) {
        //跳转到质押货物详情
        Intent intent = new Intent(activity, UnboundAuditsDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("packNum", packNum);
        bundle.putSerializable("audits", audits);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    public static void go2Unbounddetail(Context activity, Unbound unbound, String packNum) {
        Intent intent = new Intent(activity, UnboundDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("unbound", unbound);
        bundle.putString("packNum", packNum);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    //跳转到某个仓库  （捆包）
    public static void go2childunbound(Context activity, Unbound unbound, String wareHouse) {
        Intent intent = new Intent(activity, NewUnboundListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("unbound", unbound);
        bundle.putString("warehouse", wareHouse);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    //跳转到质押列表（某仓库下）
    public static void go2childimpawns(Context activity, Impawns impawns, String wareHouse) {
        Intent intent = new Intent(activity, ImpawnsListActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putSerializable("impawns", impawns);
        bundle.putString("impwarehouse", wareHouse);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    //跳转到注册审核列表（某仓库下）
    public static void go2childAuditList(Context activity, String wareHouse) {
        Intent intent = new Intent(activity, AuditListActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putSerializable("impawns", impawns);
        bundle.putString("audwarehouse", wareHouse);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void go2uploadchildunboundlist(Context activity, Unbound unbound, String wareHouse) {
        Intent intent = new Intent(activity, UploadUnboundActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("unbound", unbound);
        bundle.putString("warehouse", wareHouse);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void go2uploadchildAudit(Context activity, String wareHouse) {
        Intent intent = new Intent(activity, UploadAuditsActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putSerializable("unbound", unbound);
        bundle.putString("warehouse", wareHouse);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    public static void go2uploadchildimpawnslist(Context activity, Impawns impawns, String wareHouse) {
        Intent intent = new Intent(activity, UploadImpawnsListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("unbound", impawns);
        bundle.putString("warehouse", wareHouse);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    public static void go2newunboundlist(Context activity, Unbound unbound, String packNum) {
        Intent intent = new Intent(activity, WarehouseUnboundListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("unbound", unbound);
        bundle.putString("packNum", packNum);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

//    public static void go2DetailsActivity(Context activity,Unbound unbound,String packNum) {
//        Intent intent = new Intent(activity, UnboundDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("unbound", unbound);
//        bundle.putString("packNum", packNum);
//        intent.putExtras(bundle);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        activity.startActivity(intent);
//    }

    public static void go2UnboundList(Activity activity) {
//        Intent intent = new Intent(activity, AllUnboundListActivity.class);
        //解押
        Intent intent = new Intent(activity, WarehouseUnboundListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

//    public static void go2Unbound(Activity activity) {
//        Intent intent = new Intent(activity, UnboundActivity.class);
//        activity.startActivity(intent);
//    }

    public static void go2UploadOperation(Activity activity) {
        //上传
        Intent intent = new Intent(activity, UploadOperationActivity.class);
        activity.startActivity(intent);
    }

    public static void go2UploadImpawns(Activity activity) {
        //质押
        Intent intent = new Intent(activity, WarehouseImpawnsListActivity.class);
        activity.startActivity(intent);
    }

    public static void go2RegisterAudits(Activity activity) {
        //注册审核
        Intent intent = new Intent(activity, WarehouseAuditListActivity.class);
        activity.startActivity(intent);
    }


//    public static void go2UploadInventory(Activity activity) {
//        Intent intent = new Intent(activity, UploadInventoryActivity.class);
//        activity.startActivity(intent);
//    }

    public static void go2UploadUnbound(Activity activity) {
        Intent intent = new Intent(activity, UploadWarehouseActivity.class);
        activity.startActivity(intent);
    }

    public static void go2productdetail(Context activity, String tag) {
        Intent intent = new Intent(activity, UnboundDetailsActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putSerializable("unbound", unbound);
        bundle.putString("Tag", tag);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }

    public static void go2Auditdetail(Context activity, String num) {
        Intent intent = new Intent(activity, AuditDetailsActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putSerializable("unbound", unbound);
        bundle.putString("AuditPackNum", num);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }

    public static void go2productImpawns(Context activity, String packNum, String position) {
        Intent intent = new Intent(activity, ImpawnsItemActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putSerializable("unbound", unbound);
        bundle.putString("PackNum", packNum);
        bundle.putString("Position", position);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }*/
}
