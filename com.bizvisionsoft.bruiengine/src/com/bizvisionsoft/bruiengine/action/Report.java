package com.bizvisionsoft.bruiengine.action;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.rap.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.mongocodex.tools.BsonTools;
import com.bizvisionsoft.service.tools.Check;

public class Report {

	public Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private IBruiService br;

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT_INPUT_OBJECT) Object input_obj,
			@MethodParam(Execute.CONTEXT_SELECTION) List<?> selection,
			@MethodParam(Execute.CONTEXT_SELECTION_1ST) Object selected,
			@MethodParam(Execute.PAGE_CONTEXT_INPUT_OBJECT) Object page_input,
			@MethodParam(Execute.ROOT_CONTEXT_INPUT_OBJECT) Object root_input,
			@MethodParam(Execute.CURRENT_USER_ID) String userId, @MethodParam(Execute.ACTION) Action action) {

		JsonObject command = new JsonObject()//
				.set("jq", action.getReportJQ())//
				.set("outputType", action.getReportOutputType())//
				.set("template", action.getReportTemplate())//
				.set("fileName", action.getReportFileName())//
				.set("input_obj_id", getParam(input_obj))//
				.set("selected_id", getParam(selected))//
				.set("page_input_id", getParam(page_input))//
				.set("root_input_id", getParam(root_input))//
		// .append("selection_id_list", getListParam(selection))//
		;
		UserSession.bruiToolkit().downloadServerFile("report/command", new JsonObject().set("command", command.toString()));// обть

	}

	// private ArrayList<String> getListParam(List<?> selection) {
	// return Optional.ofNullable(selection).orElse(new ArrayList<>()).stream()
	// .map(this::getParam).collect(Collectors.toCollection(ArrayList::new));
	// }

	private String getParam(Object obj) {
		if (obj != null)
			return Check.instanceOf(BsonTools.getBson(obj).get("_id"), ObjectId.class).map(_id -> _id.toString())
					.orElse("");
		return "";
	}

}
