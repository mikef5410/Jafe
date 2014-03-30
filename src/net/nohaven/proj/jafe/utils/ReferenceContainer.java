package net.nohaven.proj.jafe.utils;

public class ReferenceContainer {
    private Object content;
    
    public ReferenceContainer() {
        content = null;
    }
    
    public ReferenceContainer(Object content) {
        this.setContent(content);
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
    
    public boolean isEmpty () {
        return content == null;
    }
}
