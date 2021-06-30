package com.hkzr.wlwd.ui.productwarehouse;

import com.hkzr.wlwd.ui.productlist.UploadProductParams;

import java.util.List;

public class SaveProductHouseParams {
    public String tokenId;
    public String action;
    public Databean data;

    //    {"tokenId":"","data":{"chkdate":"2021-6-28","productlist":[null]},"action":"chk_saves"}
    public static class Databean {
        public String chkdate;
        public String kfid;
        public List<UploadProductParams> productlist;
    }
}
