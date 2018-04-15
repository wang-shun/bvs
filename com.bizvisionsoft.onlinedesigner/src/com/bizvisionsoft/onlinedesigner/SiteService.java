package com.bizvisionsoft.onlinedesigner;

import java.util.ArrayList;
import java.util.List;

import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Site;

public class SiteService {

	@DataSet("站点结构/" + DataSet.LIST)
	private List<Site> getSite() {
		ArrayList<Site> result = new ArrayList<>();
		result.add(ModelLoader.site);
		return result;
	}

	@DataSet("站点结构/" + DataSet.COUNT)
	private long countSite() {
		return 1;
	}
	
	@DataSet("组件选择器/" + DataSet.LIST)
	private List<Assembly> getAssmebly(){
		return ModelLoader.site.getAssyLib().getAssys();
	}

	@DataSet("组件选择器/" + DataSet.COUNT)
	private long countAssmebly() {
		return ModelLoader.site.getAssyLib().getAssys().size();
	}

	
}
