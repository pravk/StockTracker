package mobile.pk.com.stocktracker.event;

import mobile.pk.com.stocktracker.dao.Position;

/**
 * Created by hello on 8/24/2015.
 */
public class RefreshPositionEvent {
    private final Position position;

    public RefreshPositionEvent(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
