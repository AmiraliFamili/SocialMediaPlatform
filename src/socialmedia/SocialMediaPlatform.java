package socialmedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * SocialMediaPlatform interface. This interface is a more elaborated version of
 * the MiniSocialMediaPlatform. The no-argument constructor of a class
 * implementing this interface should initialise the SocialMediaPlatform as an
 * empty platform with no initial accounts nor posts within it. For Pair
 * submissions.
 * 
 * @author Diogo Pacheco
 * @version 1.0
 *
 */

 
public interface SocialMediaPlatform extends MiniSocialMediaPlatform {

	// Account-related methods ****************************************


	public default int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
		/*
		 * This is a override version of createAccount and it creates a profle for users
		 * with a given
		 * handle which acts as Username , Since this is the override version it returns
		 * 0 , Normally this function
		 * will return the ID generated for the User .
		 * 
		 * Argument(s) :
		 * ---------------
		 * String handle : Acts as Username in The Platform
		 * ---------------
		 * Note(s) : This is The Override version of the method and it returns 0
		 * ---------------
		 * Return(s) : return 0 ;
		 */
		if (handle == null) {
			throw new InvalidHandleException("Username can't be empty");
		} else if (handle.length() > 30) {
			throw new InvalidHandleException("Username should be a single word less than 30 characters");
		}
		for (int i = 0; i < handle.length(); i++) {
			if (handle.charAt(i) == ' ') {
				throw new InvalidHandleException("Username must be a single word no white spaces are allowed");
			}
		}

		int UserId = User.IDForUser();

		for (Map<String, Object> Account : User.GetAccounts()) {
			if ((String) Account.get("Username") == handle) {
				throw new IllegalHandleException("Username is taken by another account choose another one");
			}
		}

		User obj = new User(handle, UserId); // creating an Object For The User

		obj.setProfile();
		obj.setAccount();
		obj.resetProfile();

		return UserId;
		//return 0;
	}
	public default int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
		/*
		 * This is a override version of createAccount and it creates a profle for users
		 * with a given
		 * handle which acts as Username and a description which is a string text, Since
		 * this is the override version it returns 0 , Normally this function
		 * will return the ID generated for the User .
		 * 
		 * Argument(s) :
		 * ---------------
		 * String handle : Acts as Username in The Platform
		 * String description : A simple text containing information related to user in
		 * String Format
		 * ---------------
		 * Note(s) : This is The Override version of the method and it returns 0
		 * ---------------
		 * Return(s) : return 0 ;
		 */
		if (handle == null) { // We should validate the description it self
			throw new InvalidHandleException("Username can't be empty");
		} else if (handle.length() > 30) {
			throw new InvalidHandleException("Username should be a single word less than 30 characters");
		}
		for (int i = 0; i < handle.length(); i++) {
			if (handle.charAt(i) == ' ') {
				throw new InvalidHandleException("Username must be a single word no white spaces are allowed");
			}
		}

		int UserId = User.IDForUser();

		for (Map<String, Object> Account : User.GetAccounts()) {
			if ((String) Account.get("Username") == handle) {
				throw new IllegalHandleException("Username is taken by another account choose another one");
			}
		}

		User obj = new User(handle, UserId);// creating an Object For The User

		obj.setProfile(description);
		obj.setAccount();
		obj.resetProfile();

		return UserId;
		// return 0;
	}

	public default void removeAccount(int id) throws AccountIDNotRecognisedException { // works Just Change The Names
		/*
		 * This is a override version of removeAccount and it removes the Profile from
		 * the platform with a
		 * given id , when the profile is removed all of it's posts and comments will be
		 * removed too ,
		 * 
		 * 
		 * Argument(s) :
		 * ---------------
		 * int id : The id of the User that is being removed from the platform
		 * ---------------
		 * Note(s) : This is The Override version of the method and this is a void
		 * method
		 * ---------------
		 * Return(s) : void method
		 */
		String username = null;
		for (Iterator<Map<String, Object>> iterator = User.GetAccounts().iterator(); iterator.hasNext();) {
			Map<String, Object> account = iterator.next();
			if (id == (int) account.get("UserID")) {
				username = (String) account.get("Username");
				iterator.remove();
				break;
			}
		}
		if (username == null) {
			throw new AccountIDNotRecognisedException(
					"The entered id does not exist in the platform. Please enter a valid id.");
		}

		List<Integer> postIds = new ArrayList<>();
		for (Iterator<Map<String, Object>> iterator = Post.GetAllPosts().iterator(); iterator.hasNext();) {
			Map<String, Object> post = iterator.next();
			if (username.equals(post.get("Sender")) || username.equals(post.get("EndorsedPerson"))) {
				postIds.add((int) post.get("PostID"));
				iterator.remove();
			}
		}

		for (Iterator<Map<String, Object>> iterator = Comment.GetAllComments().iterator(); iterator.hasNext();) {
			Map<String, Object> comment = iterator.next();
			int commentFor = (int) comment.get("CommentFor");
			String sender = (String) comment.get("Sender");
			if (postIds.contains(commentFor)) {
				iterator.remove();
			} else if (sender != null && sender.equals(username)) {
				iterator.remove();
			}
		}
	}


	/**
	 * The method removes the account with the corresponding handle from the
	 * platform. When an account is removed, all of their posts and likes should
	 * also be removed.
	 * <p>
	 * The state of this SocialMediaPlatform must be be unchanged if any exceptions
	 * are thrown.
	 * 
	 * @param handle account's handle.
	 * @throws HandleNotRecognisedException if the handle does not match to any
	 *                                      account in the system.
	 */
	public default void removeAccount(String handle) throws HandleNotRecognisedException { // Change the Names
		/*
		 * This is a override version of removeAccount and it removes the Profile from
		 * the platform with a
		 * given handle , when the profile is removed all of it's posts and comments
		 * will be removed too
		 * 
		 * Argument(s) :
		 * ---------------
		 * int id : The id of the User that is being removed from the platform
		 * ---------------
		 * Note(s) : This is The Override version of the method and this is a void
		 * method
		 * ---------------
		 * Return(s) : void method
		 */
		String username = null;
		for (Iterator<Map<String, Object>> iterator = User.GetAccounts().iterator(); iterator.hasNext();) {
			Map<String, Object> account = iterator.next();
			if (handle.equals(account.get("Username"))) {
				username = (String) account.get("Username");
				iterator.remove();
				break;
			}
		}
		if (username == null) {
			throw new HandleNotRecognisedException(
					"The entered id does not exist in the platform. Please enter a valid id.");
		}

		List<Long> postIds = new ArrayList<>();
		for (Iterator<Map<String, Object>> iterator = Post.GetAllPosts().iterator(); iterator.hasNext();) {
			Map<String, Object> post = iterator.next();
			if (username.equals(post.get("Sender")) || username.equals(post.get("EndorsedPerson"))) {
				postIds.add((Long) post.get("PostID"));
				iterator.remove();
			}
		}

		for (Iterator<Map<String, Object>> iterator = Comment.GetAllComments().iterator(); iterator.hasNext();) {
			Map<String, Object> comment = iterator.next();
			int commentFor = (int) comment.get("CommentFor");
			String sender = (String) comment.get("Sender");
			if (postIds.contains(commentFor)) {
				iterator.remove();
			} else if (sender != null && sender.equals(username)) {
				iterator.remove();
			}
		}
	}


	/**
	 * The method updates the description of the account with the respective handle.
	 * <p>
	 * The state of this SocialMediaPlatform must be be unchanged if any exceptions
	 * are thrown.
	 * 
	 * @param handle      handle to identify the account.
	 * @param description new text for description.
	 * @throws HandleNotRecognisedException if the handle does not match to any
	 *                                      account in the system.
	 */


	 public default void changeAccountHandle(String oldHandle, String newHandle)
			throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
		/*
		 * This is a override version of changeAccountHandle and it changes the
		 * account's handle (Username)
		 * throughout the Platform even the posts and comments
		 * 
		 * Argument(s) :
		 * ---------------
		 * String oldHandle : The Username that it's name is being changed
		 * String newHandle : The Username that handle should change to
		 * ---------------
		 * Note(s) : This is The Override version of the method and this is a void
		 * method
		 * ---------------
		 * Return(s) : void method
		 */
		if (newHandle == null) {
			throw new InvalidHandleException("Username can't be empty");
		} else if (newHandle.length() > 30) {
			throw new InvalidHandleException("Username should be a single word less than 30 characters");
		}
		for (int i = 0; i < newHandle.length(); i++) {
			if (newHandle.charAt(i) == ' ') {
				throw new InvalidHandleException("Username must be a single word no white spaces are allowed");
			}
		}

		boolean AccountFound = false;
		for (Map<String, Object> Account : User.GetAccounts()) {

			if (Account.get("Username") == newHandle) {
				throw new IllegalHandleException(
						"The Username you have choosen already exists in the platform, choose another username to replace");
			}
			if (Account.get("Username") == oldHandle) {
				AccountFound = true;
				Account.remove("Username", oldHandle);
				Account.put("Username", newHandle);
				break;
			}
		}

		if (!AccountFound) {
			throw new HandleNotRecognisedException("The Username doesn't exists in the platform");
		}

		for (Map<String, Object> posts : Post.GetAllPosts()) {
			if ((String) posts.get("Sender") == oldHandle) {
				posts.remove("Username", oldHandle);
				posts.put("Username", newHandle);
			}

		}
		for (Map<String, Object> comment : Comment.GetAllComments()) {
			if ((String) comment.get("Sender") == oldHandle) {
				comment.remove("Username", oldHandle);
				comment.put("Username", newHandle);
			}

		}

	}

	


	public default void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException { // Morgan
		// TODO Auto-generated method stub
















	}

	public default String showAccount(String handle) throws HandleNotRecognisedException {
		/*
		 * This is a override version of showAccount , This method will return 0 because
		 * it's an override
		 * however, normally this function will get a String as handle (Username) in
		 * platform and will return a string
		 * containing all the information related to this Account
		 * 
		 * Argument(s) :
		 * ---------------
		 * String handle : The Username of the User in The platform
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : UserInformation (information about Account)
		 */
		String UserInformation = "NotSet";
		for (Map<String, Object> Username : User.GetAccounts()) {
			if (handle.equals(Username.get("Username"))) {
				UserInformation = "<pre> \nID: [" + Username.get("UserID") + "]\nHandle: ["
						+ Username.get("Username") + "]\nDescription: [" + Username.get("Description")
						+ "]\nPost count: [" + Username.get("NumberOfPosts") + "]\nEndorse count: ["
						+ Username.get("TotalEndorsements") + "]\n</pre>";
			}
		}
		if (UserInformation == "NotSet") {
			throw new HandleNotRecognisedException("The Username entered does not exists in the platform");
		}
		return UserInformation;

		// return null;
	}


	// End Post-related methods ****************************************

	public default int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
		/*
		 * This is a override version of createPost , This method will get a String for
		 * handle (or Username) and one String for
		 * message , and will create a post message with the provided information and
		 * finally it should return the
		 * Post ID (The ID assigned to the created post) however, since this is an
		 * override version it will return 0 ;
		 * 
		 * Argument(s) :
		 * ---------------
		 * String handle : The Username of the user who wants to create a post
		 * String message : The message that the created post should contain
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : PostID (The ID assigned to the created Post)
		 */
		boolean Account = false;
		for (Map<String, Object> Username : User.GetAccounts()) {
			if ((String) Username.get("Username") == handle) {
				Account = true;
				if (message == null || message.isEmpty()) {
					throw new InvalidPostException(
							"The Post you want to share is empty, can not share an post with no content");
				} else if (message.length() > 100) {
					throw new InvalidPostException("The Post must be less than 100 characters");
				} else {
					break;
				}

			}
		}
		if (!Account) {
			throw new HandleNotRecognisedException("The Username choosen does not exists in the platform");
		}

		int PostID = Post.IDForPosts();
		Post obj = new Post(handle, PostID);

		Post.setPost(message);
		Post.SetAllPosts();
		Post.resetPost();

		return PostID;
		// return 0 ;
	}

	public default int endorsePost(String handle, int id)
			throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
		/*
		 * This is a override version of endorsePost , this method will get a String as
		 * handle which contains the Username of the person endorseing a post
		 * and an integer as id which contains the id of the post that is being endorsed
		 * , the method creates a post which is an endorsement for the post
		 * with given id and a normal post which is not commentable nor endorsable
		 * anymore , this method should return the ID of the post created as endorsement
		 * post ,
		 * However, since this is the override version of the method it returns 0 ;
		 * Argument(s) :
		 * ---------------
		 * String handle : The Username of the user who wants to endorse a post
		 * String message : The id of the post being endorsed
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : 0
		 */
		if (id < 0) {
			throw new PostIDNotRecognisedException("The Post Has Been Removed From The Platform");
		}
		boolean Account = false;
		int PostID = 0;
		boolean Post_ID = false;
		String Sender;
		for (Map<String, Object> Username : User.GetAccounts()) {
			if ((String) Username.get("Username") == handle) {
				Account = true;
			}
			for (Map<String, Object> Posts : Post.GetAllPosts()) {
				if ((int) Posts.get("PostID") == id && id > 0) {
					Post_ID = true;
					Sender = (String) Posts.get("Sender");
					if ((boolean) Posts.get("Endorsed") == true) {
						throw new NotActionablePostException(
								"The post you want to endorse has been endorsed by this user from another user, you can not endorse a post if it is not original");
					}
					String Message = "<p><code>EP@" + (String) Posts.get("Sender") + ": "
							+ (String) Posts.get("Content") + "</code></p>";
					// Find Out the Problem with This ...

					PostID = Post.IDForPosts();
					Post obj = new Post(handle, PostID);

					Post.setEndorsedPost(Message, id, Sender);
					Post.SetAllPosts();
					Post.resetPost();

					return PostID;
				}
			}
		}

		if (!Account) {
			throw new HandleNotRecognisedException("The Username choosen does not exists in the platform");
		}
		if (!Post_ID) {
			throw new PostIDNotRecognisedException(
					"The given ID does not match to any post in the platform, enter a valid ID");
		}

		return PostID;
	}

	public default int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
			PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
		/*
		 * This is a override version of commentPost, it creates a comment referencing
		 * to an existing comment or post ,
		 * it gets a String as Username , an Integer as id and a String as Message ,
		 * this method will return 0 because it is a override version of commentPost ,
		 * however it should return the ID of the created comment .
		 * 
		 * 
		 * Argument(s) :
		 * ---------------
		 * String handle : The Username that wants to create a post
		 * int id : The id of the post being commented (referenced)
		 * String message : The message that the comment should contain
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : 0
		 */
		boolean Account = false;
		boolean Post_ID = false;
		boolean Comment_ID = false;
		if (message == null || message.isEmpty()) {
			throw new InvalidPostException(
					"The Post you want to share is empty, can not share an post with no content");
		} else if (message.length() > 100) {
			throw new InvalidPostException("The Post must be less than 100 characters");
		}
		for (Map<String, Object> Username : User.GetAccounts()) {
			if ((String) Username.get("Username") == handle) {
				Account = true;
				for (Map<String, Object> posts : Post.GetAllPosts()) {
					if (id == (int) posts.get("PostID") && id > 0) {
						Post_ID = true;
						if ((boolean) posts.get("Endorsed") == true) {
							throw new NotActionablePostException(
									"You can not comment on an Endorsed post, Commenting is only allowed for Comments and Original Posts");
						}

					}
				}
				for (Map<String, Object> comments : Comment.GetAllComments()) {
					if ((int) comments.get("PostID") == id && id > 0) {
						Comment_ID = true;
						if ((boolean) comments.get("Endorsed") == true) {
							throw new NotActionablePostException(
									"You can not comment on an Endorsed post, Commenting is only allowed for Comments and Original Posts");
						}
					}
				}

			}
		}

		if (!Account) {
			throw new HandleNotRecognisedException("The Username choosen does not exists in the platform");
		}
		if (!Post_ID && !Comment_ID) {
			throw new PostIDNotRecognisedException(
					"The given ID does not match to any post in the platform, enter a valid ID");
		}

		int CommentID = Comment.IDForComments();
		Comment obj = new Comment(handle, CommentID);

		if (Post_ID) {
			Comment.setCommentToPost(message, id);
			Comment.SetAllComments();
			Comment.resetComment();

		}
		if (Comment_ID) {
			Comment.setCommentToComment(message, id);
			Comment.SetAllComments();
			Comment.resetComment();
			// Comment.AddSubCommentsToOriginal(); // Why is this never executing

		}

		Comment.SetPostComments();

		return CommentID;
		// return 0;
	}

	public default void deletePost(int id) throws PostIDNotRecognisedException { // Change Names, It Works
		/*
		 * This is a override version of deletePost, This methode will get an id
		 * belonging to a post or comment ,
		 * and it will delete the post from the platform , All comments that reference
		 * to this Post or Comment will reference to an
		 * emplty post instead , this is a void methode and it is not returning any
		 * value
		 * 
		 * Argument(s) :
		 * ---------------
		 * String handle : The Username that wants to create a post
		 * int id : The id of the post being commented (referenced)
		 * String message : The message that the comment should contain
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : void methode
		 */
		boolean removed = false;
		Iterator<Map<String, Object>> iterator = Post.GetAllPosts().iterator();
		while (iterator.hasNext()) {
			Map<String, Object> account = iterator.next();
			if (id == (int) account.get("PostID")) {
				iterator.remove();
				removed = true;
			}
			if (account.containsKey("EndorsedPost") && account.get("EndorsedPost") != null
					&& (int) account.get("EndorsedPost") == id) {
				iterator.remove();
				removed = true;
			}
		}

		Iterator<Map<String, Object>> iterator2 = Comment.GetAllComments().iterator();
		while (iterator2.hasNext()) {
			Map<String, Object> account = iterator2.next();
			if (id == (int) account.get("PostID")) {
				iterator2.remove();
				removed = true;
			}
		}

		if (!removed) {
			throw new PostIDNotRecognisedException(
					"The given ID does not match to any post in the platform, Enter a Valid ID");
		}

		int ID = (int) Post.IDForEmptyPosts();

		Post obj = new Post("None", ID);

		Post.createEmptyPost(ID);

		
		for (Map<String, Object> comments : Comment.GetAllComments()) {
			if ((int) comments.get("CommentFor") == id && id > 0) {
				comments.put("CommentFor", ID);
			}

		}

	}

	public default String showIndividualPost(int id) throws PostIDNotRecognisedException {
		/*
		 * This is a override version of showIndividualPost, It gets an Integer as an id
		 * that belongs to a post or comment in the platform , this methode
		 * will gather all information related to the post with given id and return them
		 * as a String , However since this is the override version , it will
		 * return 0
		 * Argument(s) :
		 * ---------------
		 * int id : The id of the Post (Or Comment) that it's information is returning
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : 0
		 */
		boolean post = false;
		for (Map<String, Object> posts : Post.GetAllPosts()) {
			if ((id == Integer.parseInt(posts.get("PostID").toString())) && id > 0) {
				post = true;
				String IndividualPost = "<pre>\nID: [" + posts.get("PostID") + "]\nAccount: ["
						+ posts.get("Sender") + "]\nNumber Of Endorsements: [" + posts.get("PostEndorsements")
						+ "]\nNumber Of Comments: [" + posts.get("PostComments") + "]\nMessage: ["
						+ posts.get("Content") + "]\n</pre>";
				return IndividualPost;
				// return 0 ;
			}
		}
		for (Map<String, Object> comment : Comment.GetAllComments()) {
			if ((int) comment.get("PostID") == id) {
				post = true;
				String IndividualPost = "<pre>\nID: [" + (int) comment.get("PostID") + "]\nAccount: ["
						+ comment.get("Sender") + "]\nNumber Of Endorsements: [" + comment.get("PostEndorsements")
						+ "]\nNumber Of Comments: [" + comment.get("PostComments") + "]\n Message: ["
						+ comment.get("Content") + "]\n</pre>";
				return IndividualPost;
				// return 0 ;
			}
		}
		if (!post) {
			throw new PostIDNotRecognisedException(
					"The given id does not match to any post in the platform, Enter a valid ID");
		}

		// return "Unknown Error" ;
		return null;
	}


	public default StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
		/*
		 * This is a override version of showPostChildrenDetails , it will get an
		 * Integer as id of a Post or Comment ,
		 * and it will show the Individual information of each comment that counts as a
		 * subcomment for the Post or Comment with the id passed to
		 * this function , the method should eventually return a string builder
		 * containing the tree of individual informatoin of every subcomment of the id ,
		 * However, since this is an override version of the method it will return 0
		 * 
		 * Argument(s) :
		 * ---------------
		 * int id : The id of the Post (Or Comment) that it's subcomments should be
		 * returned
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : 0
		 */
		StringBuilder PostChildren = new StringBuilder();
		String Message;
		String Space = "  ";
		boolean isComment = false;
		int Repeat = 1;
		ArrayList<Integer> Tree = new ArrayList<>();

		for (Map<String, Object> comment : Comment.GetAllComments()) {
			if ((int) comment.get("PostID") == id) {
				isComment = true;
			}
		}
		boolean isPost = false;
		if (!isComment) {
			for (Map<String, Object> post : Post.GetAllPosts()) {
				if ((int) post.get("PostID") == id) {
					isPost = true;
					Message = "<pre>\nID: [" + (int) post.get("PostID") + "]\nAccount: ["
							+ post.get("Sender") + "]\nNumber Of Endorsements: [" + post.get("PostEndorsements")
							+ "]\nNumber Of Comments: [" + post.get("PostComments") + "]\n Message: ["
							+ post.get("Content") + "]\n</pre>\n";

					PostChildren.append(Message);
				}
			}
			for (Map<String, Object> comment : Comment.GetAllComments()) {
				if ((int) comment.get("CommentFor") == id) {
					isComment = true;
				}
			}
		}
		if (isComment) {
			for (Map<String, Object> comment : Comment.GetAllComments()) {
				Tree = (ArrayList<Integer>) comment.get("Tree");
				for (Integer ID : Tree) {
					if (ID == id || ID == (int) comment.get("PostID")) {
						for (Integer PostID : Tree) {
							if (PostID != (int) comment.get("PostID")) {
								Repeat = Tree.indexOf(PostID);
								Repeat += 1;
							}
						}

						Message = Space.repeat(Repeat) + "<pre>\n" + Space.repeat(Repeat) + "ID: ["
								+ (int) comment.get("PostID") + "]\n" + Space.repeat(Repeat) + "Account: ["
								+ comment.get("Sender") + "]\n" + Space.repeat(Repeat) + "Number Of Endorsements: ["
								+ comment.get("PostEndorsements")
								+ "]\n" + Space.repeat(Repeat) + "Number Of Comments: [" + comment.get("PostComments")
								+ "]\n" + Space.repeat(Repeat) + "Message: ["
								+ comment.get("Content") + "]\n" + Space.repeat(Repeat) + "</pre>\n";

						PostChildren.append(Message);

					}

				}

			}
		}

		if (!isPost) {
			// Error : write an error
		}

		return PostChildren;
		// return null;
	}

	// Analytics-related methods ****************************************

	/**
	 * This method returns the current total number of accounts present in the
	 * platform. Note, this is NOT the total number of accounts ever created since
	 * the current total should discount deletions.
	 * 
	 * @return the total number of accounts in the platform.
	 */
	public default int getNumberOfAccounts() {

		int accountCounter = 0 ;

	     for (Map<String,Object> Username : User.GetAccounts()){

			if ((String)Username.get("Username") != null){
				 accountCounter += 1;

			}

		}

		return accountCounter;
	}


	/**
	 * This method returns the current total number of original posts (i.e.,
	 * disregarding endorsements and comments) present in the platform. Note, this
	 * is NOT the total number of posts ever created since the current total should
	 * discount deletions.
	 * 
	 * @return the total number of original posts in the platform.
	 */
	public default int getTotalOriginalPosts() { 

		int postCounter = 0 ;

	    for (Map<String,Object> Username : User.GetAccounts()){

			if ((Integer) Username.get("NumberofPosts") != null){
			    postCounter += (int)Username.get("NumberofPosts");

			}

		}
			      
		return postCounter ;

	}

	/**
	 * This method returns the current total number of endorsement posts present in
	 * the platform. Note, this is NOT the total number of endorsements ever created
	 * since the current total should discount deletions.
	 * 
	 * @return the total number of endorsement posts in the platform.
	 */
	public default int getTotalEndorsmentPosts() { 
		
		int endorsmentCounter = 0 ;

	    for (Map<String,Object> Username : User.GetAccounts()){

			if ((Integer) Username.get("TotalEndorsements") != null){
			    endorsmentCounter += (int) Username.get("TotalEndorsements");

		    }

		} 
			 
	    return endorsmentCounter;
	}

	/**
	 * This method returns the current total number of comments posts present in the
	 * platform. Note, this is NOT the total number of comments ever created since
	 * the current total should discount deletions.
	 * 
	 * @return the total number of comments posts in the platform.
	 */
	public default int getTotalCommentPosts() { 
		
        int commentCounter = 0 ;

	    for(Map<String,Object> posts : Post.GetAllPosts()){

	        if((Integer) posts.get("PostComments") != null){
			    commentCounter += (int) posts.get("PostComments");

			}

		}

	    return commentCounter;

	}
	

	public default int getMostEndorsedPost() {
		/*
		 * This is a override version of getMostEndorsedPost , it will find the Post
		 * With the Maximum number of endorsements ,
		 * however since this is an override version of the code , it wont return that
		 * value and it returns 0 instead
		 * Argument(s) :
		 * ---------------
		 * No Arguments is Passed To This methode
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : 0
		 */
		int max = 0;
		int maxID = 0;
		for (Map<String, Object> posts : Post.GetAllPosts()) {

			if ((Integer) posts.get("PostEndorsements") != null) {
				if (max < (int) posts.get("PostEndorsements")) {
					max = (int) posts.get("PostEndorsements");
					maxID = (int) posts.get("PostID");
				}
			}
		}

		if (max == 0) {
			return 0;
		}

		return maxID;
		//return 0;
	}

	public default int getMostEndorsedAccount() {
		/*
		 * This is a override version of getMostEndorsedAccount , this function will
		 * scan the Profiles in the platform
		 * and find the account with most endorsements , usually this method should
		 * return the id of that account however, since this is an
		 * override verion of the methode it only returns 0
		 * 
		 * Argument(s) :
		 * ---------------
		 * No Arguments is Passed To This methode
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : 0
		 */
		int max = 0;
		int maxID = 0;
		for (Map<String, Object> Username : User.GetAccounts()) {

			if ((Integer) Username.get("TotalEndorsements") != null) {
				if (max < (int) Username.get("TotalEndorsements")) {
					max = (int) Username.get("TotalEndorsements");
					maxID = (int) Username.get("UserID");
				}
			}
		}

		// return maxID;
		return 0;
	}

	public default void erasePlatform() {
		/*
		 * This is a override version of erasePlatform , This method will reset all data
		 * that exists in the platform ,
		 * including Profiles, Posts and Comments ...
		 * 
		 * Argument(s) :
		 * ---------------
		 * No Arguments is Passed To This methode
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : void
		 */
		Comment.GetAllComments().clear();
		Post.GetAllPosts().clear();
		User.GetAccounts().clear();
		Comment.SaveTree.clear();
		Comment.LoadTree.clear();
	}

	public default void savePlatform(String filename) throws IOException { // Change Names
		/*
		 * This is a override version of savePlatform , this method will get a String as
		 * Filename and save all the data that exists in the system ,
		 * in the given file name
		 * 
		 * Argument(s) :
		 * ---------------
		 * String filename : a random string to save the platform in a text file with
		 * the name , filename
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : void
		 */
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		writer.write("Break1\n");

		try {
			for (Map<String, Object> Accounts : User.GetAccounts()) {
				for (Map.Entry<String, Object> entry : Accounts.entrySet()) {
					writer.write(entry.getKey() + ":" + entry.getValue() + ",");
				}
				writer.write("\n");
			}
			//System.out.println("List data saved to " + filename);
		} catch (IOException e) {
			System.out.println("An error occurred while writing to the file.");
			e.printStackTrace();
		}

		writer.write("Break2\n");

		int PostID = 0;
		try {
			for (Map<String, Object> map : Comment.GetAllComments()) {
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if (entry.getKey().equals("PostID")) {
						PostID = (int) entry.getValue();
					}
					if (entry.getKey().equals("Tree")) {
						ArrayList<Integer> values = (ArrayList<Integer>) entry.getValue();

						Comment.SaveTree.put(PostID, values); // save the list with post id as key and array as value
																// ...
					} else {
						writer.write(entry.getKey() + ":" + entry.getValue() + ",");
					}
				}
				writer.write("\n");
			}

			// remove the Tree key from the map
			for (Map<String, Object> map : Comment.GetAllComments()) {
				map.remove("Tree");
			}

			//System.out.println("List data saved to " + filename);
		} catch (IOException e) {
			System.out.println("An error occurred while writing to the file.");
			e.printStackTrace();
		}

		writer.write("Break3\n");

		try {
			for (Map<String, Object> map : Post.GetAllPosts()) {
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					writer.write(entry.getKey() + ":" + entry.getValue() + ",");
				}
				writer.write("\n");
			}

			//System.out.println("List data saved to " + filename);
		} catch (IOException e) {
			System.out.println("An error occurred while writing to the file.");
			e.printStackTrace();
		}

		writer.write("Break4\n");

		// Save the SaveTree map after Break4
		for (Entry<Integer, Object> entry : Comment.SaveTree.entrySet()) {
			writer.write(entry.getKey() + ":" + entry.getValue().toString().replace("[", "").replace("]", "") + "\n");
		}

		writer.close();
	}

	public default void loadPlatform(String filename) throws IOException, ClassNotFoundException {
		/*
		 * This is a override version of loadPlatform , this method will get a String as
		 * Filename and load everything in that file into the platform
		 * 
		 * Argument(s) :
		 * ---------------
		 * String filename : The name of the file that the data should be extracted from
		 * it
		 * ---------------
		 * Note(s) : This is The Override version of the method
		 * ---------------
		 * Return(s) : void
		 */
		String currentBreak = "";
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Break")) {
					currentBreak = line;
				} else {
					if (currentBreak.equals("Break1")) {
						Map<String, Object> data = User.LoadUser(line);
						User.GetAccounts().add(data);
					} else if (currentBreak.equals("Break3")) {
						Map<String, Object> data = Post.parseLine(line);
						Post.GetAllPosts().add(data);
					} else if (currentBreak.equals("Break2")) {
						Map<String, Object> data = Comment.parseLine(line);
						Comment.GetAllComments().add(data);
					} else if (currentBreak.equals("Break4")) {
						Map<Integer, Object> data = Comment.GetTheTree(line);
						Comment.SetLoadingTree(data);
					}
				}
			}
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}

		Comment.LoadTree(Comment.LoadTree);

	}

	// End Management-related methods ****************************************

}