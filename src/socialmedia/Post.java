package socialmedia;

import java.util.*;
import java.io.*;

public class Post {

    // The design of the Post , Comment and User Files should change and be more object oriented masalan 

    public static  String username;
    public static int PostID;
    public static List<Map<String, Object>> AllPosts = new ArrayList<>(); 
    public static Map<String, Object> Post = new HashMap<>(); 

    public Post(String User, int Post_ID){
        this.username = User ;
        this.PostID = Post_ID ;
    }

    public static int counter = 1 ;
    public static int IDForPosts(){
        return counter++ ;
    }

    public static void setPost(String message){
        Post.put("Sender", username); 
        Post.put("PostID", PostID); 
        Post.put("Content", message); 
        Post.put("Endorsed" , false); 
        Post.put("PostEndorsements", 0); 
        Post.put("PostComments", 0); 
        User.addOne(username, "NumberOfPosts");
    }

    public static void setEndorsedPost(String message, int ID, String Sender){
        Post.put("Sender", username); 
        Post.put("PostID", PostID); 
        Post.put("Content", message);
        Post.put("Endorsed" , true); 
        Post.put("EndorsedPost", ID); 
        Post.put("EndorsedPerson", Sender); 
        User.addOne(username, "NumberOfPosts");
        User.addOne(Sender, "TotalEndorsements");
        addOneEn("PostEndorsements", ID);
    }

    public static void SetEndorsedPost(){
        AllPosts.add(Post);
    }

    public static void SetAllPosts(){
        AllPosts.add(Post); 
    }

    public static void resetPost(){
        Post = new HashMap<>(); 
    }

    public static int counter2 = -1 ;
    public static int IDForEmptyPosts(){
        return counter2 -- ;
    }

    public static void createEmptyPost(int EmptyID){
        Post.put("Sender", "Empty Post");
        Post.put("PostID", EmptyID);
        Post.put("Content", "The original content was removed from the system and is no longer available.");
        
    }

    public static List<Map<String,Object>> GetAllPosts(){
        return AllPosts ;
    }

    public static List<Integer> findAllSubID (int id , List<Integer>  PostChildrenID){

        boolean CommentFound = false ; 
        boolean found = false ;
        for (Map<String, Object> posts : GetAllPosts()){
            if (id == Integer.parseInt(posts.get("PostID").toString())){
                found = true ;
                PostChildrenID.add(id) ;
                for (Map<String,Object> comment : Comment.GetAllComments()){
                    if (id == Integer.parseInt(comment.get("CommentFor").toString())){
                        CommentFound = true ;
                        int ID = (int) comment.get("PostID"); 
                        findAllSubID(ID, PostChildrenID); 
                    }
                }
                if (!CommentFound){
                    return PostChildrenID ;
                }
            }
        }
        if (!found) {
            boolean subComment = false;
            for (Map<String, Object> comments : Comment.GetAllComments()){
                if ((int) comments.get("PostID") == id){
                    subComment = true ;
                    PostChildrenID.add((int) comments.get("PostID")); 
                    for (Map<String,Object> sub : Comment.GetAllComments()){
                        if((int) comments.get("PostID") == (int) sub.get("CommentFor")){
                            int ID = (int) sub.get("PostID"); 
                            findAllSubID(ID, PostChildrenID); 
                        }
                    }
                }
        }
        if (!subComment){
            return PostChildrenID ;
        }
    }
    
        return PostChildrenID ; 
    }


    public static void addOneEn(String key, int id) {
        if (key == "PostEndorsements") {
            for (Map<String, Object> posts : GetAllPosts()) {
                if ((Integer) posts.get("PostID") == id && id > 0) {
                    int num = (Integer) posts.get("PostEndorsements");
                    num += 1;
                    posts.put("PostEndorsements", num);
                }
            }
        }
    }

    public static Map<String, Object> parseLine(String line) {
        
        Map<String, Object> Block = new HashMap<>();
        String[] section = line.split(",");
        for (String part : section) {
            String[] keyVal = part.split(":", 2);
            if (keyVal.length == 2) {
                String key = keyVal[0].trim();
                Object value = keyVal[1].trim();
                if (value.toString().matches("-?\\d+")) {
                    value = Long.parseLong(value.toString());
                } else if (key.equals("Tree")) {
                    value = Arrays.asList(value.toString().replaceAll("\\[|\\]", "").split("\\s*,\\s*"));
                }
                Block.put(key, value);
            } else {
                System.err.println("Invalid input line: " + line);
            }
        }
        return Block;
    }
    
}
