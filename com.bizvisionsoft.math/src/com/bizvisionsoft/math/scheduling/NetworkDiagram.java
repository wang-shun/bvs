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

		this.routes = new ArrayList<Route>();
		this.routes.addAll(routes);

		// check start end.
		// ��ʼ��
		start = getTask(Task.START);
		end = getTask(Task.END);
	}

	public List<Task> schedule() {
		tasks.forEach(t -> t.reset());
		Collections.sort(this.tasks);

		virtualRoute = new ArrayList<Route>();

		// 1. ����ES, EF
		start.setES(0f);
		start.setEF(0f);
		tasks.forEach(t -> calculateEarlestStart(t));

		// 2. ����end
		Set<Task> exclude = new HashSet<Task>();
		exclude.add(start);
		exclude.add(end);
		ergodic(end, true, route -> exclude.add(route.end1));
		tasks.stream().filter(task -> !exclude.contains(task) && task.getEF() > end.getEF()).forEach(t -> {
			virtualRoute.add(new Route(t, end, true, new Relation(Relation.FTF, 0)));
			end.updateES(t.getEF());
		});
		routes.addAll(virtualRoute);
		// ���
		virtualRoute.clear();

		// 3. ��LS,LF
		end.setLS(end.getES());
		end.setLF(end.getEF());
		start.setLS(0f);
		start.setLF(0f);
		tasks.forEach(t -> calculateLatestFinish(t));

		// 4. ��LAG
		routes.forEach(r -> calculateLAG(r));

		// 5.��TF,FF
		tasks.forEach(t -> calculateTF(t));
		end.setTF(0f);
		end.setFF(0f);
		start.setTF(0f);
		start.setFF(0f);

		// 6.���ܹ���
		T = end.getLF();

		// System.out.println("\n============================�ų�/���ڼ���============================");
		// System.out.println("�����ų�-------------------------------");
		// tasks.forEach(t -> System.out.println(t));
		// System.out.println("����ͼ-----------------------------");
		// routes.forEach(r -> System.out.println(r));
		// System.out.println("�ܹ���-------------------------------");
		// System.out.println(T);
		// System.out.println("=================================================================");

		return tasks;
	}

	public void calculateTF(Task task) {
		task.setTF(task.getLS() - task.getES());
		ergodic(task, false, route -> {
			task.updateFF(route.LAG);
		});
	}

	private void calculateLAG(Route route) {
		final Float es1 = route.end1.getES();
		final Float es2 = route.end2.getES();
		final Float ef1 = route.end1.getEF();
		final Float ef2 = route.end2.getEF();
		route.relations.forEach(rela -> {
			float lag = 0;
			if (rela.type == Relation.FTS) {
				lag = es2 - (ef1 + rela.interval);
			} else {
				if (rela.type == Relation.STS) {
					lag = es2 - (es1 + rela.interval);
				} else {
					if (rela.type == Relation.FTF) {
						lag = ef2 - (ef1 + rela.interval);
					} else if (rela.type == Relation.STF) {
						lag = ef2 - (es1 + rela.interval);
					}
				}
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
				if (route.end2.getLS() == null) {
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
				if (route.end1.getES() == null) {
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
	 * ��ĳ�������������ͼ
	 * 
	 * @param from
	 *            ���
	 * @param reverse
	 *            ����
	 * @param action
	 */
	public void ergodic(Task from, boolean reverse, Consumer<? super Route> action) {
		if (reverse) {
			routes.stream().filter(f -> f.end2.equals(from)).forEach(action);
		} else {
			routes.stream().filter(f -> {
				return f.end1.equals(from);
			}).forEach(action);
		}
	}

	/**
	 * ���ĳ������
	 * 
	 * @param id
	 *            �����Id
	 * @return
	 */
	public Task getTask(String id) {
		return tasks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
	}

}
