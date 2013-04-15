package nrgscoutserver;

public class Alliance {

    public static final int ALLIANCE_RED = 0;
    public static final int ALLIANCE_BLUE = 1;
    private Team team1, team2, team3;
    
    public Alliance(Team team1, Team team2, Team team3, int alliance) throws IllegalArgumentException {
        this.team1 = team1;
        this.team2 = team2;
        this.team3 = team3;
        if(checkRepitions()) {
            throw new IllegalArgumentException("Cannot use the same team more than once in the same alliance.");
        }
    }
    
    public boolean teamExists(int teamNumber) {
        return teamNumber == team1.getNumber() || teamNumber == team2.getNumber() || teamNumber == team3.getNumber();
    }
    
    public Team getTeamByNumber(int teamNumber) {
        if(teamNumber == team1.getNumber()) {
            return team1;
        }else if(teamNumber == team2.getNumber()) {
            return team2;
        }else if(teamNumber == team3.getNumber()) {
            return team3;
        }else{
            return null;
        }
    }
    
    public Team getTeam(int index) {
        switch(index) {
            case 0:
                return team1;
            case 1:
                return team2;
            case 2:
                return team3;
            default:
                return null;
        }
    }
    
    private boolean checkRepitions() {
        return team1.getNumber() == team2.getNumber() || team1.getNumber() == team3.getNumber() || team2.getNumber() == team3.getNumber();
    }
}
