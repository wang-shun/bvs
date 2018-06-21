package com.bizvisionsoft.math.scheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MonteCarloSimulate {

	private final List<Task> tasks;

	private final List<Route> routes;

	private final List<Risk> protentialRisks;

	private SimulationResult[] result;

	public Map<Float,Integer> TMap;

	public float noRiskT; 
	/**
	 * 蒙特卡罗 模拟，获得项目工期的概率分布; 获得各项工作的TCP指标; 获得各项工作的TCI指标 获得各项风险的RCI指标
	 * 
	 * @param times
	 *            次数
	 */
	public MonteCarloSimulate(List<Task> tasks, List<Route> routes, List<Risk> protentialRisks) {
		this.tasks = tasks;
		this.routes = routes;
		this.protentialRisks = protentialRisks;
	}

	public void simulate(int times) {
		TMap = new HashMap<Float,Integer>();
		result = new SimulationResult[times];
		for (int i = 0; i < times; i++) {
			result[i] = simulate(protentialRisks,true);
			TMap.put(result[i].T, Optional.ofNullable(TMap.get(result[i].T)).orElse(0)+1);
		}

		// 计算ACP
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
				Task rTask = result[j].tasks.get(i);
				if (rTask.getTF() == 0) {// 在关键路径上
					count++;
				}
				avgX += rTask.getD();
				avgY += result[j].T;
				x2 += rTask.getD() * rTask.getD();
				xy = rTask.getD() * result[j].T;
			}
			task.setACP(1f * count / result.length);
			// 计算ACI,线性回归
			avgX = avgX / result.length;
			avgY = avgY / result.length;
			float f = x2 - result.length * avgX * avgY;
			if (f != 0) {
				task.setACI((xy - result.length * avgX * avgY) / f);
			} else {
				task.setACI(null);
			}
		}

		// 计算RCI，计算每个风险与项目的相关性
		noRiskT = simulate(new ArrayList<Risk>(),false).T;
		protentialRisks.forEach(r -> {
			r.T = simulate(Arrays.asList(r),false).T - noRiskT;
			r.RCI = r.T / noRiskT;
		});

	}

	public SimulationResult simulate(List<Risk> protentialRisks, boolean useRamdon) {
		SimulationResult result = new SimulationResult();
		// 创建工作
		List<Task> iTasks = new ArrayList<Task>();

		// 风险影响的工作
		ArrayList<Risk> effRisks = riskImpact(iTasks, protentialRisks,useRamdon);

		// 不受风险影响的工作
		tasks.stream().filter(t -> !iTasks.contains(t)).forEach(task -> iTasks.add(new Task(task.getId(), task.getD())));

		// 创建路径
		ArrayList<Route> iRoute = new ArrayList<Route>();
		routes.forEach(r -> {
			Task end1 = iTasks.get(iTasks.indexOf(r.end1));
			Task end2 = iTasks.get(iTasks.indexOf(r.end2));
			iRoute.add(new Route(end1, end2, r.dashed, r.relations.toArray(new Relation[0])));
		});

		// 构造网络图
		NetworkDiagram nd = new NetworkDiagram(iTasks, iRoute);
		nd.schedule();

		// 写入Task的工期数据
		result.T = nd.T;
		// 写入每个Task
		result.tasks = nd.tasks;
		result.risks = effRisks;
		return result;
	}

	private ArrayList<Risk> riskImpact(List<Task> iTasks, List<Risk> risks,boolean useRandom) {
		ArrayList<Risk> effRisks = new ArrayList<Risk>();
		risks.forEach(risk -> {
			if (!useRandom||risk.probability >= Math.random()) { // 满足发生概率
				effRisks.add(risk);
				risk.consequences.forEach(c -> {
					Task task = new Task(c.task.getId(), c.task.getD() + c.value);
					int idx = iTasks.indexOf(task);
					if (idx < 0) {
						iTasks.add(task);
					} else {
						task = iTasks.get(idx);
						task.setD(task.getD() + c.value);
					}
				});
				effRisks.addAll(riskImpact(iTasks, risk.secondary,useRandom));// 考虑次生风险
			}
		});
		return effRisks;
	}

}
