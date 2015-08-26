package mobile.pk.com.stocktracker.dataobjects;

import mobile.pk.com.stocktracker.R;
import mobile.pk.com.stocktracker.dao.Position;

/**
 * Created by hello on 8/26/2015.
 */
public class PositionData extends BaseDataObject{

    private Position position;

    public PositionData(Position position) {
        this.position = position;
    }


    public Position getPosition(){
        return position;
    }

    public String getQuantity(){
        return String.format("%d x", (int) position.getQuantity());
    }

    public String getStockName(){
        return super.getStockName(position.getStock());
    }

    public String getTicker(){
        return super.getTicker(position.getStock());
    }

    public String getError(){
        return position.getError();
    }

    public String getAveragePrice(){
        if(position.getStock().getPrice() != null)
            return super.formatCurrencyAmount(position.getStock().getPrice().getCurrency(), position.getAveragePrice());
        return null;
    }
    public String getLastTradePrice(){
        if(position.getStock().getPrice() != null)
            return super.formatCurrencyAmount(position.getStock().getPrice().getCurrency(), position.getStock().getPrice().getLastPrice());
        return null;
    }

    public String getChange(){
        if(position.getStock().getPrice() != null)
            return String.format(PRICE_CHANGE_FORMAT, position.getStock().getPrice().getChange(), position.getStock().getPrice().getChangePercent());
        return null;
    }

    public int getChangeTextColor(){
        if (position.getStock().getPrice() != null && position.getStock().getPrice().getChange() < 0)
            return R.color.red;
        else
            return R.color.green;
    }

    public String getGainLoss(){
        if(position.getStock().getPrice() != null)
            return String.format(PRICE_FORMAT, getCurrency(), position.getGainLoss());
        return null;
    }

    public String getCurrency()
    {
        if(position.getStock().getPrice() != null)
            return position.getStock().getPrice().getCurrency();
        return null;
    }

    public String getMarketValue(){
        return String.format(PRICE_FORMAT, getCurrency(), position.getMarketValue());
    }

    public int getGainLossTextColor() {
        if(position.getGainLoss() <0)
            return R.color.red;
        else
            return R.color.green;
    }

    public static class ClosedPositionData extends PositionData{

        public ClosedPositionData(Position position) {
            super(position);
        }
        @Override
        public String getQuantity(){
            return "-";
        }
        public String getAveragePrice(){
            return "-";
        }
        public String getGainLoss(){
            return String.format(PRICE_FORMAT, getCurrency(), getPosition().getNetRealizedGainLoss());
        }
        public String getMarketValue(){
            return "-";
        }
    }

    public static class PositionHeaderData extends PositionData{

        private final String positionDesc;
        private int cardColor;

        public PositionHeaderData(String positionDescription, int cardColor) {
            super(null);
            this.positionDesc = positionDescription;
            this.cardColor = cardColor;
        }

        public String getQuantity(){
            return String.format("%s x", "Quantity");
        }

        public String getStockName(){
            return "Company Name";
        }

        public String getTicker(){
            return "Ticker /";
        }

        public String getError(){
            return null;
        }

        public String getAveragePrice(){
            return "Avg Price";
        }
        public String getLastTradePrice(){
            return "Last Price";
        }

        public String getChange(){
            return "Change (%)";
        }

        public int getChangeTextColor(){
                return R.color.primary_light;
        }

        public String getGainLoss(){
            return "Gain/Loss";
        }

        public String getMarketValue(){
            return "Mkt Value";
        }

        public int getGainLossTextColor() {
            return R.color.primary_light;
        }

        public String getPositionDesc() {
            return positionDesc;
        }

        public int getCardColor() {
            return cardColor;
        }
    }
}
