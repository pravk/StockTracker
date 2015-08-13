package mobile.pk.com.stocktracker.event;

import mobile.pk.com.stocktracker.dao.Position;

/**
 * Created by hello on 8/13/2015.
 */
public class ShowPositionDetailEvent {
    private Position position;

    public ShowPositionDetailEvent(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
