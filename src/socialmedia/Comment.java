package socialmedia;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.io.*;

public class Comment {

    /*
     * This Class Creates objects related to the comments and will help the program
     * for creating and managing comments in an Object oriented way
     */

    public static String username;
    public static int CommentID;
    public static List<Map<String, Object>> AllComments = new ArrayList<>();
    public static Map<String, Object> comment = new HashMap<>();
    public static ArrayList<Integer> Tree = new ArrayList<>();
    public static Map<Integer, Object> SaveTree = new HashMap<>();
    public static List<Map<Integer, Object>> LoadTree = new ArrayList<>();

    public Comment(String User, int Comment_ID) {
        // gathering the information needed for wanting to creat a comment
        this.username = User;
        this.CommentID = Comment_ID;
        this.Tree = new ArrayList<>();
    }

    public static int counter = 100; // so that it wont get confiused with UserID

    public static int IDForComments() {
        return counter++; // creating and returning an ID for Comment
    }

    public static void SetTheTree(int Key, ArrayList<Integer> Value) {
        SaveTree.put(Key, Value);
    }

    public static Map<Integer, Object> GetTheTree() {
        return SaveTree;
    }

    public static void SetLoadingTree(Map<Integer, Object> obj) {
        LoadTree.add(obj);
    }

    public static List<Map<Integer, Object>> GetLoadingTree() {
        return LoadTree;
    }

    public static void setCommentToPost(String message, int id) { // Setting the information for a comment that
                                                                  // refrences to a post
        comment.put("Sender", username);
        comment.put("PostID", CommentID);
        comment.put("Content", message);
        comment.put("Counted", false);
        comment.put("CommentFor", id);
        comment.put("Endorsed", false);
        comment.put("PostEndorsements", 0);
        comment.put("PostComments", 0);
        ArrayList<Integer> Tree = new ArrayList<>();
        Tree = PostCommentTree(id);
        comment.put("Tree", Tree);
        resetTree();
    }

    public static void setCommentToComment(String message, int id) {// Setting the information for a comment that
                                                                    // refrences to another comment
        comment.put("Sender", username);
        comment.put("PostID", CommentID);
        comment.put("Content", message);
        comment.put("Counted", false);
        comment.put("CommentFor", id);
        comment.put("Endorsed", false);
        comment.put("PostEndorsements", 0);
        comment.put("PostComments", 0);
        ArrayList<Integer> Tree = new ArrayList<>();
        Tree = CommentTree(id);
        comment.put("Tree", Tree);
        resetTree();

    }

    public static ArrayList<Integer> CommentTree(int CommentForID) {

        ArrayList<Integer> CommentTree = new ArrayList<>();
        for (Map<String, Object> comment : GetAllComments()) {
            if ((int) comment.get("PostID") == CommentForID) {
                CommentTree = new ArrayList<Integer>((ArrayList<Integer>) comment.get("Tree"));
            }
        }

        for (Integer ID : CommentTree) {
            if (ID == CommentForID) {
                return CommentTree;
            }
        }

        return addToTree(CommentTree, CommentForID);
    }

    public static ArrayList<Integer> PostCommentTree(int CommentFor) {

        for (Map<String, Object> posts : Post.GetAllPosts()) {
            if ((int) posts.get("PostID") == CommentFor) {
                addToTree(new ArrayList<>(), CommentFor);
            }
        }

        for (Map<String, Object> comment : GetAllComments()) {
            if ((int) comment.get("PostID") == CommentID) {
                return Tree;
            }
        }

        return Tree;
    }

    public static ArrayList<Integer> addToTree(ArrayList<Integer> CommentTree, int CommentID) {
        Tree = CommentTree;

        setTree(CommentID);

        return getTree();
    }

    public static void setTree(int id) {
        Tree.add(id);
    }

    public static ArrayList<Integer> getTree() {
        return Tree;
    }

    public static void resetTree() {
        Tree = new ArrayList<>();
    }

    public static void SetAllComments() {
        AllComments.add(comment);
    }

    public static void resetComment() {
        comment = new HashMap<>();
    }

    public static List<Map<String, Object>> GetAllComments() {
        return AllComments;
    }

    public static void addOneComment() {
        boolean FoundID = false;
        ArrayList<Integer> ToAdd = new ArrayList<>();

        for (Map<String, Object> comment : Comment.GetAllComments()) {
            ArrayList<Integer> CommentTree = (ArrayList<Integer>) comment.get("Tree");
            if ((boolean) comment.get("Counted") == false) {
                for (Integer ID : CommentTree) {
                    ToAdd.add(ID);
                }
                comment.put("Counted", true);
            }
        }

        for (Integer addID : ToAdd) {
            for (Map<String, Object> posts : Post.GetAllPosts()) {

                if ((int) posts.get("PostID") == addID) {

                    int num = (int) posts.get("PostComments");
                    num += 1;
                    posts.put("PostComments", num);
                }
            }

            for (Map<String, Object> comments : Comment.GetAllComments()) {
                if ((int) comment.get("PostID") == addID) {
                    int num = (int) comments.get("PostComments");
                    num += 1;
                    comments.put("PostComments", num);
                }
            }
        }

    }

    public static Map<String, Object> parseLine(String line) {
        /*
         * This is a Helper Method for extracting the data from the given file name ,
         * This method takes a String line
         * which represents one line of data in the original text file
         * 
         * Argument(s) :
         * ---------------
         * String line : One line of text data stored in a file name
         * ---------------
         * Note(s) : This method detects the "," and ":" signs to seperate key value
         * pairs
         * ---------------
         * Return(s) : a key value Map which represents one Comment ;
         */
        Map<String, Object> Block = new HashMap<>();
        String[] section = line.split(",");
        for (String part : section) {
            String[] keyVal = part.split(":", 2);
            if (keyVal.length == 2) {
                String Key = keyVal[0].trim();
                Object Value = keyVal[1].trim();
                if (Value.toString().matches("-?\\d+")) {
                    Value = Long.parseLong(Value.toString());
                } else if (Key.equals("Tree")) {
                    Value = Arrays.asList(Value.toString().replaceAll("\\[|\\]", "").split("\\s*,\\s*"));
                }
                Block.put(Key, Value);
            } else {
                System.err.println("Invalid input line: " + line);
            }
        }
        return Block;
    }

    public static Map<Integer, Object> GetTheTree(String line) {
        /*
         * This is a Helper Method for extracting the Tree data from the Platform ,
         * because the tree array list in the text file
         * is hard to read we store it in another format to the same file and we use
         * this functoin to read that data
         * 
         * Argument(s) :
         * ---------------
         * String line : One line of text data stored in a file name
         * ---------------
         * Note(s) : This method detects the "," and ":" signs to detect elements of an
         * arraylist
         * ---------------
         * Return(s) :a key value pair Map, The key is the PostID that the tree belongs
         * to and value is a HashMap worth of tree IDs ;
         */
        Map<Integer, Object> data = new HashMap<>();
        String[] section = line.split(":");
        int treeID = Integer.parseInt(section[0]);
        String[] treeVal = section[1].split(", ");

        ArrayList<Integer> valueList = new ArrayList<>();
        for (String ID : treeVal) {
            valueList.add(Integer.parseInt(ID));

            data.put(treeID, valueList);
        }
        return data;
    }

    public static void LoadTree(List<Map<Integer, Object>> tree) {
        /*
         * This is a Helper Method for extracting the Tree data from the Platform ,
         * ther List<Map<Integer, Object>> tree is a List Worth of Map data which the elements have been added to it for when 
         * we want to load tha platform , keys represent IDs and values Represent Trees , 
         * This is a void method that places the trees in their IDs 
         * 
         * Argument(s) :
         * ---------------
         * List<Map<Integer, Object>> tree : The list of Maps that are ID and ArrayList which need to be put togother for loading the platform 
         * ---------------
         * Note(s) : This method detects the "," and ":" signs to detect elements of an Tree 
         * arraylist
         * ---------------
         * Return(s) : void method 
         */
        if (!tree.isEmpty()) {
            tree.remove(0);
        }
        for (Map<Integer, Object> commentTree : tree) {
            for (Entry<Integer, Object> AllEnterys : commentTree.entrySet()) {
                Integer Key = AllEnterys.getKey();
                Object Value = AllEnterys.getValue();
                if (Value instanceof List) {
                    List<Integer> treeList = (List<Integer>) Value;

                    for (Map<String, Object> comment : GetAllComments()) {
                        if (Integer.parseInt(comment.get("PostID").toString()) == Key) {
                            comment.put("Tree", treeList);
                        }
                    }

                }
            }
        }
    }

    public static void SetPostComments() {
         /*
         * This is a void method for seting the number of comments for posts and other comments 
         * In this method we extract all the IDs of the Tree key with the value of an ArrayList and 
         * 
         * 
         * 
         * 
         * Argument(s) :
         * ---------------
         * List<Map<Integer, Object>> tree : The list of Maps that are ID and ArrayList which need to be put togother for loading the platform 
         * ---------------
         * Note(s) : This method detects the "," and ":" signs to detect elements of an Tree 
         * arraylist
         * ---------------
         * Return(s) : void method 
         */
        ArrayList<Integer> AllCommentFor = new ArrayList<>();
        for (Map<String, Object> comment : GetAllComments()) {
            if ((boolean) comment.get("Counted") == false) {
                comment.put("Counted", true);
                ArrayList<Integer> CommentTree = (ArrayList<Integer>) comment.get("Tree");
                for (Integer ID : CommentTree) {
                    AllCommentFor.add(ID);
                }
            }
        }

        for (Integer ID : AllCommentFor) {
            for (Map<String, Object> comments : GetAllComments()) {
                if (ID == (int) comments.get("PostID")) {
                    int num = (int) comments.get("PostComments");
                    num += 1;
                    comments.put("PostComments", num);
                }
            }
            for (Map<String, Object> posts : Post.GetAllPosts()) {
                if (ID == (int) posts.get("PostID")) {
                    int num = (int) posts.get("PostComments");
                    num += 1;
                    posts.put("PostComments", num);
                }
            }
        }
    }

}
