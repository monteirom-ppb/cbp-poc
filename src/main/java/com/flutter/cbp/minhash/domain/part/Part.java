package com.flutter.cbp.minhash.domain.part;

public class Part {
    private SelectionInfo selectionInfo;
    private MarketInfo marketInfo;
    private SportInfo sportInfo;
    private CompetitionInfo competitionInfo;
    private EventInfo eventInfo;
    private MarketTypeInfo marketTypeInfo;
    private FractionalPrice fractionalPrice;
    private int partNumber;

    public String getMinHashSignature() {
        return selectionInfo.getEntity().getName() +
                marketInfo.getEntity().getName() +
                sportInfo.getEntity().getName() +
                competitionInfo.getEntity().getName() +
                eventInfo.getEntity().getName();
    }

    public SelectionInfo getSelectionInfo() {
        return selectionInfo;
    }

    public MarketInfo getMarketInfo() {
        return marketInfo;
    }

    public SportInfo getSportInfo() {
        return sportInfo;
    }

    public CompetitionInfo getCompetitionInfo() {
        return competitionInfo;
    }

    public EventInfo getEventInfo() {
        return eventInfo;
    }

    public MarketTypeInfo getMarketTypeInfo() {
        return marketTypeInfo;
    }

    public FractionalPrice getFractionalPrice() {
        return fractionalPrice;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public Part(SelectionInfo selectionInfo, MarketInfo marketInfo, SportInfo sportInfo,
                CompetitionInfo competitionInfo, EventInfo eventInfo, MarketTypeInfo marketTypeInfo,
                FractionalPrice fractionalPrice, int partNumber) {
        this.selectionInfo = selectionInfo;
        this.marketInfo = marketInfo;
        this.sportInfo = sportInfo;
        this.competitionInfo = competitionInfo;
        this.eventInfo = eventInfo;
        this.marketTypeInfo = marketTypeInfo;
        this.fractionalPrice = fractionalPrice;
        this.partNumber = partNumber;
    }

    @Override
    public String toString() {
        return "Part{" +
                "selectionInfo=" + selectionInfo +
                ", marketInfo=" + marketInfo +
                ", sportInfo=" + sportInfo +
                ", competitionInfo=" + competitionInfo +
                ", eventInfo=" + eventInfo +
                ", marketTypeInfo=" + marketTypeInfo +
                ", fractionalPrice=" + fractionalPrice +
                ", partNumber=" + partNumber +
                '}';
    }
}
