package com.bizvisionsoft.math.scheduling;

/**
 * ���
 * 
 * @author hua
 *
 */
public class Consequence {

	/**
	 * �ܵ�Ӱ�������
	 */
	public final Task task;

	/**
	 * Ӱ��ֵ
	 */
	public final float value;

	/**
	 * @param task
	 *            �յ�Ӱ�������
	 * @param value
	 *            Ӱ��ֵ
	 */
	public Consequence(Task task, float value) {
		this.task = task;
		this.value = value;
	}
}
