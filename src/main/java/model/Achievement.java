package model;

import java.io.Serializable;

public interface Achievement extends Serializable {
    void calcPoints(Player player);
}
