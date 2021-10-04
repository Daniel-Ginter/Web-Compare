package web.compare.modell;

import java.io.Serializable;

public enum ElementStatus implements Serializable {
     NEW,
     CHANGED,
     SAME,
     MAYBE,
     DELETED
}
