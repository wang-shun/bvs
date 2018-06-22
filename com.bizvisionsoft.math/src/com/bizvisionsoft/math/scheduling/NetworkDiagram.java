package com.bizvisionsoft.math.scheduling;

import java.util.ArrayList;
import java.util.Arrays;
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
		start.setES(0);
		start.setEF(0);
		tasks.forEach(t -> calculateEarlestStart(t));

		// 2. 修正end
		Set<Task> exclude = new HashSet<Task>();
		exclude.add(start);
		exclude.add(end);
		ergodic(end, true, route -> exclude.add(route.end1));
		tasks.parallelStream().filter(task -> !exclude.contains(task) && task.getEF() > end.getEF()).forEach(t -> {
			virtualRoute.add(new Route(t, end, true, new Relation(Relation.FTF, 0)));
			end.updateES(t.getEF());
		});
		routes.addAll(virtualRoute);
		// 清空
		virtualRoute.clear();

		// 3. 求LS,LF
		end.setLS(end.getES());
		end.setLF(end.getEF());
		start.setLS(0);
		start.setLF(0);
		tasks.forEach(t -> calculateLatestFinish(t));

		// 4. 求LAG
		routes.forEach(r -> calculateLAG(r));

		// 5.求TF,FF
		tasks.forEach(t -> calculateTF(t));
		end.setTF(0);
		end.setFF(0);
		start.setTF(0);
		start.setFF(0);

		// 6.求总工期
		T = end.getLF();

		System.out.println("\n============================排程/工期计算============================");
		System.out.println("工作排程-------------------------------");
		tasks.forEach(t -> System.out.println(t));
		System.out.println("网络图-----------------------------");
		routes.forEach(r -> System.out.println(r));
		System.out.println("总工期-------------------------------");
		System.out.println(T);
		System.out.println("=================================================================");

		return tasks;
	}

	private void calculateTF(Task task) {
		task.setTF(task.getLS() - task.getES());
		ergodic(task, false, route -> {
			task.updateFF(route.LAG);
		});
	}

	private void calculateLAG(Route route) {
		route.relations.forEach(rela -> {
			float lag = 0;
			if (rela.type == Relation.FTS) {
				lag = route.end2.getES() - (route.end1.getEF() + rela.interval);
			} else if (rela.type == Relation.STS) {
				lag = route.end2.getES() - (route.end1.getES() + rela.interval);
			} else if (rela.type == Relation.FTF) {
				lag = route.end2.getEF() - (route.end1.getEF() + rela.interval);
			} else if (rela.type == Relation.STF) {
				lag = route.end2.getEF() - (route.end1.getES() + rela.interval);
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
				if (route.end2.getLS() == -1) {
					calculateLatestFinish(route.end2);
				}
				float lf = 0;
				if (rela.type == Relation.FTS) {
					lf = route.end2.getLS() - rela.interval;
				} else if (rela.type == Relation.FTF) {
					lf = route.end2.getLF() - rela.interval;
				} else if (rela.type == Relation.STF) {
					lf = route.end2.getLF() - rela.interval + task.getD();
				} else if (rela.type == Relation.STS) {
					lf = route.end2.getLS() - rela.interval + task.getD();
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
				if (route.end1.getES() == -1) {
					calculateEarlestStart(route.end1);
				}
				float es = 0;
				if (rela.type == Relation.FTS) {
					es = route.end1.getEF() + rela.interval;
				} else if (rela.type == Relation.FTF) {
					es = route.end1.getEF() + rela.interval - task.getD();
				} else if (rela.type == Relation.STF) {
					es = route.end1.getES() + rela.interval - task.getD();
				} else if (rela.type == Relation.STS) {
					es = route.end1.getES() + rela.interval;
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
		return tasks.parallelStream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
	}

	/**
	 * 根据输入的任务和搭接关系 获得网络图的连通子图
	 * 
	 * @param tasks
	 * @param routes
	 * @return
	 */
	public static List<NetworkDiagram> listConnectedSubgraphic(List<Task> tasks, List<Route> routes) {
		List<NetworkDiagram> result = new ArrayList<NetworkDiagram>();
		for (int i = 0; i < routes.size(); i++) {
			Route route = routes.get(i);
			ArrayList<Route> _routes = new ArrayList<Route>();
			_routes.addAll(searchNextFromRoute(routes, route.end1));
			_routes.add(route);
			_routes.addAll(searchNextToRoute(routes, route.end2));

			NetworkDiagram net = result.stream().filter(nd -> {
				ArrayList<Route> temp = new ArrayList<>(nd.routes);
				temp.retainAll(_routes);
				return temp.size() > 0;
			}).findFirst().orElse(null);
			if (net == null) {
				ArrayList<Task> _tasks = new ArrayList<Task>();
				_routes.forEach(r -> {
					if (!_tasks.contains(r.end1))
						_tasks.add(r.end1);
					if (!_tasks.contains(r.end2))
						_tasks.add(r.end2);
				});
				result.add(new NetworkDiagram(new ArrayList<Task>(_tasks), _routes));
			} else {
				_routes.forEach(r -> {
					if (!net.tasks.contains(r.end1))
						net.tasks.add(r.end1);
					if (!net.tasks.contains(r.end2))
						net.tasks.add(r.end2);
				});
				net.routes.addAll(_routes);
			}
		}
		return result;
	}

	private static List<Route> searchNextFromRoute(List<Route> routes, Task to) {
		ArrayList<Route> result = new ArrayList<>();
		List<Route> from = Arrays.asList(routes.stream().filter(r -> r.end2.equals(to)).toArray(Route[]::new));
		if (!from.isEmpty()) {
			result.addAll(from);
			from.forEach(r -> result.addAll(searchNextFromRoute(routes, r.end1)));
		} else {
			result.add(new Route(Task.startTask(), to));
		}
		return result;
	}

	private static List<Route> searchNextToRoute(List<Route> routes, Task from) {
		ArrayList<Route> result = new ArrayList<>();
		List<Route> to = Arrays.asList(routes.stream().filter(r -> r.end1.equals(from)).toArray(Route[]::new));
		if (!to.isEmpty()) {
			result.addAll(to);
			to.forEach(r -> result.addAll(searchNextToRoute(routes, r.end2)));
		} else {
			result.add(new Route(from, Task.endTask()));
		}
		return result;
	}

}
