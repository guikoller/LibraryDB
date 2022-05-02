/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package booksdb;
import java.sql.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author guilhermekoller
 */
public class BooksDB {

    public static void main(String[] args) throws FileNotFoundException {
        try{
            Connection conn;
            conn = DriverManager.getConnection("jdbc:mysql://localhost/livrosdb", "koller", "password");
            
            Statement stmt;
            stmt = conn.createStatement();
            
            PreparedStatement  insertCover;
            insertCover = conn.prepareStatement("update books set cover = ? where book_id = ?");
            
            ResultSet rs;
            rs = stmt.executeQuery("Select book_id from books");
            
            System.out.println("Inserting covers");
            while(rs.next()){
                System.out.println(rs.getInt(1));
                
                //Loading BLOB
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                FileInputStream file;
                file = new FileInputStream("/home/guilhermekoller/Documents/Repos/LibraryDB/livros-db/"+rs.getInt(1)+".jpg");
                int readByte = file.read();
                while(readByte != -1){
                    buffer.write(readByte);
                    readByte = file.read();
                }
                
                //updating bookcover
                insertCover.setInt(2, rs.getInt(1));
                insertCover.setBytes(1, buffer.toByteArray());
                insertCover.executeUpdate();
                
            }
            
            PreparedStatement  insertAuthor;
            insertAuthor = conn.prepareStatement("insert into authors (book_id, name) values(?, ?)");
            
            rs = stmt.executeQuery("Select book_id, author from books");
            
            while(rs.next()){                
                String data = rs.getString(2);
                String[] split = data.split(",");
                for (int i=0; i < split.length; i++){
                    split[i] = split[i].trim();
                    System.out.println(split[i]);
                    insertAuthor.setInt(1, rs.getInt(1));
                    insertAuthor.setString(2, split[i]);
                    insertAuthor.executeUpdate();
                }           
                
                
            }
            
            conn.close();
            
        }catch(SQLException  e){
            System.out.println(e);
        } catch (IOException ex) {
            Logger.getLogger(BooksDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
