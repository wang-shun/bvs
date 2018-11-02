package com.bizvisionsoft.bruicommons.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;

/**
 * @author hua
 *
 */
public class Site extends ModelObject {

	@ReadValue(ReadValue.TYPE)
	private String typeName = "站点";

	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}

	@ReadValue
	@WriteValue
	private String id;

	@ReadValue
	@WriteValue
	private String name;

	@ReadValue
	@WriteValue
	private String title;

	@Behavior({ "添加", "编辑" })
	private boolean behavior = true;

	@ReadValue
	@WriteValue
	private String path;

	@ReadValue
	@WriteValue
	private String headHtml;

	@ReadValue
	@WriteValue
	private String bodyHtml;

	@ReadValue
	@WriteValue
	private String pageOverflow;

	@ReadValue
	@WriteValue
	private String aliasOfResFolder;

	@ReadValue
	@WriteValue
	private String favIcon;

	@ReadValue
	@WriteValue
	private String description;

	private String login;

	@ReadValue("login")
	public Assembly getLoginAssembly() {
		Assembly ass = Optional.ofNullable(login).map(id -> getAssembly(id)).orElse(null);
		return ass;
	}

	@WriteValue("login")
	public void setLoginAssembly(Assembly loginAssembly) {
		login = Optional.ofNullable(loginAssembly).map(l -> l.getId()).orElse(null);
	}

	@Structure("list")
	public List<Page> getPages() {
		return pages;
	}

	@Structure("count")
	public long countPages() {
		return Optional.ofNullable(pages).map(p -> p.size()).orElse(0);
	}

	private Folder rootFolder;

	private List<Page> pages;

	private AssemblyLib assyLib;

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public void setAssyLib(AssemblyLib assyLib) {
		this.assyLib = assyLib;
	}

	public AssemblyLib getAssyLib() {
		return assyLib;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Object old = this.name;
		this.name = name;
		firePropertyChange("name", old, this.name);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		Object old = this.title;
		this.title = title;
		firePropertyChange("title", old, this.title);
	}

	public String getHeadHtml() {
		return headHtml;
	}

	public String getBodyHtml() {
		return bodyHtml;
	}

	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	public void setHeadHtml(String headHtml) {
		this.headHtml = headHtml;
	}

	public String getPageOverflow() {
		return pageOverflow;
	}

	public void setPageOverflow(String pageOverflow) {
		this.pageOverflow = pageOverflow;
	}

	public String getFavIcon() {
		return favIcon;
	}

	public void setFavIcon(String favIcon) {
		this.favIcon = favIcon;
	}

	public void setAliasOfResFolder(String aliasOfResFolder) {
		this.aliasOfResFolder = aliasOfResFolder;
	}

	public String getAliasOfResFolder() {
		return aliasOfResFolder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * 获得首页Page
	 * 
	 * @return
	 */
	public Page getHomePage() {
		return pages.parallelStream().filter(p -> p.isHome()).findFirst().orElse(null);
	}

	/**
	 * 获得组件
	 * 
	 * @param assyId
	 * @return
	 */
	public Assembly getAssembly(String assyId) {
		return getAssyLib().getAssys().stream().filter(a -> a.getId().equals(assyId)).findFirst().orElse(null);
	}

	public Assembly getAssemblyByName(String assemblyName) {
		return getAssyLib().getAssys().stream().filter(a -> a.getName().equals(assemblyName)).findFirst().orElse(null);
	}

	public List<Assembly> getAssysByFolder(String id) {
		ArrayList<Assembly> result = new ArrayList<Assembly>();
		getAssyLib().getAssys().stream().filter(a -> id.equals(a.getFolderId())).forEach(i -> result.add(i));
		return result;
	}

	public Folder getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(Folder rootFolder) {
		this.rootFolder = rootFolder;
	}

	public Page getPageByName(String pageName) {
		return pages.stream().filter(p -> pageName.equals(p.getName())).findFirst().orElse(null);
	}

	public Page getPageById(String pageId) {
		return pages.stream().filter(p -> pageId.equals(p.getId())).findFirst().orElse(null);
	}

	private String password;

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	private String footLeftText;

	public String getFootLeftText() {
		return footLeftText;
	}

	public void setFootLeftText(String footLeftText) {
		this.footLeftText = footLeftText;
	}

	private String headLogo;

	public String getHeadLogo() {
		return headLogo;
	}

	public void setHeadLogo(String headLogo) {
		this.headLogo = headLogo;
	}

	private String pageBackgroundImage;

	public String getPageBackgroundImage() {
		return pageBackgroundImage;
	}

	public void setPageBackgroundImage(String pageBackgroundImage) {
		this.pageBackgroundImage = pageBackgroundImage;
	}
	
	private String pageBackgroundImageCSS;

	public String getPageBackgroundImageCSS() {
		return pageBackgroundImageCSS;
	}
	
	public void setPageBackgroundImageCSS(String pageBackgroundImageCSS) {
		this.pageBackgroundImageCSS = pageBackgroundImageCSS;
	}

	private String welcome;

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}
	
	public String getWelcome() {
		return welcome;
	}
	
	private Integer headLogoWidth;
	
	private Integer headLogoHeight;
	
	public Integer getHeadLogoHeight() {
		return headLogoHeight;
	}
	
	public Integer getHeadLogoWidth() {
		return headLogoWidth;
	}
	
	public void setHeadLogoHeight(Integer headLogoHeight) {
		this.headLogoHeight = headLogoHeight;
	}
	
	public void setHeadLogoWidth(Integer headLogoWidth) {
		this.headLogoWidth = headLogoWidth;
	}
	
	public Date getShutDown() {
		return ShutDown;
	}

	public void setShutDown(Date shutDown) {
		ShutDown = shutDown;
	}

	private Date ShutDown;
	
	private boolean disableNotice;
	
	private String noticeImg1;
	
	private String noticeContent1;
	
	private String noticeImg2;
	
	private String noticeContent2;

	private String noticeImg3;
	
	private String noticeContent3;

	public String getNoticeImg1() {
		return noticeImg1;
	}

	public void setNoticeImg1(String noticeImg1) {
		this.noticeImg1 = noticeImg1;
	}

	public String getNoticeContent1() {
		return noticeContent1;
	}

	public void setNoticeContent1(String noticeContent1) {
		this.noticeContent1 = noticeContent1;
	}

	public String getNoticeImg2() {
		return noticeImg2;
	}

	public void setNoticeImg2(String noticeImg2) {
		this.noticeImg2 = noticeImg2;
	}

	public String getNoticeContent2() {
		return noticeContent2;
	}

	public void setNoticeContent2(String noticeContent2) {
		this.noticeContent2 = noticeContent2;
	}

	public String getNoticeImg3() {
		return noticeImg3;
	}

	public void setNoticeImg3(String noticeImg3) {
		this.noticeImg3 = noticeImg3;
	}

	public String getNoticeContent3() {
		return noticeContent3;
	}

	public void setNoticeContent3(String noticeContent3) {
		this.noticeContent3 = noticeContent3;
	}
	
	public boolean isDisableNotice() {
		return disableNotice;
	}
	
	public void setDisableNotice(boolean disableNotice) {
		this.disableNotice = disableNotice;
	}
	
	private String defaultPageCSS;
	
	public String getDefaultPageCSS() {
		return defaultPageCSS;
	}
	
	public void setDefaultPageCSS(String defaultPageCSS) {
		this.defaultPageCSS = defaultPageCSS;
	}
}
