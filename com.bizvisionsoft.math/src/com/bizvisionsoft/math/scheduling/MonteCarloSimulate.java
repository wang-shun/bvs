package com.bizvisionsoft.math.scheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class MonteCarloSimulate {

	private final List<Task> tasks;

	private final List<Route> routes;

	private final List<Risk> protentialRisks;

	private SimulationResult[] result;

	public Map<Float, Integer> TMap;

	public float noRiskT;

	private Consumer<Route> startRouteHandle;

	/**
	 * ���ؿ��� ģ�⣬�����Ŀ���ڵĸ��ʷֲ�; ��ø������TCPָ��; ��ø������TCIָ�� ��ø�����յ�RCIָ��
	 * 
	 * @param times
	 *            ����
	 */
	public MonteCarloSimulate(List<Task> tasks, List<Route> routes, List<Risk> protentialRisks,
			Consumer<Route> startRouteHandle) {
		this.tasks = tasks;
		this.routes = routes;
		this.protentialRisks = protentialRisks;
		this.startRouteHandle = startRouteHandle;
	}

	public void simulate(int times) {
		TMap = new HashMap<Float, Integer>();
		result = new SimulationResult[times];
		for (int i = 0; i < times; i++) {
			result[i] = simulate(protentialRisks, true);
			TMap.put(result[i].T, Optional.ofNullable(TMap.get(result[i].T)).orElse(0) + 1);// �������д���
		}

		// ����ACP
		Collections.sort(this.tasks);
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			if (task.getId().equals(Task.START) || task.getId().equals(Task.END)) {
				continue;
			}
			long count = 0;
			float avgX = 0;
			float avgY = 0;
			float x2 = 0;
			float xy = 0;
			for (int j = 0; j < result.length; j++) {
				Collections.sort(result[j].tasks);
				Task rTask = result[j].tasks.get(i);
				if (rTask.getTF() == 0) {// �ڹؼ�·����
					count++;
				}
				avgX += rTask.getD();
				avgY += result[j].T;
				x2 += rTask.getD() * rTask.getD();
				xy = rTask.getD() * result[j].T;
			}
			task.setACP(1f * count / result.length);
			// ����ACI,���Իع�
			avgX = avgX / result.length;
			avgY = avgY / result.length;
			float f = x2 - result.length * avgX * avgY;
			if (f != 0) {
				task.setACI((xy - result.length * avgX * avgY) / f);
			} else {
				task.setACI(null);
			}
		}

		// ����RCI������ÿ����������Ŀ�������
		noRiskT = simulate(new ArrayList<Risk>(), false).T;
		protentialRisks.forEach(r -> {
			r.T = simulate(Arrays.asList(r), false).T - noRiskT;
			r.RCI = r.T / noRiskT;
		});

	}

	public SimulationResult simulate(List<Risk> protentialRisks, boolean useRamdon) {
		SimulationResult result = new SimulationResult();
		// ��������
		List<Task> iTasks = new ArrayList<Task>();

		// ����Ӱ��Ĺ���
		ArrayList<Risk> effRisks = riskImpact(iTasks, protentialRisks, useRamdon);

		// ���ܷ���Ӱ��Ĺ���
		tasks.stream().filter(t -> !iTasks.contains(t))
				.forEach(task -> iTasks.add(new Task(task.getId(), task.getD()).setName(task.getName())));

		// �����¼��ڵ�
		dealSubTasks(iTasks);

		// ����·��
		ArrayList<Route> iRoute = new ArrayList<Route>();
		routes.forEach(r -> {
			Task end1 = iTasks.get(iTasks.indexOf(r.end1));
			Task end2 = iTasks.get(iTasks.indexOf(r.end2));
			iRoute.add(new Route(end1, end2, r.dashed, r.relations.toArray(new Relation[0])));
		});

		// ��������ͼ
		// NetworkDiagram nd = new NetworkDiagram(iTasks, iRoute);
		// nd.schedule();
		Graphic gh = new Graphic(iTasks, iRoute);

		if (startRouteHandle != null)
			gh.getStartRoute().forEach(startRouteHandle);

		gh.schedule();

		// д��Task�Ĺ�������
		result.T = gh.getT();
		// д��ÿ��Task
		result.tasks = new ArrayList<>();
		gh.getTasks().forEach(t -> {
			result.tasks.add(t);
		});
		result.risks = effRisks;
		return result;
	}

	private void dealSubTasks(List<Task> iTasks) {
		tasks.stream().filter(t -> !t.getSubTasks().isEmpty()).forEach(sumTask -> {
			Task iTask = getNewTask(iTasks, sumTask);
			sumTask.getSubTasks().forEach(s -> iTask.addSubTask(getNewTask(iTasks, s)));
		});
	}

	private Task getNewTask(List<Task> iTasks, Task sumTask) {
		return iTasks.stream().filter(it -> sumTask.getId().equals(it.getId())).findFirst().get();
	}

	private ArrayList<Risk> riskImpact(List<Task> iTasks, List<Risk> risks, boolean useRandom) {
		ArrayList<Risk> effRisks = new ArrayList<Risk>();
		risks.forEach(risk -> {
			if (!useRandom || risk.probability >= Math.random()) { // ���㷢������
				effRisks.add(risk);
				risk.consequences.forEach(c -> {
					if (c.task.getSubTasks().isEmpty()) {
						float d = c.task.getD() + c.value;
						Task task = new Task(c.task.getId(), d < 0 ? 0 : d).setName(c.task.getName());
						int idx = iTasks.indexOf(task);
						if (idx < 0) {
							iTasks.add(task);
						} else {
							task = iTasks.get(idx);
							d = task.getD() + c.value;
							task.setD(d < 0 ? 0 : d);
						}
					}
				});
				effRisks.addAll(riskImpact(iTasks, risk.secondary, useRandom));// ���Ǵ�������
			}
		});
		return effRisks;
	}

}
