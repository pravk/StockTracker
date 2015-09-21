package mobile.pk.com.stocktracker.dao;

import java.util.List;

public class BlogComment {
	
	private long commentDate;
	private String user;
	private String avatarUrl;
	private String profileUrl;
	private String comment;
	
	private List<BlogComment> replies;

	public long getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(long commentDate) {
		this.commentDate = commentDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<BlogComment> getReplies() {
		return replies;
	}

	public void setReplies(List<BlogComment> replies) {
		this.replies = replies;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

}
