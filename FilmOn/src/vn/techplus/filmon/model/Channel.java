package vn.techplus.filmon.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Channel implements Parcelable {
	private String name;
	private String logo;
	private String id;
	private String description;
	private String type;
	private String categoryId;
	private boolean isMyChannel = false;
	private boolean isHD = false;
	private boolean isSeekable = false;
	private String streamUrl;	// this is used for manual channel

	public Channel(){}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getId() {
		return id;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isMyChannel() {
		return isMyChannel;
	}

	public void setMyChannel(boolean isMyChannel) {
		this.isMyChannel = isMyChannel;
	}

	public boolean isHD() {
		return isHD;
	}

	public void setHD(boolean isHD) {
		this.isHD = isHD;
	}

	public boolean isSeekable() {
		return isSeekable;
	}

	public void setSeekable(boolean isSeekable) {
		this.isSeekable = isSeekable;
	}

	public String getStreamUrl() {
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}

	@Override
	public String toString() {
		return "Channel [name=" + name + ", logo=" + logo + ", id=" + id
				+ ", description=" + description + ", type=" + type
				+ ", categoryId=" + categoryId + ", isMyChannel=" + isMyChannel
				+ ", isHD=" + isHD + ", isSeekable=" + isSeekable
				+ ", streamUrl=" + streamUrl + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(categoryId);
		dest.writeString(description);
		dest.writeString(logo);
		dest.writeString(name);
		dest.writeString(streamUrl);
		dest.writeString(type);
		dest.writeBooleanArray(new boolean[]{isHD, isMyChannel, isSeekable});
	}

	public static final Parcelable.Creator<Channel> CREATOR = new Creator<Channel>() {
		
		@Override
		public Channel[] newArray(int size) {
			return new Channel[size];
		}
		
		@Override
		public Channel createFromParcel(Parcel source) {
			return new Channel(source);
		}
	};

	
	private Channel(Parcel parcel) {
		id = parcel.readString();
		categoryId = parcel.readString();
		description = parcel.readString();
		logo = parcel.readString();
		name = parcel.readString();
		streamUrl = parcel.readString();
		type = parcel.readString();
		boolean[] bools = new boolean[3];
		parcel.readBooleanArray(bools);
		isHD = bools[0];
		isMyChannel = bools[1];
		isSeekable = bools[2];
	}
	
}
