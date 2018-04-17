package com.bizvisionsoft.service.model;

import java.util.List;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Exclude;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.datatools.Query;
import com.mongodb.BasicDBObject;

@PersistenceCollection("resourceType")
public class ResourceType {

	public static final String TYPE_HR = "������Դ";
	public static final String TYPE_ER = "�豸��ʩ";

	/** ��ʶ Y **/
	@ReadValue
	@WriteValue
	private ObjectId _id;

	/** ��� Y **/
	@ReadValue
	@WriteValue
	private String id;

	@ReadValue(ReadValue.TYPE)
	@Exclude
	private String typeName = "��Դ����";

	/** ��Դ������� 25 Y **/
	@ReadValue
	@WriteValue
	private String name;

	@ReadValue
	@WriteValue
	private String type;

	@ReadValue
	@WriteValue
	private String description;

	/** �Ƽ۷�ʽ 10 Y **/
	@WriteValue
	@ReadValue("��Դ���ͱ༭��/pricingModel")
	private String pricingModel;

	/** ��׼����(Ԫ) Y **/
	@ReadValue
	private double basicRate;

	/** �Ӱ����(Ԫ) Y **/
	@ReadValue
	private double overtimeRate;

	@WriteValue("��Դ���ͱ༭��/basicRate")
	private void set_basicRate(String _basicRate) {
		try {
			this.basicRate = Double.parseDouble(_basicRate);
		} catch (Exception e) {
			throw new RuntimeException("��׼�����ֶ�ֻ��������ֵ");
		}
	}

	@WriteValue("��Դ���ͱ༭��/overtimeRate")
	private void set_overtimeRate(String _overtimeRate) {
		try {
			this.overtimeRate = Double.parseDouble(_overtimeRate);
		} catch (Exception e) {
			throw new RuntimeException("�Ӱ�����ֶ�ֻ��������ֵ");
		}
	}

	@ReadValue("��Դ����/pricingModel")
	private String getPricingModelText() {
		if ("byHour".equals(pricingModel)) {
			return "��ÿСʱ�Ʒ�";
		} else if ("byTimes".equals(pricingModel)) {
			return "��ʹ�ô����Ʒ�";
		}
		return "";
	}

	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}

	public String getType() {
		return type;
	}

	public ObjectId get_id() {
		return _id;
	}

	@Structure("list")
	public List<?> getResource() {
		if (TYPE_HR.equals(type))
			return ServicesLoader.get(UserService.class).createDataSet(new Query().filter(new BasicDBObject("resourceType_id",_id)).bson());
		else
			return ServicesLoader.get(CommonService.class).getERResources(_id);

	}
	
	@Structure("count")
	public long countResource() {
		if (TYPE_HR.equals(type))
			return ServicesLoader.get(UserService.class).count(new BasicDBObject("resourceType_id",_id));
		else
			return ServicesLoader.get(CommonService.class).countERResources(_id);

	}
	
	@Behavior({"��Դ����/�����Դ","��Դ����/�༭��Դ����","��Դ����/ɾ����Դ����"})
	public boolean enabledBehavior = true;
}
