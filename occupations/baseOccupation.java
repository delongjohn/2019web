package occupations;

public class baseOccupation {
    private String occupation;
    private boolean isAlive;
    private boolean isAction;

    public boolean isAction() {
        return isAction;
    }

    public void setAction(boolean action) {
        isAction = action;
    }

    public String getOccupatin() {
        return occupation;
    }

    public void setOccupatin(String occupation) {
        this.occupation = occupation;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

}
