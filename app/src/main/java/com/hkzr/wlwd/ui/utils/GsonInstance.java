package com.hkzr.wlwd.ui.utils;

import com.google.gson.Gson;

public class GsonInstance {
	static private Gson gson = null;

	static public  Gson gson() {
		if (gson == null)
			gson = new Gson();

		return gson;
	}
}
