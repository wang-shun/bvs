package com.bizvisionsoft.bruiengine.assembly;

import org.bson.Document;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.BruiQueryEngine;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.tools.Check;
import com.mongodb.BasicDBObject;

public interface IQueryEnable extends IDataSetEngineProvider, IAssembly{

	public default void doQuery(BasicDBObject result) {
		setFilter(result);
		setCurrentPage(0);
		setSkip(0);
		BruiDataSetEngine dataSetEngine = getDataSetEngine();
		long cnt = dataSetEngine.count(getFilter(), getContext());
		setCount(cnt);
		setViewerInput();
	}
	
	public default void openQueryEditor() {
		Assembly config = getConfig();
		IBruiContext context = getContext();
		IBruiService bruiService = getBruiService();
		Assembly queryConfig = (Assembly) AUtil.simpleCopy(config, new Assembly());
		queryConfig.setType(Assembly.TYPE_EDITOR);
		queryConfig.setTitle("≤È—Ø");

		String bundleId = config.getQueryBuilderBundle();
		String classId = config.getQueryBuilderClass();
		Object input;
		if (Check.isAssigned(bundleId,classId)) {
			input = BruiQueryEngine.create(bundleId, classId, bruiService, context).getTarget();
		} else {
			input = new Document();
		}
		new Editor<Object>(queryConfig, context).setInput(true, input).ok((r, t) -> doQuery(r));
	}

	void setViewerInput();

	void setCount(long cnt);

	BasicDBObject getFilter();

	void setSkip(int i);

	void setCurrentPage(int i);

	void setFilter(BasicDBObject result);

}
