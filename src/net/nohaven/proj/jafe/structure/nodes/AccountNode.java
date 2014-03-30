package net.nohaven.proj.jafe.structure.nodes;

import java.io.*;

import net.nohaven.proj.jafe.structure.*;
import net.nohaven.proj.jafe.utils.*;

public class AccountNode extends Node {
   public static final byte TYPE_ID = 0x03;

   private String url;

   private String userId;

   private String password;

   private String notes;

   public AccountNode(Tree t) {
      super(t);
   }

   public final String getNotes() {
      return notes;
   }

   public final void setNotes(String notes) {
      this.notes = notes;
   }

   public final String getPassword() {
      return password;
   }

   public final void setPassword(String password) {
      this.password = password;
   }

   public final String getUrl() {
      return url;
   }

   public final void setUrl(String url) {
      this.url = url;
   }

   public final String getUserId() {
      return userId;
   }

   public final void setUserId(String userId) {
      this.userId = userId;
   }

   public byte getTypeID() {
      return TYPE_ID;
   }

   protected int getContentSize() {
      CounterOutputStream cos = new CounterOutputStream();
      DataOutputStream dos = new DataOutputStream(cos);
      try {
         writeContentToStream(dos);
      } catch (IOException e) {
         return 0; //cannot be
      }
      return cos.getCount();
   }

   protected void writeContentToStream(DataOutputStream dos) throws IOException {
      Utils.putStringToDataStream(url, dos);
      Utils.putStringToDataStream(userId, dos);
      Utils.putStringToDataStream(password, dos);
      Utils.putStringToDataStream(notes, dos);
   }

   protected void readContentFromStream(DataInputStream dis, int length)
         throws IOException {
      url = Utils.getStringFromDataStream(dis);
      userId = Utils.getStringFromDataStream(dis);
      password = Utils.getStringFromDataStream(dis);
      notes = Utils.getStringFromDataStream(dis);
   }

}
