package net.nohaven.proj.jafe.structure.nodes;

import java.io.*;

import net.nohaven.proj.jafe.structure.*;
import net.nohaven.proj.jafe.utils.*;

public class SimpleFileNode extends Node {
    public static final byte TYPE_ID = 0x04;

    private String fileName;

    private String notes;

    private byte[] fileContents;

    public SimpleFileNode(Tree t) {
        super(t);
    }

    public byte getTypeID() {
        return TYPE_ID;
    }

    public void loadFile(File f) throws IOException {
        if (!f.exists())
            return;

        byte[] tmpContent = new byte[(int) f.length()];
        FileInputStream fis = new FileInputStream(f);
        fis.read(tmpContent);
        fis.close();

        fileName = f.getName();
        fileContents = tmpContent;
    }

    public void resetFile() {
        fileName = null;
        fileContents = null;
    }

    public void saveFile(File out) throws IOException {
        FileOutputStream fos = new FileOutputStream(out, false);
        fos.write(fileContents);
        fos.close();
    }

    public final String getNotes() {
        return notes;
    }

    public final void setNotes(String notes) {
        this.notes = notes;
    }

    public final String getFileName() {
        return fileName;
    }

    public final String getFileSize() {
        return Integer.toString(fileContents.length);
    }

    protected int getContentSize() {
        CounterOutputStream cos = new CounterOutputStream();
        DataOutputStream dos = new DataOutputStream(cos);
        try {
            Utils.putStringToDataStream(fileName, dos);
            Utils.putStringToDataStream(notes, dos);
        } catch (IOException e) {
            return 0; //cannot be
        }
        return cos.getCount()
                + (fileContents == null ? 0 : fileContents.length);
    }

    protected void writeContentToStream(DataOutputStream dos)
            throws IOException {
        Utils.putStringToDataStream(fileName, dos);
        Utils.putStringToDataStream(notes, dos);
        if (fileContents != null)
        	dos.write(fileContents);
    }

    protected void readContentFromStream(DataInputStream dis, int length)
            throws IOException {
        fileName = Utils.getStringFromDataStream(dis);
        notes = Utils.getStringFromDataStream(dis);

        //FIXME ugly
        fileContents = null;
        int alreadyRead = getContentSize();

        fileContents = new byte[length - alreadyRead];
        dis.read(fileContents);
    }
}
