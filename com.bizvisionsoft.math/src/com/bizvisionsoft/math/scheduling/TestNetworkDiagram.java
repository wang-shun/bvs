package com.bizvisionsoft.math.scheduling;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestNetworkDiagram {
	
	private static Logger logger = LoggerFactory.getLogger(TestNetworkDiagram.class);


	public static void main(String[] args) {
		// 1. ����Ҫ���������task��������ͼ��
//		test3();
		
		
	}

	/**
	 * �����ܳɹ��������ų�
	 */
	public static void test4() {
		Task a = new Task("a", 5);
		Task b = new Task("b");// �ܳ�
		Task c = new Task("c", 13);
		Task d = new Task("d", 7);
		Task e = new Task("e", 7);
		Task f = new Task("f", 7);
		Task g = new Task("g");
		Task h = new Task("h", 7);
		Task i = new Task("i", 12);
		Task j = new Task("j", 6);
		
		b.setSubTasks(Arrays.asList(g, h));
		g.setSubTasks(Arrays.asList(j));

		Route a_b = new Route(a, b, new Relation(Relation.FTS, 6));
		Route a_c = new Route(a, c, new Relation(Relation.FTS, 7));
		Route d_b = new Route(d, b, new Relation(Relation.FTS, 6));
		Route c_e = new Route(c, e, new Relation(Relation.FTS, 7));

		Route g_h = new Route(g, h, new Relation(Relation.FTS, 7));
		Route h_f = new Route(h, f, new Relation(Relation.FTS, 2));
		
		Route i_g = new Route(i, g, new Relation(Relation.FTS, 5));
		
		Route j_e = new Route(j, e, new Relation(Relation.FTS, 2));


		Graphic gh = new Graphic(Arrays.asList(a, b, c, d, e, f, g, h, i), Arrays.asList(a_b, a_c, d_b, c_e, g_h, h_f,i_g,j_e));
		
		List<Route> start = gh.getStartRoute();
		logger.debug("��ʼ�ڵ㣺");
		logger.debug(""+start);
		// �ó�����ʼ�ڵ�Ϊa, d, i
		
		//�ٶ���Ŀ��ʼʱ��Ϊ2018��6��1��
		Calendar pjStart = Calendar.getInstance();
		pjStart.set(2018, 5, 1);
		
		//a������ʼ��2018��6��1��
		Calendar aStart = Calendar.getInstance();
		aStart.set(2018, 5, 1);
	
		//b������ʼ��2018��6��2��
		Calendar dStart = Calendar.getInstance();
		dStart.set(2018, 5, 2);

		//i������ʼ��2018��6��10��
		Calendar iStart = Calendar.getInstance();
		iStart.set(2018, 5, 10);

		//���������������Ŀ���ӳ�����
		long timeInMillis = pjStart.getTimeInMillis();
		int intervalA = (int) ((aStart.getTimeInMillis()-timeInMillis)/(1000*60*60*24));
		int intervalD = (int) ((dStart.getTimeInMillis()-timeInMillis)/(1000*60*60*24));
		int intervalI = (int) ((iStart.getTimeInMillis()-timeInMillis)/(1000*60*60*24));
		
		gh.setStartInterval("a",intervalA);
		gh.setStartInterval("d",intervalD);
		gh.setStartInterval("i",intervalI);
		gh.schedule();
		
		//��Ŀ�ܹ���Ϊ
		logger.debug("��Ŀ���ڣ�");
		float pjDuration = gh.getT();
		logger.debug(""+pjDuration);
		pjStart.add(Calendar.DATE, (int)pjDuration);
		logger.debug("��Ŀ�깤����");
		logger.debug(new SimpleDateFormat("yyyy-MM-dd").format(pjStart.getTime()));
		
		//��ʾ����ļƻ�
		displayTaskInfo(gh, "c");
		displayTaskInfo(gh, "d");

	}

	private static void displayTaskInfo(Graphic gh, String taskId) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("����"+taskId+": ");
		Task taskC = gh.getTask(taskId);
		logger.debug("���ڣ�"+taskC.getD());
		
		Calendar cStart = Calendar.getInstance();
		cStart.set(2018, 5, 1);
		cStart.add(Calendar.DATE, taskC.getES().intValue());
		logger.debug("�ƻ���ʼ��"+new SimpleDateFormat("yyyy-MM-dd").format(cStart.getTime()));

		Calendar cFinish = Calendar.getInstance();
		cFinish.set(2018, 5, 1);
		cFinish.add(Calendar.DATE, taskC.getEF().intValue());
		logger.debug("�ƻ���ɣ�"+new SimpleDateFormat("yyyy-MM-dd").format(cFinish.getTime()));
		
		
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	public static void test0() {
		Task start = Task.startTask();
		Task a = new Task("a", 5);
		Task b = new Task("b", 10);
		Task c = new Task("c", 13);
		Task d = new Task("d", 7);
		Task end = Task.endTask();

		Route start_a = new Route(start, a);
		Route a_b = new Route(a, b, new Relation(Relation.FTS, 6));
		Route a_c = new Route(a, c, new Relation(Relation.FTS, 7));
		Route a_d = new Route(a, d);
		Route b_d = new Route(b, d, new Relation(Relation.FTS, 3));

		List<Task> tasks = Arrays.asList(b, c, start, a, d, end);
		List<Route> routes = Arrays.asList(start_a, a_b, a_c, a_d, b_d);
		NetworkDiagram nd = new NetworkDiagram(tasks, routes);
		nd.schedule();
	}

	public static void test3() {
		// ����
//		Task start = Task.startTask();
		Task a = new Task("a", 5);
		Task b = new Task("b", 8);
		Task c = new Task("c", 17);
		Task d = new Task("d", 12);
		Task e = new Task("e", 15);
		Task f = new Task("f", 25);
		Task g = new Task("g", 10);
//		Task end = Task.endTask();

		// �����ӹ�ϵ
//		Route start_a = new Route(start, a);
		Route a_b = new Route(a, b, new Relation(Relation.STF, 6));
		Route b_e = new Route(b, e, new Relation(Relation.FTF, 10));
		Route e_g = new Route(e, g);
		Route a_c = new Route(a, c, new Relation(Relation.STS, 7));
		Route c_f = new Route(c, f, new Relation(Relation.FTF, 15));
		Route f_g = new Route(f, g, new Relation(Relation.STF, 10));
		Route a_d = new Route(a, d);
		Route b_d = new Route(b, d, new Relation(Relation.FTS, 3));
		Route d_g = new Route(d, g, new Relation(Relation.STS, 3), new Relation(Relation.FTF, 2));
//		Route g_end = new Route(g, end);

		// ����
		Risk r1 = new Risk("R1", .3f, new Consequence(b, 12), new Consequence(c, 13));
		Risk r2 = new Risk("R2", .5f, new Consequence(d, 15));
		Risk r3 = new Risk("R3", .2f, new Consequence(g, 5), new Consequence(f, 6));
		Risk r4 = new Risk("R4", .4f, new Consequence(e, 17));
		r2.addSecondaryRisks(r3);// ��Ӵ�������

		List<Task> tasks = Arrays.asList( a, d, e, b, c, f, g);
		List<Route> routes = Arrays.asList( a_b, b_e, c_f, f_g, e_g, a_c, a_d, b_d, d_g);
		List<Risk> risks = Arrays.asList(r1, r2, r4);
		MonteCarloSimulate mcs = new MonteCarloSimulate(tasks, routes, risks,null);
		/////////////////////////////////////////////////// ����ģ�������þ�ȷ���
		int times = 100000;
		////////////////////////////////////////////////////////////////////////////////
		logger.debug("���Ƿ��գ��������ؿ���ģ�⣺" + times);

		long _s = System.currentTimeMillis();
		mcs.simulate(times);
		long _e = System.currentTimeMillis();
		logger.debug("���ģ�⣬��ʱ���룩��" + ((_e - _s) / 1000));
		logger.debug("ָ��˵����");
		logger.debug("����ACI ��ACP ���ϸߵĹ���,��Ӧ������Ϊ���ȹ����е�����֮��,���๤�򲻽���ʵ�ֹ��ڸϹ���ѹ����");
		logger.debug("�ڵĹؼ�;��,����,ͬʱ��Ӧ��ǿ���չ����������򽵵��䲻ȷ����Ӱ��;�෴,��������ЩACI ��ACP ���ϵ͵Ĺ�");
		logger.debug("��,�����Ӧ���͹���Ҫ��");
		logger.debug("����ACP �ߡ�ACI �ͻ���ACP �͡�ACI �ߵĹ�����Ӧ�������⿼�ǡ�");
		logger.debug("�ٶ���ǰһ�����͵Ĺ���,������Ӧ��Ҫ��ע��������̴����͹���ĳ���ʱ��(���������Щ��Դ�������ڵ�");
		logger.debug("����Ͷ��������Դ) ����������:ACP ����ζ�Ŵ����͹����п���λ�ڹؼ���·��Ϊ�ؼ�����,Ҳ����˵�����͹�");
		logger.debug("��������ΪӰ����Ŀ�ܹ��ڵġ�ƿ����,�ǹ��ڸϹ���ѹ���ܷ�ʵ�ֵĹؼ����ڡ���һ����,ACI ������ζ�Ŵ����");
		logger.debug("�Ǹ߷���,����Ϊ���ڷ��չ�����ص㡣");
		logger.debug("�ں�һ���͵Ĺ���Ӧ������շ�����������ص�,�����б�Ҫ��ȡǿ�����Ľ��ȷ��չ����ʩ����������:����");
		logger.debug("����������,ʮ��ǿ�����ڡ�ϡ���¼����Ĺ���[7 ] ,�������͵Ĺ��������ڡ�ϡ���¼����б���Ϊ���㡪����С��(Zero - In2");
		logger.debug("finity Dilemmas) �ķ������(����˵�վ���ش��¹�) :��Ȼ��ؼ����ʺ�С,��ACI �ߺܴ�̶��ϱ���ζ�Ÿ߷���,һ��");
		logger.debug("�ù���λ�ڹؼ���·�ϳ�Ϊ�ؼ�����,���������Ӱ�쵽��Ŀ�ܹ��ڡ�");
		logger.debug("����RCIָ��ߵķ�����ζ�����ķ�������Ŀ���ȵ�Ӱ��ϴ�Ӧ���ص��ע");

		tasks.forEach(t -> logger.debug(t.getId() + "  ACI:" + t.getACI() + ", ACP:" + t.getACP()));
		risks.forEach(r -> logger.debug(r.id + " RCI:" + r.RCI + " ,T:" + r.T));

		logger.debug("�޷���ʱ����Ŀ����Ϊ��" + mcs.noRiskT);
		logger.debug("����Ϊ��" + mcs.noRiskT + "��ĸ���Ϊ��");
		Integer cnt = mcs.TMap.get(mcs.noRiskT);
		if (cnt != null) {
			logger.debug(""+1f * cnt / times);
		}
	}

	public static void test1() {
		Task start = Task.startTask();
		Task a = new Task("a", 5);
		Task b = new Task("b", 10);
		Task c = new Task("c", 13);
		Task d = new Task("d", 17);
		Task e = new Task("e", 18);
		Task f = new Task("f", 27);
		Task g = new Task("g", 12);
		Task end = Task.endTask();

		Route start_a = new Route(start, a);
		Route a_b = new Route(a, b, new Relation(Relation.STF, 6));
		Route b_e = new Route(b, e, new Relation(Relation.FTF, 10));
		Route e_g = new Route(e, g);
		Route a_c = new Route(a, c, new Relation(Relation.STS, 7));
		Route c_f = new Route(c, f, new Relation(Relation.FTF, 15));
		Route f_g = new Route(f, g, new Relation(Relation.STF, 10));
		Route a_d = new Route(a, d);
		Route b_d = new Route(b, d, new Relation(Relation.FTS, 3));
		Route d_g = new Route(d, g, new Relation(Relation.STS, 3), new Relation(Relation.FTF, 2));
		Route g_end = new Route(g, end);

		List<Task> tasks = Arrays.asList(b, c, e, start, a, d, f, g, end);
		List<Route> routes = Arrays.asList(start_a, a_b, b_e, c_f, f_g, e_g, a_c, a_d, b_d, d_g, g_end);
		NetworkDiagram nd = new NetworkDiagram(tasks, routes);
		nd.schedule();
	}

	public static void test2() {
		// ����
		Task start = Task.startTask();
		Task a = new Task("a", 5);
		Task b = new Task("b", 8);
		Task c = new Task("c", 10);
		Task d = new Task("d", 12);
		Task e = new Task("e", 15);
		Task f = new Task("f", 25);
		Task g = new Task("g", 10);
		Task end = Task.endTask();

		// �����ǰ���ӹ�ϵ
		Route start_a = new Route(start, a);
		Route a_b = new Route(a, b, new Relation(Relation.STF, 6));
		Route b_e = new Route(b, e, new Relation(Relation.FTF, 10));
		Route e_g = new Route(e, g);
		Route a_c = new Route(a, c, new Relation(Relation.STS, 7));
		Route c_f = new Route(c, f, new Relation(Relation.FTF, 15));
		Route f_g = new Route(f, g, new Relation(Relation.STF, 10));
		Route a_d = new Route(a, d);
		Route b_d = new Route(b, d, new Relation(Relation.FTS, 3));
		Route d_g = new Route(d, g, new Relation(Relation.STS, 3), new Relation(Relation.FTF, 2));
		Route g_end = new Route(g, end);

		List<Task> tasks = Arrays.asList(start, a, d, e, b, c, f, g, end);
		List<Route> routes = Arrays.asList(start_a, a_b, b_e, c_f, f_g, e_g, a_c, a_d, b_d, d_g, g_end);

		// �����ų�
		// NetworkDiagram nd = new NetworkDiagram(tasks, routes);
		// nd.schedule();

		// ��ӷ���
		Risk r1 = new Risk("R1", .3f, new Consequence(b, 12), new Consequence(c, 13));
		Risk r2 = new Risk("R2", .5f, new Consequence(d, 15));
		Risk r3 = new Risk("R3", .2f, new Consequence(g, 5), new Consequence(f, 6));
		Risk r4 = new Risk("R4", .4f, new Consequence(e, 17));
		r2.addSecondaryRisks(r3);// r3��r2�Ĵ������ա�

		List<Risk> risks = Arrays.asList(r1, r2, r4);

		// ��ʼģ��
		MonteCarloSimulate mcs = new MonteCarloSimulate(tasks, routes, risks,null);
		mcs.simulate(1000);
	}
	

}
