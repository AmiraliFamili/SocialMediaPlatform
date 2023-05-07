package socialmedia;

import java.util.*;
import java.io.*;

public class User {
    public static  String username;
    public static int id;
    public static List<Map<String, Object>> Accounts = new ArrayList<>(); 
    public static Map<String, Object> Profile = new HashMap<>(); 

    public User(String Username, int Id) {
       
        this.username = Username;
        this.id = Id;
    }

    public static void setProfile(){
        Profile.put("Username" , username ); 
        Profile.put("UserID" , id ); 
        Profile.put("Description" , "Not Set"); 
        Profile.put("NumberOfPosts", 0);
        Profile.put("TotalComments", 0);
        Profile.put("TotalEndorsements", 0);
    }
    public static void setProfile(String description){
        Profile.put("Username" , username ); 
        Profile.put("UserID" , id ); 
        Profile.put("Description" , description); 
        Profile.put("NumberOfPosts", 0);
        Profile.put("TotalComments", 0);
        Profile.put("TotalEndorsements", 0);
    }

    public static void setAccount(){
        Accounts.add(Profile);
    }

    public static void resetProfile(){
        Profile = new HashMap<>() ;
    }


    public static List<Map<String, Object>> GetAccounts(){
        return Accounts ;
    }

    public static int counter = 10000 ;
    public static int IDForUser(){
        return counter++ ;
    }

    public static void removeProfile(Map<String,Object> User){
        // This function removes an account
        GetAccounts().remove(User); 
    }

    public static Map<String, Object> LoadUser (String line) {
        Map<String, Object> Block = new HashMap<>();
        String[] parts = line.split(",");
        for (String part : parts) {
            String[] keyValue = part.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                Object value = keyValue[1].trim();
                if (value.toString().matches("-?\\d+")) {
                    value = Long.parseLong(value.toString());
                }
                Block.put(key, value);
            } else {
                System.err.println("Invalid input line: " + line);
            }
        }
        return Block;
    }


    public static void addOne(Object Username , String key){

        // Appends a value to a given key

        if (key == "NumberOfPosts" || key == "TotalEndorsements" || key == "TotalComments") {
            for (Map<String,Object> Users : GetAccounts()){
                if ((String) Username == Users.get("Username")) {
                    if (key == "NumberOfPosts"){
                        int num = (Integer) Users.get("NumberOfPosts") ;
                        num += 1 ; 
                        Users.put("NumberOfPosts", num); 
                        break; 
                    }
                    if (key == "TotalEndorsements"){
                        int num = (Integer) Users.get("TotalEndorsements") ;
                        num += 1 ; 
                        Users.put("TotalEndorsements", num); 
                        break;
                    }
                    if (key == "TotalComments"){
                        int num = (Integer) Users.get("TotalComments") ;
                        num += 1 ; 
                        Users.put("TotalComments", num); 
                        break;
                    }
                }
            }

        }
    }
}
