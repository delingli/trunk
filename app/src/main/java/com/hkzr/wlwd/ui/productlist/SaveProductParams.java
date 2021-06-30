package com.hkzr.wlwd.ui.productlist;

import java.util.List;

public class SaveProductParams {
    public String tokenId;
    public String action;
    public Databean data;

    //    {"tokenId":"","data":{"chkdate":"2021-6-28","productlist":[null]},"action":"chk_saves"}
    public static class Databean {
        public String chkdate;
        public List<UploadProductParams> productlist;
    }
}
