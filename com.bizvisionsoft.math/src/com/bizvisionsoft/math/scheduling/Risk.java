package com.bizvisionsoft.math.scheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Risk {

	/**
	 * �����ĸ���
	 */
	public final float probability;

	/**
	 * ��������
	 */
	public List<Risk> secondary;

	/**
	 * ���
	 */
	public List<Consequence> consequences;

	/**
	 * �÷��ն�������Ŀ��Ӱ��
	 */
	public float T;

	/**
	 * ����Ӱ��ָ��
	 */
	public Float RCI;

	/**
	 * id
	 */
	public final String id;

	/**
	 * 
	 * @param probability ����
	 * @param consequences ���
	 */
	public Risk(String id,float probability, Consequence... consequences) {
		this.id = id;
		this.consequences = new ArrayList<Consequence>();
		this.secondary = new ArrayList<Risk>();

		this.probability = probability;
		this.consequences.addAll(Arrays.asList(consequences));
	}

	/**
	 * ��Ӵ�������
	 * @param risks ��������
	 */
	public void addSecondaryRisks(Risk... risks) {
		secondary.addAll(Arrays.asList(risks));
	}

	public String getId() {
		return id;
	}

}
