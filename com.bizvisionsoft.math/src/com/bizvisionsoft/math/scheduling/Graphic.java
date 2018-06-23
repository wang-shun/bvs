package com.bizvisionsoft.math.scheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graphic {

	private List<NetworkDiagram> nets;
	private List<Task> tasks;
	private float T;

	public Graphic(List<Task> tasks, List<Route> routes) {
		this.tasks = tasks;
		nets = listConnectedSubgraphic(tasks, routes);
	}

	private <T> void addToList(List<T> src, T target) {
		if (!src.contains(target)) {
			src.add(target);
		}
	}

	/**
	 * ����ͼ�Ķ����ͨ��ͼ
	 * 
	 * @param tasks
	 * @param routes
	 * @return
	 */
	public List<NetworkDiagram> listConnectedSubgraphic(List<Task> inputTasks, List<Route> inputRoutes) {
		final List<Route> routes = new ArrayList<>(inputRoutes);
		final List<Task> tasks = new ArrayList<>(inputTasks);
		// ·�ɽ���
		inputTasks.stream().filter(t -> !t.getSubTasks().isEmpty()).forEach(sum -> {
			// �������sumTaskd������
			inputRoutes.stream().filter(r -> sum.equals(r.end2)).forEach(in -> {
				linkToLeafTask(sum, in, routes);
				routes.remove(in);
			});
			// �������sumTaskd������
			inputRoutes.stream().filter(r -> sum.equals(r.end1)).forEach(out -> {
				linkFromLeafTask(sum, out, routes);
				routes.remove(out);
			});
		});

		List<NetworkDiagram> result = new ArrayList<NetworkDiagram>();
		// ������ͼ
		for (int i = 0; i < routes.size(); i++) {
			Route route = routes.get(i);
			final ArrayList<Route> _routes = new ArrayList<Route>();
			_routes.add(route);
			searchNextFromRoute(routes, route.end1).forEach(r -> addToList(_routes, r));
			searchNextToRoute(routes, route.end2).forEach(r -> addToList(_routes, r));

			NetworkDiagram net = result.stream().filter(nd -> {
				ArrayList<Route> temp = new ArrayList<>(nd.routes);
				temp.retainAll(_routes);
				return temp.size() > 0;
			}).findFirst().orElse(null);
			if (net == null) {
				ArrayList<Task> _tasks = new ArrayList<Task>();
				_routes.forEach(r -> {
					addToList(_tasks, r.end1);
					addToList(_tasks, r.end2);
				});
				result.add(new NetworkDiagram(new ArrayList<Task>(_tasks), _routes));
			} else {
				_routes.forEach(r -> {
					addToList(net.tasks, r.end1);
					addToList(net.tasks, r.end2);
					addToList(net.routes, r);
				});
			}
		}
		// ���������
		tasks.stream().filter(t -> !result.stream().anyMatch(n -> n.tasks.contains(t))).forEach(_t -> {
			NetworkDiagram nd = new NetworkDiagram(Arrays.asList(Task.startTask(), _t, Task.endTask()),
					Arrays.asList(new Route(Task.startTask(), _t), new Route(_t, Task.endTask())));
			result.add(nd);
		});

		return result;
	}

	private void linkToLeafTask(Task parent, Route in, List<Route> routes) {
		parent.getSubTasks().forEach(t -> {
			if (t.getSubTasks().isEmpty()) {
				Route r = new Route(in.end1, t, in.relations.toArray(new Relation[0]));
				addRoute(routes, r);
			} else {
				linkToLeafTask(t, in, routes);
			}
		});
	}

	private void linkFromLeafTask(Task parent, Route out, List<Route> routes) {
		parent.getSubTasks().forEach(t -> {
			if (t.getSubTasks().isEmpty()) {
				Route r = new Route(t, out.end2, out.relations.toArray(new Relation[0]));
				addRoute(routes, r);
			} else {
				linkFromLeafTask(t, out, routes);
			}
		});
	}

	private void addRoute(List<Route> routes, Route route) {
		int idx = routes.indexOf(route);
		if (idx == -1) {
			routes.add(route);
		} else {
			routes.get(idx).relations.addAll(route.relations);
		}
	}

	private List<Route> searchNextFromRoute(List<Route> routes, Task to) {
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

	private List<Route> searchNextToRoute(List<Route> routes, Task from) {
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

	public void schedule1() {
		nets.forEach(nd -> nd.schedule());
		T = (float) nets.stream().mapToDouble(nd -> nd.T).max().getAsDouble();
		System.out.println(T);

		tasks.forEach(t -> System.out.println(t.getId() + " D:" + t.getD()));
	}

	public void schedule() {
		// ֻ��Ҷ���ų�
		nets.stream().filter(nd -> nd.tasks.stream().allMatch(t -> t.getD() != -1))
				.forEach(pureNd -> pureNd.schedule());

		// �����ܳɹ���
		tasks.stream().filter(t -> !t.getSubTasks().isEmpty()).forEach(st -> calculteSummaryTask(st));

		// �ܹ���
		T = (float) nets.stream().mapToDouble(nd -> nd.T).max().getAsDouble();
		System.out.println(T);

		tasks.forEach(t -> System.out.println(t.getId() + " D:" + t.getD()));
	}

	private void calculteSummaryTask(Task task) {
		List<Task> subTasks = task.getSubTasks();
		for (int i = 0; i < subTasks.size(); i++) {
			Task sub = subTasks.get(i);
			if (!sub.getSubTasks().isEmpty()) {
				calculteSummaryTask(sub);
			}

			if (task.getES() == -1 || task.getES() > sub.getES()) {
				task.setES(sub.getES());
			}

			if (task.getEF() == -1 || task.getEF() < sub.getEF()) {
				task.setEF(sub.getEF());
			}
			
			if (task.getLS() == -1 || task.getLS() > sub.getLS()) {
				task.setLS(sub.getLS());
			}

			if (task.getLF() == -1 || task.getLF() < sub.getLF()) {
				task.setLF(sub.getLF());
			}
			
		}

		task.setD(task.getLF()-task.getLS());
	}


}
