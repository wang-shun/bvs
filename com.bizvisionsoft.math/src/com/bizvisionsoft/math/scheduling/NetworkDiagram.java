package com.bizvisionsoft.math.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class NetworkDiagram {

	public final List<Task> tasks;

	public final List<Route> routes;

	private Task start;

	private Task end;

	private ArrayList<Route> virtualRoute;

	public float T = 0;


	public NetworkDiagram(List<Task> tasks, List<Route> routes) {

		this.tasks = new ArrayList<Task>();
		this.tasks.addAll(tasks);
		Collections.sort(this.tasks);
		
		this.routes = new ArrayList<Route>();
		this.routes.addAll(routes);


		// check start end.
		// 初始化
		start = getTask(Task.START);
		end = getTask(Task.END);
	}


	public List<Task> schedule() {

		virtualRoute = new ArrayList<Route>();

		// 1. 计算ES, EF
		start.ES = 0;
		start.EF = 0;
		tasks.forEach(t -> calculateEarlestStart(t));

		// 2. 修正end
		Set<Task> exclude = new HashSet<Task>();
		exclude.add(start);
		exclude.add(end);
		ergodic(end, true, route -> exclude.add(route.end1));
		tasks.parallelStream().filter(task -> !exclude.contains(task) && task.EF > end.EF).forEach(t -> {
			virtualRoute.add(new Route(t, end, true, new Relation(Relation.FTF, 0)));
			end.updateES(t.EF);
		});
		routes.addAll(virtualRoute);
		//清空
		virtualRoute.clear();

		// 3. 求LS,LF
		end.LS = end.ES;
		end.LF = end.EF;
		start.LS = 0;
		start.LF = 0;
		tasks.forEach(t -> calculateLatestFinish(t));

		// 4. 求LAG
		routes.forEach(r -> calculateLAG(r));

		// 5.求TF,FF
		tasks.forEach(t -> calculateTF(t));
		end.TF = 0;
		end.FF = 0;
		start.TF = 0;
		start.FF = 0;

		// 6.求总工期
		T = end.LF;
		
		System.out.println("\n============================排程/工期计算============================");
		System.out.println("工作排程-------------------------------");
		tasks.forEach(t->System.out.println(t));
		System.out.println("网络图-----------------------------");
		routes.forEach(r -> System.out.println(r));
		System.out.println("总工期-------------------------------");
		System.out.println(T);
		System.out.println("=================================================================");
		
		
		return tasks;
	}

	private void calculateTF(Task task) {
		task.TF = task.LS - task.ES;
		ergodic(task, false, route -> {
			task.updateFF(route.LAG);
		});
	}

	private void calculateLAG(Route route) {
		route.relations.forEach(rela -> {
			float lag = 0;
			if (rela.type == Relation.FTS) {
				lag = route.end2.ES - (route.end1.EF + rela.interval);
			} else if (rela.type == Relation.STS) {
				lag = route.end2.ES - (route.end1.ES + rela.interval);
			} else if (rela.type == Relation.FTF) {
				lag = route.end2.EF - (route.end1.EF + rela.interval);
			} else if (rela.type == Relation.STF) {
				lag = route.end2.EF - (route.end1.ES + rela.interval);
			}
			route.updateLAG(lag);
		});
	}

	private void calculateLatestFinish(Task task) {
		if (task.equals(end) || task.equals(start)) {
			return;
		}
		ergodic(task, false, route -> {
			route.relations.forEach(rela -> {
				if (route.end2.LS == -1) {
					calculateLatestFinish(route.end2);
				}
				float lf = 0;
				if (rela.type == Relation.FTS) {
					lf = route.end2.LS - rela.interval;
				} else if (rela.type == Relation.FTF) {
					lf = route.end2.LF - rela.interval;
				} else if (rela.type == Relation.STF) {
					lf = route.end2.LF - rela.interval + task.D;
				} else if (rela.type == Relation.STS) {
					lf = route.end2.LS - rela.interval + task.D;
				}
				task.updateLF(lf);
			});
		});

	}

	private void calculateEarlestStart(Task task) {
		if (task.equals(start)) {
			return;
		}
		ergodic(task, true, route -> {
			route.relations.forEach(rela -> {
				if (route.end1.ES == -1) {
					calculateEarlestStart(route.end1);
				}
				float es = 0;
				if (rela.type == Relation.FTS) {
					es = route.end1.EF + rela.interval;
				} else if (rela.type == Relation.FTF) {
					es = route.end1.EF + rela.interval - task.D;
				} else if (rela.type == Relation.STF) {
					es = route.end1.ES + rela.interval - task.D;
				} else if (rela.type == Relation.STS) {
					es = route.end1.ES + rela.interval;
				}
				if (es < 0) {
					es = 0;
					Route vr = new Route(start, task, true, new Relation(Relation.STS, 0));
					if (virtualRoute.contains(vr))
						virtualRoute.add(vr);
				}
				task.updateES(es);
			});
		});
	}

	/**
	 * 从某个任务遍历网络图
	 * 
	 * @param from
	 *            起点
	 * @param reverse
	 *            方向
	 * @param action
	 */
	public void ergodic(Task from, boolean reverse, Consumer<? super Route> action) {
		if (reverse) {
			routes.stream().filter(f -> f.end2.equals(from)).forEach(action);
		} else {
			routes.stream().filter(f -> f.end1.equals(from)).forEach(action);
		}
	}

	/**
	 * 获得某个任务
	 * 
	 * @param id
	 *            任务的Id
	 * @return
	 */
	public Task getTask(String id) {
		return tasks.parallelStream().filter(t -> t.id.equals(id)).findFirst().orElse(null);
	}


}
