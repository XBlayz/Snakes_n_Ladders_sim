package snakes_n_ladders_sim.utility;

public class MatchConfig {
    public int players;
    public int rows;
    public int columns;
    public String boardBuildStrategyString;
    public boolean isPriceCellOn;
    public boolean isStopCellOn;
    public boolean isCardOn;
    public boolean isExtraCardsOn;
    public int diceType;
    public int numberOfDice;
    public boolean isSingleDiceRuleOn;
    public boolean isMaxDiceRuleOn;
    public int numberOfEachCard;

    public MatchConfig() {}

    public MatchConfig(int players, int rows, int columns, String boardBuildStrategyString, boolean isPriceCellOn, boolean isStopCellOn, boolean isCardOn, boolean isExtraCardsOn, int diceType, int numberOfDice, boolean isSingleDiceRuleOn, boolean isMaxDiceRuleOn, int numberOfEachCard) {
        this.players = players;
        this.rows = rows;
        this.columns = columns;
        this.boardBuildStrategyString = boardBuildStrategyString;
        this.isPriceCellOn = isPriceCellOn;
        this.isStopCellOn = isStopCellOn;
        this.isCardOn = isCardOn;
        this.isExtraCardsOn = isExtraCardsOn;
        this.diceType = diceType;
        this.numberOfDice = numberOfDice;
        this.isSingleDiceRuleOn = isSingleDiceRuleOn;
        this.isMaxDiceRuleOn = isMaxDiceRuleOn;
        this.numberOfEachCard = numberOfEachCard;
    }
}
