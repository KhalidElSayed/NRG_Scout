package nrgscoutserver;

public class Match {

    private Alliance redAlliance, blueAlliance;
    private int number, redScore, blueScore;

    public Match(int number, Alliance redAlliance, Alliance blueAlliance, int redScore, int blueScore) {
        this.redAlliance = redAlliance;
        this.blueAlliance = blueAlliance;
        this.number = number;
        this.redScore = redScore;
        this.blueScore = blueScore;
    }

    public Team getTeam(int alliance, int teamNumber) {
        switch (alliance) {
            case Alliance.ALLIANCE_RED:
                //Return team XXXX from red alliance
                return redAlliance.getTeamByNumber(teamNumber);
            default:
                //Return team XXXX from blue alliance
                return blueAlliance.getTeamByNumber(teamNumber);
        }
    }
    
    public int[] getTeamList() {
        return new int[] { redAlliance.getTeam(0).getNumber(), redAlliance.getTeam(1).getNumber(), redAlliance.getTeam(2).getNumber(), blueAlliance.getTeam(0).getNumber(), blueAlliance.getTeam(1).getNumber(), blueAlliance.getTeam(2).getNumber() };
    }
    
    public Team getTeam(int teamNumber) {
        if(redAlliance.getTeamByNumber(teamNumber) != null) {
            return redAlliance.getTeamByNumber(teamNumber);
        }else{
            return blueAlliance.getTeamByNumber(teamNumber);
        }
    }

    public boolean checkTeamExists(int alliance, int number) {
        Team[] teams = new Team[3];
        switch (alliance) {
            case Alliance.ALLIANCE_RED:
                teams[0] = redAlliance.getTeam(0);
                teams[1] = redAlliance.getTeam(1);
                teams[2] = redAlliance.getTeam(2);
                break;
            default:
                teams[0] = blueAlliance.getTeam(0);
                teams[1] = blueAlliance.getTeam(1);
                teams[2] = blueAlliance.getTeam(2);
                break;
        }
        for (Team team : teams) {
            if (team.getNumber() == number) {
                return true;
            }
        }
        return false;
    }

    public Alliance getAlliance(int alliance) {
        switch (alliance) {
            case Alliance.ALLIANCE_RED:
                return redAlliance;
            default:
                return blueAlliance;
        }
    }

    public int getNumber() {
        return number;
    }
    
    public void setRedScore(int redScore) {
        this.redScore = redScore;
    }
    
    public void setBlueScore(int blueScore) {
        this.blueScore = blueScore;
    }

    public int getScore(int alliance) {
        switch (alliance) {
            case Alliance.ALLIANCE_RED:
                return redScore;
            default:
                return blueScore;
        }
    }
}
