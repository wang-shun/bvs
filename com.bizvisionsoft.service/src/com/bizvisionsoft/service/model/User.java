package com.bizvisionsoft.service.model;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.bizvisionsoft.annotations.md.mongocodex.Persistence;
import com.bizvisionsoft.annotations.md.mongocodex.PersistenceCollection;
import com.bizvisionsoft.annotations.md.mongocodex.SetValue;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.ServicesLoader;

@PersistenceCollection(value = "account")
public class User {

	@Persistence
	private ObjectId _id;

	@Persistence
	@ReadValue
	private String userId;

	@SetValue
	@WriteValue
	private String password;

	@WriteValue("password2")
	private void setPassword2(String password) {
		if (this.password != null && !this.password.isEmpty() && !password.equals(this.password))
			throw new RuntimeException("两次输入的密码不一致。");
	}

	@Persistence
	@ReadValue
	@WriteValue
	private String name;

	@Persistence
	@ReadValue
	@WriteValue
	private String email;

	@Persistence
	@ReadValue
	@WriteValue
	private String tel;

	@Persistence
	@ReadValue
	@WriteValue
	private String mobile;

	@Persistence
	@ReadValue
	@WriteValue
	private String weixin;

	@Persistence
	@ReadValue
	@WriteValue
	private boolean activated;

	@Persistence
	@ReadValue
	@WriteValue
	private List<RemoteFile> headPics;

	@Persistence("org_id")
	private ObjectId organizationId;

	public String getUserId() {
		return userId;
	}

	public String getHeadpicURL() {
		if (headPics != null && headPics.size() > 0)
			return headPics.get(0).getURL(ServicesLoader.url);
		return null;
	}

	@WriteValue("organization ")
	public void setOrganization(Organization org) {
		this.organizationId = Optional.ofNullable(org).map(o -> o.get_id()).orElse(null);
	}

	@ReadValue("organization ")
	public Organization getOrganization() {
		return Optional.ofNullable(organizationId).map(_id -> ServicesLoader.get(OrganizationService.class).get(_id))
				.orElse(null);
	}

	@Override
	@Label
	public String toString() {
		return name + " [" + userId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		User other = (User) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	
	public boolean isActivated() {
		return activated;
	}

}
