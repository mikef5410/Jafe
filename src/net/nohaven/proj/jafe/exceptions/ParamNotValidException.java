package net.nohaven.proj.jafe.exceptions;

public class ParamNotValidException extends RuntimeException {
   private static final long serialVersionUID = -1244424358399850660L;

   public ParamNotValidException(String string) {
      super(string);
   }
}
