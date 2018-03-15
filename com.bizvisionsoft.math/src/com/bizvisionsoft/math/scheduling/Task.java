package com.bizvisionsoft.math.scheduling;

public class Task implements Comparable<Task>{

	public static final String START = "_start";

	public static final String END = "_end";

	public final String id;

	public float D;

	public float ES = -1f;

	public float EF = -1f;

	public float LS = -1f;

	public float LF = -1f;

	public float TF = -1f;

	public float FF = -1f;
	
	/**
	 * 关键概率
	 */
	public float ACP;
	
	/**
	 * 进度风险指标
	 */
	public Float ACI;

	/**
	 * 位于关键线路上、成为关键工序的概率
	 */
	public long acp = 0;

	public Task(String id, float d) {

		this.id = id;

		this.D = d;

	}

	public Task(String id) {

		this(id, 0);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", D=" + D + ", ES=" + ES + ", EF=" + EF + ", LS=" + LS + ", LF=" + LF + ", TF=" + TF
				+ ", FF=" + FF + "]";
	}

	void updateES(float es) {
		if (es > ES) {
			this.ES = es;
			this.EF = es + D;
		}
	}

	public void updateLF(float lf) {
		if (LF == -1 || lf < LF) {
			this.LF = lf;
			this.LS = lf - D;
		}
	}

	public void updateFF(float lag) {
		if (FF == -1 || lag < FF) {
			FF = lag;
		}
	}

	@Override
	public int compareTo(Task o) {
		return this.id.compareTo(o.id);
	}

}
