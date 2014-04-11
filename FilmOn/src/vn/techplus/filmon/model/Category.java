package vn.techplus.filmon.model;

public class Category {
	private String name;
	private String id;
	private String logo;
	
	public Category() {}
	
	public Category(String name){
		this.name = name;
	}
	
	public Category(String name, String id, String logo) {
		super();
		this.name = name;
		this.id = id;
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Override
	public String toString() {
		return "Category [name=" + name + ", id=" + id + ", logo=" + logo + "]";
	}

	
}
