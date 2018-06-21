package com.bizvisionsoft.math.scheduling;

import java.util.List;

public class Task implements Comparable<Task>{

	public static final String START = "_start";

	public static final String END = "_end";
	
	private final String id;

	/**
	 * ����
	 */
	private float D;

	/**
	 * ���翪ʼ
	 */
	private float ES = -1f;

	/**
	 * �������
	 */
	private float EF = -1f;

	/**
	 * ����ʼ
	 */
	private float LS = -1f;

	/**
	 * �������
	 */
	private float LF = -1f;

	/**
	 * ��ʱ��
	 */
	private float TF = -1f;

	/**
	 * ����ʱ��
	 */
	private float FF = -1f;
	
	/**
	 * �ؼ�����
	 */
	private float ACP;
	
	/**
	 * ���ȷ���ָ��
	 */
	private Float ACI;

	private List<Task> subTasks;

	public Task(String id, float d) {
		this.id = id;
		this.setD(d);
	}
	
	public Task(String id, List<Task> subTasks) {
		this.id = id;
		this.subTasks = subTasks;
	}
	
	public List<Task> getSubTasks() {
		return subTasks;
	}

	public static Task startTask() {
		return new Task(START, 0);
	}
	
	public static Task endTask() {
		return new Task(END, 0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task [id=" + getId() + ", D=" + getD() + ", ES=" + getES() + ", EF=" + getEF() + ", LS=" + getLS() + ", LF=" + getLF() + ", TF=" + getTF()
				+ ", FF=" + getFF() + "]";
	}

	void updateES(float es) {
		if (es > getES()) {
			this.setES(es);
			this.setEF(es + getD());
		}
	}

	public void updateLF(float lf) {
		if (getLF() == -1 || lf < getLF()) {
			this.setLF(lf);
			this.setLS(lf - getD());
		}
	}

	public void updateFF(float lag) {
		if (getFF() == -1 || lag < getFF()) {
			setFF(lag);
		}
	}

	@Override
	public int compareTo(Task o) {
		return this.getId().compareTo(o.getId());
	}

	public String getId() {
		return id;
	}

	public Float getACI() {
		return ACI;
	}

	public void setACI(Float aCI) {
		ACI = aCI;
	}

	public float getACP() {
		return ACP;
	}

	public void setACP(float aCP) {
		ACP = aCP;
	}

	public float getTF() {
		return TF;
	}

	public void setTF(float tF) {
		TF = tF;
	}

	public float getFF() {
		return FF;
	}

	public void setFF(float fF) {
		FF = fF;
	}

	public float getLS() {
		return LS;
	}

	public void setLS(float lS) {
		LS = lS;
	}

	public float getES() {
		return ES;
	}

	public void setES(float eS) {
		ES = eS;
	}

	public float getLF() {
		return LF;
	}

	public void setLF(float lF) {
		LF = lF;
	}

	public float getEF() {
		return EF;
	}

	public void setEF(float eF) {
		EF = eF;
	}

	public float getD() {
		return D;
	}

	public void setD(float d) {
		D = d;
	}
	

}
